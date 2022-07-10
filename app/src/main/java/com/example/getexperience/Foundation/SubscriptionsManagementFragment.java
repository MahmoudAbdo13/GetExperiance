package com.example.getexperience.Foundation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.adapters.SubscrbtionAdapter;
import com.example.getexperience.databinding.FragmentSubscriptionsManagementBinding;
import com.example.getexperience.model.CourseRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SubscriptionsManagementFragment extends Fragment {

    SubscrbtionAdapter adapter;
    ArrayList<CourseRequestModel> submodles;
    LinearLayoutManager linearLayoutManager;
    private FragmentSubscriptionsManagementBinding binding;
    private static FirebaseDatabase database;
    private static DatabaseReference myRef = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSubscriptionsManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // call firebase method
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("CourseRequests");
        ReadDate();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //  binding = null;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_foundation, someFragment);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    // Read Date from firebase

    public void ReadDate() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                submodles = new ArrayList<>();
                submodles.clear();
                for (DataSnapshot demo : snapshot.getChildren()) {
                    CourseRequestModel model = demo.getValue(CourseRequestModel.class);
                    if (model.getFoundationID().equals(Prevalent.currentOnlineOrganization.getId())) {

                        Log.e("key",demo.getKey()+"");
                        submodles.add(0, new CourseRequestModel(model.getFoundationID(),model.getCourseId(),model.getCourseName(),model.getCourseInstrcutor(),model.getEndDate(),model.getRequestStatus(),model.getStudentId(),model.getStudentName(),model.getStudentNumber(),demo.getKey()));
                    }
                }

                adapter = new SubscrbtionAdapter(submodles);
                linearLayoutManager = new LinearLayoutManager(getContext());
                binding.recycleSubscription.setAdapter(adapter);
                binding.recycleSubscription.setLayoutManager(linearLayoutManager);

                adapter.setOnItemrequest(new SubscrbtionAdapter.OnItemSubscribrequest() {
                    @Override
                    public void OnItemsubscriberequest(CourseRequestModel subModels, int postion) {
                        Fragment selectedScreen = new SubscriptionsDetailsFragment(subModels);
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