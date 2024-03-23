package com.example.poshan_track;

public class CareDataHolder {
    private double height;
    private double weight;

    // Required no-argument constructor
    public CareDataHolder() {
        // Default constructor required for Firebase deserialization
    }

    public CareDataHolder(double height, double weight) {
        this.height = height;
        this.weight = weight;
    }

    // Getters and setters
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
