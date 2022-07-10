package com.example.getexperience.OuterPages.Fragment;

import android.content.Intent;
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
import com.example.getexperience.databinding.FragmentStudentRegisterBinding;
import com.example.getexperience.model.StudentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class StudentRegisterFragment extends Fragment {

    private FragmentStudentRegisterBinding binding;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private SweetAlertDialog loadingBar,dialog;
    private int number;

    public StudentRegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        //.child("Students");
        loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText("SignUp");
        loadingBar.setContentText("Please wait...");
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);

        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitle("Your account has been created successfully");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        binding.studentRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        String name, email, password, confirmPassword, phone, address;

        name = binding.studentRegistrationNameData.getText().toString();
        email = binding.studentRegistrationEmailData.getText().toString();
        password = binding.studentRegistrationPasswordData.getText().toString();
        confirmPassword = binding.studentRegistrationConfirmPasswordData.getText().toString();
        phone = binding.studentRegistrationPhoneData.getText().toString();
        address = binding.studentRegistrationAddressData.getText().toString();

        if (name.isEmpty()) {
            binding.studentRegistrationNameData.setError("Please enter your name");
            binding.studentRegistrationNameData.setFocusable(true);
        } else if (email.isEmpty()) {
            binding.studentRegistrationEmailData.setError("Please enter your email");
            binding.studentRegistrationEmailData.setFocusable(true);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.studentRegistrationEmailData.setError("Invalid email, try again");
            binding.studentRegistrationEmailData.setFocusable(true);
        } else if (password.isEmpty()) {
            binding.studentRegistrationPasswordData.setError("Please enter your password");
            binding.studentRegistrationPasswordData.setFocusable(true);
        } else if (password.length() < 8) {
            binding.studentRegistrationPasswordData.setError("The password must be at least 8 digits");
            binding.studentRegistrationPasswordData.setFocusable(true);
        } else if (!password.equals(confirmPassword)) {
            binding.studentRegistrationConfirmPasswordData.setError("The password don't match, try again");
            binding.studentRegistrationConfirmPasswordData.setFocusable(true);
        } else if (phone.isEmpty()) {
            binding.studentRegistrationPhoneData.setError("Please enter your phone number");
            binding.studentRegistrationPhoneData.setFocusable(true);
        } else if (!phone.startsWith("05") && !phone.startsWith("966")) {
            binding.studentRegistrationPhoneData.setError("The phone number must be start with '05' or '966' if it mobile number, try again");
            binding.studentRegistrationPhoneData.setFocusable(true);
        } else if (phone.startsWith("05") && phone.length() != 10 || phone.startsWith("966") && phone.length() != 12) {
            if (phone.startsWith("05")) {
                binding.studentRegistrationPhoneData.setError("The phone number start with '05' then phone number must be 10 numbers");
                binding.studentRegistrationPhoneData.setFocusable(true);
            } else {
                binding.studentRegistrationPhoneData.setError("The phone number start with '966' then phone number must be 12 numbers");
                binding.studentRegistrationPhoneData.setFocusable(true);
            }
        } else if (address.isEmpty()) {
            binding.studentRegistrationAddressData.setError("Please enter your address");
            binding.studentRegistrationAddressData.setFocusable(true); } else {
            loadingBar.show();
            StudentModel model = new StudentModel(FirebaseAuth.getInstance().getUid(), name, email
                    , password, phone, address, "");
            signUp(model);
        }
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signUp(StudentModel studentModel) {
        firebaseAuth.createUserWithEmailAndPassword(studentModel.getEmail(), studentModel.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                    root.child("StudentsData").child("Student Number").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                number = snapshot.getValue(Integer.class);
                                root.child("StudentsData").child("Student Number").setValue(number + 1);
                                studentModel.setNumber(String.valueOf(number));
                                studentModel.setId(getCurrentUser().getUid());
                                root.child("StudentsData").child("Students").child(getCurrentUser().getUid()).setValue(studentModel);
                            } else {
                                Toast.makeText(getContext(), "Error: there is no Student Number to add you",
                                        Toast.LENGTH_SHORT).show();
                            } }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }});
                    loadingBar.dismiss();
                    dialog.setConfirmClickListener(sDialog -> {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        firebaseAuth.signOut();
                        dialog.dismissWithAnimation(); });
                    dialog.show();
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show(); } }
        });
    }

}

