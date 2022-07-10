package com.example.getexperience.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.adapters.NewOpportunityAdapter;
import com.example.getexperience.adapters.OpportunityRequestAdapter;
import com.example.getexperience.databinding.FragmentStudentOpportunityRequestsBinding;
import com.example.getexperience.model.NewOpportunityModel;
import com.example.getexperience.model.OpportunityRequestModel;
import com.example.getexperience.model.RequestStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StudentOpportunityRequestsFragment extends Fragment {

    private FragmentStudentOpportunityRequestsBinding binding;
    private DatabaseReference reference;
    private ArrayList<OpportunityRequestModel> model;

    public static StudentOpportunityRequestsFragment createFor() {
        StudentOpportunityRequestsFragment fragment = new StudentOpportunityRequestsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentOpportunityRequestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();
        binding.recyclerStudentOpportunityRequestState.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getOpportunityRequests();
    }

    private void getOpportunityRequests() {
        binding.studentOpportunityRequestStateProgressbar.setVisibility(View.VISIBLE);
        reference.child("OpportunityRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        OpportunityRequestModel requestStudent = data.getValue(OpportunityRequestModel.class);
                        System.out.println("Opportunity Request Model Student Id: "+requestStudent.getStudentId());
                        if (requestStudent.getStudentId().equals(Prevalent.currentOnlineStudent.getId())) {
                            model.add(0, requestStudent);
                            try {
                                binding.studentOpportunityRequestStateProgressbar.setVisibility(View.GONE);
                                enableViews(model);
                            } catch (Exception e) {
                                Log.d("Null", "" + e);
                            }
                        }
                    }
                    enableViews(model);
                } else {
                    binding.studentOpportunityRequestStateProgressbar.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableViews(ArrayList<OpportunityRequestModel> model) {
        if (model.isEmpty()) {
            binding.studentOpportunityRequestStateProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentOpportunityRequestState.setVisibility(View.VISIBLE);
            binding.recyclerStudentOpportunityRequestState.setVisibility(View.GONE);
        } else {
            binding.studentOpportunityRequestStateProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentOpportunityRequestState.setVisibility(View.GONE);
            binding.recyclerStudentOpportunityRequestState.setVisibility(View.VISIBLE);
            OpportunityRequestAdapter adapter = new OpportunityRequestAdapter();
            adapter.setList(model);
            binding.recyclerStudentOpportunityRequestState.setAdapter(adapter);
        }
    }

}