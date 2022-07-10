package com.example.getexperience.EventBus;

import com.example.getexperience.model.CourseModel;
import com.example.getexperience.model.CourseRequestModel;

public class StudentMyCourseClick {

    private boolean success;
    private CourseRequestModel courseModel;

    public StudentMyCourseClick(boolean success, CourseRequestModel courseModel) {
        this.success = success;
        this.courseModel = courseModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CourseRequestModel getMyCourseRequestModel() {
        return courseModel;
    }

    public void setMyCourseRequestModel(CourseRequestModel courseModel) {
        this.courseModel = courseModel;
    }
}
