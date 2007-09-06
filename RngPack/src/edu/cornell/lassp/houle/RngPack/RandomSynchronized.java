package edu.cornell.lassp.houle.RngPack;

import java.util.*;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

//
// RngPack 1.1a by Paul Houle
// http://www.honeylocust.com/RngPack/
//

/**
* RandomSynchronized is a wrapper class that makes a random number generator
* safe for multi-threaded operation by serializing access in time.  For
* high-performance applications,  it is better for each thread to have it's
* own random number generator.
*
* Note this class is declared serializable,  but serialization won't be
* successful if it's wrapping a non-serializable generator.
* <P>
* <A HREF="/RngPack/src/edu/cornell/lassp/houle/RngPack/RandomSynchronized.java">
* Source code </A> is available.
* 
* @author <A HREF="http://www.honeylocust.com/"> Paul Houle </A> (E-mail: <A HREF="mailto:paul@honeylocust.com">paul@honeylocust.com</A>)
* @version 1.1a
* 
* @see RandomElement
*/

public abstract class RandomSynchronized extends RandomElement implements Serializable {

    private RandomElement rng;

    public RandomSynchronized(RandomElement rng) {
	this.rng=rng;
    }
    
    /**
     * Synchronized the raw() method,  which is generally not threadsafe.
     *
     * @see RandomJava
     *
     * @return a random double in the range [0,1]
     */
    
    synchronized public double raw() {
	return rng.raw();
    }
    
/**
 * This method probably isn't threadsafe in implementations,  so it's
 * synchronized
 *
 *
 * @param d array to be filled with doubles
 * @param n number of doubles to generate
 */     

    synchronized public void raw(double d[],int n) {
	rng.raw(d,n);
    }
  
/**
 *
 * Wrapped so generators can override.
 *
 * @param lo lower limit of range
 * @param hi upper limit of range
 * @return a random integer in the range <STRONG>lo</STRONG>, <STRONG>lo</STRONG>+1, ... ,<STRONG>hi</STRONG>
 *
 */ 


    synchronized public int choose(int lo,int hi) {
	return rng.choose(lo,hi);
    }
    
/** 
 *
 * Must be synchronized because state is stored in BMoutput
 *
 * @return a random real with a gaussian distribution,  standard deviation 
 *
 */
 
    synchronized public double gaussian() {
	return rng.gaussian();
    }

/**
 *
 * We wouldn't want some sneaky person to serialize this in the middle
 * of generating a number
 *
 */


    private synchronized void writeObject(final ObjectOutputStream out)
	throws IOException {
	// just so we're synchronized.
	out.defaultWriteObject();
    }

    private synchronized void readObject (final ObjectInputStream in) 
	throws IOException, ClassNotFoundException {
	// just so we're synchronized.
	in.defaultReadObject();
    }    
    
    

}






