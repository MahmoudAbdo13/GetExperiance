package com.example.getexperience.Manager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.getexperience.EventBus.PassMassageActionClick;
import com.example.getexperience.Prevalent;
import com.example.getexperience.databinding.FragmentManagerFoundationDetailsBinding;
import com.example.getexperience.model.OrganizationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManagerFoundationDetailsFragment extends Fragment {

    private FragmentManagerFoundationDetailsBinding binding;
    private static OrganizationModel model;

    public static ManagerFoundationDetailsFragment createFor(OrganizationModel organizationModel) {
        ManagerFoundationDetailsFragment fragment = new ManagerFoundationDetailsFragment();
        Prevalent.currentOnlineOrganization = organizationModel;
        model = organizationModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentManagerFoundationDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.managerFoundationNameData.setText(Prevalent.currentOnlineOrganization.getName());
        binding.managerFoundationAddressData.setText(Prevalent.currentOnlineOrganization.getAddress());
        binding.managerFoundationAddressData.setText(model.getAddress());
        System.out.println(Prevalent.currentOnlineOrganization.getAddress());
        System.out.println(model.getAddress());
        binding.managerFoundationEmailData.setText(Prevalent.currentOnlineOrganization.getEmail());
        binding.managerFoundationPhoneData.setText(Prevalent.currentOnlineOrganization.getPhone());
        Glide.with(view).load(Prevalent.currentOnlineOrganization.getImageUrl()).into(binding.managerFoundationImage);

        binding.managerFieldDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirm Block")
                        .setContentText("Do you really want to block this Organization")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismiss();
                            blockFoundation();
                        }).setCancelText("No")
                        .setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                        }).show();
            }
        });
    }

    private void blockFoundation() {
        SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText("Blocking");
        loadingBar.setContentText("Please wait...");
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("blockedFoundations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("blockedFoundations").child(Prevalent.currentOnlineOrganization.getId()).setValue(Prevalent.currentOnlineOrganization);
                reference.child("Foundation").child(Prevalent.currentOnlineOrganization.getId()).removeValue();
                loadingBar.dismissWithAnimation();
                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle("Organization account has been blocked successfully");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(sDialog -> {
                    EventBus.getDefault().postSticky(new PassMassageActionClick("DisplayFoundationManagement"));
                    dialog.dismissWithAnimation();
                });
                dialog.show();
                Toast.makeText(getContext(), "Blocked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}