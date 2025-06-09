package com.example.continuiapp.models;

import java.util.Date;

public class CalculationResult {
    private long id;
    private Double area1;
    private Double velocity1;
    private Double area2;
    private Double velocity2;
    private double calculatedValue;
    private double displayValue;
    private String calculatedParameter;
    private String calculatedUnit;
    private Date timestamp;

    // Unit information
    private String area1Unit;
    private String velocity1Unit;
    private String area2Unit;
    private String velocity2Unit;

    // Constructors
    public CalculationResult() {
        this.timestamp = new Date();
        this.id = System.currentTimeMillis();
    }

    public CalculationResult(Double area1, Double velocity1, Double area2, Double velocity2,
                             double calculatedValue, String calculatedParameter, String calculatedUnit) {
        this();
        this.area1 = area1;
        this.velocity1 = velocity1;
        this.area2 = area2;
        this.velocity2 = velocity2;
        this.calculatedValue = calculatedValue;
        this.calculatedParameter = calculatedParameter;
        this.calculatedUnit = calculatedUnit;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Double getArea1() { return area1; }
    public void setArea1(Double area1) { this.area1 = area1; }

    public Double getVelocity1() { return velocity1; }
    public void setVelocity1(Double velocity1) { this.velocity1 = velocity1; }

    public Double getArea2() { return area2; }
    public void setArea2(Double area2) { this.area2 = area2; }

    public Double getVelocity2() { return velocity2; }
    public void setVelocity2(Double velocity2) { this.velocity2 = velocity2; }

    public double getCalculatedValue() { return calculatedValue; }
    public void setCalculatedValue(double calculatedValue) { this.calculatedValue = calculatedValue; }

    public double getDisplayValue() { return displayValue; }
    public void setDisplayValue(double displayValue) { this.displayValue = displayValue; }

    public String getCalculatedParameter() { return calculatedParameter; }
    public void setCalculatedParameter(String calculatedParameter) { this.calculatedParameter = calculatedParameter; }

    public String getCalculatedUnit() { return calculatedUnit; }
    public void setCalculatedUnit(String calculatedUnit) { this.calculatedUnit = calculatedUnit; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getArea1Unit() { return area1Unit; }
    public void setArea1Unit(String area1Unit) { this.area1Unit = area1Unit; }

    public String getVelocity1Unit() { return velocity1Unit; }
    public void setVelocity1Unit(String velocity1Unit) { this.velocity1Unit = velocity1Unit; }

    public String getArea2Unit() { return area2Unit; }
    public void setArea2Unit(String area2Unit) { this.area2Unit = area2Unit; }

    public String getVelocity2Unit() { return velocity2Unit; }
    public void setVelocity2Unit(String velocity2Unit) { this.velocity2Unit = velocity2Unit; }

    // Helper methods
    public String getFormattedResult() {
        return String.format("%.4f %s", displayValue, calculatedUnit);
    }

    public String getCalculationDescription() {
        return calculatedParameter + " calculada";
    }
}