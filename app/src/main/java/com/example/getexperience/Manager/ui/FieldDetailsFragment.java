package com.example.getexperience.Manager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getexperience.databinding.FragmentFieldDetailsBinding;
import com.example.getexperience.model.FieldModel;
import com.google.firebase.database.FirebaseDatabase;


public class FieldDetailsFragment extends Fragment {

    private static FieldModel model;
    private FragmentFieldDetailsBinding binding;

    public static FieldDetailsFragment createFor(FieldModel fieldModel) {

        FieldDetailsFragment fragment = new FieldDetailsFragment();
        model = fieldModel;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFieldDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

/*    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fieldTitleData.setText(model.getTitle());
        binding.fieldFoundationNameData.setText(model.getFoundationName());


        binding.managerFieldDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance()
                        .getReference("Fields").push()
                        .child(model.getFoundationName())
                        .removeValue();
            }
        });
    }
*/
}