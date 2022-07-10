package com.example.getexperience.EventBus;


import com.example.getexperience.model.RequestStudent;

public class StudentMyOpportunityClick {

    private boolean success;
    private RequestStudent requestStudent;

    public StudentMyOpportunityClick(boolean success, RequestStudent requestStudent) {
        this.success = success;
        this.requestStudent = requestStudent;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public RequestStudent getMyOpportunityModel() {
        return requestStudent;
    }

    public void setMyOpportunityModel(RequestStudent requestStudent) {
        this.requestStudent = requestStudent;
    }
}
