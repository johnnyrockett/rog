package com.buzzfuzz.robjg.decisions;

public class Target {
	private String instancePath;
	private String methodPath;
	private String argumentName;
	private String subTypeOf;
	private String factoryFrom;
	private String constructorOf;
	
	public Target clone() {
		Target clone = new Target();
		clone.setInstancePath(this.instancePath);
		clone.setMethodPath(this.methodPath);
		clone.setArgumentName(this.argumentName);
		clone.setSubTypeOf(this.subTypeOf);
		clone.setFactoryFrom(this.factoryFrom);
		clone.setConstructorOf(this.constructorOf);
		return clone;
	}
	
	public void override(Target target) {
		if (this.instancePath == null)
			this.instancePath = target.instancePath;
		if (this.methodPath == null)
			this.methodPath = target.methodPath;
		if (this.argumentName == null)
			this.argumentName = target.argumentName;
		if (this.subTypeOf == null)
			this.subTypeOf = target.subTypeOf;
		if (this.factoryFrom == null)
			this.factoryFrom = target.factoryFrom;
		if (this.constructorOf == null)
			this.constructorOf = target.constructorOf;
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
	public String getArgumentName() {
		return argumentName;
	}
	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
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
	
//	@Override
//    public int hashCode() {
//        return this.toString().hashCode();
//    }
}
