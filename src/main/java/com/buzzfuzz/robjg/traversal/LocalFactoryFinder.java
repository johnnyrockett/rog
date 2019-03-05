package com.buzzfuzz.robjg.traversal;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class LocalFactoryFinder extends FactoryFinder {

	public LocalFactoryFinder(InstanceDispatcher dispatcher) {
		super(dispatcher);
		logName = "Local Factory";
	}
	
	@Override
	public ArrayList<?> getOptions(Class<?> target) {
		
		ArrayList<Method> candidates = new ArrayList<Method>();
		for ( Method mth : target.getMethods()) {
			if (mth.getReturnType().equals(target)) {
				log("Found method " + mth.getName() + " that returns a " + target.getSimpleName());
				candidates.add(mth);
			}
		}
		
        return candidates;
	}
	
	@Override
	public boolean validateChoice(Object choice, Class<?> target) {
		// Factory methods could be within the current class, which would be in the history
		return false;
	}

}
