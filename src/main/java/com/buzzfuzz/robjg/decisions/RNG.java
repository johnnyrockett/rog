package com.buzzfuzz.robjg.decisions;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.buzzfuzz.robjg.decisions.Config;
import com.buzzfuzz.robjg.decisions.Constraint;
import com.buzzfuzz.robjg.decisions.Target;

public class RNG {

	private Random rand;
	private Config config;
	private long seed;
	private Set<String> crashes;
	private int crashCount;

	public RNG() {
		this(System.currentTimeMillis());
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
		return rand.nextInt(high - low + 1) + low;
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
		return rand.nextInt();
	}

	public double getDouble() {
		byte[] bytes = new byte[8];
		rand.nextBytes(bytes);
		return ByteBuffer.wrap(bytes).getDouble();
	}

	public float getFloat() {
		byte[] bytes = new byte[4];
		rand.nextBytes(bytes);
		return ByteBuffer.wrap(bytes).getFloat();
	}

	public long getLong() {
		return rand.nextLong();
	}

	public short getShort() {
		return (short) rand.nextInt();
	}

	public char getChar() {
		return (char) rand.nextInt();
	}

	public boolean getBool() {
		return rand.nextBoolean();
	}

	public byte getByte() {
		byte[] bytes = new byte[1];
		rand.nextBytes(bytes);
		return bytes[0];
	}

	public String getString() {
		int length = rand.nextInt(30);
		return getString(length);
	}

	public String getString(int length) {
		byte[] bytes = new byte[length];
		rand.nextBytes(bytes);
		return new String(bytes, Charset.forName("UTF-8"));
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
		constraint.setNullProb(rand.nextDouble());
		constraint.setProb(rand.nextDouble());
		config.addPair(target, constraint);
		return constraint;
	}
}
