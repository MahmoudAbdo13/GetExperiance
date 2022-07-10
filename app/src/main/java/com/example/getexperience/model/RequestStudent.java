package com.example.getexperience.model;

public class RequestStudent {
    private String foundationID,opportunityID,studentId,opportunityName,requestStatus,studentName,studentNumber;

    public RequestStudent() {
    }

    public RequestStudent(String foundationID, String opportunityId,String studentId, String opportunityName, String requestStatus, String studentName, String studentNumber) {
        this.foundationID = foundationID;
        this.opportunityID = opportunityId;
        this.studentId = studentId;
        this.opportunityName = opportunityName;
        this.requestStatus = requestStatus;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
    }

    public String getFoundationID() {
        return foundationID;
    }

    public void setFoundationID(String foundationID) {
        this.foundationID = foundationID;
    }

    public String getOpportunityId() {
        return opportunityID;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityID = opportunityId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
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
}
