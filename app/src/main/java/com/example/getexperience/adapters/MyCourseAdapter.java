package com.example.getexperience.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.EventBus.StudentMyCourseClick;
import com.example.getexperience.R;
import com.example.getexperience.model.CourseRequestModel;
import com.example.getexperience.model.OrganizationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.MyCourseViewHolder> {

    private ArrayList<CourseRequestModel> courseRequestModels = new ArrayList<>();


    @NonNull
    @Override
    public MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_courses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCourseViewHolder holder, int position) {
        CourseRequestModel model = courseRequestModels.get(position);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Foundation");
        reference.child(model.getFoundationID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OrganizationModel organizationModel = snapshot.getValue(OrganizationModel.class);
                holder.foundationName.setText(organizationModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.courseName.setText(model.getCourseName());
        holder.instractorName.setText(model.getCourseInstrcutor());
        holder.endDate.setText(model.getEndDate());

        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new StudentMyCourseClick(true, model));
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseRequestModels.size();
    }

    public void setList(ArrayList<CourseRequestModel> courseRequestModels) {
        this.courseRequestModels = courseRequestModels;
        notifyDataSetChanged();

    }

    public class MyCourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, instractorName, foundationName, endDate;
        Button showDetails;

        public MyCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.item_new_course_name);
            instractorName = itemView.findViewById(R.id.item_new_course_instructor);
            foundationName = itemView.findViewById(R.id.item_new_course_foundation_name);
            endDate = itemView.findViewById(R.id.item_new_course_end_date);
            showDetails = itemView.findViewById(R.id.item_new_show_course_details_btn);
        }
    }
}
