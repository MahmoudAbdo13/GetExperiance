package com.example.getexperience.model;

public class OpportunityRequestModel {
    private String opportunityID, opportunityName, requestStatus, studentId, studentNumber, studentName, foundationID, endDate;

    public OpportunityRequestModel() {
    }

    public OpportunityRequestModel(String opportunityId, String opportunityName, String requestStatus, String studentId, String studentNumber, String studentName, String foundationId, String endDate) {
        this.opportunityID = opportunityId;
        this.opportunityName = opportunityName;
        this.requestStatus = requestStatus;
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.foundationID = foundationId;
        this.endDate= endDate;
    }

    public String getOpportunityId() {
        return opportunityID;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityID = opportunityId;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFoundationID() {
        return foundationID;
    }

    public void setFoundationID(String foundationID) {
        this.foundationID = foundationID;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
