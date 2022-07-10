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

import com.example.getexperience.adapters.OpportunityAdapter;
import com.example.getexperience.databinding.FragmentOpportunitiesManagementBinding;
import com.example.getexperience.model.NewOpportunityModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OpportunityManagementFragment extends Fragment {

    private LinearLayoutManager linearLayoutManager;
    private OpportunityAdapter adapter;
    private static FirebaseDatabase database;
    private static DatabaseReference myRef = null;
    private ArrayList<NewOpportunityModel> Opportunitymodel;
    private FragmentOpportunitiesManagementBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOpportunitiesManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // call firebase method
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("OPPortunities");
        ReadDate();

        binding.addOpportunityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedScreen = new AddOpportunityFragment();
                replaceFragment(selectedScreen);
            }
        });
        return root;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction().remove(new OpportunityManagementFragment());
        transaction.replace(R.id.nav_host_fragment_content_foundation, someFragment);
        transaction.commit();
    }

    // upload Date from firebase

    public void ReadDate() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Opportunitymodel = new ArrayList<>();
                Opportunitymodel.clear();
                for (DataSnapshot demo : snapshot.getChildren()) {
                    NewOpportunityModel model = demo.getValue(NewOpportunityModel.class);
                    if (model.getFoundationID().equals(Prevalent.currentOnlineOrganization.getId())) {
                        Opportunitymodel.add(0, new NewOpportunityModel(model.getOpportunityName(), model.getOpportunityType(), model.getOpportunityField(), model.getStartDate(), model.getEndDate(), model.getNumberOfDays(), model.getNumberOfHours(), model.getDescription(), model.getFoundationID(), model.getOpportunityID()));
                    }
                }
                adapter = new OpportunityAdapter(Opportunitymodel);
                linearLayoutManager = new LinearLayoutManager(getContext());
                binding.recycleFoundtionHome.setAdapter(adapter);
                binding.recycleFoundtionHome.setLayoutManager(linearLayoutManager);

                adapter.setOnOppornuityItemclick(new OpportunityAdapter.OnOppornuityItemclick() {
                    @Override
                    public void Onitemclick(NewOpportunityModel models, int pos) {
                        Fragment selectedScreen = new OpportunityDetailsFragment(models);
                        replaceFragment(selectedScreen);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}