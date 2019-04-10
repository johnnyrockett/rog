package com.buzzfuzz.rog.decisions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Constraint {
	private Double nullProb;
    private Double prob;
    private Boolean negative;
    private Double lowerBound;
    private Double upperBound;
    private String[] stringExamples;

    public void setNullProb(Double prob) {
        this.nullProb = prob;
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

    public Double getNullProb() {
		return this.nullProb;
	}
	
	public void setProb(Double prob) {
		this.prob = prob;
	}
	
	public Double getProb() {
		return this.prob;
	}

	public Constraint clone() {
		Constraint clone = new Constraint();
		clone.setNullProb(this.nullProb);
        clone.setProb(this.prob);
        clone.setNegative(this.negative);
        clone.setLowerBound(this.lowerBound);
        clone.setUpperBound(this.upperBound);
		return clone;
	}

	public void override(Constraint constraint) {
        if (constraint == null)
            return;
		if (constraint.nullProb != null)
			this.nullProb = constraint.nullProb;
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
        return Objects.hash(this.nullProb, this.negative, this.lowerBound, this.upperBound);
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
