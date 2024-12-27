package edu.pitt.lersais.mhealth.model;

public class MedicalRecord {

    private String name;
    private String dob;
    private String occupation;
    private String contact;
    private String sex;
    private String maritalStatus;
    private String comments;

    private boolean allergy;
    private boolean heartAttack;
    private boolean rheumaticFever;
    private boolean heartMurmur;

    private String fatherDisease;
    private String motherDisease;
    private String siblingDisease;

    private String alcohol;
    private String cannabis;

    // Default constructor required for calls to DataSnapshot.getValue(MedicalRecord.class)
    public MedicalRecord() {
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isAllergy() {
        return allergy;
    }

    public void setAllergy(boolean allergy) {
        this.allergy = allergy;
    }

    public boolean isHeartAttack() {
        return heartAttack;
    }

    public void setHeartAttack(boolean heartAttack) {
        this.heartAttack = heartAttack;
    }

    public boolean isRheumaticFever() {
        return rheumaticFever;
    }

    public void setRheumaticFever(boolean rheumaticFever) {
        this.rheumaticFever = rheumaticFever;
    }

    public boolean isHeartMurmur() {
        return heartMurmur;
    }

    public void setHeartMurmur(boolean heartMurmur) {
        this.heartMurmur = heartMurmur;
    }

    public String getFatherDisease() {
        return fatherDisease;
    }

    public void setFatherDisease(String fatherDisease) {
        this.fatherDisease = fatherDisease;
    }

    public String getMotherDisease() {
        return motherDisease;
    }

    public void setMotherDisease(String motherDisease) {
        this.motherDisease = motherDisease;
    }

    public String getSiblingDisease() {
        return siblingDisease;
    }

    public void setSiblingDisease(String siblingDisease) {
        this.siblingDisease = siblingDisease;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getCannabis() {
        return cannabis;
    }

    public void setCannabis(String cannabis) {
        this.cannabis = cannabis;
    }
}
