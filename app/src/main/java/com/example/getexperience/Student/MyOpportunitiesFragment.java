package com.example.getexperience.Student;

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
import android.widget.Toast;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.adapters.MyOpportunityAdapter;
import com.example.getexperience.adapters.NewOpportunityAdapter;
import com.example.getexperience.databinding.FragmentMyCoursesBinding;
import com.example.getexperience.databinding.FragmentMyOpportunitiesBinding;
import com.example.getexperience.databinding.FragmentStudentNewOpportunitiesBinding;
import com.example.getexperience.model.NewOpportunityModel;
import com.example.getexperience.model.RequestStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOpportunitiesFragment extends Fragment {

    private FragmentMyOpportunitiesBinding binding;
    private DatabaseReference reference;
    private ArrayList<RequestStudent> model;

    public static MyOpportunitiesFragment createFor() {
        MyOpportunitiesFragment fragment = new MyOpportunitiesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyOpportunitiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("Acceptrequest");
        model = new ArrayList<>();
        binding.recyclerStudentMyOpportunity.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getMyOpportunities();
    }

    private void getMyOpportunities() {
        binding.studentMyOpportunityProgressbar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        RequestStudent opportunityModel = data.getValue(RequestStudent.class);
                        System.out.println("opportunity Model Id: " + opportunityModel.getStudentId());
                        if (opportunityModel.getStudentNumber().equals(Prevalent.currentOnlineStudent.getNumber())) {
                            model.add(0, opportunityModel);
                            try {
                                binding.studentMyOpportunityProgressbar.setVisibility(View.GONE);
                                enableViews(model);
                            } catch (Exception e) {
                                Log.d("studentMyOpportunityDetails", "" + e);
                            }
                        }
                    }
                    enableViews(model);
                } else {
                    binding.studentMyOpportunityProgressbar.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableViews(ArrayList<RequestStudent> model) {

        if (model.isEmpty()) {
            binding.studentMyOpportunityProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentMyOpportunity.setVisibility(View.VISIBLE);
            binding.recyclerStudentMyOpportunity.setVisibility(View.GONE);
        } else {
            binding.studentMyOpportunityProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentMyOpportunity.setVisibility(View.GONE);
            binding.recyclerStudentMyOpportunity.setVisibility(View.VISIBLE);
            MyOpportunityAdapter adapter = new MyOpportunityAdapter();
            adapter.setList(model);
            binding.recyclerStudentMyOpportunity.setAdapter(adapter);

        }
    }

}