package com.example.getexperience.EventBus;


import com.example.getexperience.model.FieldModel;


public class ManagerFieldModelClick {

    private boolean success;
    private FieldModel fieldModel;

    public ManagerFieldModelClick(boolean success, FieldModel fieldModel) {
        this.success = success;
        this.fieldModel = fieldModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public FieldModel getFieldModel() {
        return fieldModel;
    }

    public void setFieldModel(FieldModel fieldModel) {
        this.fieldModel = fieldModel;
    }
}
