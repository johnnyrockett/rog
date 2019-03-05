package com.buzzfuzz.rog.traversal;

import java.util.ArrayList;

public class SubclassFinder extends InstanceFinder {


	public SubclassFinder(InstanceDispatcher dispatcher) {
		super(dispatcher);
		logName = "Subclass";
	}

	@Override
	public Object attemptPath(Object choice) {
		return new InstanceDispatcher(this).tryGetInstance((Class<?>)choice);
	}

	@Override
	public ArrayList<?> getOptions(Class<?> target) {
		ArrayList<Class<?>> typesList = new ArrayList<Class<?>>();
		typesList.addAll(this.rog.getReflections().getSubTypesOf(target));
		return typesList;
	}

	@Override
	public boolean validateChoice(Object choice, Class<?> target) {
		// Any subType would be fine
		return isClassinHistory((Class<?>)choice);
	}

}
