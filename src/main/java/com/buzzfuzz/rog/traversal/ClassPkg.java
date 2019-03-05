package com.buzzfuzz.rog.traversal;

import java.lang.reflect.Type;
import java.util.Arrays;

public class ClassPkg {
	
	private Class<?> clazz;
	
	private Type[] generics;
	
	public ClassPkg(Class<?> clazz, Type[] generics) {
		this.clazz = clazz;
		this.generics = generics;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public Type[] getGenerics() {
		return generics;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o instanceof Class<?>) {
        		return this.clazz.equals((Class<?>)o);
        } else if (o instanceof ClassPkg) {
        		return this.toString().equals(o.toString());
        } 
        
        return false;
    }
	
	@Override
    public int hashCode() {
        return this.toString().hashCode();
    }
	
	@Override
	public String toString() {
		return "Class: " + clazz.getSimpleName() + ", Generics: " + Arrays.toString(generics);
	}

}
