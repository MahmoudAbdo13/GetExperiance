package com.example.getexperience.Foundation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.getexperience.R;
import com.example.getexperience.databinding.FragmentSubscriptionsDetailsBinding;
import com.example.getexperience.model.CourseRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SubscriptionsDetailsFragment extends Fragment {

    private FragmentSubscriptionsDetailsBinding binding;
    private CourseRequestModel models;
    private DatabaseReference reference;

    public SubscriptionsDetailsFragment(CourseRequestModel models) {
        this.models = models;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSubscriptionsDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        reference = FirebaseDatabase.getInstance().getReference();

        binding.subscriptionDetailsNameData.setText(models.getCourseName());
        binding.subscriptionDetailsTraineeData.setText(models.getStudentName());
        binding.courseDetailsSubscriptionNumberData.setText(models.getStudentNumber());

        binding.subscriptionAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSubscribeAccept();
            }
        });

        binding.subscriptionCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadcancleSubscribe();
            }
        });

        return root;
    }

    public void uploadSubscribeAccept() {
        //upload Date
        final SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText("Sending");
        loadingBar.setContentText("Please wait...");
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        CourseRequestModel submodel = new CourseRequestModel(models.getFoundationID(), models.getCourseId(), models.getCourseName(),models.getCourseInstrcutor(), models.getEndDate() ,"Accept", models.getStudentId(), models.getStudentName(), models.getStudentNumber(),models.getCourseKey());

        reference.child("AcceptCourseRequest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("AcceptCourseRequest").child(models.getCourseKey()).setValue(submodel);
                loadingBar.dismissWithAnimation();
                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle("Request Accepted");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(sDialog -> {
                    dialog.dismissWithAnimation();
                    RemoveRequest(models.getCourseId(),models.getCourseKey());
                });
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void uploadcancleSubscribe() {
        //upload Date
        final SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText("Adding");
        loadingBar.setContentText("Please wait...");
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        CourseRequestModel submodel = new CourseRequestModel(models.getFoundationID(), models.getCourseId(), models.getCourseName(), models.getCourseInstrcutor(), models.getEndDate() ,"Refused", models.getStudentId(), models.getStudentName(), models.getStudentNumber(),models.getCourseKey());

        reference.child("CancleCourseRequest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("CancleCourseRequest").child(models.getCourseKey()).setValue(submodel);
                loadingBar.dismissWithAnimation();
                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle("Request Refused");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(sDialog -> {
                    dialog.dismissWithAnimation();
                    RemoveRequest(models.getCourseId(),models.getCourseKey());


                });
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_foundation, someFragment);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }


    public void RemoveRequest(String CourseId,String key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query q = ref.child("CourseRequests").orderByChild("courseId").equalTo(CourseId);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot demo : snapshot.getChildren()) {

                 if(models.getCourseKey().equals(demo.getKey())) {
                        demo.getRef().removeValue();

                    }
                }
                Fragment selectedScreen = new SubscriptionsManagementFragment();
                replaceFragment(selectedScreen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("e", error.toException() + "");

            }
        });


    }

}