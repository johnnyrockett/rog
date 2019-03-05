package com.buzzfuzz.rog.traversal;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;

public class ConstructorFinder extends InstanceFinder {

	public ConstructorFinder(InstanceDispatcher dispatcher) {
		super(dispatcher);
		logName = "Constructor";
	}

	@Override
	public Object attemptPath(Object choice) {
		Constructor<?> cntr = (Constructor<?>)choice;
		Object[] args = new InstanceDispatcher(this).randomArgs(cntr.getGenericParameterTypes());
		if (args == null) {
			log("Returning null because arguments were null");
			return null;
		}
		
		Object instance = null;
		try {
			instance = cntr.newInstance(args);
		} catch (Exception e) {
			rog.logCrash(e, rng.getConfig());
		}

		return instance;
	}

	@Override
	public ArrayList<?> getOptions(Class<?> target) {
		return new ArrayList<Constructor<?>>(Arrays.asList(target.getConstructors()));
	}

	@Override
	public boolean validateChoice(Object choice, Class<?> target) {
		// Any constructor will be fine
		return false;
	}

}
