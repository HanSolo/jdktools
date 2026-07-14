package eu.hansolo.jdktools.versioning;

import eu.hansolo.jdktools.ReleaseStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class SdkmanVersionTest {

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
    void testParseSdkmanVersion() {
        final String v1  = "8.0.472";
        final String v2  = "25.0.2";
        final String v3  = "26.0.0";
        final String v4  = "25.0.2+1";
        final String v5  = "8.0.482+1";
        final String v6  = "21.0.10-fx";
        final String v7  = "21.0.10-crac";
        final String v8  = "26.0.0-fx";
        final String v9  = "26.0.0-crac";
        final String v10 = "27.0.0+ea.16";
        final String v11 = "26.0.0+ea.35";
        final String v12 = "26.0.0-crac+ea.35";
        final String v13 = "25.0.2+r25";
        final String v14 = "23.1.10+r21";
        final String v15 = "22.3.5+r11";
        final String v16 = "25.0.2-fx+1";
        final String v17 = "22.1.0+1.r17";
        final String v18 = "25.0.3+ea";
        final String v19 = "25.0.3+ea.r45";
        final String v20 = "25.0.3-crac+ea.2";
        final String v21 = "1.8u262";
        final String v22 = "8u242+1";

        SdkmanVersion version1  = SdkmanVersion.fromText(v1);
        SdkmanVersion version2  = SdkmanVersion.fromText(v2);
        SdkmanVersion version3  = SdkmanVersion.fromText(v3);
        SdkmanVersion version4  = SdkmanVersion.fromText(v4);
        SdkmanVersion version5  = SdkmanVersion.fromText(v5);
        SdkmanVersion version6  = SdkmanVersion.fromText(v6);
        SdkmanVersion version7  = SdkmanVersion.fromText(v7);
        SdkmanVersion version8  = SdkmanVersion.fromText(v8);
        SdkmanVersion version9  = SdkmanVersion.fromText(v9);
        SdkmanVersion version10 = SdkmanVersion.fromText(v10);
        SdkmanVersion version11 = SdkmanVersion.fromText(v11);
        SdkmanVersion version12 = SdkmanVersion.fromText(v12);
        SdkmanVersion version13 = SdkmanVersion.fromText(v13);
        SdkmanVersion version14 = SdkmanVersion.fromText(v14);
        SdkmanVersion version15 = SdkmanVersion.fromText(v15);
        SdkmanVersion version16 = SdkmanVersion.fromText(v16);
        SdkmanVersion version17 = SdkmanVersion.fromText(v17);
        SdkmanVersion version18 = SdkmanVersion.fromText(v18);
        SdkmanVersion version19 = SdkmanVersion.fromText(v19);
        SdkmanVersion version20 = SdkmanVersion.fromText(v20);
        SdkmanVersion version21 = SdkmanVersion.fromText(v21);
        SdkmanVersion version22 = SdkmanVersion.fromText(v22);

        System.out.println(v1 + " -> " + version1.toString());
        System.out.println(v2 + " -> " + version2.toString());
        System.out.println(v3 + " -> " + version3.toString());
        System.out.println(v4 + " -> " + version4.toString());
        System.out.println(v5 + " -> " + version5.toString());
        System.out.println(v6 + " -> " + version6.toString());
        System.out.println(v7 + " -> " + version7.toString());
        System.out.println(v8 + " -> " + version8.toString());
        System.out.println(v9 + " -> " + version9.toString());
        System.out.println(v10 + " -> " + version10.toString());
        System.out.println(v11 + " -> " + version11.toString());
        System.out.println(v12 + " -> " + version12.toString());
        System.out.println(v13 + " -> " + version13.toString());
        System.out.println(v14 + " -> " + version14.toString());
        System.out.println(v15 + " -> " + version15.toString());
        System.out.println(v16 + " -> " + version16.toString());
        System.out.println(v17 + " -> " + version17.toString());
        System.out.println(v18 + " -> " + version18.toString());
        System.out.println(v19 + " -> " + version19.toString());
        System.out.println(v20 + " -> " + version20.toString());
        System.out.println(v21 + " -> " + version21.toString());
        System.out.println(v22 + " -> " + version22.toString());

        assert v1.equals(version1.toString());
        assert v2.equals(version2.toString());
        assert v3.equals(version3.toString());
        assert v4.equals(version4.toString());
        assert v5.equals(version5.toString());
        assert v6.equals(version6.toString());
        assert v7.equals(version7.toString());
        assert v8.equals(version8.toString());
        assert v9.equals(version9.toString());
        assert v10.equals(version10.toString());
        assert v11.equals(version11.toString());
        assert v12.equals(version12.toString());
        assert v13.equals(version13.toString());
        assert v14.equals(version14.toString());
        assert v15.equals(version15.toString());
        assert v16.equals(version16.toString());
        assert v17.equals(version17.toString());
        assert v18.equals(version18.toString());
        assert v19.equals(version19.toString());
        assert v20.equals(version20.toString());
        assert "8.0.262".equals(version21.toString());
        assert "8.0.242+1".equals(version22.toString());

        List<SdkmanVersion> versions = Arrays.asList(version1, version2, version3, version4, version5, version6, version7, version8, version9, version10, version11, version12, version13, version14, version15, version16, version17, version18, version19, version20, version21, version22);
        versions.sort(Comparator.naturalOrder());
        assert versions.get(0).equals(version22);
        assert versions.get(14).equals(version19);
        assert versions.get(15).equals(version20);
        assert versions.get(21).equals(version10);

        SdkmanVersion cv1  = SdkmanVersionBuilder.feature(8).interim(0).update(472).build();
        SdkmanVersion cv2  = SdkmanVersionBuilder.feature(25).interim(0).update(2).build();
        SdkmanVersion cv3  = SdkmanVersionBuilder.feature(26).interim(0).update(0).build();
        SdkmanVersion cv4  = SdkmanVersionBuilder.feature(25).interim(0).update(2).buildNumber(1).build();
        SdkmanVersion cv5  = SdkmanVersionBuilder.feature(8).interim(0).update(482).buildNumber(1).build();
        SdkmanVersion cv6  = SdkmanVersionBuilder.feature(21).interim(0).update(10).fx(true).build();
        SdkmanVersion cv7  = SdkmanVersionBuilder.feature(21).interim(0).update(10).crac(true).build();
        SdkmanVersion cv8  = SdkmanVersionBuilder.feature(26).interim(0).update(0).fx(true).build();
        SdkmanVersion cv9  = SdkmanVersionBuilder.feature(26).interim(0).update(0).crac(true).build();
        SdkmanVersion cv10 = SdkmanVersionBuilder.feature(27).interim(0).update(0).earlyAccess(true).buildNumber(16).build();
        SdkmanVersion cv11 = SdkmanVersionBuilder.feature(26).interim(0).update(0).earlyAccess(true).buildNumber(35).build();
        SdkmanVersion cv12 = SdkmanVersionBuilder.feature(26).interim(0).update(0).earlyAccess(true).buildNumber(35).crac(true).build();
        SdkmanVersion cv13 = SdkmanVersionBuilder.feature(25).interim(0).update(2).graal(true).target(25).build();
        SdkmanVersion cv14 = SdkmanVersionBuilder.feature(23).interim(1).update(10).graal(true).target(21).build();
        SdkmanVersion cv15 = SdkmanVersionBuilder.feature(22).interim(3).update(5).graal(true).target(11).build();
        SdkmanVersion cv16 = SdkmanVersionBuilder.feature(25).interim(0).update(2).fx(true).buildNumber(1).build();
        SdkmanVersion cv17 = SdkmanVersionBuilder.feature(22).interim(1).update(0).graal(true).buildNumber(1).target(17).build();
        SdkmanVersion cv18 = SdkmanVersionBuilder.feature(25).interim(0).update(3).earlyAccess(true).build();
        SdkmanVersion cv19 = SdkmanVersionBuilder.feature(25).interim(0).update(3).earlyAccess(true).graal(true).target(45).build();
        SdkmanVersion cv20 = SdkmanVersionBuilder.feature(25).interim(0).update(3).earlyAccess(true).buildNumber(2).crac(true).build();
        SdkmanVersion cv21 = SdkmanVersionBuilder.feature(8).interim(0).update(262).build();
        SdkmanVersion cv22 = SdkmanVersionBuilder.feature(8).interim(0).update(242).buildNumber(1).build();
        assert version1.equals(cv1);
        assert version2.equals(cv2);
        assert version3.equals(cv3);
        assert version4.equals(cv4);
        assert version5.equals(cv5);
        assert version6.equals(cv6);
        assert version7.equals(cv7);
        assert version8.equals(cv8);
        assert version9.equals(cv9);
        assert version10.equals(cv10);
        assert version11.equals(cv11);
        assert version12.equals(cv12);
        assert version13.equals(cv13);
        assert version14.equals(cv14);
        assert version15.equals(cv15);
        assert version16.equals(cv16);
        assert version17.equals(cv17);
        assert version18.equals(cv18);
        assert version19.equals(cv19);
        assert version20.equals(cv20);
        assert version21.equals(cv21);
        assert version22.equals(cv22);

        SdkmanVersion sdkv1 = SdkmanVersionBuilder.feature(25).interim(0).update(2).buildNumber(1).build();
        SdkmanVersion sdkv2 = SdkmanVersionBuilder.feature(25).interim(0).update(2).buildNumber(3).build();
        assert !sdkv1.isGreaterThan(sdkv2);
        assert !sdkv1.equals(sdkv2);
        assert sdkv1.isSmallerThan(sdkv2);

        SdkmanVersion sdkv3 = SdkmanVersionBuilder.feature(25).interim(0).update(0).buildNumber(1).fx(true).build();
        SdkmanVersion sdkv4 = SdkmanVersionBuilder.feature(25).interim(0).update(0).buildNumber(1).fx(true).build();
        assert !sdkv3.isGreaterThan(sdkv4);
        assert sdkv3.equals(sdkv4);
        assert !sdkv3.isSmallerThan(sdkv4);

        VersionNumber vn20 = cv20.toVersionNumber();
        VersionNumber correct = VersionNumberBuilder.create(25).interimNumber(0).updateNumber(3).releaseStatus(ReleaseStatus.EA).buildNumber(2).build();
        System.out.println(vn20 + " == " + correct);
        assert vn20.equals(correct);
    }
}
