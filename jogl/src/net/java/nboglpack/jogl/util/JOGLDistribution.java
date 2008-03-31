/*
 * Created on 24.03.2008, 18:16:02
 */
package net.java.nboglpack.jogl.util;

/**
 * Enum for all JOGL distributions.
 * @author Michael Bien
 */
public enum JOGLDistribution {
    
    WINDOWS_AMD64("windows", "amd64"),
    WINDOWS_I586("windows", "i586"),
    
    LINUX_I586("linux", "i586"),
    LINUX_AMD64("linux", "amd64"),
    
    MACOSX_UNIVERSAL("macosx", "universal"),
    MACOSX_PPC("macosx", "ppc"),
    
    SOLARIS_AMD64("solaris", "amd64"),
    SOLARIS_I586("solaris", "i586"),
    SOLARIS_SPARC("solaris", "sparc"),
    SOLARIS_SPARC_V9("solaris", "sparcv9");
    
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
        String osName;
        if(os.equals(MACOSX_UNIVERSAL.os))
            osName = "MacOS X";
        else
            osName = os.substring(0,1).toUpperCase() + os.substring(1);
        return osName + " (" + arch + ")";
    }

    /**
     * Returns the unique key for the distribution (eg linux-amd64).
     */
    public String key() {
        return os + "-" + arch;
    }
    
    public static JOGLDistribution parseKey(String key) {
        JOGLDistribution[] values = JOGLDistribution.values();
        for (int i = 0; i < values.length; i++) {
            JOGLDistribution value = values[i];
	    if(value.key().equals(key))
                return value;
        }
        return null;
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
