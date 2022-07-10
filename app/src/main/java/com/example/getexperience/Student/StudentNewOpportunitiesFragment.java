package com.example.getexperience.Student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getexperience.adapters.NewOpportunityAdapter;
import com.example.getexperience.databinding.FragmentStudentNewOpportunitiesBinding;
import com.example.getexperience.model.NewOpportunityModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StudentNewOpportunitiesFragment extends Fragment {

    private FragmentStudentNewOpportunitiesBinding binding;
    private DatabaseReference reference;
    private ArrayList<NewOpportunityModel> model;

    public static StudentNewOpportunitiesFragment createFor() {
        StudentNewOpportunitiesFragment fragment = new StudentNewOpportunitiesFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudentNewOpportunitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("OPPortunities");
        model = new ArrayList<>();
        binding.recyclerStudentNewOpportunity.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        getNewOpportunities();
    }

    private void getNewOpportunities() {
        binding.studentNewOpportunityProgressbar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        NewOpportunityModel opportunityModel = data.getValue(NewOpportunityModel.class);
                        model.add(0,opportunityModel);
                        try {
                            binding.studentNewOpportunityProgressbar.setVisibility(View.GONE);
                            enableViews(model);
                        } catch (Exception e) {
                            Log.d("Null", "" + e);
                        }
                    }
                }else {
                    binding.studentNewOpportunityProgressbar.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableViews(ArrayList<NewOpportunityModel> model){

        if(model.isEmpty()){
            binding.studentNewOpportunityProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentNewOpportunity.setVisibility(View.VISIBLE);
            binding.recyclerStudentNewOpportunity.setVisibility(View.GONE);
        }else {
            binding.studentNewOpportunityProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentNewOpportunity.setVisibility(View.GONE);
            binding.recyclerStudentNewOpportunity.setVisibility(View.VISIBLE);
            NewOpportunityAdapter adapter = new NewOpportunityAdapter(true);
            adapter.setList(model);
            binding.recyclerStudentNewOpportunity.setAdapter(adapter);

        }
    }

}
