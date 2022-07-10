package com.example.getexperience.adapters;

/*public class StudentAdapter {
}*/


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.R;
import com.example.getexperience.model.RequestStudent;

import java.util.ArrayList;

public class StudentrequestAdapter extends RecyclerView.Adapter<StudentrequestAdapter.FieldViewHolder> {

    public ArrayList<RequestStudent> requestModelss;
    OnItemrequest onItemrequest;

    public void setOnItemrequest(OnItemrequest onItemrequest) {
        this.onItemrequest = onItemrequest;
    }

    public StudentrequestAdapter(ArrayList<RequestStudent> Models) {
        this.requestModelss = Models;

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FieldViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        RequestStudent model = requestModelss.get(position);

        holder.student_name.setText(model.getStudentName());
        holder.Opprt_name.setText(model.getOpportunityName());

        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemrequest.OnItemrequest(model, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return requestModelss.size();
    }


    public class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView Opprt_name, student_name;
        Button btn_details;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            Opprt_name = itemView.findViewById(R.id.request_student_name_foundation);
            student_name = itemView.findViewById(R.id.request_course_name_foundation);
            btn_details = itemView.findViewById(R.id.show_request_details_foundation_btn);

        }
    }

    public interface OnItemrequest {

        public void OnItemrequest(RequestStudent requestStudent, int postion);
    }

}
