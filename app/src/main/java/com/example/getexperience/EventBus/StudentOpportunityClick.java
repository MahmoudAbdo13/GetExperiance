package com.example.getexperience.EventBus;


import com.example.getexperience.model.NewOpportunityModel;

public class StudentOpportunityClick {

    private boolean success;
    private NewOpportunityModel newOpportunityModel;

    public StudentOpportunityClick(boolean success, NewOpportunityModel newOpportunityModel) {
        this.success = success;
        this.newOpportunityModel = newOpportunityModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public NewOpportunityModel getNewOpportunityModel() {
        return newOpportunityModel;
    }

    public void setNewOpportunityModel(NewOpportunityModel newOpportunityModel) {
        this.newOpportunityModel = newOpportunityModel;
    }
}
