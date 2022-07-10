package com.example.getexperience.Foundation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.getexperience.R;
import com.example.getexperience.databinding.FragmentUploadCertificateBinding;
import com.example.getexperience.model.CertificateModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UploadCertificateFragment extends Fragment {

    private FragmentUploadCertificateBinding binding;
    private String studentNumber, certificateName;
    private int certificateID;
    private SweetAlertDialog loadingBar;
    private DatabaseReference reference, referenceOpprtunity;
    private FirebaseAuth firebaseAuth;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String foundationRandomKey, downloadImageUrl, saveCurrentDate, saveCurrentTime;
    private StorageReference courseImagesRef, opportunityImagesRef;
    private DatabaseReference foundationsRef;
    private boolean check;

    public UploadCertificateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUploadCertificateBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("CourseCertificates");
        courseImagesRef = FirebaseStorage.getInstance().getReference().child("Certificate").child("Course Certificates");
        //reference.child("courseCertificateID").setValue(101010);

        referenceOpprtunity = FirebaseDatabase.getInstance().getReference().child("OpprtunityCertificates");
        opportunityImagesRef = FirebaseStorage.getInstance().getReference().child("Certificate").child("Opprtunity Certificates");
        //referenceOpprtunity.child("opportunityCertificateID").setValue(104010);

        binding.uploadCertificateSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        binding.uploadCertificateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check = true;
                validateData();
            }
        });

        binding.uploadCertificateOpportunityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check = false;
                validateData();
            }
        });
    }

    private void validateData() {
        studentNumber = binding.uploadCertificateSubscriptionNumber.getText().toString();
        certificateName = binding.uploadCertificateSubscriptionNumber.getText().toString();
        if (studentNumber.isEmpty()) {
            binding.uploadCertificateSubscriptionNumber.setError("Please enter Student Number");
            binding.uploadCertificateSubscriptionNumber.setFocusable(true);
        } else if (certificateName.isEmpty()) {
            binding.uploadCertificateSubscriptionName.setError("Please enter certificate Name");
            binding.uploadCertificateSubscriptionName.setFocusable(true);
        } else if (ImageUri == null) {
            binding.uploadCertificateSelectFile.setError("Please select Certificate File");
            binding.uploadCertificateSelectFile.setFocusable(true);
        } else {
            loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            loadingBar.setTitleText("Upload Certificate");
            loadingBar.setContentText("Please wait...");
            loadingBar.setCancelable(false);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            if (check) {
                storeCourseCertificate();
            } else {
                storeOpportunityCertificate();
            }
        }
    }

    private void storeCourseCertificate() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        foundationRandomKey = saveCurrentDate + saveCurrentTime;
        final StorageReference filePath = courseImagesRef.child(ImageUri.getLastPathSegment() + foundationRandomKey + ".jpg");
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
                            uploadCourseCertificate(downloadImageUrl);
                        }
                    }
                });
            }
        });
    }

    private void storeOpportunityCertificate() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        foundationRandomKey = saveCurrentDate + saveCurrentTime;
        final StorageReference filePath = opportunityImagesRef.child(ImageUri.getLastPathSegment() + foundationRandomKey + ".jpg");
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
                            uploadOpportunityCertificate(downloadImageUrl);
                        }
                    }
                });
            }
        });
    }

    private void uploadCourseCertificate(String certificateUrl) {
        reference.child("courseCertificateID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    certificateID = snapshot.getValue(Integer.class);
                    reference.child("courseCertificateID").setValue(certificateID + 1);

                    CertificateModel certificateModel = new CertificateModel(studentNumber, certificateUrl, certificateName);
                    reference.child("Certificate").child(String.valueOf(certificateID)).setValue(certificateModel);
                    loadingBar.dismiss();
                    SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("Course Certificate file has been uploaded successfully");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        dialog.dismiss();
                    });
                    dialog.show();
                } else {
                    SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("There is no course Certificate ID");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        dialog.dismiss();
                    });
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void uploadOpportunityCertificate(String certificateUrl) {
        referenceOpprtunity.child("opportunityCertificateID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                certificateID = snapshot.getValue(Integer.class);
                if (snapshot.exists()) {
                    referenceOpprtunity.child("opportunityCertificateID").setValue(certificateID + 1);

                    CertificateModel certificateModel = new CertificateModel(studentNumber, certificateUrl, certificateName);
                    referenceOpprtunity.child("Certificate").child(String.valueOf(certificateID)).setValue(certificateModel);
                    loadingBar.dismiss();
                    SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("OpportunityCertificate file has been uploaded successfully");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        dialog.dismiss();
                    });
                    dialog.show();
                } else {
                    SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("there is no opportunity Certificate ID");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        dialog.dismiss();
                    });
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            binding.uploadCertificateSubscriptionImage.setImageURI(ImageUri);
            binding.uploadCertificateSelectFile.setText("File has been Selected Successfully");
            binding.uploadCertificateSelectFile.setTextColor(getResources().getColor(R.color.green));
        }

    }

}

