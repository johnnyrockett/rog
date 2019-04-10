package com.buzzfuzz.rog.decisions;

enum ValueType
{ 
    INT, LONG, CHAR, FLOAT, DOUBLE, BOOL, BYTE, SHORT, STRING, ENUM
}

class Choice {
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

    public Target getTarget() {
        return this.target;
    }

    public String getChoiceType() {
        return this.type.name();
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
                return this.stringChoice;
            case ENUM:
                return Integer.toString(this.enumChoice);
            default:
                return "N/A";
        }
    }

}