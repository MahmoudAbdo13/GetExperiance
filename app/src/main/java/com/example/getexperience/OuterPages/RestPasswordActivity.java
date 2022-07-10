package com.example.getexperience.OuterPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.getexperience.databinding.ActivityRestPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RestPasswordActivity extends AppCompatActivity {

    private ActivityRestPasswordBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestPasswordBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        binding.restPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.restPasswordEmailData.getText().toString();
                if (email.isEmpty()) {
                    binding.restPasswordEmailData.setError("Please enter your email");
                    binding.restPasswordEmailData.setFocusable(true);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.restPasswordEmailData.setError("Invalid email, try again");
                    binding.restPasswordEmailData.setFocusable(true);
                } else {
                    final SweetAlertDialog loadingBar = new SweetAlertDialog(RestPasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    loadingBar.setTitleText("Check Email");
                    loadingBar.setContentText("Please wait...");
                    loadingBar.setCancelable(false);
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    resetPassword(email,loadingBar);
                }
            }
        });

    }

    public void resetPassword(String email, SweetAlertDialog dialog){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    SweetAlertDialog dialog = new SweetAlertDialog(RestPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("Reset password link has been send to your gmail successfully");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        startActivity(new Intent(RestPasswordActivity.this, LoginActivity.class));
                        dialog.dismissWithAnimation();
                    });
                    dialog.show();
                }else {
                    dialog.dismiss();
                    Toast.makeText(dialog.getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}