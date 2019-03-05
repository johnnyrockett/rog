package com.buzzfuzz.robjg.decisions;

public class Constraint {
	private Double nullProb;
	private Double prob;
	
	public void setNullProb(Double prob) {
		this.nullProb = prob;
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
		return clone;
	}
	
	public void override(Constraint constraint) {
		if (constraint.nullProb != null)
			this.nullProb = constraint.nullProb;
		if (constraint.prob != null)
			this.prob = constraint.prob;
	}
	
	@Override
    public int hashCode() {
        return this.toString().hashCode();
    }
	
	@Override
	public String toString() {
		return nullProb + ", " + prob;
	}
}
