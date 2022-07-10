package com.example.getexperience.Foundation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.adapters.StudentrequestAdapter;
import com.example.getexperience.databinding.FragmentRequestsManagementBinding;
import com.example.getexperience.model.RequestStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsManagementFragment extends Fragment {

    private FragmentRequestsManagementBinding binding;
    LinearLayoutManager linearLayoutManager;
    StudentrequestAdapter adapter;
    ArrayList<RequestStudent> requestStudents;
    private static FirebaseDatabase database;
    private static DatabaseReference myRef = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRequestsManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // call firebase method
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("OpportunityRequests");

        ReadDate();
        return root;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_foundation, someFragment);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    public void ReadDate() {
        binding.opportunityRequestBrogressbar.setVisibility(View.VISIBLE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestStudents = new ArrayList<>();
                requestStudents.clear();
                for (DataSnapshot demo : snapshot.getChildren()) {
                    RequestStudent model = demo.getValue(RequestStudent.class);
                    System.out.println("FID"+ model.getFoundationID());
                    System.out.println("cou"+ Prevalent.currentOnlineOrganization.getId());
                    if (model.getFoundationID().equals(Prevalent.currentOnlineOrganization.getId())) {
                        requestStudents.add(model);
                    }
                }

                if (!requestStudents.isEmpty()) {
                    binding.opportunityRequestBrogressbar.setVisibility(View.GONE);
                    binding.notFoundFoundationOpportunityRequest.setVisibility(View.GONE);
                    binding.recStudent.setVisibility(View.VISIBLE);
                    adapter = new StudentrequestAdapter(requestStudents);
                    linearLayoutManager = new LinearLayoutManager(getContext());
                    binding.recStudent.setAdapter(adapter);
                    binding.recStudent.setLayoutManager(linearLayoutManager);

                    adapter.setOnItemrequest(new StudentrequestAdapter.OnItemrequest() {
                        @Override
                        public void OnItemrequest(RequestStudent requestStudent, int postion) {
                            Fragment selectedScreen = new RequestDetailsFragment(requestStudent);
                            replaceFragment(selectedScreen);
                        }
                    });
                } else {
                    binding.opportunityRequestBrogressbar.setVisibility(View.GONE);
                    binding.notFoundFoundationOpportunityRequest.setVisibility(View.VISIBLE);
                    binding.recStudent.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}