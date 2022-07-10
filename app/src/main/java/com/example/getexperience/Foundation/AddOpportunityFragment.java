package com.example.getexperience.Foundation;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.getexperience.Prevalent;
import com.example.getexperience.R;
import com.example.getexperience.databinding.FragmentAddOpportunityBinding;
import com.example.getexperience.model.FieldModel;
import com.example.getexperience.model.NewOpportunityModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddOpportunityFragment extends Fragment {

    String OpportName, opprtType, OpprtField, StartDate, EndDate, Numberofdays, Numberofhours, Description;

    private DatabaseReference reference;
    final Calendar myCalendar = Calendar.getInstance();
    private FragmentAddOpportunityBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentAddOpportunityBinding.inflate(inflater, container, false);

        reference = FirebaseDatabase.getInstance().getReference();

        View root = binding.getRoot();

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("select opportunity type ^");
        arrayList.add("part time");
        arrayList.add("full time");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addOpportunityTypeOpportunity.setAdapter(arrayAdapter);
        binding.addOpportunityTypeOpportunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                opprtType = adapterView.getItemAtPosition(i).toString();
                binding.warrnig.setText("  ");
                Toast.makeText(adapterView.getContext(), "Selected: " + opprtType, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        // Read Field
        ArrayList<String> arrayListField = new ArrayList<>();
        arrayListField.add("select Field type ^");
        reference.child("FieldsData").child("Fields").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot demo : snapshot.getChildren()) {
                    FieldModel models = demo.getValue(FieldModel.class);
                    arrayListField.add(models.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayAdapter<String> arrayAdapterField = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayListField);
        arrayAdapterField.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addOpportunityField.setAdapter(arrayAdapterField);
        binding.addOpportunityField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                OpprtField = adapterView.getItemAtPosition(i).toString();
                binding.warrnig.setText("   ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.addOpportunityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToDatabase();
            }
        });

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
        // get EndDate  ->DatePickerDialog
        DatePickerDialog.OnDateSetListener enddate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabelendDate();
            }
        };


        // handle click to open DatePickerDialog ->startDate
        binding.addOpportunityStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), stdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        // handle click to open DatePickerDialog - endDate
        binding.addOpportunityEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), enddate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Check validation  and uplaod Date
    public void uploadToDatabase() {
        OpportName = binding.addOpportunityName.getText().toString();
        StartDate = binding.addOpportunityStartDate.getText().toString();
        EndDate = binding.addOpportunityEndDate.getText().toString();
        Numberofdays = binding.addOpportunityDaysNumber.getText().toString();
        Numberofhours = binding.addOpportunityHoursDay.getText().toString();
        Description = binding.addOpportunityDescription.getText().toString();

        // Check validation
        if (OpportName.isEmpty()) {
            binding.addOpportunityName.setError("Please enter your  OpportunityNam");
            binding.addOpportunityName.setFocusable(true);
        } else if (opprtType == "select opportunity type ^") {
            binding.warrnig.setText("you must select opportunity type ");
            binding.addOpportunityTypeOpportunity.setFocusable(true);
        } else if (OpprtField == "select opportunity type ^") {
            binding.warrnig.setText("you must select OpportunityField ");
            binding.addOpportunityField.setFocusable(true);
        } else if (StartDate.isEmpty()) {
            binding.addOpportunityStartDate.setError("Please enter your  StartDate");
            binding.addOpportunityStartDate.setFocusable(true);
        } else if (EndDate.isEmpty()) {
            binding.addOpportunityEndDate.setError("Please enter your  EndDate");
            binding.addOpportunityEndDate.setFocusable(true);
        } else if (Numberofdays.isEmpty()) {
            binding.addOpportunityDaysNumber.setError("Please enter your  DaysNumber");
            binding.addOpportunityDaysNumber.setFocusable(true);
        } else if (Numberofhours.isEmpty()) {
            binding.addOpportunityHoursDay.setError("Please enter your  Numberofhours");
            binding.addOpportunityHoursDay.setFocusable(true);
        } else if (Description.isEmpty()) {
            binding.addOpportunityDescription.setError("Please enter your  Description");
            binding.addOpportunityDescription.setFocusable(true);
        } else {
            binding.warrnig.setText("");
            //upload Date
            final SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            loadingBar.setTitleText("Adding");
            loadingBar.setContentText("Please wait...");
            loadingBar.setCancelable(false);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            String oppkey = reference.push().getKey();
            NewOpportunityModel model = new NewOpportunityModel(OpportName, opprtType, OpprtField, StartDate, EndDate, Numberofdays, Numberofhours, Description, Prevalent.currentOnlineOrganization.getId(), oppkey);

            reference.child("OPPortunities").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reference.child("OPPortunities").child(oppkey).setValue(model);
                    loadingBar.dismissWithAnimation();
                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitle("Foundations has been added successfully");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setConfirmClickListener(sDialog -> {
                        Fragment selectedScreen = new OpportunityManagementFragment();
                        replaceFragment(selectedScreen);
                        dialog.dismissWithAnimation();

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
    // Method to Move from Current Fragment to other
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_foundation, someFragment);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }
    // make formate to startDate
    private void updateLabelstDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.addOpportunityStartDate.setText(dateFormat.format(myCalendar.getTime()));
    }
    // make formate to EndtDate
    private void updateLabelendDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.addOpportunityEndDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}