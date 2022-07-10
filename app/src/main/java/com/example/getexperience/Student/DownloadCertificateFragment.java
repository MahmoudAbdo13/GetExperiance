package com.example.getexperience.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getexperience.EventBus.PassMassageActionClick;
import com.example.getexperience.Manager.ui.FoundationManagementFragment;
import com.example.getexperience.R;
import com.example.getexperience.databinding.FragmentDownloadCertificateBinding;

import org.greenrobot.eventbus.EventBus;

public class DownloadCertificateFragment extends Fragment {

    private FragmentDownloadCertificateBinding binding;

    public static DownloadCertificateFragment createFor() {
        DownloadCertificateFragment fragment = new DownloadCertificateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDownloadCertificateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cardOpportunityCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new PassMassageActionClick("OpportunityCertificates"));
            }
        });

        binding.cardCourseCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new PassMassageActionClick("CourseCertificates"));
            }
        });
    }
}