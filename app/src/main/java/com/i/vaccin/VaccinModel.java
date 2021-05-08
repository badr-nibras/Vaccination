package com.i.vaccin;

public class VaccinModel {

    private String Name;
    private int Dose;

    private VaccinModel(){}

    public VaccinModel(String name, int dose) {
        this.Name = name;
        this.Dose = dose;
    }

    public int getDose() {
        return Dose;
    }

    public void setDose(int dose) {
        Dose = dose;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
