package edu.ncsu.csc.itrust.unit.T822.Misc;

import edu.ncsu.csc.itrust.util.OSInfo;
import junit.framework.TestCase;
import org.junit.After;

public class OSinfoTest extends TestCase {
    String osName = System.getProperty("os.name");

    @After
    public void tearDown() {
        System.setProperty("os.name", osName);
    }

    public void testUnix(){
        System.setProperty("os.name", "Super unix");
        OSInfo.OS os = OSInfo.getOs();

        assertEquals(OSInfo.OS.UNIX, os);
    }

    public void testWindows(){
        System.setProperty("os.name", "mundane windows");
        OSInfo.OS os = OSInfo.getOs();

        assertEquals(OSInfo.OS.WINDOWS, os);
    }

    public void testPosix1(){
        System.setProperty("os.name", "solaris");
        OSInfo.OS os = OSInfo.getOs();

        assertEquals(OSInfo.OS.POSIX_UNIX, os);
    }

    public void testPosix2(){
        System.setProperty("os.name", "aix");
        OSInfo.OS os = OSInfo.getOs();

        assertEquals(OSInfo.OS.POSIX_UNIX, os);
    }

    public void testOther(){
        System.setProperty("os.name", "this is not an os");
        OSInfo.OS os = OSInfo.getOs();

        assertEquals(OSInfo.OS.OTHER, os);
    }

    public void testMac(){
        System.setProperty("os.name", "garbage mac os");
        OSInfo.OS os = OSInfo.getOs();

        assertEquals(OSInfo.OS.MAC, os);
    }

}
