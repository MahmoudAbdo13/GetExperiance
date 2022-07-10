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

import com.example.getexperience.R;
import com.example.getexperience.adapters.NewCourseAdapter;
import com.example.getexperience.adapters.NewOpportunityAdapter;
import com.example.getexperience.databinding.FragmentStudentNewCoursesBinding;
import com.example.getexperience.model.CourseModel;
import com.example.getexperience.model.NewOpportunityModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StudentNewCoursesFragment extends Fragment {

    private FragmentStudentNewCoursesBinding binding;
    private DatabaseReference reference;
    private ArrayList<CourseModel> model;

    public static StudentNewCoursesFragment createFor() {
        StudentNewCoursesFragment fragment = new StudentNewCoursesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentStudentNewCoursesBinding.inflate(inflater, container, false);
         return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("Courses");
        model = new ArrayList<>();
        binding.recyclerStudentNewCourse.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        getNewOpportunities();
    }

    private void getNewOpportunities() {
        binding.studentNewCourseProgressbar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                //binding.notFoundFieldManager.setVisibility(View.VISIBLE);
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CourseModel courseModel = data.getValue(CourseModel.class);
                        model.add(0,courseModel);
                        try {
                            binding.studentNewCourseProgressbar.setVisibility(View.GONE);
                            enableViews(model);
                        } catch (Exception e) {
                            Log.d("Null", "" + e);
                        }
                    }
                }else {
                    binding.studentNewCourseProgressbar.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void enableViews(ArrayList<CourseModel> model){

        if(model.isEmpty()){
            binding.studentNewCourseProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentNewCourse.setVisibility(View.VISIBLE);
            binding.recyclerStudentNewCourse.setVisibility(View.GONE);
        }else {
            binding.studentNewCourseProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentNewCourse.setVisibility(View.GONE);
            binding.recyclerStudentNewCourse.setVisibility(View.VISIBLE);
            NewCourseAdapter adapter = new NewCourseAdapter();
            adapter.setList(model);
            binding.recyclerStudentNewCourse.setAdapter(adapter);

        }
    }

}