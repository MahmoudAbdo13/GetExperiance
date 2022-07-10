package com.example.getexperience.Manager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.getexperience.EventBus.PassMassageActionClick;
import com.example.getexperience.Prevalent;
import com.example.getexperience.databinding.FragmentBlockedFoundationDetailsBinding;
import com.example.getexperience.model.OrganizationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BlockedFoundationDetailsFragment extends Fragment {

    private FragmentBlockedFoundationDetailsBinding binding;

    public static BlockedFoundationDetailsFragment createFor(OrganizationModel organizationModel) {
        BlockedFoundationDetailsFragment fragment = new BlockedFoundationDetailsFragment();
        Prevalent.currentOnlineOrganization = organizationModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBlockedFoundationDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.managerBlockedFoundationNameData.setText(Prevalent.currentOnlineOrganization.getName());
        binding.managerUnblockedFoundationAddressData.setText(Prevalent.currentOnlineOrganization.getAddress());
        binding.managerUnblockedFoundationEmailData.setText(Prevalent.currentOnlineOrganization.getEmail());
        binding.managerUnblockedFoundationPhoneData.setText(Prevalent.currentOnlineOrganization.getPhone());
        Glide.with(view).load(Prevalent.currentOnlineOrganization.getImageUrl()).into(binding.managerUnblockedFoundationImage);

        binding.managerUnblockedFoundationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirm Unblock")
                        .setContentText("Do you really want to unblock this Organization")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismiss();
                            unblockFoundation();
                            // Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        }).setCancelText("No")
                        .setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                        }).show();
            }
        });
    }

    private void unblockFoundation() {
        SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText("Unblocking");
        loadingBar.setContentText("Please wait...");
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Foundation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("Foundation").child(Prevalent.currentOnlineOrganization.getId()).setValue(Prevalent.currentOnlineOrganization);
                reference.child("blockedFoundations").child(Prevalent.currentOnlineOrganization.getId()).removeValue();
                loadingBar.dismissWithAnimation();
                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle("Organization account has been unblocked successfully");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(sDialog -> {
                    EventBus.getDefault().postSticky(new PassMassageActionClick("DisplayBlockedFoundationManagement"));
                    dialog.dismissWithAnimation();
                });
                dialog.show();
                Toast.makeText(getContext(), "UnBlocked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}