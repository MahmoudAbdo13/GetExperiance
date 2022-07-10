package com.example.getexperience.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getexperience.EventBus.PassMassageActionClick;
import com.example.getexperience.R;
import com.example.getexperience.databinding.FragmentStudentNewCoursesBinding;
import com.example.getexperience.databinding.FragmentStudentRequestStateBinding;

import org.greenrobot.eventbus.EventBus;

public class StudentRequestStateFragment extends Fragment {

    private FragmentStudentRequestStateBinding binding;

    public static StudentRequestStateFragment createFor() {
        StudentRequestStateFragment fragment = new StudentRequestStateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentRequestStateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cardOpportunityRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new PassMassageActionClick("DisplayOpportunityRequest"));
                //getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_sutdent,new StudentOpportunityRequestsFragment()).commit();
            }
        });

        binding.cardCourseRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new PassMassageActionClick("Display Course Request"));
                //getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_sutdent,new StudentCourseRequestsFragment()).commit();
            }
        });
    }
}