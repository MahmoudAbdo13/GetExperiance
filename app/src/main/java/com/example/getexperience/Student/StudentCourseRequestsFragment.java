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
import com.example.getexperience.adapters.CourseRequestAdapter;
import com.example.getexperience.adapters.OpportunityRequestAdapter;
import com.example.getexperience.databinding.FragmentDownloadCertificateBinding;
import com.example.getexperience.databinding.FragmentStudentCourseRequestsBinding;
import com.example.getexperience.databinding.FragmentStudentRequestStateBinding;
import com.example.getexperience.model.CourseRequestModel;
import com.example.getexperience.model.OpportunityRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentCourseRequestsFragment extends Fragment {

    private FragmentStudentCourseRequestsBinding binding;
    private DatabaseReference reference;
    private ArrayList<CourseRequestModel> model;
    public static StudentCourseRequestsFragment createFor() {
        StudentCourseRequestsFragment fragment = new StudentCourseRequestsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentCourseRequestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();
        binding.recyclerStudentCourseRequestState.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getCourseRequests();
    }

    private void getCourseRequests() {
        binding.studentCourseRequestStateProgressbar.setVisibility(View.VISIBLE);
        reference.child("CourseRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CourseRequestModel requestStudent = data.getValue(CourseRequestModel.class);
                        System.out.println("Student Id: "+requestStudent.getStudentId());
                        if (requestStudent.getStudentId().equals(Prevalent.currentOnlineStudent.getId())) {
                            model.add(0, requestStudent);
                            try {
                                binding.studentCourseRequestStateProgressbar.setVisibility(View.GONE);
                                enableViews(model);
                            } catch (Exception e) {
                                Log.d("Null", "" + e);
                            }
                        }
                    }
                    enableViews(model);
                } else {
                    binding.studentCourseRequestStateProgressbar.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableViews(ArrayList<CourseRequestModel> model) {
        if (model.isEmpty()) {
            binding.studentCourseRequestStateProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentCourseRequestState.setVisibility(View.VISIBLE);
            binding.recyclerStudentCourseRequestState.setVisibility(View.GONE);
        } else {
            binding.studentCourseRequestStateProgressbar.setVisibility(View.GONE);
            binding.notFoundStudentCourseRequestState.setVisibility(View.GONE);
            binding.recyclerStudentCourseRequestState.setVisibility(View.VISIBLE);
            CourseRequestAdapter adapter = new CourseRequestAdapter();
            adapter.setList(model);
            binding.recyclerStudentCourseRequestState.setAdapter(adapter);
        }
    }


}