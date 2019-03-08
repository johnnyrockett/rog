package com.buzzfuzz.buzz.robjg;

import com.buzzfuzz.rog.ROG;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testString()
    {
        // Make it with an array of string paths to the .class files
        String[] sPaths = {".../target/classes"};
        ROG rog = new ROG(sPaths);

        for( int i =0; i<100; i++ )
        {
            String str = (String)rog.getInstance(String.class);
            System.out.println(str);
        }

    }
}
