package com.buzzfuzz.rog.decisions;

import java.util.Objects;

public class Constraint {
	private Boolean isNull;
    private Double prob;
    private Boolean negative;
    private Double lowerBound;
    private Double upperBound;
    private String[] stringExamples;

    public void setIsNull(Boolean isNull) {
        this.isNull = isNull;
    }

    /**
     * @return the stringExamples
     */
    public String[] getStringExamples() {
        return stringExamples;
    }

    /**
     * @param stringExamples the stringExamples to set
     */
    public void setStringExamples(String[] stringExamples) {
        this.stringExamples = stringExamples;
    }

    /**
     * @return the upperBound
     */
    public Double getUpperBound() {
        return upperBound;
    }

    /**
     * @param upperBound the upperBound to set
     */
    public void setUpperBound(Double upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * @return the lowerBound
     */
    public Double getLowerBound() {
        return lowerBound;
    }

    /**
     * @param lowerBound the lowerBound to set
     */
    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * @return the negative
     */
    public Boolean isNegative() {
        return negative;
    }

    /**
     * @param negative the negative to set
     */
    public void setNegative(Boolean negative) {
        this.negative = negative;
    }

    public Boolean isNull() {
		return this.isNull;
	}
	
	public void setProb(Double prob) {
		this.prob = prob;
	}
	
	public Double getProb() {
		return this.prob;
	}

	public Constraint clone() {
		Constraint clone = new Constraint();
		clone.setIsNull(this.isNull);
        clone.setProb(this.prob);
        clone.setNegative(this.negative);
        clone.setLowerBound(this.lowerBound);
        clone.setUpperBound(this.upperBound);
		return clone;
	}

	public void override(Constraint constraint) {
        if (constraint == null)
            return;
		if (constraint.isNull != null)
			this.isNull = constraint.isNull;
		if (constraint.prob != null)
            this.prob = constraint.prob;
        if (constraint.negative != null)
            this.negative = constraint.negative;
        if (constraint.lowerBound != null)
            this.lowerBound = constraint.lowerBound;
        if (constraint.upperBound != null)
            this.upperBound = constraint.upperBound;
    }

	@Override
    public int hashCode() {
        return Objects.hash(this.isNull, this.negative, this.lowerBound, this.upperBound);
    }

    public int size() {
        int size = 0;
        if (this.isNull != null)
            size++;
        if (this.prob != null)
            size++;
        if (this.getStringExamples() != null)
            size += this.getStringExamples().length;
        if (this.upperBound != null)
            size++;
        if (this.lowerBound != null)
            size++;
        if (this.negative != null && !this.negative)
            size++;
        return size;
    }

    // Meant to rate validity of a null value
    public int rateValidity() {
        return this.isNull != null && this.isNull ? 1 : -1;
    }

    public int rateValidity(int choice) {
        int validity = 0;
        if (this.upperBound != null)
            validity += (this.upperBound > choice) ? 1 : -1;
        if (this.lowerBound != null)
            validity += (this.lowerBound < choice) ? 1 : -1;
        if (this.negative != null && !this.negative)
            validity += (choice >= 0) ? 1 : -1;
        return validity;
    }

    public int rateValidity(float choice) {
        int validity = 0;
        if (this.upperBound != null)
            validity += (this.upperBound > choice) ? 1 : -1;
        if (this.lowerBound != null)
            validity += (this.lowerBound < choice) ? 1 : -1;
        if (this.negative != null && !this.negative)
            validity += (choice >= 0) ? 1 : -1;
        return validity;
    }

    public int rateValidity(double choice) {
        int validity = 0;
        if (this.upperBound != null)
            validity += (this.upperBound > choice) ? 1 : -1;
        if (this.lowerBound != null)
            validity += (this.lowerBound < choice) ? 1 : -1;
        if (this.negative != null && !this.negative)
            validity += (choice >= 0) ? 1 : -1;
        return validity;
    }

    public int rateValidity(long choice) {
        int validity = 0;
        if (this.upperBound != null)
            validity += (this.upperBound > choice) ? 1 : -1;
        if (this.lowerBound != null)
            validity += (this.lowerBound < choice) ? 1 : -1;
        if (this.negative != null && !this.negative)
            validity += (choice >= 0) ? 1 : -1;
        return validity;
    }

    public int rateValidity(short choice) {
        int validity = 0;
        if (this.upperBound != null)
            validity += (this.upperBound > choice) ? 1 : -1;
        if (this.lowerBound != null)
            validity += (this.lowerBound < choice) ? 1 : -1;
        if (this.negative != null && !this.negative)
            validity += (choice >= 0) ? 1 : -1;
        return validity;
    }

    public int rateValidity(String choice) {
        if (this.stringExamples != null) {
            for (String example : this.stringExamples) {
                if (example.equals(choice))
                    return 1;
            }
        }
        return -1;
    }
}
