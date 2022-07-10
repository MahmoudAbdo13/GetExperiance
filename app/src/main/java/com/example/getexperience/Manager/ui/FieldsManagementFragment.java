package com.example.getexperience.Manager.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.getexperience.R;
import com.example.getexperience.adapters.FieldAdapter;
import com.example.getexperience.databinding.FragmentFieldManagementBinding;
import com.example.getexperience.model.FieldModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FieldsManagementFragment extends Fragment {

    private FragmentFieldManagementBinding binding;
    private DatabaseReference request;
    private ArrayList<FieldModel> model;
    private TextView notFoundNewFound;
    private int fieldID;

    public static FieldsManagementFragment createFor() {
        FieldsManagementFragment fragment = new FieldsManagementFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFieldManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFieldDialog();

            }
        });
        binding.recyclerFieldManager.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        request = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();

        getFields();

    }

    private void getFields() {
        request.child("FieldsData").child("Fields").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        FieldModel fieldModel = data.getValue(FieldModel.class);
                        model.add(fieldModel);
                        try {
                            binding.fieldProgressBarManager.setVisibility(View.GONE);
                            enableViews(model);
                        } catch (Exception e) {
                            Log.d("Null", "" + e);
                            System.out.println("Error  " + e);
                        }
                    }
                } else {
                    binding.fieldProgressBarManager.setVisibility(View.GONE);
                    enableViews(model);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableViews(ArrayList<FieldModel> model){

        if(model.isEmpty()){
            binding.fieldProgressBarManager.setVisibility(View.GONE);
            binding.notFoundFieldManager.setVisibility(View.VISIBLE);
            binding.recyclerFieldManager.setVisibility(View.GONE);
        }else {
            binding.fieldProgressBarManager.setVisibility(View.GONE);
            binding.notFoundFieldManager.setVisibility(View.GONE);
            binding.recyclerFieldManager.setVisibility(View.VISIBLE);
            FieldAdapter adapter = new FieldAdapter();
            adapter.setList(model);
            binding.recyclerFieldManager.setAdapter(adapter);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showAddFieldDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog, null);
        final Button addFieldBtn, cancelBtn;
        final EditText nameData;

        dialog.setContentView(mView);

        nameData = mView.findViewById(R.id.dialog_add_field_name_data);
        addFieldBtn = mView.findViewById(R.id.dialog_add_field_btn);
        cancelBtn = mView.findViewById(R.id.dialog_cancel_btn);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addFieldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameData.getText().toString();
                if (name.isEmpty()){
                    nameData.setError("Please Enter Field Name");
                }else {
                    request.child("FieldsData").child("field_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                fieldID = snapshot.getValue(Integer.class);
                                request.child("FieldsData").child("field_ID").setValue(fieldID + 1);
                                FieldModel model = new FieldModel(String.valueOf(fieldID), name);
                                request.child("FieldsData").child("Fields").child(String.valueOf(fieldID)).setValue(model);
                                Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(getContext(), "There Is No Field IDs", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }



}
