package com.buzzfuzz.rog.decisions;

import java.util.ArrayList;
import java.util.List;

public class ConfigTree {
    private Scope root;

    public ConfigTree() {
        root = new Scope();
    }
    
    public ConfigTree(Scope root) {
    		this.root = root;
    }
    
    public Scope getRoot() {
    		return this.root;
    }
    
    @Override
    public int hashCode() {
        return root.hashCode();
    }
    
    public void addPair(Target target, Constraint constraint) {
		// find deepest existing scope that fits my target
		Tuple<Scope, Tuple<Target, Constraint>> location = findPairFor(target, root).x;
		
		// wait but this should always work
		
		// Two things, either add it to child scope, or override current scope constraint
		// Child if this target isn't exactly pair target
		// override if this target is exactly pair target
		if (location.y.x == null || !location.y.x.equals(target)) { // If it isn't the same, add child
			// Add a more specific child scope
			Scope child = new Scope();
			child.setTarget(target);
			child.setConstraint(constraint);
			location.x.addChild(child);
		} else { // Otherwise, override their constraint
			location.x.getConstraint().override(constraint);
		}
	}
    
    public Tuple<Tuple<Scope, Tuple<Target, Constraint>>, Integer> findPairFor(Target target, Scope scope) {
		if (validateContext(scope.getTarget(), target)) {
			// Should also build up the constraints as we go down
			// This is getting complicated
			Scope lowestScope = scope;
			Target targetSum = (scope.getTarget() == null) ? null : scope.getTarget().clone();
			Constraint constraintSum = (scope.getConstraint() == null) ? null : scope.getConstraint().clone();
			int maxDepth = 0;
			if (scope.getChildren() != null) {
				for (Scope child : scope.getChildren()) {
					Tuple<Tuple<Scope, Tuple<Target, Constraint>>, Integer> result = findPairFor(target, child);
					if (result == null)
						continue;
					if (result.y >= maxDepth) {
						lowestScope = result.x.x;
						
						// Override target
						if (targetSum == null)
							targetSum = result.x.y.x;
						else targetSum.override(result.x.y.x); // should have old target override new target
						
						// Override constraint
						if (constraintSum == null)
							constraintSum = result.x.y.y;
						else constraintSum.override(result.x.y.y);
						
						maxDepth = result.y;
					}
				}
			}
			Tuple<Target, Constraint> pair = new Tuple<Target, Constraint>(targetSum, constraintSum);
			return new Tuple<Tuple<Scope, Tuple<Target, Constraint>>, Integer>(new Tuple<Scope, Tuple<Target, Constraint>>(lowestScope, pair), maxDepth);
		}
		return null;
    }

    // Returns true if the first target is equal to or a subset of the second target
    private boolean validateContext(Target target, Target context) {
		if (target == null) {
			return true;
		} else if (target.getInstancePath() != null) {
            if (context.getInstancePath() == null)
                return false;
            if (!context.getInstancePath().contains(target.getInstancePath())) // Eventually use regex
                return false;
        } if (target.getTypeName() != null) {
            if (context.getTypeName() == null)
                return false;
            if (!context.getTypeName().equals(target.getTypeName())) {
                return false;
            }
        }
		return true; // This should have a lot of things later
	}
    
    public class Tuple<X, Y> { 
		public final X x;
		public final Y y; 
		public Tuple(X x, Y y) { 
			this.x = x; 
			this.y = y; 
		} 
	}

    public static class Scope {
        private Target target;
        private Constraint constraint;
        private List<Scope> children;
        
        public Scope() {
        		this.children = new ArrayList<Scope>();
        }
        
        public void addChild(Scope scope) {
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
        
        public List<Scope> getChildren() {
        		return this.children;
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
            if (o instanceof Scope) {
            		return this.target.equals(((Scope) o).target);
            } 
            
            return false;
        }
    }
}