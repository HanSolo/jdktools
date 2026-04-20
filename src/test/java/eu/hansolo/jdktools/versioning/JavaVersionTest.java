package eu.hansolo.jdktools.versioning;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JavaVersionTest {

    /*
    8.0.472
    25.0.2
    26.0.0
    25.0.2+1
    8.0.482+1
    21.0.10+fx
    21.0.10+crac
    26.0.0+fx
    26.0.0+crac
    27.0.0-ea.16
    26.0.0-ea.35
    26.0.0-ea.35+crac
    25.0.2+r25
    23.1.10+r21
    22.3.5+r11
    25.0.2+fx
    22.1.0+1.r17
    25.0.3-ea
    25.0.3-ea+r45
    25.0.3-ea.2+crac
    1.8u262
    8u242+1
    */

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
        final String v20 = "25.0.3-ea.2+crac";
        final String v21 = "1.8u262";
        final String v22 = "8u242+1";

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
        JavaVersion version20 = JavaVersion.fromText(v20);
        JavaVersion version21 = JavaVersion.fromText(v21);
        JavaVersion version22 = JavaVersion.fromText(v22);

        System.out.println("- FROM TEXT TO STRING -----------------------------");

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
        System.out.println(v20 + " -> " + version20);
        System.out.println(v21 + " -> " + version21);
        System.out.println(v22 + " -> " + version22);

        System.out.println("- SORTING -----------------------------");

        List<JavaVersion> versions = Arrays.asList(version1, version2, version3, version4, version5, version6, version7, version8, version9, version10, version11, version12, version13, version14, version15, version16, version17, version18, version19, version20, version21, version22);
        versions.sort(Comparator.naturalOrder());
        versions.forEach(System.out::println);

        System.out.println("- EQUALS -----------------------------");
        JavaVersion cv1  = JavaVersionBuilder.create().feature(8).interim(0).update(472).build();
        JavaVersion cv2  = JavaVersionBuilder.create().feature(25).interim(0).update(2).build();
        JavaVersion cv3  = JavaVersionBuilder.create().feature(26).interim(0).update(0).build();
        JavaVersion cv4  = JavaVersionBuilder.create().feature(25).interim(0).update(2).buildNumber(1).build();
        JavaVersion cv5  = JavaVersionBuilder.create().feature(8).interim(0).update(482).buildNumber(1).build();
        JavaVersion cv6  = JavaVersionBuilder.create().feature(21).interim(0).update(10).fx(true).build();
        JavaVersion cv7  = JavaVersionBuilder.create().feature(21).interim(0).update(10).crac(true).build();
        JavaVersion cv8  = JavaVersionBuilder.create().feature(26).interim(0).update(0).fx(true).build();
        JavaVersion cv9  = JavaVersionBuilder.create().feature(26).interim(0).update(0).crac(true).build();
        JavaVersion cv10  = JavaVersionBuilder.create().feature(27).interim(0).update(0).earlyAccess(true).buildNumber(16).build();
        JavaVersion cv11 = JavaVersionBuilder.create().feature(26).interim(0).update(0).earlyAccess(true).buildNumber(35).build();
        JavaVersion cv12 = JavaVersionBuilder.create().feature(26).interim(0).update(0).earlyAccess(true).buildNumber(35).crac(true).build();
        JavaVersion cv13 = JavaVersionBuilder.create().feature(25).interim(0).update(2).graal(true).target(25).build();
        JavaVersion cv14 = JavaVersionBuilder.create().feature(23).interim(1).update(10).graal(true).target(21).build();
        JavaVersion cv15 = JavaVersionBuilder.create().feature(22).interim(3).update(5).graal(true).target(11).build();
        JavaVersion cv16 = JavaVersionBuilder.create().feature(25).interim(0).update(2).fx(true).build();
        JavaVersion cv17 = JavaVersionBuilder.create().feature(22).interim(1).update(0).graal(true).buildNumber(1).target(17).build();
        JavaVersion cv18 = JavaVersionBuilder.create().feature(25).interim(0).update(3).earlyAccess(true).build();
        JavaVersion cv19 = JavaVersionBuilder.create().feature(25).interim(0).update(3).earlyAccess(true).graal(true).target(45).build();
        JavaVersion cv20 = JavaVersionBuilder.create().feature(25).interim(0).update(3).earlyAccess(true).buildNumber(2).crac(true).build();
        JavaVersion cv21 = JavaVersionBuilder.create().feature(8).interim(0).update(262).build();
        JavaVersion cv22 = JavaVersionBuilder.create().feature(8).interim(0).update(242).buildNumber(1).build();
        assert version1.equals(cv1) == true;
        assert version2.equals(cv2) == true;
        assert version3.equals(cv3) == true;
        assert version4.equals(cv4) == true;
        assert version5.equals(cv5) == true;
        assert version6.equals(cv6) == true;
        assert version7.equals(cv7) == true;
        assert version8.equals(cv8) == true;
        assert version9.equals(cv9) == true;
        assert version10.equals(cv10) == true;
        assert version11.equals(cv11) == true;
        System.out.println(version12.toString(false, true) + " -> " + cv12.toString(false, true));
        assert version12.equals(cv12) == true;
        assert version13.equals(cv13) == true;
        assert version14.equals(cv14) == true;
        assert version15.equals(cv15) == true;
        assert version16.equals(cv16) == true;
        assert version17.equals(cv17) == true;
        assert version18.equals(cv18) == true;
        assert version19.equals(cv19) == true;
        assert version20.equals(cv20) == true;
        assert version21.equals(cv21) == true;
        assert version22.equals(cv22) == true;

        System.out.println("- COMPARES -----------------------------");
        JavaVersion jv1 = JavaVersionBuilder.create().feature(25).interim(0).update(2).buildNumber(1).build();
        JavaVersion jv2 = JavaVersionBuilder.create().feature(25).interim(0).update(2).buildNumber(3).build();

        System.out.println(jv1.toString(false, true) + " > " + jv2 + " -> " + jv1.isGreaterThan(jv2));
        System.out.println(jv1 + " = " + jv2 + " -> " + jv1.equals(jv2));
        System.out.println(jv1 + " < " + jv2 + " -> " + jv1.isSmallerThan(jv2));
    }
}
