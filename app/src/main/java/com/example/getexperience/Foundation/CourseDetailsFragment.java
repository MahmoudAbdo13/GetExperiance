package com.example.getexperience.Foundation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.model.CourseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CourseDetailsFragment extends Fragment {

    TextView course_Name, course_Instructor, course_time, course_sdata, course_enddata, course_description;
    CourseModel model;
    View root;

    Button btn_delete;

    public CourseDetailsFragment() {

    }

    public CourseDetailsFragment(CourseModel model) {
        Prevalent.courseModel = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_course_details, container, false);

        course_Name = root.findViewById(R.id.course_details_name_data);
        course_Instructor = root.findViewById(R.id.course_details_instructor_data);
        course_time = root.findViewById(R.id.course_details_time_data);
        course_sdata = root.findViewById(R.id.course_details_start_date_data);
        course_enddata = root.findViewById(R.id.course_details_end_date_data);
        course_description = root.findViewById(R.id.course_details_description_data);
        btn_delete = root.findViewById(R.id.course_details_delete_btn);

        course_Name.setText(Prevalent.courseModel.getCourseName());
        course_Instructor.setText(Prevalent.courseModel.getCourseInstrcutor());
        course_time.setText(Prevalent.courseModel.getCourseTime());
        course_sdata.setText(Prevalent.courseModel.getStartDate());
        course_enddata.setText(Prevalent.courseModel.getEndDate());
        course_description.setText(Prevalent.courseModel.getDescription());
        Log.e( "delete ID: ", Prevalent.courseModel.getCourseId());


        // Delete item
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitle("Are you want to delete?");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(sDialog -> {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query q = ref.child("Courses").orderByChild("courseId").equalTo(Prevalent.courseModel.getCourseId());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot demo : snapshot.getChildren()) {
                                demo.getRef().removeValue();
                            }
                            Log.e("s", "delete sucess");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("e", error.toException() + "");
                        }
                    });
                    dialog.dismiss();
                    Fragment selectedScreen = new CoursesManagementFragment();
                    replaceFragment(selectedScreen);

                }).setCancelText("Cancel").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.e("c", "cancle");
                        dialog.dismiss();
                        Fragment selectedScreen = new CoursesManagementFragment();
                        replaceFragment(selectedScreen);
                    }
                });
                dialog.show();
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

}