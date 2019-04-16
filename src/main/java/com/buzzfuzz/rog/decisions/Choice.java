package com.buzzfuzz.rog.decisions;

enum ValueType
{ 
    INT, LONG, CHAR, FLOAT, DOUBLE, BOOL, BYTE, SHORT, STRING, ENUM, NULL
}

public class Choice {
    private Target target;

    // Store the choices made
    private Integer intChoice;
    private Long longChoice;
    private Character charChoice;
    private Float floatChoice;
    private Double doubleChoice;
    private Boolean boolChoice;
    private Byte byteChoice;
    private Short shortChoice;
    private String stringChoice;
    private Integer enumChoice;

    private ValueType type;

    public Choice() {
        // assuming that they will set the values later
    }

    public Choice(Target target, int choice) {
        this.target = target;
        this.intChoice = choice;
        this.type = ValueType.INT;
    }

    public Choice(Target target, long choice) {
        this.target = target;
        this.longChoice = choice;
        this.type = ValueType.LONG;
    }
    
    public Choice(Target target, char choice) {
        this.target = target;
        this.charChoice = choice;
        this.type = ValueType.CHAR;
    }
    
    public Choice(Target target, float choice) {
        this.target = target;
        this.floatChoice = choice;
        this.type = ValueType.FLOAT;
    }
    
    public Choice(Target target, double choice) {
        this.target = target;
        this.doubleChoice = choice;
        this.type = ValueType.DOUBLE;
    }

    public Choice(Target target, boolean choice) {
        this.target = target;
        this.boolChoice = choice;
        this.type = ValueType.BOOL;
    }
    
    public Choice(Target target, byte choice) {
        this.target = target;
        this.byteChoice = choice;
        this.type = ValueType.BYTE;
    }
    
    public Choice(Target target, short choice) {
        this.target = target;
        this.shortChoice = choice;
        this.type = ValueType.SHORT;
    }
    
    public Choice(Target target, String choice) {
        this.target = target;
        this.stringChoice = choice;
        this.type = ValueType.STRING;
    }
    
    public Choice(Target target, String name, int choice) {
        this.target = target;
        this.enumChoice = choice;
        this.type = ValueType.ENUM;
    }

    public void setValue(int choice) {
        this.intChoice = choice;
        this.type = ValueType.INT;
    }
    
    public void setValue(long choice) {
        this.longChoice = choice;
        this.type = ValueType.LONG;
    }
    
    public void setValue(char choice) {
        this.charChoice = choice;
        this.type = ValueType.CHAR;
    }
    
    public void setValue(float choice) {
        this.floatChoice = choice;
        this.type = ValueType.FLOAT;
    }
    
    public void setValue(double choice) {
        this.doubleChoice = choice;
        this.type = ValueType.DOUBLE;
    }
    
    public void setValue(boolean choice) {
        this.boolChoice = choice;
        this.type = ValueType.BOOL;
    }
    
    public void setValue(byte choice) {
        this.byteChoice = choice;
        this.type = ValueType.BYTE;
    }
    
    public void setValue(short choice) {
        this.shortChoice = choice;
        this.type = ValueType.SHORT;
    }
    
    public void setValue(String choice) {
        this.stringChoice = choice;
        this.type = ValueType.STRING;
    }
    
    public void setValue(String name, int choice) {
        this.enumChoice = choice;
        this.type = ValueType.ENUM;
    }

    // Sets value to null
    public void setValue() {
        this.type = ValueType.NULL;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return this.target;
    }

    public String getChoiceType() {
        return this.type.name();
    }

    public int rateValidity(Constraint constraint) {
        if (constraint == null)
            return 0;
        switch (this.type) {
            case INT:
                return constraint.rateValidity(this.intChoice);
            case LONG:
                return constraint.rateValidity(this.longChoice);
            case CHAR:
                return constraint.rateValidity(this.charChoice);
            case FLOAT:
                return constraint.rateValidity(this.floatChoice);
            case DOUBLE:
                return constraint.rateValidity(this.doubleChoice);
            case BOOL:
                return 0; // constraint.rateValidity(this.boolChoice); // not implemented yet
            case BYTE:
                return constraint.rateValidity(this.byteChoice);
            case SHORT:
                return constraint.rateValidity(this.shortChoice);
            case STRING:
                return constraint.rateValidity("Temp String"); // this.stringChoice; xml can't handle this yet
            case ENUM:
                return 0; // constraint.rateValidity("enum", this.enumChoice); // not implemented yet
            case NULL:
                return constraint.rateValidity();
            default:
                return 0;
        }
    }

    public String getValueString() {
        switch (this.type) {
            case INT:
                return Integer.toString(this.intChoice);
            case LONG:
                return Long.toString(this.longChoice);
            case CHAR:
                return Character.toString(this.charChoice);
            case FLOAT:
                return Float.toString(this.floatChoice);
            case DOUBLE:
                return Double.toString(this.doubleChoice);
            case BOOL:
                return Boolean.toString(this.boolChoice);
            case BYTE:
                return Byte.toString(this.byteChoice);
            case SHORT:
                return Short.toString(this.shortChoice);
            case STRING:
                return "Temp String"; // this.stringChoice; xml can't handle this yet
            case ENUM:
                return Integer.toString(this.enumChoice);
            case NULL:
                return ""; // I think its better for the value to be empty. Maybe make "null"
            default:
                return "N/A";
        }
    }

}