package com.buzzfuzz.rog.decisions;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.buzzfuzz.rog.decisions.Config;
import com.buzzfuzz.rog.decisions.Constraint;
import com.buzzfuzz.rog.decisions.Target;

public class RNG {

	private Random rand;
	private Config config;
	private long seed;
	private Set<String> crashes;
	private int crashCount;

	public RNG() {
        this(new Random().nextLong());
	}

	public RNG(long seed) {
		this.seed = seed;
		rand = new Random(seed);
		crashes = new HashSet<String>();
		crashCount++;
		config = new Config();
	}

	public void log(String msg) {
		config.log(msg);
    }

    public Config getConfig() {
        return this.config;
    }

	public void setConfig(Config config) {
        if (config != null)
		    this.config = config;
	}

	public long getSeed() {
		return this.seed;
	}

	public Set<String> getCrashes() {
		return this.crashes;
	}

	public int getCrashCount() {
		return this.crashCount;
	}

	public Random getRandom() {
		return this.rand;
	}

	public int fromRange(int low, int high) {
		if (low > high) {
			int temp = low;
			low = high;
			high = temp;
        }
        return rand.nextInt(Math.abs(high - low) + 1) + low;
	}

	public double fromRange(double low, double high) {
		if (low > high) {
			double temp = low;
			low = high;
			high = temp;
		}
		return rand.nextDouble() * (high - low) + low;
	}

	public float fromRange(float low, float high) {
		if (low > high) {
			float temp = low;
			low = high;
			high = temp;
		}
		return rand.nextFloat() * (high - low) + low;
	}

	public long fromRange(long low, long high) {
		if (low > high) {
			long temp = low;
			low = high;
			high = temp;
		}
		return low + (long) (rand.nextDouble() * (high - low));
	}

	public short fromRange(short low, short high) {
		if (low > high) {
			short temp = low;
			low = high;
			high = temp;
		}
		return (short) (low + (rand.nextDouble() * (high - low)));
	}

	public int getInt() {
        return getInt(null, null);
    }
    public int getInt(Target target, Constraint constraint) {
        if (constraint == null)
            return rand.nextInt();
        int lowerBound = (int)((constraint.getLowerBound() == null) ? Integer.MIN_VALUE : constraint.getLowerBound());
        int upperBound = (int)((constraint.getUpperBound() == null) ? Integer.MAX_VALUE : constraint.getUpperBound());
        int choice = fromRange(lowerBound, upperBound);
        choice = (constraint.isNegative() != null && !constraint.isNegative()) ? Math.max(0, choice) : choice;
        config.choices.add(new Choice(target, choice));
        return choice;
    }

	public double getDouble() {
        return getDouble(null, null);
    }

    public double getDouble(Target target, Constraint constraint) {
        if (constraint == null) {
            byte[] bytes = new byte[8];
            rand.nextBytes(bytes);
            return ByteBuffer.wrap(bytes).getDouble();
        }

        double lowerBound = (double)((constraint.getLowerBound() == null) ? Double.MIN_VALUE : constraint.getLowerBound());
        double upperBound = (double)((constraint.getUpperBound() == null) ? Double.MAX_VALUE : constraint.getUpperBound());
        double choice = fromRange(lowerBound, upperBound);
        choice = (constraint.isNegative() != null && !constraint.isNegative()) ? Math.max(0, choice) : choice;
        config.choices.add(new Choice(target, choice));
        return choice;
    }

	public float getFloat() {
		return getFloat(null, null);
    }

    public float getFloat(Target target, Constraint constraint) {
        if (constraint == null) {
            byte[] bytes = new byte[4];
            rand.nextBytes(bytes);
            return ByteBuffer.wrap(bytes).getFloat();
        }

        float lowerBound = (float)((constraint.getLowerBound() == null) ? Float.MIN_VALUE : constraint.getLowerBound());
        float upperBound = (float)((constraint.getUpperBound() == null) ? Float.MAX_VALUE : constraint.getUpperBound());
        float choice = fromRange(lowerBound, upperBound);
        choice = (constraint.isNegative() != null && !constraint.isNegative()) ? Math.max(0, choice) : choice;
        config.choices.add(new Choice(target, choice));
        return choice;
    }

	public long getLong() {
		return getLong(null, null);
    }

    public long getLong(Target target, Constraint constraint) {
        if (constraint == null)
            return rand.nextLong();

        long lowerBound = (long)((constraint.getLowerBound() == null) ? Long.MIN_VALUE : constraint.getLowerBound());
        long upperBound = (long)((constraint.getUpperBound() == null) ? Long.MAX_VALUE : constraint.getUpperBound());
        long choice = fromRange(lowerBound, upperBound);
        choice = (constraint.isNegative() != null && !constraint.isNegative()) ? Math.max(0, choice) : choice;
        config.choices.add(new Choice(target, choice));
        return choice;
    }

	public short getShort() {
		return getShort(null, null);
    }

    public short getShort(Target target, Constraint constraint) {
        if (constraint == null) {
            return (short) rand.nextInt();
        }

        short lowerBound = (short)((constraint.getLowerBound() == null) ? Short.MIN_VALUE : constraint.getLowerBound());
        short upperBound = (short)((constraint.getUpperBound() == null) ? Short.MAX_VALUE : constraint.getUpperBound());
        short choice = fromRange(lowerBound, upperBound);
        choice = (constraint.isNegative() != null && !constraint.isNegative()) ? (short)Math.max(0, choice) : choice;
        config.choices.add(new Choice(target, choice));
        return choice;
    }

	public char getChar() {
        return getChar(null, null);
    }

    public char getChar(Target target, Constraint constraint) {
        // TODO: should lower and upper bounds affect chars?
        char choice = (char) rand.nextInt();
        config.choices.add(new Choice(target, choice));
        return choice;
    }

	public boolean getBool() {
		return getBool(null, null);
    }

    public boolean getBool(Target target, Constraint constraint) {
        // TODO: constraint for boolean probability?
        boolean choice = rand.nextBoolean();
        config.choices.add(new Choice(target, choice));
        return choice;
    }

	public byte getByte() {
        return getByte(null, null);
    }

    public byte getByte(Target target, Constraint constraint) {
        byte[] bytes = new byte[1];
		rand.nextBytes(bytes);
        byte choice = bytes[0];
        config.choices.add(new Choice(target, choice));
        return choice;
    }

	public String getString() {
		return getString(null, null);
	}

    public String getString(Target target, Constraint constraint) {
        String choice;
        if (constraint.getStringExamples() == null || constraint.getStringExamples().length == 0) {
            int length = rand.nextInt(30);
            byte[] bytes = new byte[length];
            rand.nextBytes(bytes);
            choice = new String(bytes, Charset.forName("UTF-8"));
        } else {
            int index = fromRange(0, constraint.getStringExamples().length-1);
            choice = constraint.getStringExamples()[index];
        }
        config.choices.add(new Choice(target, choice)); // Always "choice" until I can find a better way to store arbitrary Strings in xml
        return choice;
    }

    public Object getEnum(Target target, Constraint constraint, Class<?> enumClass) {
        Object[] values = enumClass.getEnumConstants();
        int index = fromRange(0, values.length - 1);
        config.choices.add(new Choice(target, enumClass.getSimpleName(), index));
        return values[index];
    }

	public void mutateConfig() {
		// Should iterate through all constraints specified in config and have a small
		// chance to mutate it
	}

	public Constraint getConstraint(Target target) {
		return config.findConstraintFor(target);
	}

	public boolean shouldMutate() {
		// Eventually, this should be a ratio based on how many constraints there are
		return should(0.2);
	}

	public boolean should(double prob) {
		return rand.nextDouble() < prob;
	}

	public Constraint makeConstraint(Target target) {
		// These should be set based on constraints / probabilities that create the best
		// random constraint for exploratory fuzzing
        Constraint constraint = new Constraint();
        // TODO: make these constraints set only sometimes
		constraint.setNullProb(rand.nextDouble());
        constraint.setProb(rand.nextDouble());
        constraint.setNegative(rand.nextBoolean());

        if (should(0.5)) {
            double[] range = {rand.nextInt()/2, rand.nextInt()/2};
            constraint.setLowerBound(Math.min(range[0], range[1]));
            constraint.setUpperBound(Math.max(range[0], range[1]));
        }

        if (should(0.3)) {
            String[] stringExamples = {"Johnny"};
            constraint.setStringExamples(stringExamples);
        }

		config.addPair(target, constraint);
		return constraint;
	}
}
