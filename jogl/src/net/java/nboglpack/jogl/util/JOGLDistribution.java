/*
 * Created on 24.03.2008, 18:16:02
 */
package net.java.nboglpack.jogl.util;

/**
 * Enum for all JOGL distributions.
 * @author Michael Bien
 */
public enum JOGLDistribution {

    WINDOWS_AMD64("Windows", "AMD64"),
    WINDOWS_I586("Windows", "I586"),
    
    LINUX_I586("Linux", "I586"),
    LINUX_AMD64("Linux", "AMD64"),
    
    MACOSX_UNIVERSAL("MacOS X", "UNIVERSAL"),
    MACOSX_PPC("MacOS X", "PPC"),
    
    SOLARIS_AMD64("Solaris", "AMD64"),
    SOLARIS_I586("Solaris", "I586"),
    SOLARIS_SPARC("Solaris", "SPARC"),
    SOLARIS_SPARC_V9("Solaris", "SPARC V9");
    
    /**
     * OS name.
     */
    public final String os;
    
    /**
     * CPU architectur.
     */
    public final String arch;

    private JOGLDistribution(String os, String arch) {
        this.os = os;
        this.arch = arch;
    }

    @Override
    public String toString() {
        return os + " (" + arch + ")";
    }

    /**
     * Returns the unique key for the distribution (eg linux-amd64).
     */
    public String key() {
        String osKey = os.toLowerCase();
        if (osKey.startsWith("mac")) {
            osKey = "maxosx";
        }
        return osKey + "-" + arch.toLowerCase();
    }
    
    /**
     * Returns the with this system compatible distribuion enum or null.
     */
    public static JOGLDistribution getCompatible() {

        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        if(os == null || arch == null)
            return null;
        
        if (os.contains("mac")) { // Mac Os X
            if (arch.contains("ppc")) { // ppc64 currently not supported - is ppc compatible with ppc64?
                return JOGLDistribution.MACOSX_PPC;
            } else {
                return JOGLDistribution.MACOSX_UNIVERSAL;
            }
        } else if (os.contains("windows")) {
            if (isAMD64bit(arch)) {
                return JOGLDistribution.WINDOWS_AMD64;
            } else {
                return JOGLDistribution.WINDOWS_I586;
            }
        } else if (os.contains("linux")) {
            if (isAMD64bit(arch)) {
                return JOGLDistribution.LINUX_AMD64;
            } else {
                return JOGLDistribution.LINUX_I586;
            }
        } else if (os.contains("solaris") || os.contains("sunos")) {
            if (isAMD64bit(arch)) {
                return JOGLDistribution.SOLARIS_AMD64;
            } else if (arch.equals("sparcv9")) {
                return JOGLDistribution.SOLARIS_SPARC_V9;
            } else if (arch.endsWith("sparc")) {
                return JOGLDistribution.SOLARIS_SPARC;
            } else {
                return JOGLDistribution.SOLARIS_I586;
            }
        } else {
            return null;
        }
    }

    private final static boolean isAMD64bit(String arch) {
        return arch.contains("amd64") || arch.endsWith("_64");
    }
    
}
