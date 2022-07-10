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
import com.example.getexperience.adapters.MyCourseAdapter;
import com.example.getexperience.adapters.NewCourseAdapter;
import com.example.getexperience.databinding.FragmentMyCoursesBinding;
import com.example.getexperience.databinding.FragmentStudentNewCoursesBinding;
import com.example.getexperience.model.CourseModel;
import com.example.getexperience.model.CourseRequestModel;
import com.example.getexperience.model.NewOpportunityModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyCoursesFragment extends Fragment {

    private FragmentMyCoursesBinding binding;
    private DatabaseReference reference;
    private ArrayList<CourseRequestModel> model;

    public static MyCoursesFragment createFor() {
        MyCoursesFragment fragment = new MyCoursesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyCoursesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("AcceptCourseRequest");
        model = new ArrayList<>();
        binding.recyclerStudentMyCourse.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getMyCourses();
    }

    private void getMyCourses() {
        binding.studentMyCourseProgressbar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CourseRequestModel courseRequestModel = data.getValue(CourseRequestModel.class);
                        if (courseRequestModel.getStudentNumber().equals(Prevalent.currentOnlineStudent.getNumber())) {
                            model.add(0, courseRequestModel);
                            try {
                                binding.studentMyCourseProgressbar.setVisibility(View.GONE);
                                enableViews(model);
                            } catch (Exception e) {
                                Log.d("Null", "" + e);
                            }
                        }
                    }
                    enableViews(model);
                } else {
                    binding.studentMyCourseProgressbar.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

/*    private void getMyCourses() {
        binding.studentMyCourseProgressbar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    model.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            CourseRequestModel requestModel = data.getValue(CourseRequestModel.class);
                            System.out.println("CourseRequestModel Student Id: " + requestModel.getStudentId());
                            if (requestModel.getStudentNumber().equals(Prevalent.currentOnlineStudent.getNumber())) {
                                model.add(0, requestModel);
                                try {
                                    binding.studentMyCourseProgressbar.setVisibility(View.GONE);
                                    enableViews(model);
                                } catch (Exception e) {
                                    Log.d("Null", "" + e);
                                }
                            }
                        }
                    } else {
                        binding.studentMyCourseProgressbar.setVisibility(View.GONE);
                        enableViews(model);
                    }
                } else {
                    binding.studentMyCourseProgressbar.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void enableViews(ArrayList<CourseRequestModel> model) {

        if (model.isEmpty()) {
            binding.studentMyCourseProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentMyCourse.setVisibility(View.VISIBLE);
            binding.recyclerStudentMyCourse.setVisibility(View.GONE);
        } else {
            binding.studentMyCourseProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentMyCourse.setVisibility(View.GONE);
            binding.recyclerStudentMyCourse.setVisibility(View.VISIBLE);
            MyCourseAdapter adapter = new MyCourseAdapter();
            adapter.setList(model);
            binding.recyclerStudentMyCourse.setAdapter(adapter);

        }
    }

}