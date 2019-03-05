package com.buzzfuzz.robjg.traversal;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.reflections.util.Utils;

import com.google.common.collect.Multimap;

import java.util.Arrays;

public class FactoryFinder extends InstanceFinder {


	public FactoryFinder(InstanceDispatcher dispatcher) {
		super(dispatcher);
		logName = "Factory";
	}

	@Override
	public Object attemptPath(Object choice) {
		Method candidate = (Method)choice;
		log("Attempting factory method " + candidate.getName() + " from " + candidate.getDeclaringClass().getSimpleName());
		
		Object instance = null;
		
		if (!Modifier.isStatic(candidate.getModifiers())) {
			instance = new InstanceDispatcher(this).tryGetInstance(candidate.getDeclaringClass());
			if (instance == null) {
				return null;
			}
		}
		
		Object[] args = new InstanceDispatcher(this).randomArgs(candidate.getGenericParameterTypes());
		if (args == null) {
			return null;
		}
		
		Object outcome = null;
		try {
			log("Using args: " + Arrays.toString(args));
			candidate.setAccessible(true); // For some reason this is needed for static methods?
			outcome = candidate.invoke(instance, args);
		} catch (Exception e) {
			robjg.logCrash(e, rng.getConfig());
		}
		
		return outcome;
		
	}

	@Override
	public ArrayList<?> getOptions(Class<?> target) {
		// TODO: A hack until I figure out how javax ELResolver works
		if (target.equals(List.class) || target.equals(Collection.class)) {
			return new ArrayList<Method>();
		}
		Multimap<String, String> store = this.robjg.getReflections().getStore().get("CarefulMethodParameterScanner");
		
		Set<String> result = new HashSet<String>();
        for (String key : Utils.names(target)) {
            result.addAll(store.get(key));
        }
        
//        System.out.println("About to find factories for " + target.getSimpleName());
        
        ArrayList<Method> candidates = new ArrayList<Method>();
        try {
        		Set<Method> results = Utils.getMethodsFromDescriptors( result, this.robjg.getReflections().getConfiguration().getClassLoaders());
        		candidates.addAll(results);
        		return candidates;
        } catch (NoClassDefFoundError e) {
        		return new ArrayList<Method>();
        }
	}

	@Override
	public boolean validateChoice(Object choice, Class<?> target) {
		// shouldn't be valid if method is private
		Method methodChoice = (Method)choice;
		if (Modifier.isPrivate(methodChoice.getModifiers()))
			return false;
		
		// Factory methods could be within the current class, which would be in the history
		Class<?> declaringClass = methodChoice.getDeclaringClass();
		return isClassinHistory(declaringClass) || declaringClass.equals(target);
	}

}
