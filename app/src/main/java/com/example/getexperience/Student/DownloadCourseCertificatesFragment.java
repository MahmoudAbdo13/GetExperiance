package com.example.getexperience.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.adapters.CertificateAdapter;
import com.example.getexperience.databinding.FragmentDownloadCertificateBinding;
import com.example.getexperience.databinding.FragmentDownloadCourseCertificatesBinding;
import com.example.getexperience.databinding.FragmentDownloadOpportunityCertificatesBinding;
import com.example.getexperience.model.CertificateModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DownloadCourseCertificatesFragment extends Fragment {

    private FragmentDownloadCourseCertificatesBinding binding;
    private DatabaseReference reference;
    private ArrayList<CertificateModel> model;

    public static DownloadCourseCertificatesFragment createFor() {
        DownloadCourseCertificatesFragment fragment = new DownloadCourseCertificatesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDownloadCourseCertificatesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference().child("CourseCertificates").child("Certificate");
        model = new ArrayList<>();
        binding.recyclerCourseCertificate.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getOpportunityCertificates();
    }

    private void getOpportunityCertificates() {
        binding.certificateCourseProgressbar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                //binding.notFoundFieldManager.setVisibility(View.VISIBLE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CertificateModel certificateModel = data.getValue(CertificateModel.class);
                        Log.e("certificateModel", "" + certificateModel.getStudentNumber());
                        if (certificateModel.getStudentNumber().equals(Prevalent.currentOnlineStudent.getNumber())) {
                            model.add(0, certificateModel);
                            try {
                                binding.certificateCourseProgressbar.setVisibility(View.GONE);
                                enableViews(model);
                            } catch (Exception e) {
                                Log.e("getOpportunityCertificates", "" + e);
                            }
                        }else{
                            binding.certificateCourseProgressbar.setVisibility(View.GONE);
                            enableViews(model);
                        }
                    }
                } else {
                    binding.certificateCourseProgressbar.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableViews(ArrayList<CertificateModel> model) {

        if (model.isEmpty()) {
            binding.certificateCourseProgressbar.setVisibility(View.GONE);
            binding.notFoundCourseCertificate.setVisibility(View.VISIBLE);
            binding.recyclerCourseCertificate.setVisibility(View.GONE);
        } else {
            binding.certificateCourseProgressbar.setVisibility(View.GONE);
            binding.notFoundCourseCertificate.setVisibility(View.GONE);
            binding.recyclerCourseCertificate.setVisibility(View.VISIBLE);
            CertificateAdapter adapter = new CertificateAdapter();
            adapter.setList(model);
            binding.recyclerCourseCertificate.setAdapter(adapter);

        }
    }
}