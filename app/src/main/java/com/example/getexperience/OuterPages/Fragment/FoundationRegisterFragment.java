package com.example.getexperience.OuterPages.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.getexperience.OuterPages.LoginActivity;
import com.example.getexperience.databinding.FragmentFoundationRegisterBinding;
import com.example.getexperience.model.OrganizationModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FoundationRegisterFragment extends Fragment {

    private FragmentFoundationRegisterBinding binding;
    private String name, email, password, confirmPassword, phone, address, imageUrl;
    private SweetAlertDialog loadingBar;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String foundationRandomKey, downloadImageUrl, saveCurrentDate, saveCurrentTime;
    private StorageReference foundationImagesRef;
    private DatabaseReference foundationsRef;

    private WifiManager wifiManager;
    private final static int PLACE_PICKER_REQUEST = 999;

    public FoundationRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFoundationRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Foundation");
        foundationImagesRef = FirebaseStorage.getInstance().getReference().child("Foundation Images");

        binding.foundationRegistrationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        binding.foundationRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {

        name = binding.foundationRegistrationFoundationNameData.getText().toString();
        email = binding.foundationRegistrationEmailData.getText().toString();
        password = binding.foundationRegistrationPasswordData.getText().toString();
        phone = binding.foundationRegistrationPhoneData.getText().toString();
        address = binding.foundationRegistrationAddressData.getText().toString();
        confirmPassword = binding.foundationRegistrationConfirmPasswordData.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(getContext(), "Please select foundation image", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty()) {
            binding.foundationRegistrationFoundationNameData.setError("Please enter your name");
            binding.foundationRegistrationFoundationNameData.setFocusable(true);
        } else if (email.isEmpty()) {
            binding.foundationRegistrationEmailData.setError("Please enter your email");
            binding.foundationRegistrationEmailData.setFocusable(true);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.foundationRegistrationEmailData.setError("Invalid email, try again");
            binding.foundationRegistrationEmailData.setFocusable(true);
        } else if (password.isEmpty()) {
            binding.foundationRegistrationPasswordData.setError("Please enter your password");
            binding.foundationRegistrationPasswordData.setFocusable(true);
        } else if (password.length() < 8) {
            binding.foundationRegistrationPasswordData.setError("The password must be at least 8 digits");
            binding.foundationRegistrationPasswordData.setFocusable(true);
        } else if (!password.equals(confirmPassword)) {
            binding.foundationRegistrationConfirmPasswordData.setError("The password don't match, try again");
            binding.foundationRegistrationConfirmPasswordData.setFocusable(true);
        } else if (phone.isEmpty()) {
            binding.foundationRegistrationPhoneData.setError("Please enter your phone number");
            binding.foundationRegistrationPhoneData.setFocusable(true);
        } else if (!phone.startsWith("05") && !phone.startsWith("966")) {
                binding.foundationRegistrationPhoneData.setError("The phone number must be start with '05' or '966' if it mobile number, try again");
                binding.foundationRegistrationPhoneData.setFocusable(true);
        } else if (phone.startsWith("05") && phone.length() != 10 || phone.startsWith("966") && phone.length() != 12) {
            if (phone.startsWith("05")){
                binding.foundationRegistrationPhoneData.setError("The phone number start with '05' then phone number must be 10 numbers");
                binding.foundationRegistrationPhoneData.setFocusable(true);
            }else {
                binding.foundationRegistrationPhoneData.setError("The phone number start with '966' then phone number must be 12 numbers");
                binding.foundationRegistrationPhoneData.setFocusable(true);
            }

        } else if (address.isEmpty()) {
            binding.foundationRegistrationAddressData.setError("Please enter your address");
            binding.foundationRegistrationAddressData.setFocusable(true);
        } else {
            loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            loadingBar.setTitleText("SignUp");
            loadingBar.setContentText("Please wait...");
            loadingBar.setCancelable(false);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            storeFoundationImage();
        }
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    private void storeFoundationImage() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        foundationRandomKey = saveCurrentDate + saveCurrentTime;
        final StorageReference filePath = foundationImagesRef.child(ImageUri.getLastPathSegment() + foundationRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            signUp();
                        }
                    }
                });
            }
        });
    }

    public void signUp() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    OrganizationModel organizationModel = new OrganizationModel(firebaseAuth.getUid(), name, email
                            , password, phone, address, downloadImageUrl);
                    reference.child(getCurrentUser().getUid()).setValue(organizationModel);
                    loadingBar.dismiss();
                    SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("Your Organization account has been created successfully");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        firebaseAuth.signOut();
                        dialog.dismissWithAnimation();
                    });
                    dialog.show();
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == Activity.RESULT_OK && data != null) {
            ImageUri = data.getData();
            binding.foundationRegistrationImage.setImageURI(ImageUri);
        }

    }

}

