package com.example.getexperience.Foundation;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.model.CourseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddCourseFragment extends Fragment {

    View root;
    EditText c_Name, C_instructor, c_time, start_date, end_date, c_Desc;
    String courseName, courseInstructor, courseTime, courseStartDate, courseEndDate, courseDescription;
    Button add_btn;
    final Calendar myCalendar = Calendar.getInstance();
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_course, container, false);
        reference = FirebaseDatabase.getInstance().getReference();

        //ge ID
        c_Name = root.findViewById(R.id.add_course_name);
        C_instructor = root.findViewById(R.id.add_course_instructor);
        c_time = root.findViewById(R.id.add_course_time);
        start_date = root.findViewById(R.id.add_course_start_date);
        end_date = root.findViewById(R.id.add_course_end_date);
        c_Desc = root.findViewById(R.id.add_course_description);
        add_btn = root.findViewById(R.id.add_opportunity_btn);

        // get StartDate  ->DatePickerDialog
        DatePickerDialog.OnDateSetListener stdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabelstDate();
            }
        };

        //getEndDate ->DatePickerDialog
        DatePickerDialog.OnDateSetListener enddate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabelendDate();
            }
        };

        //  handle click to open DatePickerDialog _> start_date
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), stdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        //  handle click to open DatePickerDialog ->EndData
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), enddate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // click to  upload Data
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call method to upload Data
                uploadToDatabase();
            }
        });
        return root;
    }


    public void uploadToDatabase() {
        // get Date from user
        courseName = c_Name.getText().toString();
        courseInstructor = C_instructor.getText().toString();
        courseTime = c_time.getText().toString();
        courseStartDate = start_date.getText().toString();
        courseEndDate = end_date.getText().toString();
        courseDescription = c_Desc.getText().toString();

        // Check validation
        if (courseName.isEmpty()) {
            c_Name.setError("Please enter your  CourseName");
            c_Name.setFocusable(true);
        } else if (courseInstructor.isEmpty()) {
            C_instructor.setError("Please enter your  Course Instructor");
            C_instructor.setFocusable(true);
        } else if (courseTime.isEmpty()) {
            c_time.setError("Please enter your  Time");
            c_time.setFocusable(true);
        } else if (courseStartDate.isEmpty()) {
            start_date.setError("Please enter your  Start Date");
            start_date.setFocusable(true);
        } else if (courseEndDate.isEmpty()) {
            end_date.setError("Please enter your  End Date");
            end_date.setFocusable(true);
        } else if (courseDescription.isEmpty()) {
            c_Desc.setError("Please enter your  Description");
            c_Desc.setFocusable(true);
        } else {
            //upload Date

            final SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            loadingBar.setTitleText("Adding");
            loadingBar.setContentText("Please wait...");
            loadingBar.setCancelable(false);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            String coursekey = reference.push().getKey();
            CourseModel model = new CourseModel(courseName, courseInstructor, courseTime, courseStartDate
                    , courseEndDate, courseDescription, coursekey, Prevalent.currentOnlineOrganization.getId());
            reference.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reference.child("Courses").child(coursekey).setValue(model);
                    loadingBar.dismissWithAnimation();
                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("Courses has been added successfully");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        Log.e("course_add", "course sucess");
                        dialog.dismissWithAnimation();
                        Fragment selectedScreen = new CoursesManagementFragment();
                        replaceFragment(selectedScreen);
                    });
                    dialog.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // make formate to startDate
    private void updateLabelstDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        start_date.setText(dateFormat.format(myCalendar.getTime()));
    }

    // make formate to EndDate
    private void updateLabelendDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        end_date.setText(dateFormat.format(myCalendar.getTime()));
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