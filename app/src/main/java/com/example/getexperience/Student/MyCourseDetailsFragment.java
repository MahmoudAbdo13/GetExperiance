package com.example.getexperience.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getexperience.R;
import com.example.getexperience.databinding.FragmentMyCourseDetailsBinding;
import com.example.getexperience.model.CourseModel;
import com.example.getexperience.model.CourseRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyCourseDetailsFragment extends Fragment {

    private FragmentMyCourseDetailsBinding binding;
    private static CourseRequestModel model;

    public static MyCourseDetailsFragment createFor(CourseRequestModel courseModel) {
        MyCourseDetailsFragment fragment = new MyCourseDetailsFragment();
        model = courseModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyCourseDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase.getInstance().getReference("Courses").child(model.getCourseId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CourseModel model = snapshot.getValue(CourseModel.class);
                binding.studentMyCourseDetailsNameData.setText(model.getCourseName());
                binding.studentMyCourseDetailsDescriptionData.setText(model.getDescription());
                binding.studentMyCourseDetailsInstructorData.setText(model.getCourseInstrcutor());
                binding.studentMyCourseDetailsStartDateData.setText(model.getStartDate());
                binding.studentMyCourseDetailsEndDateData.setText(model.getEndDate());
                binding.studentMyCourseDetailsTimeData.setText(model.getCourseTime());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}