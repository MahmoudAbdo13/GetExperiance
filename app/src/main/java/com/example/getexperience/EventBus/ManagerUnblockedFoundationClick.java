package com.example.getexperience.EventBus;


import com.example.getexperience.model.OrganizationModel;


public class ManagerUnblockedFoundationClick {

    private boolean success;
    private OrganizationModel organizationModel;

    public ManagerUnblockedFoundationClick(boolean success, OrganizationModel organizationModel) {
        this.success = success;
        this.organizationModel = organizationModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public OrganizationModel getFoundationModel() {
        return organizationModel;
    }

    public void setFoundationModel(OrganizationModel organizationModel) {
        this.organizationModel = organizationModel;
    }
}
