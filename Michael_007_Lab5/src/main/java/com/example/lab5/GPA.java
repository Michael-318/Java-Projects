package com.example.lab5;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GPA {
    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    public SimpleDoubleProperty gpa1  = new SimpleDoubleProperty();
    public SimpleDoubleProperty gpa2 = new SimpleDoubleProperty();
    public SimpleDoubleProperty gpa3 = new SimpleDoubleProperty();
    public SimpleDoubleProperty cgpa = new SimpleDoubleProperty();

    public GPA(int id, double gpa1, double gpa2, double gpa3) {
        this.id.set(id);
        this.gpa1.set(gpa1);
        this.gpa2.set(gpa2);
        this.gpa3.set(gpa3);
        this.cgpa.set((gpa1 + gpa2 + gpa3)/3);
    }

    public GPA(int id, double gpa1, double gpa2, double gpa3, double cgpa) {
        this.id.set(id);
        this.gpa1.set(gpa1);
        this.gpa2.set(gpa2);
        this.gpa3.set(gpa3);
        this.cgpa.set(cgpa);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public double getGpa1() {
        return gpa1.get();
    }

    public SimpleDoubleProperty gpa1Property() {
        return gpa1;
    }

    public void setGpa1(double gpa1) {
        this.gpa1.set(gpa1);
    }

    public double getGpa2() {
        return gpa2.get();
    }

    public SimpleDoubleProperty gpa2Property() {
        return gpa2;
    }

    public void setGpa2(double gpa2) {
        this.gpa2.set(gpa2);
    }

    public double getGpa3() {
        return gpa3.get();
    }

    public SimpleDoubleProperty gpa3Property() {
        return gpa3;
    }

    public void setGpa3(double gpa3) {
        this.gpa3.set(gpa3);
    }

    public double getCgpa() {
        return cgpa.get();
    }

    public SimpleDoubleProperty cgpaProperty() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa.set(cgpa);
    }
}
