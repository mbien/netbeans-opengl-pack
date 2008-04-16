/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.nativelibsupport;

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
    public void readTest() {
        try {
            JAXBContext jc = JAXBContext.newInstance("net.java.nativelibsupport.natives_config", this.getClass().getClassLoader());

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Object obj = unmarshaller.unmarshal(this.getClass().getResourceAsStream("test-natives-config.xml"));
            
            if(obj instanceof Library) {
                Library lib = (Library)obj;
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
                
            }else{
                fail("wrong root type: "+obj);
            }
            
        } catch (JAXBException ex) {
            Logger.getLogger(NativeLibSupportTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deploy method, of class NativeLibSupport.
     */
    @Test
    public void testDeploy() {
//        System.out.println("deploy");
//        String libraryJarName = "";
//        Distribution distribution = null;
//        File distFolder = null;
//        String nativesFolderPrefix = "";
//        NativeLibSupport.deploy(libraryJarName, distribution, distFolder, nativesFolderPrefix);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }



}