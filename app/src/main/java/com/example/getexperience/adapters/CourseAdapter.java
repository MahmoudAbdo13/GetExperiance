package com.example.getexperience.adapters;

/*public class CourseAdapter {
}*/


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.R;
import com.example.getexperience.model.CourseModel;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.FieldViewHolder> {

    private ArrayList<CourseModel> coursedModels = new ArrayList<>();
    OnCourseItemclick onCourseItemclick;

    public void setOnCourseItemclick(OnCourseItemclick onCourseItemclick) {
        this.onCourseItemclick = onCourseItemclick;
    }

    public CourseAdapter(ArrayList<CourseModel> coursedModels) {
        this.coursedModels = coursedModels;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FieldViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.courselayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        CourseModel model = coursedModels.get(position);
        holder.Coursename.setText(model.getCourseName());
        holder.CourseInstrcutor.setText(model.getCourseInstrcutor());
        holder.courseemddate.setText(model.getEndDate());

        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCourseItemclick.Onitemclick(model, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return coursedModels.size();
    }


    public class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView Coursename, CourseInstrcutor, courseemddate;
        Button showDetails;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            Coursename = itemView.findViewById(R.id.course_name_foundation);
            CourseInstrcutor = itemView.findViewById(R.id.course_instructor_foundation);
            courseemddate = itemView.findViewById(R.id.course_end_date_foundation);

            showDetails = itemView.findViewById(R.id.show_course_details_foundation_btn);
        }
    }

    public interface OnCourseItemclick {
        void Onitemclick(CourseModel models, int pos);
    }
}
