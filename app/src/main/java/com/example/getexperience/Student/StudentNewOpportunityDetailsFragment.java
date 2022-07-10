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
import com.example.getexperience.databinding.FragmentStudentNewOpportunityDetailsBinding;
import com.example.getexperience.model.OrganizationModel;
import com.example.getexperience.model.NewOpportunityModel;
import com.example.getexperience.model.OpportunityRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class StudentNewOpportunityDetailsFragment extends Fragment {

    private FragmentStudentNewOpportunityDetailsBinding binding;
    private static NewOpportunityModel model;
    private DatabaseReference reference;

    public static StudentNewOpportunityDetailsFragment createFor(NewOpportunityModel newOpportunityModel) {
        StudentNewOpportunityDetailsFragment fragment = new StudentNewOpportunityDetailsFragment();
        model = newOpportunityModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentNewOpportunityDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Foundation").child(model.getFoundationID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganizationModel organizationModel = snapshot.getValue(OrganizationModel.class);
                binding.studentNewOpportunityFoundationNameData.setText(organizationModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.studentNewOpportunityDetailsNameData.setText(model.getOpportunityName());
        binding.studentNewOpportunityDetailsTypeData.setText(model.getOpportunityType());
        binding.studentNewOpportunityDetailsFieldData.setText(model.getOpportunityField());
        binding.studentNewOpportunityDetailsDescriptionData.setText(model.getDescription());
        binding.studentNewOpportunityDetailsStartDateData.setText(model.getStartDate());
        binding.studentNewOpportunityDetailsEndDateData.setText(model.getEndDate());
        binding.studentNewOpportunityDetailsDaysNumberData.setText(model.getNumberOfDays());

        binding.opportunityDetailsJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        OpportunityRequestModel requestModel = new OpportunityRequestModel(model.getOpportunityID(), model.getOpportunityName(), "Not Accepted Yet", Prevalent.currentOnlineStudent.getId(), Prevalent.currentOnlineStudent.getNumber(), Prevalent.currentOnlineStudent.getName(), model.getFoundationID(),model.getEndDate());
        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Confirm Request");
        dialog.setContentText("Do you really want to Join to this Opportunity");
        dialog.setConfirmText("Yes");
        dialog.setConfirmClickListener(sDialog -> {
            sDialog.dismiss();
            reference.child("OpportunityRequests").child(model.getOpportunityID() + Prevalent.currentOnlineStudent.getNumber()).setValue(requestModel);

            SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
            alertDialog.setTitle("Your Request has been send successfully");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setConfirmClickListener(ssDialog -> {
                EventBus.getDefault().postSticky(new PassMassageActionClick("Back"));
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