package eu.hansolo.jdktools.versioning;

import org.junit.jupiter.api.Test;


public class JavaVersionTest {

    @Test
    void testParseJavaVersion() {
        final String v1  = "8.0.472";
        final String v2  = "25.0.2";
        final String v3  = "26.0.0";
        final String v4  = "25.0.2+1";
        final String v5  = "8.0.482+1";
        final String v6  = "21.0.10+fx";
        final String v7  = "21.0.10+crac";
        final String v8  = "26.0.0+fx";
        final String v9  = "26.0.0+crac";
        final String v10 = "27.0.0-ea.16";
        final String v11 = "26.0.0-ea.35";
        final String v12 = "26.0.0-ea.35+crac";
        final String v13 = "25.0.2+r25";
        final String v14 = "23.1.10+r21";
        final String v15 = "22.3.5+r11";
        final String v16 = "25.0.2+fx";
        final String v17 = "22.1.0+1.r17";
        final String v18 = "25.0.3-ea";
        final String v19 = "25.0.3-ea+r45";

        JavaVersion version1  = JavaVersion.fromText(v1);
        JavaVersion version2  = JavaVersion.fromText(v2);
        JavaVersion version3  = JavaVersion.fromText(v3);
        JavaVersion version4  = JavaVersion.fromText(v4);
        JavaVersion version5  = JavaVersion.fromText(v5);
        JavaVersion version6  = JavaVersion.fromText(v6);
        JavaVersion version7  = JavaVersion.fromText(v7);
        JavaVersion version8  = JavaVersion.fromText(v8);
        JavaVersion version9  = JavaVersion.fromText(v9);
        JavaVersion version10 = JavaVersion.fromText(v10);
        JavaVersion version11 = JavaVersion.fromText(v11);
        JavaVersion version12 = JavaVersion.fromText(v12);
        JavaVersion version13 = JavaVersion.fromText(v13);
        JavaVersion version14 = JavaVersion.fromText(v14);
        JavaVersion version15 = JavaVersion.fromText(v15);
        JavaVersion version16 = JavaVersion.fromText(v16);
        JavaVersion version17 = JavaVersion.fromText(v17);
        JavaVersion version18 = JavaVersion.fromText(v18);
        JavaVersion version19 = JavaVersion.fromText(v19);


        System.out.println(v1 + " -> " + version1);
        System.out.println(v2 + " -> " + version2);
        System.out.println(v3 + " -> " + version3);
        System.out.println(v4 + " -> " + version4);
        System.out.println(v5 + " -> " + version5);
        System.out.println(v6 + " -> " + version6);
        System.out.println(v7 + " -> " + version7);
        System.out.println(v8 + " -> " + version8);
        System.out.println(v9 + " -> " + version9);
        System.out.println(v10 + " -> " + version10);
        System.out.println(v11 + " -> " + version11);
        System.out.println(v12 + " -> " + version12);
        System.out.println(v13 + " -> " + version13);
        System.out.println(v14 + " -> " + version14);
        System.out.println(v15 + " -> " + version15);
        System.out.println(v16 + " -> " + version16);
        System.out.println(v17 + " -> " + version17);
        System.out.println(v18 + " -> " + version18);
        System.out.println(v19 + " -> " + version19);

        //assert version2.compareTo(version4) == -1; // Returns 0 at the moment because the build number is not taken into account
    }
}
