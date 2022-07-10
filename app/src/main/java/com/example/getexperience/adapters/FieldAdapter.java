package com.example.getexperience.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.EventBus.ManagerFieldModelClick;
import com.example.getexperience.EventBus.PassMassageActionClick;
import com.example.getexperience.R;
import com.example.getexperience.model.FieldModel;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {

    private ArrayList<FieldModel> fieldModels = new ArrayList<>();

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FieldViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        FieldModel model = fieldModels.get(position);
        holder.name.setText(model.getName());
        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirm Delete")
                        .setContentText("Do you really want to Deleted this field")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismiss();
                            FirebaseDatabase.getInstance().getReference().child("FieldsData").child("Fields").child(model.getId()).removeValue();
                            Toast.makeText(v.getContext(), "Field Deleted successfully", Toast.LENGTH_SHORT).show();
                        }).setCancelText("No")
                        .setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fieldModels.size();
    }

    public void setList(ArrayList<FieldModel> fieldModels) {
        this.fieldModels = fieldModels;
        notifyDataSetChanged();

    }

    public class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button showDetails;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_field_name_manager);
            showDetails = itemView.findViewById(R.id.show_field_details_manager_btn);
        }
    }
}
