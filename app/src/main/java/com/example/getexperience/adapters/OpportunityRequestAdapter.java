package com.example.getexperience.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.R;
import com.example.getexperience.model.OrganizationModel;
import com.example.getexperience.model.NewOpportunityModel;
import com.example.getexperience.model.OpportunityRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OpportunityRequestAdapter extends RecyclerView.Adapter<OpportunityRequestAdapter.OpportunityRequestViewHolder> {

    private ArrayList<OpportunityRequestModel> requestStudents = new ArrayList<>();

    @NonNull
    @Override
    public OpportunityRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OpportunityRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opportunity_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityRequestViewHolder holder, int position) {
        OpportunityRequestModel model = requestStudents.get(position);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Foundation").child(model.getFoundationID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganizationModel organizationModel = snapshot.getValue(OrganizationModel.class);
                holder.foundationName.setText(organizationModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
                reference.child("OPPortunities").child(model.getOpportunityId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NewOpportunityModel opportunityModel = snapshot.getValue(NewOpportunityModel.class);
                holder.endDate.setText(opportunityModel.getEndDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.requestStatus.setText("Status: "+model.getRequestStatus());
        holder.opportunityName.setText(model.getOpportunityName());


        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query q = ref.child("OpportunityRequests").orderByChild("opportunityId").equalTo(model.getOpportunityId());
                SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(),SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("Confirm");
                dialog.setContentText("Are you sure to delete this request?");
                dialog.setConfirmText("Yes");
                dialog.setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot demo : snapshot.getChildren()) {
                                demo.getRef().removeValue();
                                SweetAlertDialog alertDialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitle("Your Request has been deleted successfully");
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setConfirmClickListener(ssDialog -> {
                                    ssDialog.dismiss();
                                });
                                alertDialog.show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("e", error.toException() + "");
                        }
                    });
                });
                dialog.setCancelText("No");
                dialog.setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestStudents.size();
    }

    public void setList(ArrayList<OpportunityRequestModel> requestStudent) {
        this.requestStudents = requestStudent;
        notifyDataSetChanged();
    }

    public class OpportunityRequestViewHolder extends RecyclerView.ViewHolder {
        TextView requestStatus, opportunityName, foundationName, endDate;
        Button cancel;
        public OpportunityRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestStatus = itemView.findViewById(R.id.opportunity_request_status);
            opportunityName = itemView.findViewById(R.id.opportunity_request_opportunity_name);
            foundationName = itemView.findViewById(R.id.opportunity_request_foundation_name);
            endDate = itemView.findViewById(R.id.opportunity_request_end_date);
            cancel = itemView.findViewById(R.id.opportunity_request_cancel_btn);
        }
    }

}
