package com.buzzfuzz.rog.decisions;

import java.util.ArrayList;
import java.util.List;

public class Scope {
    private Target target;
    private Constraint constraint;

    private List<Scope> children;

    public Scope() {
        this.children = new ArrayList<Scope>();
    }

    public Scope(List<Scope> children) {
        this.children = children;
    }

    public void addChild(Scope scope) {
        if (scope != null)
            children.add(scope);
    }

    public void setTarget(Target t) {
        this.target = t;
    }

    public Target getTarget() {
        return this.target;
    }

    public void setConstraint(Constraint c) {
        this.constraint = c;
    }

    public Constraint getConstraint() {
        return this.constraint;
    }

    public void setChildren(List<Scope> children) {
        this.children = children;
    }

    public List<Scope> getChildren() {
        return this.children;
    }

    public Scope clone() {
        Scope clone = new Scope();
        if (this.target != null)
            clone.setTarget(this.target.clone());
        if (this.constraint != null)
            clone.setConstraint(this.constraint.clone());
        for (Scope child : this.children) {
            clone.addChild(child.clone());
        }
        return clone;
    }

    @Override
    public int hashCode() {
        int hash = 1;

        if (this.getTarget() != null)
            hash += this.getTarget().hashCode();

        if (this.getConstraint() != null)
            hash += this.getConstraint().hashCode();

        for (Scope child : this.children) {
            hash += child.hashCode();
        }

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null && this.target == null)
            return true;
        if (!(o instanceof Scope) || this.target == null)
            return false;
        return this.target.equals(((Scope) o).target);
    }
}