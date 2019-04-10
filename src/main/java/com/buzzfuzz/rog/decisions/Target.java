package com.buzzfuzz.rog.decisions;

public class Target {
	private String instancePath;
	private String methodPath;
	private String typeName;
	private String subTypeOf;
	private String factoryFrom;
	private String constructorOf;
	
	public Target clone() {
		Target clone = new Target();
		clone.setInstancePath(this.instancePath);
		clone.setMethodPath(this.methodPath);
		clone.setTypeName(this.typeName);
		clone.setSubTypeOf(this.subTypeOf);
		clone.setFactoryFrom(this.factoryFrom);
		clone.setConstructorOf(this.constructorOf);
		return clone;
	}

    // This Target overrides the given Target.
    // Might make more sense to do this the other way around in the future...
	public void override(Target target) {
        if (target != null) {
            if (this.instancePath == null)
                this.instancePath = target.instancePath;
            if (this.methodPath == null)
                this.methodPath = target.methodPath;
            if (this.typeName == null)
                this.typeName = target.typeName;
            if (this.subTypeOf == null)
                this.subTypeOf = target.subTypeOf;
            if (this.factoryFrom == null)
                this.factoryFrom = target.factoryFrom;
            if (this.constructorOf == null)
                this.constructorOf = target.constructorOf;
        }
    }

    public boolean isEmpty() {
        return this.instancePath == null &&
            this.methodPath == null &&
            this.typeName == null &&
            this.subTypeOf == null &&
            this.factoryFrom == null &&
            this.constructorOf == null;
    }

    // Subtracts the values stored in the given target from this target
    public void subtract(Target target) {
        if (this.instancePath != null && target.instancePath != null) {
            int index;
            if ((index = this.instancePath.indexOf(target.instancePath)) != -1 ) {
                this.instancePath = this.instancePath.substring(index + target.instancePath.length());
            }
        }
        if (this.typeName != null && target.typeName != null) {
            if (target.typeName.equals(this.typeName))
                this.typeName = null;
        }
    }

    // Returns true if the target is equal to or a subset of the second target
    public static boolean validateContext(Target target, Target context) {
        if (target == null)
            return true;
        if (context == null)
            return false; // This is suspicious
        if (target.getInstancePath() != null) {
            if (context.getInstancePath() == null)
                return false;
            if (!context.getInstancePath().contains(target.getInstancePath())) // Eventually use regex
                return false;
        }
        if (target.getTypeName() != null) {
            if (context.getTypeName() == null)
                return false;
            if (!context.getTypeName().equals(target.getTypeName())) {
                return false;
            }
        }
        return true; // This should have a lot of things later
    }

    // Returns true if this target is equal to or a subset of the second target
    public  boolean validateContext(Target context) {
        if (context == null)
            return false; // This is suspicious
        if (this.getInstancePath() != null) {
            if (context.getInstancePath() == null)
                return false;
            if (!context.getInstancePath().contains(this.getInstancePath())) // Eventually use regex
                return false;
        }
        if (this.getTypeName() != null) {
            if (context.getTypeName() == null)
                return false;
            if (!context.getTypeName().equals(this.getTypeName())) {
                return false;
            }
        }
        return true; // This should have a lot of things later
    }
	
	public String getInstancePath() {
		return instancePath;
	}
	public void setInstancePath(String instancePath) {
		this.instancePath = instancePath;
	}
	public String getMethodPath() {
		return methodPath;
	}
	public void setMethodPath(String methodPath) {
		this.methodPath = methodPath;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getSubTypeOf() {
		return subTypeOf;
	}
	public void setSubTypeOf(String subTypeOf) {
		this.subTypeOf = subTypeOf;
	}
	public String getFactoryFrom() {
		return factoryFrom;
	}
	public void setFactoryFrom(String factoryFrom) {
		this.factoryFrom = factoryFrom;
	}
	public String getConstructorOf() {
		return constructorOf;
	}
	public void setConstructorOf(String constructorOf) {
		this.constructorOf = constructorOf;
    }

    public static Target clone(Target target) {
        if (target == null)
            return null;
        return target.clone();
    }

    public static boolean isEmpty(Target target) {
        if (target == null)
            return true;
        return target.isEmpty();
    }

    // Subtracts t2 from t1 and returns result
    public static Target subtract(Target t1, Target t2) {
        if (t1 == null)
            return null;
        Target result = t1.clone();
        result.subtract(t2);
        return result;
    }

    @Override
    public int hashCode() {
        return (this.instancePath + this.typeName).hashCode();
    }
}
