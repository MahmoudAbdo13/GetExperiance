package com.example.getexperience.Manager.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getexperience.adapters.FoundationAdapter;
import com.example.getexperience.databinding.FragmentFoundationManagementBinding;
import com.example.getexperience.model.OrganizationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoundationManagementFragment extends Fragment {

    private FragmentFoundationManagementBinding binding;
    private DatabaseReference reference;
    private ArrayList<OrganizationModel> model;

    public static FoundationManagementFragment createFor() {
        FoundationManagementFragment fragment = new FoundationManagementFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFoundationManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("Foundation");
        model = new ArrayList<>();
        binding.recyclerFoundationManagement.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getOrganizations();
    }

    private void getOrganizations() {
        binding.organizationBrogressbarManager.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        OrganizationModel organizationModel = data.getValue(OrganizationModel.class);
                        model.add(organizationModel);
                        try {
                            binding.organizationBrogressbarManager.setVisibility(View.GONE);
                            enableViews(model);
                        } catch (Exception e) {
                            Log.d("getOrganizations", "" + e);
                        }
                    }
                } else {
                    binding.organizationBrogressbarManager.setVisibility(View.GONE);
                    enableViews(model);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void enableViews(ArrayList<OrganizationModel> model) {

        if (model.isEmpty()) {
            binding.organizationBrogressbarManager.setVisibility(View.GONE);
            binding.notFoundFoundationManager.setVisibility(View.VISIBLE);
            binding.recyclerFoundationManagement.setVisibility(View.GONE);
        } else {
            binding.organizationBrogressbarManager.setVisibility(View.GONE);
            binding.notFoundFoundationManager.setVisibility(View.GONE);
            binding.recyclerFoundationManagement.setVisibility(View.VISIBLE);
            FoundationAdapter adapter = new FoundationAdapter();
            adapter.setList(model);
            binding.recyclerFoundationManagement.setAdapter(adapter);

        }
    }

}