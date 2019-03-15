package com.buzzfuzz.buzz.robjg;

import java.util.ArrayList;
import java.util.List;

import com.buzzfuzz.rog.ROG;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Returns new clean example ROG object
     */
    private static ROG getROG() {
        // Make it with an array of string paths to the .class files
        String[] sPaths = { ".../target/classes" };
        return new ROG(sPaths);
    }

    /**
     * Rigourous Test :-)
     */
    public void testString() {
        ROG rog = getROG();

        for (int i = 0; i < 100; i++) {
            String str = rog.getInstance(String.class);
            System.out.println(str);
        }

    }

    /**
     * Test creating an object using a class
     */
    public void testClass() {
        ROG rog = getROG();

        String str = rog.getInstance(String.class);
        assert (str != null);
    }

    /**
     * Test creating nested generic arguments for a method
     */
    private void NestedListString(List<List<String>> nested) {
        // Do nothing: just need method signiature
    }

    public void testNestedListString() {
        ROG rog = getROG();

        Object[] argInstancesFor;
        try {
            argInstancesFor = rog
                    .getArgInstancesFor(this.getClass().getDeclaredMethod("NestedListString", List.class));
            System.out.println(argInstancesFor);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            argInstancesFor = null;
        } catch (SecurityException e) {
            e.printStackTrace();
            argInstancesFor = null;
        }
        assert(argInstancesFor != null);
    }
}
