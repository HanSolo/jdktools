/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2022 Gerrit Grunwald.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.jdktools.versioning;

import eu.hansolo.jdktools.ReleaseStatus;
import eu.hansolo.jdktools.util.Comparison;
import eu.hansolo.jdktools.util.OutputFormat;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;


class SemverTest {
    @Test
    void semVerFromTextTest() {
        String t1 = ">=11.0.9.0-ea+b1";
        Semver semVer1 = SemverParser.fromText(t1).getSemver1();
        assert semVer1.toString().equals(">=11.0.9-ea+b1");
        assert semVer1.getVersionNumber().toString(OutputFormat.REDUCED, true, false).equals("11.0.9");
        assert ReleaseStatus.EA == semVer1.getReleaseStatus();
        assert semVer1.getPre().equals("-ea");
        assert semVer1.getMetadata().equals("+b1");
        assert Comparison.GREATER_THAN_OR_EQUAL == semVer1.getComparison();

        String t2 = "1.8.0.252-ea";
        Semver semVer2 = SemverParser.fromText(t2).getSemver1();
        assert semVer2.toString().equals("8.0.252-ea");

        List<Semver> versions = List.of(new Semver(new VersionNumber(11, 0, 7, 0)),
                                        new Semver(new VersionNumber(11, 0, 7, 5)),
                                        new Semver(new VersionNumber(11, 0, 8, 0)),
                                        new Semver(new VersionNumber(11, 0, 8, 0), ReleaseStatus.EA),
                                        new Semver(new VersionNumber(11, 0, 8, 0), ReleaseStatus.EA, "meta"),
                                        new Semver(new VersionNumber(11, 0, 8, 2)),
                                        new Semver(new VersionNumber(11, 0, 8, 5)),
                                        new Semver(new VersionNumber(11, 0, 8, 9)),
                                        new Semver(new VersionNumber(11, 0, 9, 1)),
                                        new Semver(new VersionNumber(11, 0, 9, 1), ReleaseStatus.EA),
                                        new Semver(new VersionNumber(11, 0, 10, 0)));

        String              t3              = ">11.0.8-ea+meta<=11.0.9.1-ea+meta";
        SemverParsingResult result3         = SemverParser.fromText(t3);
        List<Semver>        versionsBetween = versions.stream().filter(result3.getFilter()).collect(Collectors.toList());
        assert versionsBetween.toString().equals("[11.0.8, 11.0.8.2, 11.0.8.5, 11.0.8.9, 11.0.9.1-ea]");

        String              t4                 = ">11.0.8.2";
        SemverParsingResult result4            = SemverParser.fromText(t4);
        List<Semver>        versionsLargerThan = versions.stream().filter(result4.getFilter()).collect(Collectors.toList());
        assert versionsLargerThan.toString().equals("[11.0.8.5, 11.0.8.9, 11.0.9.1, 11.0.9.1-ea, 11.0.10]");

        String              t5                  = "<=11.0.8";
        SemverParsingResult result5             = SemverParser.fromText(t5);
        List<Semver>        versionsSmallerThan = versions.stream().filter(result5.getFilter()).collect(Collectors.toList());
        assert versionsSmallerThan.toString().equals("[11.0.7, 11.0.7.5, 11.0.8, 11.0.8-ea, 11.0.8-ea+meta]");

        Semver semVer3 = new Semver(new VersionNumber(11, 0, 8, 0), ReleaseStatus.EA);
        Semver semVer4 = new Semver(new VersionNumber(11, 0, 8, 0), ReleaseStatus.EA, "meta");
        assert semVer3.equalTo(semVer4);
        assert semVer3.toString().equals("11.0.8-ea");
        assert semVer4.toString().equals("11.0.8-ea+meta");

        String              t6                  = "11.0.8.0.1-ea+meta";
        SemverParsingResult result6             = SemverParser.fromText(t6);
        Semver              semVer6             = result6.getSemver1();
        assert semVer6.toString(false).equals("11.0.8.0.1-ea+meta");

        String              t7                  = "12.0.9.0.5.1-ea+meta";
        SemverParsingResult result7             = SemverParser.fromText(t7);
        Semver              semVer7             = result7.getSemver1();
        assert semVer7.toString(false).equals("12.0.9.0.5.1-ea+meta");

        String              t8                  = "14.0.0-ea.36+meta";
        SemverParsingResult result8             = SemverParser.fromText(t8);
        Semver              semVer8             = result8.getSemver1();

        assert ReleaseStatus.EA == semVer8.getReleaseStatus();
        assert semVer8.getPreBuild().equals("36");
        assert semVer8.toString(false).equals("14-ea+36");

        String              t9                  = "18-ea+10";
        SemverParsingResult result9             = SemverParser.fromText(t9);
        Semver              semVer9             = result9.getSemver1();

        assert ReleaseStatus.EA == semVer9.getReleaseStatus();
        assert semVer9.getPreBuild().equals("10");
        assert semVer9.toString(false).equals("18-ea+10");

        String              t10                 = "1.8.0.302-b8";
        SemverParsingResult result10            = SemverParser.fromText(t10);
        Semver              semVer10            = result10.getSemver1();

        assert ReleaseStatus.GA == semVer10.getReleaseStatus();
        assert semVer10.getPreBuild().equals("8");
        assert semVer10.toString(true).equals("8.0.302+b8");

        String              t11                 = "17.0.1-beta+12.0.202111240007";
        SemverParsingResult resultt11           = SemverParser.fromText(t11);
        Semver              semVer11            = resultt11.getSemver1();

        assert ReleaseStatus.EA == semVer11.getReleaseStatus();
        assert semVer11.toString(true).equals("17.0.1-ea+12.0.202111240007");

        String              t12                 = "1.8.0.302+8";
        SemverParsingResult result12            = SemverParser.fromText(t12);
        Semver              semVer12            = result12.getSemver1();

        assert ReleaseStatus.GA == semVer12.getReleaseStatus();
        assert semVer12.getPreBuild().equals("8");
        assert semVer12.toString(true).equals("8.0.302+8");

        String              t13                 = "17-ea+17";
        SemverParsingResult result13            = SemverParser.fromText(t13);
        Semver              semver13            = result13.getSemver1();

        assert ReleaseStatus.EA == semver13.getReleaseStatus();
        assert semver13.getPreBuild().equals("17");
        assert semver13.toString(true).equals("17-ea+17");

        String              t14                 = "17-loom+6-225";
        SemverParsingResult result14            = SemverParser.fromText(t14);
        Semver              semver14            = result14.getSemver1();

        assert ReleaseStatus.EA == semver14.getReleaseStatus();
        assert semver14.getPreBuild().isEmpty();
        assert semver14.toString(true).equals("17-ea+6-225");

        String              t15                 = "17-ea+2-10";  // 17.0.2-crac+10
        SemverParsingResult result15            = SemverParser.fromText(t15);
        Semver              semver15            = result15.getSemver1();

        assert ReleaseStatus.EA == semver15.getReleaseStatus();
        assert semver15.getPreBuild().isEmpty();
        assert semver15.toString(true).equals("17-ea+2-10");
    }

    @Test
    void semVerToStringTest() {
        Semver semVer = new Semver(new VersionNumber(11, 0, 9, 1, 0, 5), ReleaseStatus.EA,"", "+b1");

        assert "11.0.9.1-ea+b1".equals(semVer.toString());
        assert "11.0.9.1.0.5-ea+b1".equals(semVer.toString(false));

        Semver semVer1 = Semver.fromText("14.0.0-ea.36").getSemver1();

        assert "14-ea+36".equals(semVer1.toString());
    }

    @Test
    void semVerEqualsToOtherSemverTest() {
        Semver semver1 = Semver.fromText(new VersionNumber(17, null, null, null, null, null, 28, ReleaseStatus.EA).toString()).getSemver1();
        Semver semver2 = Semver.fromText("17-ea.28").getSemver1();
        Semver semver3 = Semver.fromText("17-ea.34").getSemver1();
        Semver semver4 = Semver.fromText("17-ea+34").getSemver1();

        assert semver1.equalTo(semver2);
        assert semver1.compareTo(semver2) == 0;

        assert !semver1.equalTo(semver3);
        assert semver1.compareTo(semver3) != 0;

        assert semver1.compareTo(semver3) < 0;
        assert semver3.compareTo(semver1) > 0;

        assert semver3.compareTo(semver4) == 0;
        assert semver3.equalTo(semver4);
    }

    @Test
    void semVerConstructorTest() {
        VersionNumber versionNumber = new VersionNumber(11, 0, 0, 0, 0, 0, 5, ReleaseStatus.EA);
        Semver        semver        = new Semver(versionNumber);
        assert versionNumber.toString(OutputFormat.REDUCED, true, true).equals(semver.toString(true));
    }

    @Test
    void semverMetadataTest() {
        final String   filename = "jbrsdk-11_0_11-windows-x86-b1504.12.tar.gz";
        final String   withoutPrefix = filename.replace("jbrsdk-", "");
        final String   withoutSuffix = withoutPrefix.replace(".tar.gz", "");
        final String[] filenameParts = withoutSuffix.split("-");
        final Semver   semver        = Semver.fromText(filenameParts[0].replaceAll("_", "\\.") +(filenameParts.length == 4 ? "+" + filenameParts[3] : "")).getSemver1();
        final String   pre           = "ea";
        final String   metadata      = "+b1504.12";

        semver.setMetadata(metadata);
        semver.setPre(pre);

        assert semver.toString(true).equals("11.0.11-ea+b1504.12");

        Semver semver1 = Semver.fromText("10.0.0-ea+001.2.3").getSemver1();
        assert semver1.toString(true).equals("10-ea+001.2.3");
    }

    @Test
    public void zeroBuildNumber() {
        VersionNumber versionNumber1 = VersionNumber.fromText("8.0.202+0");
        Semver        semver1        = new Semver(versionNumber1);
        Semver        semver2        = Semver.fromText("8.0.202+0").getSemver1();
        Semver        semver3        = new Semver(new VersionNumber(8,0,202), "", "+0");

        String correctResult = "8.0.202";

        assert versionNumber1.toString(OutputFormat.REDUCED_COMPRESSED, true, true).equals(correctResult);
        assert semver1.toString(true).equals(correctResult);
        assert semver2.toString(true).equals(correctResult);
        assert semver3.toString(true).equals(correctResult);
    }

    @Test
    public void emptyVersionNumberConstructor() {
        VersionNumber versionNumber = new VersionNumber();
        Semver        semver        = new Semver(versionNumber);
        assert semver.toString(true).isEmpty();
    }
}
