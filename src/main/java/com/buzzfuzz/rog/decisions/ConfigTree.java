package com.buzzfuzz.rog.decisions;

import java.util.Iterator;
import java.util.Stack;

public class ConfigTree implements Iterable<Scope> {
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

        // Two things, either add it to child scope, or override current scope
        // constraint
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
        if (Target.validateContext(scope.getTarget(), target)) {
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
                        else
                            targetSum.override(result.x.y.x); // should have old target override new target

                        // Override constraint
                        if (constraintSum == null)
                            constraintSum = result.x.y.y;
                        else
                            constraintSum.override(result.x.y.y);

                        maxDepth = result.y;
                    }
                }
            }
            Tuple<Target, Constraint> pair = new Tuple<Target, Constraint>(targetSum, constraintSum);
            return new Tuple<Tuple<Scope, Tuple<Target, Constraint>>, Integer>(
                    new Tuple<Scope, Tuple<Target, Constraint>>(lowestScope, pair), maxDepth);
        }
        return null;
    }

    public class Tuple<X, Y> {
        public final X x;
        public final Y y;

        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    public ConfigTree clone() {
        ConfigTree clone = new ConfigTree(this.root.clone());
        return clone;
    }

    public Iterator<Scope> iterator() {
        return new ScopeIterator();
    }

    class ScopeIterator implements Iterator<Scope>{
        private Stack<Scope> scopes;

        public ScopeIterator() {
            this.scopes = new Stack<Scope>();
            this.scopes.add(root);
        }

        public boolean hasNext() {
            return !this.scopes.empty();
        }

        public Scope next() {
            if (!hasNext ()) {
                return null;
            }
            Scope node = this.scopes.pop();
            if (node.getChildren() != null) {
                for (Scope child : node.getChildren())
                    scopes.push(child);
            }
            return node;
        }
    }
}