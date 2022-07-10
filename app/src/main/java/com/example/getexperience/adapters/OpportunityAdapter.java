package com.example.getexperience.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.R;
import com.example.getexperience.model.NewOpportunityModel;

import java.util.ArrayList;

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.FieldViewHolder> {

    public ArrayList<NewOpportunityModel> opportunityModels;
    OnOppornuityItemclick onOppornuityItemclick;

    public void setOnOppornuityItemclick(OnOppornuityItemclick onOppornuityItemclick) {
        this.onOppornuityItemclick = onOppornuityItemclick;
    }

    public OpportunityAdapter(ArrayList<NewOpportunityModel> Models) {
        this.opportunityModels = Models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FieldViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.approt_foundation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        NewOpportunityModel model = opportunityModels.get(position);
        holder.opprtname.setText(model.getOpportunityName());
        holder.opprtDate.setText(model.getEndDate());

        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOppornuityItemclick.Onitemclick(model, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return opportunityModels.size();
    }


    public class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView opprtname, opprtDate;
        Button btn_details;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            opprtname = itemView.findViewById(R.id.opportunity_name_foundation);
            opprtDate = itemView.findViewById(R.id.opportunity_end_date_foundation);
            btn_details = itemView.findViewById(R.id.show_opportunity_details_foundation_btn);
        }
    }

    public interface OnOppornuityItemclick {
        void Onitemclick(NewOpportunityModel models, int pos);
    }

}
