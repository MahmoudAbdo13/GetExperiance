package com.example.getexperience.model;

public class CourseRequestModel {
    private String foundationID, courseId, courseName, courseInstrcutor, endDate, requestStatus, studentId, studentName, studentNumber,courseKey;

    public CourseRequestModel() {
    }

    public CourseRequestModel(String foundationID, String courseId, String courseName, String courseInstrcutor, String endDate, String requestStatus, String studentId, String studentName, String studentNumber, String courseKey) {
        this.foundationID = foundationID;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseInstrcutor = courseInstrcutor;
        this.endDate = endDate;
        this.requestStatus = requestStatus;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.courseKey = courseKey;
    }

    public String getFoundationID() {
        return foundationID;
    }

    public void setFoundationID(String foundationID) {
        this.foundationID = foundationID;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCourseKey() {
        return courseKey;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }
}
