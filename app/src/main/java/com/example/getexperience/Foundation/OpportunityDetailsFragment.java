package com.example.getexperience.Foundation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.getexperience.R;
import com.example.getexperience.model.NewOpportunityModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OpportunityDetailsFragment extends Fragment {

    NewOpportunityModel Opportunitymodel;
    View root;
    TextView opprt_name, opprt_type, opprt_field, start_date, End_date, num_days, num_hours, Description;
    Button delete;

    public OpportunityDetailsFragment() {

    }

    public OpportunityDetailsFragment(NewOpportunityModel opportunityModel) {
        this.Opportunitymodel = opportunityModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get ID
        root = inflater.inflate(R.layout.fragment_opportunity_details, container, false);
        opprt_name = root.findViewById(R.id.opportunity_details_name_data);
        opprt_type = root.findViewById(R.id.opportunity_details_type_data);
        opprt_field = root.findViewById(R.id.opportunity_details_field_data);
        start_date = root.findViewById(R.id.opportunity_details_start_date_data);
        End_date = root.findViewById(R.id.opportunity_details_end_date_data);
        num_days = root.findViewById(R.id.opportunity_details_days_number_data);
        num_hours = root.findViewById(R.id.opportunity_details_hours_number_data);
        Description = root.findViewById(R.id.opportunity_details_description_data);

        // add Data opportunity_details

        opprt_name.setText(Opportunitymodel.getOpportunityName());
        opprt_type.setText(Opportunitymodel.getOpportunityType());
        opprt_field.setText(Opportunitymodel.getOpportunityField());
        start_date.setText(Opportunitymodel.getStartDate());
        End_date.setText(Opportunitymodel.getEndDate());
        num_days.setText(Opportunitymodel.getNumberOfDays());
        num_hours.setText(Opportunitymodel.getNumberOfHours());
        Description.setText(Opportunitymodel.getDescription());


        // Delete item   opportunity_details_delete_btn
        delete = root.findViewById(R.id.opportunity_details_delete_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitle("Are you want to delete?");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(sDialog -> {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query q = ref.child("OPPortunities").orderByChild("opportunityID").equalTo(Opportunitymodel.getOpportunityID());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot demo : snapshot.getChildren()) {
                                demo.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("e", error.toException() + "");
                        }
                    });
                    dialog.dismissWithAnimation();
                    Fragment selectedScreen = new OpportunityManagementFragment();
                    replaceFragment(selectedScreen);

                }).setCancelText("Cancel").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Log.e("c", "cancle");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return root;
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