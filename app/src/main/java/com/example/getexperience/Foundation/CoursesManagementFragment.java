package com.example.getexperience.Foundation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.adapters.CourseAdapter;
import com.example.getexperience.databinding.FragmentCoursesManagementBinding;
import com.example.getexperience.model.CourseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CoursesManagementFragment extends Fragment {

    private FragmentCoursesManagementBinding binding;
    private CourseAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<CourseModel> models;
    private static FirebaseDatabase database;
    private static DatabaseReference myRef = null;

    public CoursesManagementFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCoursesManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // call firebase method
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Courses");

        ReadDate();
        binding.addCourseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment selectedScreen = new AddCourseFragment();
                replaceFragment(selectedScreen);
            }
        });
        return root;
    }

    // Method to Move from Current Fragment to other
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_foundation, someFragment);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    // Read Data from firebase ->Courses Table

    public void ReadDate() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models = new ArrayList<>();
                models.clear();
                for (DataSnapshot demo : snapshot.getChildren()) {
                    CourseModel model = demo.getValue(CourseModel.class);
                    if (model.getFoundationID().equals(Prevalent.currentOnlineOrganization.getId())) {
                        models.add(0,model);
                    }
                    Log.e("course: ", model.getCourseName());
                }
                adapter = new CourseAdapter(models);
                linearLayoutManager = new LinearLayoutManager(getContext());
                binding.recyCourse.setAdapter(adapter);
                binding.recyCourse.setLayoutManager(linearLayoutManager);

                adapter.setOnCourseItemclick(new CourseAdapter.OnCourseItemclick() {
                    @Override
                    public void Onitemclick(CourseModel models, int pos) {
                        Fragment selectedScreen = new CourseDetailsFragment(models);
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