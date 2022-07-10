package com.example.getexperience.model;

public class NewOpportunityModel {
    private String opportunityName, opportunityType, opportunityField, startDate, endDate, numberOfDays, numberOfHours, description, foundationID, opportunityID;

    public NewOpportunityModel() {}

    public NewOpportunityModel(String opportunityName, String opportunityType, String opportunityField, String startDate, String endDate, String numberOfDays, String numberofhours, String description, String foundationID, String opportunityID) {
        this.opportunityName = opportunityName;
        this.opportunityType = opportunityType;
        this.opportunityField = opportunityField;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.numberOfHours = numberofhours;
        this.description = description;
        this.foundationID = foundationID;
        this.opportunityID = opportunityID;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public String getOpportunityType() {
        return opportunityType;
    }

    public void setOpportunityType(String opportunityType) {
        this.opportunityType = opportunityType;
    }

    public String getOpportunityField() {
        return opportunityField;
    }

    public void setOpportunityField(String opportunityField) {
        this.opportunityField = opportunityField;
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

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(String numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoundationID() {
        return foundationID;
    }

    public void setFoundationID(String foundationID) {
        this.foundationID = foundationID;
    }

    public String getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(String opportunityID) {
        this.opportunityID = opportunityID;
    }
}