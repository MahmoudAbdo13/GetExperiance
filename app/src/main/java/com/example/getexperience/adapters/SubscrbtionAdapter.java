package com.example.getexperience.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.R;
import com.example.getexperience.model.CourseRequestModel;

import java.util.ArrayList;

public class SubscrbtionAdapter extends RecyclerView.Adapter<SubscrbtionAdapter.FieldViewHolder> {

    public ArrayList<CourseRequestModel> subModelss;
    OnItemSubscribrequest onItemrequest;


    public void setOnItemrequest(OnItemSubscribrequest onItemrequest) {
        this.onItemrequest = onItemrequest;
    }

    public SubscrbtionAdapter(ArrayList<CourseRequestModel> Models) {
        this.subModelss = Models;

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FieldViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribtion_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        CourseRequestModel model = subModelss.get(position);
        holder.student_name.setText(model.getStudentName());
        holder.Curse_name.setText(model.getCourseName());

        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemrequest.OnItemsubscriberequest(model, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subModelss.size();
    }

    public class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView Curse_name, student_name;
        Button btn_details;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            Curse_name = itemView.findViewById(R.id.subscription_student_name_foundation);
            student_name = itemView.findViewById(R.id.subscription_course_name_foundation);
            btn_details = itemView.findViewById(R.id.show_subscription_details_foundation_btn);
        }
    }


    public interface OnItemSubscribrequest {
        public void OnItemsubscriberequest(CourseRequestModel subModels, int postion);
    }

}


