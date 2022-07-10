package com.example.getexperience.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.EventBus.StudentMyOpportunityClick;
import com.example.getexperience.R;
import com.example.getexperience.model.OrganizationModel;
import com.example.getexperience.model.NewOpportunityModel;
import com.example.getexperience.model.RequestStudent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class MyOpportunityAdapter extends RecyclerView.Adapter<MyOpportunityAdapter.NewOpportunityViewHolder> {

    private ArrayList<RequestStudent> requestStudents = new ArrayList<>();


    @NonNull
    @Override
    public NewOpportunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewOpportunityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_opportunity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewOpportunityViewHolder holder, int position) {
        RequestStudent model = requestStudents.get(position);

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

        holder.opportunityName.setText(model.getOpportunityName());


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

        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new StudentMyOpportunityClick(true, model));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestStudents.size();
    }

    public void setList(ArrayList<RequestStudent> requestStudent) {
        this.requestStudents = requestStudent;
        notifyDataSetChanged();

    }

    public class NewOpportunityViewHolder extends RecyclerView.ViewHolder {
        TextView opportunityName, foundationName, endDate;
        Button showDetails;

        public NewOpportunityViewHolder(@NonNull View itemView) {
            super(itemView);
            opportunityName = itemView.findViewById(R.id.item_new_opportunity_name);
            foundationName = itemView.findViewById(R.id.item_new_opportunity_foundation_name);
            endDate = itemView.findViewById(R.id.item_new_opportunity_end_date);
            showDetails = itemView.findViewById(R.id.item_show_new_opportunity_details_btn);
        }
    }
}
