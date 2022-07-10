package com.example.getexperience.EventBus;

import com.example.getexperience.model.CourseModel;

public class StudentCourseClick {

    private boolean success;
    private CourseModel courseModel;

    public StudentCourseClick(boolean success, CourseModel courseModel) {
        this.success = success;
        this.courseModel = courseModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CourseModel getNewCourseModel() {
        return courseModel;
    }

    public void setNewCourseModel(CourseModel courseModel) {
        this.courseModel = courseModel;
    }
}
