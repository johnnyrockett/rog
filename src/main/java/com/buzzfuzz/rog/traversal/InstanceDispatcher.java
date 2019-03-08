package com.buzzfuzz.rog.traversal;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.buzzfuzz.rog.decisions.RNG;
import com.buzzfuzz.rog.ROG;
import com.buzzfuzz.rog.decisions.Constraint;
import com.buzzfuzz.rog.decisions.Target;

public class InstanceDispatcher {
	
	private Set<ClassPkg> history;
    private Constraint constraint;
    private ROG robjg;
    private RNG rng;

    public InstanceDispatcher(ROG rog) {
        this(new RNG(), rog);
    }
	
	public InstanceDispatcher(RNG rng, Set<ClassPkg> chain, ROG robjg) {
		this.rng = rng;
        this.history = chain == null ? new LinkedHashSet<ClassPkg>() : new LinkedHashSet<ClassPkg>(chain);
        this.robjg = robjg;
    }

	public InstanceDispatcher(RNG rng, ROG robjg) {
		this(rng, null, robjg);
	}
	
	public InstanceDispatcher(InstanceDispatcher dispatcher) {
		this(dispatcher.rng, dispatcher.history, dispatcher.robjg);
	}
	
	public InstanceDispatcher(InstanceFinder finder) {
		this(finder.rng, finder.history, finder.rog);
	}
	
	public Set<ClassPkg> getHistory() {
		return history;
	}
	
	public RNG getRNG() {
		return rng;
    }

    public ROG getROG() {
        return robjg;
    }
	
	private void log(String msg) {
		int indent = history.size();
		while (indent > 0) {
			msg = "    " + msg;
			indent--;
		}
		rng.log(msg + '\n');
	}
	
	public Object tryGetInstance(ClassPkg target) {
		if (history.contains(target))
			return null;
		
		// Maybe in load method?
		history.add(target);
		loadConstraint(getContext(target.getClazz()));
		
		if (!target.getClazz().isPrimitive() && rng.should(constraint.getNullProb())) {
			log("Returning null instead of instance");
			return null;
		}
		
		return getInstance(target);
	}
	
	public Object tryGetInstance(Class<?> target) {
		return tryGetInstance(new ClassPkg(target, null));
	}
	
	public Object getInstance(ClassPkg target) {
		
		// Might need to move history check here
		
		// If this method was called directly
		if(constraint == null) {
			history.add(target);
			loadConstraint(getContext(target.getClazz()));
		}
		
		Object instance = checkPrimatives(target.getClazz());
		
		if (instance == null) {
			instance = checkCommon(target);
		}
		
		if (instance == null) {
			// Eventually will need full ClassPkg
			instance = checkClasses(target.getClazz());
		}
		return instance;
	}
	
	public Object getInstance(Class<?> target) {
		return getInstance(new ClassPkg(target, null));
	}
	
	private Target getContext(Class<?> target) {
		Target context = new Target();
		String instancePath = "";
		for (ClassPkg instance : history) {
			instancePath += instance.getClazz().getSimpleName();
			if (instance.getGenerics() != null && instance.getGenerics().length != 0) {
				instancePath += '<';
				for (Type generic : instance.getGenerics()) {
					instancePath += generic.getTypeName().substring(generic.getTypeName().lastIndexOf('.')+1) + ',';
				}
				instancePath = instancePath.substring(0, instancePath.length()-1);
				instancePath += '>';
			}
			instancePath += '.';
		}
		instancePath = instancePath.substring(0, instancePath.length()-1);
		context.setInstancePath(instancePath);
		
		log("current path: " + instancePath);
		
		return context;
	}
	
	private void loadConstraint(Target target) {
		Constraint constraint = rng.getConstraint(target);
		
		if (constraint == null) {
//			System.out.println("MAKING NEW CONSTRAINT");
			constraint = rng.makeConstraint(target);
		} 
//		else System.out.println("USING EXISTING CONSTRAINT" + constraint.toString());
		this.constraint = constraint;
	}
	
	public Object checkClasses(Class<?> target) {
		
		Object inst = new FuzzConstructorFinder(this).findInstance(target);
		if (inst == null) {
			inst = new ConstructorFinder(this).findInstance(target);
			if (inst == null) {
				inst = new LocalFactoryFinder(this).findInstance(target);
				if (inst == null) {
					inst = new FactoryFinder(this).findInstance(target);
					if (inst == null) {
						inst = new SubclassFinder(this).findInstance(target);
						if (inst == null)
							log("Could not find a way to get an instance of this class.");
					}
				}
			}
		}
		return inst;
	}
	
	public Object checkPrimatives(Class<?> target) {
		if (target.equals(int.class)) {
			return rng.getInt();
		} else if (target.equals(long.class)) {
			System.out.println("GETTING LONG PRIMITIVE");
			return rng.getLong();
		} else if (target.equals(char.class)) {
			return rng.getChar();
		} else if (target.equals(float.class)) {
			return rng.getFloat();
		} else if (target.equals(double.class)) {
			return rng.getDouble();
		} else if (target.equals(boolean.class)) {
			return rng.getBool();
		} else if (target.equals(byte.class)) {
			return rng.getByte();
		} else if (target.equals(short.class)) {
			return rng.getShort();
		} else if (target.equals(String.class)) {
			return rng.getString();
		} else if (target.isEnum()) {
			Object[] values = target.getEnumConstants();
			int index = rng.fromRange(0, values.length - 1);
			return values[index];
		} else {
			return null;
		}
	}
	
	private Object checkCommon(ClassPkg target) {
		
		if (target.getClazz().isArray()) {
			Class<?> type = target.getClazz().getComponentType();
			return randomArray(type);
		} else if (target.getClazz().equals(List.class) ) {
//			log(String.valueOf(target.getGenerics() == null));
			Class<?> type = (Class<?>)target.getGenerics()[0];
			
			// Unfortunately this needs to be separate from the randArray method because 
			// there can be arrays of primitives but not Lists of primitives
			int length = rng.fromRange(0, 10);
			Object[] array = (Object[])Array.newInstance(type, length);
			for (int i = 0; i < length; i++) {
				Object instance = new InstanceDispatcher(this).getInstance(type);
				if (instance == null) {
					return null;
				}
				array[i] = instance;
			}
			return Arrays.asList(array);
		} else if (target.getClazz().equals(BigInteger.class)) {
			return new BigInteger(rng.fromRange(2, 32), rng.getRandom());
		} else if (target.getClazz().equals(Number.class)) {
			return rng.getDouble();
		}
		return null;
	}
	
	private Object randomArray(ClassPkg type) {
		int length = rng.fromRange(0, 10);
		Object array = Array.newInstance(type.getClazz(), length);
		for (int i = 0; i < length; i++) {
			Object instance = new InstanceDispatcher(this).getInstance(type);
			if (instance == null) {
				return null;
			}
			Array.set(array, i, instance);
		}
		return array;
//		return Array.newInstance(type.getClazz(), 0).getClass().cast(array);
	}

	private Object randomArray(Class<?> type) {
		return randomArray(new ClassPkg(type, null));
	}
	
	public static ClassPkg[] packageClasses(Type[] genArgs) {
		// TODO: Sometimes target or generics is null. Maybe this is because of cases like 'E'
		

		// We are creating one classPkg per argument
		ClassPkg[] pkgs = new ClassPkg[genArgs.length];
		
		// Proposing new way to do generics based on what I know now
		for (int i=0; i < genArgs.length; i++) {
			Type type = genArgs[i];
			if (type instanceof Class) {
				// This doesn't have generics.
				pkgs[i] = new ClassPkg((Class<?>)type, null);
			} else if (type instanceof ParameterizedType){
				ParameterizedType pt = (ParameterizedType)type;
				// Eventually will have to store ClassPkgs here to account for List<List<Integer>>
				Type[] generics = new Type[pt.getActualTypeArguments().length];
				for (int j=0; j < pt.getActualTypeArguments().length; j++) {
					Type gtype = pt.getActualTypeArguments()[j];
					if (gtype instanceof WildcardType) {
						// Generic is in "? extends Class" format. We want its upperbound
						WildcardType wc = (WildcardType)gtype;
						// if (wc.getUpperBounds()[0] == null)
						// 	log("New kind of wildcard");
						generics[j] = wc.getUpperBounds()[0];
					} else {
						// Generic is a normal class at this point (probably)
						generics[j] = gtype;
					}
				}
				pkgs[i] = new ClassPkg((Class<?>)pt.getRawType(), generics);
			} else {
				// TODO: This is a generic type E. Will need to feed in a random object
			}
		}
		
		return pkgs;
	}

    // I can probably make methods like these private. It would be best...
	public Object[] randomArgs(Type[] genArgs) {
		
		ClassPkg[] pkgs = packageClasses(genArgs);
		
		Object[] instances = new Object[genArgs.length];
		for (int i = 0; i < pkgs.length; i++) {
			instances[i] = new InstanceDispatcher(this).tryGetInstance(pkgs[i]);
			// If any of the arguments return BadPath, return BadPath
//			if (instances[i].getClass().)
//				return null;
		}
		return instances;
	}

}
