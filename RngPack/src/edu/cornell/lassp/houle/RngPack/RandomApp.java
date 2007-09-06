package edu.cornell.lassp.houle.RngPack;
import java.util.*;

//
// RngPack 1.1a by Paul Houle
// http://www.honeylocust.com/RngPack/
//

/**
*
*
* RandomApp is a simple application that demonstrates the use of
* RngPack.  RandomApp generates random numbers and writes them to the
* standard output.  This is very useful on Unix systems since the
* output can be piped to another application.
* See <A HREF="../RandomApp.txt">RandomApp</A> documentation for
* how to run it.
* 
*
*
* <P>
* <A HREF="/RngPack/src/edu/cornell/lassp/houle/RngPack/RandomApp.java">
* Source code </A> is available.
* 
* @author <A HREF="http://www.honeylocust.com/"> Paul Houle </A> (E-mail: <A HREF="mailto:paul@honeylocust.com">paul@honeylocust.com</A>)
* @version 1.1a
* 
* @see RandomJava
* @see RandomShuffle
*/

public class RandomApp {

    static String generators[]={"ranmar","ranecu","ranlux","randomjava","null","ranmt"};
    static String distributions[]={"flat","gaussian","choose1","choose2","coin1","coin2"};
    
    static final int RANMAR=0,RANECU=1,RANLUX=2,RANJAVA=3,NULL=4,RANMT=5;
    static final int FLAT=0,GAUSSIAN=1,CHOOSE1=2,CHOOSE2=3,COIN1=4,COIN2=5;
    
    public static void main(String args[]) {
	
	String a;
	RandomElement e;
	int i,j,generator,distribution,n;
	int luxury;
	double x;
	long seed;
	int hi,lo;
	boolean seeded=false,gselected=false,dselected=false;
	boolean noprint=false,numbered=false;
	boolean luxuryset=false;
	
	generator=RANMAR;
	distribution=FLAT;
	seed=RandomSeedable.ClockSeed();
	luxury=Ranlux.lxdflt;
	n=1;
	
	
	/* Primitive parser for command line arguments. */
	
	parse_loop: for(i=0;i<args.length;i++)
	    {
		a=args[i].toLowerCase().intern();
		
		if (a=="noprint") {
		    noprint=true;
		    continue;
		};
		
		if (a=="seed") {
		    if (seeded)
			die("RandomApp: only one seed can be passed");
		    if (i==args.length-1) 
			die("RandomApp: missing seed.");
		    
		    i++;
		    a=new String(args[i]);
		    
		    try { seed=Long.parseLong(a); }
		    catch (NumberFormatException ex) {
			die("RandomApp: seed is not a valid number.");};
		    
		    seeded=true;
		    continue;
		};
		
		if (a=="luxury") {
		    if (luxuryset)
			die("RandomApp: only one luxury level can be passed");
		    if (i==args.length-1) 
			die("RandomApp: missing luxury level.");
		    
		    i++;
		    a=new String(args[i]);
		    
		    try { luxury=Integer.parseInt(a); }
		    catch (NumberFormatException ex) {
			die("RandomApp: luxury level is not a valid number.");};
		    
		    luxuryset=true;
		    
		    if (luxury<0 || luxury>Ranlux.maxlev)
			die("RandomApp: luxury level must be between 0 and "+Ranlux.maxlev);
		    continue;
		};
		
		
		for(j=0;j<generators.length;j++)
		    if (a==generators[j])
			{
			    if(gselected)
				die("RandomApp: only one generator can be selected.");
			    generator=j;
			    gselected=true;
			    continue parse_loop;
			};
		
		for(j=0;j<distributions.length;j++)
		    if (a==distributions[j])
			{
			    if(dselected)
				die("RandomApp: only one distribution can be selected.");
			    distribution=j;
			    dselected=true;
			    continue parse_loop;
			};
		
		try
		    {
			
			n=Integer.parseInt(a);
			if (numbered)
			    die("RandomApp: only one number of random numbers can be selected.");
			numbered=true;
		    } catch(NumberFormatException ex) {
			die("RandomApp: syntax error <"+a+">");
		    };
	    };
	
	e=null;
	
	if (generator==RANMAR) {
	    e=new Ranmar(seed);
	} else if (generator==RANECU) {
	    e=new Ranecu(seed);
	} else if (generator==RANLUX) {
	    e=new Ranlux(luxury,seed);
	} else if (generator==RANJAVA) {
	    e=new RandomJava();
	} else if (generator==RANMT) {
	    e=new RanMT(seed);
	}
	
	for(i=1;i<=n;i++)
	    {
		x=0.0;
		if (generator!=NULL) {
		    if(distribution==FLAT) {
			x=e.raw();
		    } else if(distribution==GAUSSIAN) {
			x=e.gaussian();
		    } else if (distribution==CHOOSE1) {
			x=e.choose(5);
		    } else if (distribution==CHOOSE2) {
			x=e.choose(7,10);
		    } else if (distribution==COIN1) {
			x=e.coin() ? 1.0 : 0.0;
		    } else if (distribution==COIN2) {
			x=e.coin(0.7) ? 1.0 : 0.0;
		    } else {
			die("Invalid distribution: "+distribution);
		    };
		};
				
		if(!noprint) System.out.println(x);
	    };
	
	
    };
    
    static void die(String s) {
	System.err.println(s);
	System.exit(-1);
    };
    
    static double nullgen() {
	return 0.0;
    };
    
    
};







