package com.example.getexperience.model;

public class CertificateModel {
    String studentNumber, certificateUrl, certificateName;

    public CertificateModel() {
    }

    public CertificateModel(String studentNumber, String certificateUrl, String certificateName) {
        this.studentNumber = studentNumber;
        this.certificateUrl = certificateUrl;
        this.certificateName = certificateName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }
}
