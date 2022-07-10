package com.example.getexperience.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getexperience.EventBus.PassMassageActionClick;
import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.databinding.FragmentStudentNewCoursesDetailsBinding;
import com.example.getexperience.model.CourseModel;
import com.example.getexperience.model.CourseRequestModel;
import com.example.getexperience.model.NewOpportunityModel;
import com.example.getexperience.model.OpportunityRequestModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class StudentNewCoursesDetailsFragment extends Fragment {

    private FragmentStudentNewCoursesDetailsBinding binding;
    private static CourseModel model;
    private DatabaseReference reference;

    public static StudentNewCoursesDetailsFragment createFor(CourseModel courseModel) {
        StudentNewCoursesDetailsFragment fragment = new StudentNewCoursesDetailsFragment();
        model = courseModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentNewCoursesDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("CourseRequests");

        binding.studentNewCourseDetailsNameData.setText(model.getCourseName());
        binding.studentNewCourseDetailsInstructorData.setText(model.getCourseInstrcutor());
        binding.studentNewCourseDetailsTimeData.setText(model.getCourseTime());
        binding.studentNewCourseDetailsStartDateData.setText(model.getStartDate());
        binding.studentNewCourseDetailsEndDateData.setText(model.getEndDate());
        binding.studentNewCourseDetailsDescriptionData.setText(model.getDescription());

        binding.studentNewCourseDetailsJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCourseRequest();
            }
        });
    }

    private void sendCourseRequest() {
        CourseRequestModel requestModel = new CourseRequestModel(model.getFoundationID(),model.getCourseId(), model.getCourseName(),model.getCourseInstrcutor(), model.getEndDate(), "Not Accepted Yet", Prevalent.currentOnlineStudent.getId(), Prevalent.currentOnlineStudent.getName(), Prevalent.currentOnlineStudent.getNumber(),model.getCourseId()+Prevalent.currentOnlineStudent.getNumber());
        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Confirm Request");
        dialog.setContentText("Do you really want to Join to this course");
        dialog.setConfirmText("Yes");
        dialog.setConfirmClickListener(sDialog -> {
            sDialog.dismiss();
            reference.child(model.getCourseId() + Prevalent.currentOnlineStudent.getNumber()).setValue(requestModel);

            SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.setTitle("Your Request has been send successfully");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setConfirmClickListener(ssDialog -> {
                EventBus.getDefault().postSticky(new PassMassageActionClick("New Courses"));
                ssDialog.dismissWithAnimation();
            });
            alertDialog.show();
        });
        dialog.setCancelText("No");
        dialog.setCancelClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismiss();
        });
        dialog.show();

    }

}