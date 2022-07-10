package com.example.getexperience.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.getexperience.EventBus.ManagerUnblockedFoundationClick;
import com.example.getexperience.R;
import com.example.getexperience.model.OrganizationModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class BlockedFoundationAdapter extends RecyclerView.Adapter<BlockedFoundationAdapter.FoundationViewHolder> {

    private ArrayList<OrganizationModel> organizationModels = new ArrayList<>();

    @NonNull
    @Override
    public FoundationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoundationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked_foundation_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoundationViewHolder holder, int position) {
        OrganizationModel model = organizationModels.get(position);
        holder.name.setText(model.getName());
        Glide.with(holder.itemView).load(model.getImageUrl()).into(holder.imageView);
        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ManagerUnblockedFoundationClick(true,model));
            }
        });

    }

    @Override
    public int getItemCount() {
        return organizationModels.size();
    }

    public void setList(ArrayList<OrganizationModel> organizationModels) {
        this.organizationModels = organizationModels;
        notifyDataSetChanged();

    }

    public class FoundationViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;
        Button showDetails;

        public FoundationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_blocked_foundation_name_manager);
            imageView = itemView.findViewById(R.id.item_blocked_foundation_image_manager);
            showDetails = itemView.findViewById(R.id.item_blocked_foundation_show_details_manager_btn);
        }
    }
}
