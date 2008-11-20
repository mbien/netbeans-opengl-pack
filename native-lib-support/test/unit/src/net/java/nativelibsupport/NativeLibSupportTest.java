/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.nativelibsupport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.java.nativelibsupport.natives_config.Library;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Bien
 */
public class NativeLibSupportTest {
    
    public NativeLibSupportTest() {
    }


    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void readXMLConfigTest() {
        System.out.println("* * * read xml configuration test * * *");
        Library lib = loadLibConfigXML();
        System.out.println("lib name: "+lib.getName());
        System.out.println("lib folder: "+lib.getFolder());

        List<Library.Os> oses = lib.getOs();
        for (Library.Os os : oses) {
            System.out.println("-OS folder: "+os.getFolder());
            System.out.println("-OS regexp: "+os.getRegex());

            List<Library.Os.Cpu> cpus = os.getCpu();
            for (Library.Os.Cpu cpu : cpus) {
                System.out.println("--CPU folder: "+cpu.getFolder());
                System.out.println("--CPU regexp: "+cpu.getRegex());
            }
        }
                
    }
    
    @Test
    public void testAssambleLibPath() {
        
        System.out.println("* * * path assamble test * * *");
        
        String[] spec = {
            "Windows 2000", "amd64",
            "Windows Vista", "i586",
            "Windows", "i386",
            "Linux", "x86",
            "Linux", "x86_64",
            "Linux", "x64",
            "Solaris", "sparc",
            "MacOS X", "ppc",
            "MacOS X", "something else",
            System.getProperty("os.name"), System.getProperty("os.arch")
        };
        
        Library lib = loadLibConfigXML();
        
        for (int i = 0; i < spec.length; i+=2) {
            
            StringBuilder path = new StringBuilder();
            
            System.out.println(spec[i]+" "+ spec[i+1]);
            
            invoke("assambleLibPath", null, NativeLibSupport.class, 
                    lib, spec[i], spec[i+1], path);

            assertTrue(path.toString().trim().length() != 0);

            System.out.println("path: "+path.toString());
        }
        
    }
    
    
    private Library loadLibConfigXML() {
        try {
            JAXBContext jc = JAXBContext.newInstance("net.java.nativelibsupport.natives_config", this.getClass().getClassLoader());
            
            System.out.println("JAXB context is:\n"+jc);
            
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Object obj = unmarshaller.unmarshal(this.getClass().getResourceAsStream("test-natives-config.xml"));

            if (obj instanceof Library) {
                return (Library) obj;
            } else {
                fail("wrong root element");
            }
        } catch (JAXBException ex) {
            Logger.getLogger(NativeLibSupportTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
        return null;
    }
    
    
    /**
     * Invokes a method.
     * @param instance instance of class c (may be null for static methods)
     * @param c class containing method to invoke (never null)
     * @return Returns null for void else value of invoked method
     */
    public Object invoke(String method, Object instance, Class c, Object... params) {
        
        assertNotNull(method);
        assertNotNull(c);
        
        if(instance != null) {
            assertTrue(c.isInstance(instance));
        }
        
        try {
            final Method[] methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; ++i) {
                if (methods[i].getName().equals(method)) {
                    methods[i].setAccessible(true);
                    return methods[i].invoke(instance, params);
                }
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NativeLibSupportTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NativeLibSupportTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(NativeLibSupportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        fail();
        return null;
    }


}