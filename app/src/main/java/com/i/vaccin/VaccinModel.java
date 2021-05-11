package com.i.vaccin;

public class VaccinModel {

    private String Name;
    private int Doze;

    private VaccinModel(){}

    public VaccinModel(String Name, int Doze) {
        this.Name = Name;
        this.Doze = Doze;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getDoze() {
        return Doze;
    }

    public void setDoze(int doze) {
        Doze = doze;
    }
}
