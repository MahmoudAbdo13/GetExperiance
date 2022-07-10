package com.example.getexperience.model;

public class CourseModel {

    private  String courseName,courseInstrcutor,courseTime,startDate,endDate,description,courseId,foundationID;

    public CourseModel() {
    }

    public CourseModel(String courseName, String courseInstrcutor, String courseTime, String startDate, String endDate, String description, String courseId, String foundationId) {
        this.courseName = courseName;
        this.courseInstrcutor = courseInstrcutor;
        this.courseTime = courseTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.courseId = courseId;
        this.foundationID = foundationId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseInstrcutor() {
        return courseInstrcutor;
    }

    public void setCourseInstrcutor(String courseInstrcutor) {
        this.courseInstrcutor = courseInstrcutor;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getFoundationID() {
        return foundationID;
    }

    public void setFoundationID(String foundationId) {
        this.foundationID = foundationId;
    }
}
