package com.example.getexperience.OuterPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getexperience.Foundation.OrganizationActivity;
import com.example.getexperience.Manager.AdminActivity;
import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.Student.StudentActivity;
import com.example.getexperience.databinding.ActivityLoginBinding;
import com.example.getexperience.model.OrganizationModel;
import com.example.getexperience.model.StudentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();


        TextView link = findViewById(R.id.registration_link);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        TextView contactUs = findViewById(R.id.forget_password_link);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RestPasswordActivity.class));
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                //startActivity(new Intent(LoginActivity.this, StudentActivity.class));

                String email = binding.loginEmailData.getText().toString();
                String password = binding.loginPasswordData.getText().toString();
                if (email.isEmpty()) {
                    binding.loginEmailData.setError("Please enter your email");
                    binding.loginEmailData.setFocusable(true);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.loginEmailData.setError("Invalid email, try again");
                    binding.loginEmailData.setFocusable(true);
                } else if (password.isEmpty()) {
                    binding.loginPasswordData.setError("Please enter your password");
                    binding.loginPasswordData.setFocusable(true);
                } else {
                    final SweetAlertDialog loadingBar = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    loadingBar.setTitleText("SignIn");
                    loadingBar.setContentText("Please wait...");
                    loadingBar.setCancelable(false);
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    signIn(email, password, loadingBar);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.outer_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.contact_us:
                startActivity(new Intent(LoginActivity.this, ContactUsActivity.class));
                return true;
            case R.id.about_us:
                startActivity(new Intent(LoginActivity.this, AboutUsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void signIn(String email, String password, SweetAlertDialog dialog) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uId = firebaseAuth.getCurrentUser().getUid();
                    if (uId != null) {
                        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("StudentsData").child("Students").child(uId).exists()) {
                                    Prevalent.currentOnlineStudent = snapshot.child("StudentsData").child("Students").child(uId).getValue(StudentModel.class);
                                    startActivity(new Intent(LoginActivity.this, StudentActivity.class));
                                    dialog.dismiss();
                                finish();
                                }
                                else if (snapshot.child("Foundation").child(uId).exists()) {
                                    Prevalent.currentOnlineOrganization = snapshot.child("Foundation").child(uId).getValue(OrganizationModel.class);
                                    startActivity(new Intent(LoginActivity.this, OrganizationActivity.class));
                                    dialog.dismiss();
                                    finish();
                                } else if (snapshot.child("Admin").child(uId).exists()) {
                                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                    dialog.dismiss();
                                    finish();
                                } else if (snapshot.child("blockedFoundations").child(uId).exists()) {
                                    dialog.dismiss();
                                    SweetAlertDialog warning = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                                    warning.setTitle("Sorry,Organization account has been blocked");
                                    warning.setCancelable(false);
                                    warning.setCanceledOnTouchOutside(false);
                                    warning.setConfirmClickListener(sDialog -> { warning.dismiss(); });
                                    warning.show(); }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { dialog.dismiss(); }});
                    } else { dialog.dismiss();
                        Toast.makeText(dialog.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show(); } } }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                SweetAlertDialog warning = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                warning.setTitle(e.getMessage());
                warning.setCancelable(false);
                warning.setCanceledOnTouchOutside(false);
                warning.setConfirmClickListener(sDialog -> {
                    warning.dismiss();
                });
                warning.show();
            }
        });
    }
}