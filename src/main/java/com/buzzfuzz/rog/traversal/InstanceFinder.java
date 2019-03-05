package com.buzzfuzz.rog.traversal;

import java.util.ArrayList;
import java.util.Set;

import com.buzzfuzz.rog.ROG;
import com.buzzfuzz.rog.decisions.RNG;

public abstract class InstanceFinder {
	
	ArrayList<?> options;
    Set<ClassPkg> history;
    ROG rog;
	RNG rng;
	String logName = null;
	
	public InstanceFinder(Set<ClassPkg> chain, RNG rng, ROG robjg) {
		this.history = chain;
        this.rng = rng;
        this.rog = robjg;
	}

	public InstanceFinder(InstanceDispatcher dispatcher) {
		history = dispatcher.getHistory();
        rng = dispatcher.getRNG();
        rog = dispatcher.getROG();
	}

	protected void log(String msg) {
		int indent = history.size();
		while (indent > 0) {
			msg = "    " + msg;
			indent--;
		}
		rng.log(msg + '\n');
	}
	
	public Object findInstance(Class<?> target) {
		
//		log("Finding " + logName + " for type " + target.getSimpleName());
		
		// get group of options
		options = getOptions(target);
		
		// loop through options
		while (true) {
			if (options.size() == 0) {
//				log("Couldn't find " + logName + " for type " + target.getSimpleName());
				return null;
			}
			
			int index = rng.fromRange(0, options.size()-1);
			
			Object choice = options.get(index);
			
			if (validateChoice(choice, target)) {
				log("Already tried " + target.getSimpleName() + " before.");
				options.remove(choice);
				continue;
			}
		
		
			Object attempt = attemptPath(choice);
			if (attempt == null) {
				options.remove(choice);
			} else {
				return attempt;
			}
		}
		
		// verify if the option is good
	}

	public boolean isClassinHistory(Class<?> target) {
		return history.contains(new ClassPkg(target, null));
	}
	
	public abstract Object attemptPath(Object choice);
	
	public abstract ArrayList<?> getOptions(Class<?> target);
	
	// Returns true is this choice is invalid
	public abstract boolean validateChoice(Object choice, Class<?> target);

}
