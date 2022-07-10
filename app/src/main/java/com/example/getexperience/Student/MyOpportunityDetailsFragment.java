package com.example.getexperience.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getexperience.databinding.FragmentMyOpportunityDetailsBinding;
import com.example.getexperience.model.OrganizationModel;
import com.example.getexperience.model.NewOpportunityModel;
import com.example.getexperience.model.RequestStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyOpportunityDetailsFragment extends Fragment {

    private FragmentMyOpportunityDetailsBinding binding;
    private static RequestStudent model;

    public static MyOpportunityDetailsFragment createFor(RequestStudent newOpportunityModel) {
        MyOpportunityDetailsFragment fragment = new MyOpportunityDetailsFragment();
        model = newOpportunityModel;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyOpportunityDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Foundation").child(model.getFoundationID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganizationModel organizationModel = snapshot.getValue(OrganizationModel.class);
                binding.studentMyOpportunityFoundationNameData.setText(organizationModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        System.out.println("OID: "+model.getOpportunityId());
        reference.child("OPPortunities").child(model.getOpportunityId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NewOpportunityModel opportunityModel = snapshot.getValue(NewOpportunityModel.class);

                binding.studentMyOpportunityDetailsNameData.setText(opportunityModel.getOpportunityName());
                binding.studentMyOpportunityDetailsTypeData.setText(opportunityModel.getOpportunityType());
                binding.studentMyOpportunityDetailsFieldData.setText(opportunityModel.getOpportunityField());
                binding.studentMyOpportunityDetailsDescriptionData.setText(opportunityModel.getDescription());
                binding.studentMyOpportunityDetailsStartDateData.setText(opportunityModel.getStartDate());
                binding.studentMyOpportunityDetailsEndDateData.setText(opportunityModel.getEndDate());
                binding.studentMyOpportunityDetailsDaysNumberData.setText(opportunityModel.getNumberOfDays());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}