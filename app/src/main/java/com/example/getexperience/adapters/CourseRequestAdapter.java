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
import com.example.getexperience.model.CourseRequestModel;
import com.example.getexperience.model.OrganizationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CourseRequestAdapter extends RecyclerView.Adapter<CourseRequestAdapter.CourseRequestViewHolder> {

    private ArrayList<CourseRequestModel> courseRequestModels = new ArrayList<>();

    @NonNull
    @Override
    public CourseRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseRequestViewHolder holder, int position) {
        CourseRequestModel model = courseRequestModels.get(position);

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

        holder.requestStatus.setText("Status: "+model.getRequestStatus());
        holder.courseName.setText(model.getCourseName());
        holder.courseInstructor.setText(model.getCourseInstrcutor());
        holder.endDate.setText(model.getEndDate());


        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query q = ref.child("CourseRequests").orderByChild("courseKey").equalTo(model.getCourseKey());
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
        return courseRequestModels.size();
    }

    public void setList(ArrayList<CourseRequestModel> courseRequestModel) {
        this.courseRequestModels = courseRequestModel;
        notifyDataSetChanged();
    }

    public class CourseRequestViewHolder extends RecyclerView.ViewHolder {
        TextView requestStatus, courseName, foundationName, courseInstructor, endDate;
        Button cancel;
        public CourseRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestStatus = itemView.findViewById(R.id.item_course_request_status);
            courseName = itemView.findViewById(R.id.item_course_request_course_name);
            foundationName = itemView.findViewById(R.id.item_course_request_foundation_name);
            courseInstructor = itemView.findViewById(R.id.item_course_request_course_instructor);
            endDate = itemView.findViewById(R.id.item_course_request_course_end_date);
            cancel = itemView.findViewById(R.id.item_course_request_course_cancel_btn);
        }
    }

}
