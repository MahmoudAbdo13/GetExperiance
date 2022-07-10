package com.example.getexperience.Manager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.getexperience.databinding.FragmentAddFieldBinding;
import com.example.getexperience.model.FieldModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddFieldFragment extends Fragment {

    private FragmentAddFieldBinding binding;
    private String name, title, description;
    private DatabaseReference reference;

    public static AddFieldFragment createFor() {

        AddFieldFragment fragment = new AddFieldFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddFieldBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();

        binding.addFieldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDatabase();
            }
        });
    }

    private void addToDatabase(){
        name = binding.addFieldFoundationNameData.getText().toString();
        title = binding.addFieldTitleData.getText().toString();
        description = binding.addFieldDescriptionData.getText().toString();

        if (name.isEmpty()) {
            binding.addFieldFoundationNameData.setError("Please enter your foundation name");
            binding.addFieldFoundationNameData.setFocusable(true);
        } else if (title.isEmpty()) {
            binding.addFieldTitleData.setError("Please enter your title");
            binding.addFieldTitleData.setFocusable(true);
        } else if (description.isEmpty()) {
            binding.addFieldDescriptionData.setError("Please enter description");
            binding.addFieldDescriptionData.setFocusable(true);
        } else {
            final SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            loadingBar.setTitleText("Adding");
            loadingBar.setContentText("Please wait...");
            loadingBar.setCancelable(false);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            FieldModel model = new FieldModel(name, title);
            reference.child("Fields").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reference.child("Fields").push().setValue(model);
                    loadingBar.dismissWithAnimation();
                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("Field has been added successfully");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        binding.addFieldFoundationNameData.toString().trim();
                        binding.addFieldTitleData.toString().trim();
                        binding.addFieldDescriptionData.toString().trim();
                        dialog.dismissWithAnimation();
                    });
                    dialog.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}