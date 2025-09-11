package eu.hansolo.jdktools.util;

import org.junit.jupiter.api.Test;


public class ArchiveTypeTest {
    @Test
    void testArchiveType() {
        final String wrongFilename   = "ibm-semeru-certified-jdk_x86-64_linux_21.0.4.1.tap.zip";
        final String correctFilename = "ibm-semeru-certified-jdk_x86-64_linux_21.0.4.1.tar.gz";
        assert !isValid(wrongFilename);
        assert isValid(correctFilename);
    }

    boolean isValid(final String filename) {
        return null != filename &&
               !filename.isEmpty() &&
               !filename.endsWith("txt") &&
               !filename.contains("debugimage") &&
               !filename.contains("testimage") &&
               !filename.contains("tap") &&
               !filename.endsWith("json") &&
               !filename.endsWith("bin") &&
               !filename.endsWith("sig") &&
               !filename.contains("-debug-") &&
               filename.startsWith("ibm-semeru-certified");
    }
}
