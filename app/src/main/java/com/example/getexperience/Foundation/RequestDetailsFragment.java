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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.adapters.StudentrequestAdapter;
import com.example.getexperience.model.CourseModel;
import com.example.getexperience.model.RequestStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RequestDetailsFragment extends Fragment {

    private View root;
    private RequestStudent student_request;
    private TextView opp_Name, student_Name, sub_No;
    private Button btn_accept, btn_cancle;
    private DatabaseReference reference;

    public RequestDetailsFragment(RequestStudent student_request) {
        this.student_request = student_request;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_request_details, container, false);

        reference = FirebaseDatabase.getInstance().getReference();

        opp_Name = root.findViewById(R.id.request_details_name_data);
        student_Name = root.findViewById(R.id.subscription_details_trainee_data);
        sub_No = root.findViewById(R.id.request_details_description_data);
        btn_accept = root.findViewById(R.id.request_accept_btn);
        btn_cancle = root.findViewById(R.id.request_cancel_btn);

        opp_Name.setText(student_request.getOpportunityName());
        student_Name.setText(student_request.getStudentName());
        sub_No.setText(student_request.getStudentNumber());

        //upload Date
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadacceptRequest();
            }
        });

        //delete item
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadcancleRequest();
            }
        });
        return root;
    }

    public void uploadacceptRequest() {
        //upload Date
        final SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText("Adding");
        loadingBar.setContentText("Please wait...");
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        RequestStudent model = new RequestStudent(student_request.getFoundationID(), student_request.getOpportunityId(), student_request.getStudentId(), student_request.getOpportunityName(), "Accepted", student_request.getStudentName(), student_request.getStudentNumber());

        reference.child("Acceptrequest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("Acceptrequest").child(student_request.getOpportunityId()).setValue(model);
                loadingBar.dismissWithAnimation();
                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle("Request Accepted");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(sDialog -> {
                    dialog.dismiss();
                    RemoveRequest(student_request.getOpportunityId());
                });
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadcancleRequest() {

        //upload Date
        final SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText("Adding");
        loadingBar.setContentText("Please wait...");
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        RequestStudent model = new RequestStudent(student_request.getFoundationID(), student_request.getOpportunityId(), student_request.getStudentId(), student_request.getOpportunityName(), "Refused", student_request.getStudentName(), student_request.getStudentNumber());

        reference.child("CancleRequest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("CancleRequest").child(student_request.getOpportunityId()).setValue(model);
                loadingBar.dismissWithAnimation();
                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle("Request Refused");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(sDialog -> {
                    dialog.dismissWithAnimation();
                    RemoveRequest(student_request.getOpportunityId());

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


    public void RemoveRequest(String opportunityID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query q = ref.child("OpportunityRequests").orderByChild("opportunityID").equalTo(opportunityID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot demo : snapshot.getChildren()) {
                    demo.getRef().removeValue();
                }
                Fragment selectedScreen = new RequestsManagementFragment();
                replaceFragment(selectedScreen);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("e", error.toException() + "");

            }
        });
    }
}