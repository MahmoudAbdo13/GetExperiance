package com.example.getexperience.Manager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getexperience.R;
import com.example.getexperience.adapters.BlockedFoundationAdapter;
import com.example.getexperience.adapters.FoundationAdapter;
import com.example.getexperience.databinding.FragmentBlockedFoundationBinding;
import com.example.getexperience.model.OrganizationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BlockedFoundationFragment extends Fragment {


    private FragmentBlockedFoundationBinding binding;
    private DatabaseReference reference;
    private ArrayList<OrganizationModel> model;
    private FoundationAdapter adapter;

    public static BlockedFoundationFragment createFor() {
        BlockedFoundationFragment fragment = new BlockedFoundationFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBlockedFoundationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("blockedFoundations");
        model = new ArrayList<>();
        adapter = new FoundationAdapter();
        binding.recyclerBlockedFoundationManagement.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        getFoundation();
    }

    private void getFoundation() {
        binding.blockedFoundationBrogressbarManager.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        OrganizationModel organizationModel = data.getValue(OrganizationModel.class);
                        model.add(organizationModel);
                        try {
                            binding.blockedFoundationBrogressbarManager.setVisibility(View.GONE);
                            enableViews(model);
                        } catch (Exception e) {
                            Log.d("Null", "" + e);
                        }
                    }
                }else {
                    binding.blockedFoundationBrogressbarManager.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("blocked org databaseError: "+databaseError.getMessage());

                //Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableViews(ArrayList<OrganizationModel> model){

        if(model.isEmpty()){
            binding.blockedFoundationBrogressbarManager.setVisibility(View.GONE);
            binding.notFoundBlockedFoundationManager.setVisibility(View.VISIBLE);
            binding.recyclerBlockedFoundationManagement.setVisibility(View.GONE);
        }else {
            binding.blockedFoundationBrogressbarManager.setVisibility(View.GONE);
            binding.notFoundBlockedFoundationManager.setVisibility(View.GONE);
            binding.recyclerBlockedFoundationManagement.setVisibility(View.VISIBLE);
            BlockedFoundationAdapter adapter = new BlockedFoundationAdapter();
            adapter.setList(model);
            binding.recyclerBlockedFoundationManagement.setAdapter(adapter);

        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_admin, someFragment);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }
}