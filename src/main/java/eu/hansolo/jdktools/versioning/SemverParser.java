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

import eu.hansolo.jdktools.util.Comparison;
import eu.hansolo.jdktools.util.Helper;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class SemverParser {
    private static final Pattern SEM_VER_PATTERN = Pattern.compile("^(<|<=|>|>=|=)?v?([0-9]+)(\\.[0-9]+)?(\\.[0-9]+)?(\\.[0-9]+)?(\\.[0-9]+)?(\\.[0-9]+)?(-([0-9A-Za-z\\-]+(\\.[0-9A-Za-z\\-]+)*))?(\\+([0-9A-Za-z\\-]+(\\.[0-9A-Za-z\\-]+)*))?((<|<=|>|>=|=)?v?([0-9]+)(\\.[0-9]+)?(\\.[0-9]+)?(\\.[0-9]+)?(\\.[0-9]+)?(\\.[0-9]+)?(-([0-9A-Za-z\\-]+(\\.[0-9A-Za-z\\-]+)*))?(\\+([0-9A-Za-z\\-]+(\\.[0-9A-Za-z\\-]+)*))?)?$");
    private static final Pattern OPT_PATTERN     = Pattern.compile("(([0-9]+(\\.[0-9]+)?))?(\\-([a-zA-Z0-9\\-]*))?");

    private SemverParser() {}


    public static SemverParsingResult fromText(final String text) {
        SemverParsingResult parsingResult = new SemverParsingResult();

        // ******************** Parsing 1st Semver ****************************
        // Remove leading "1." to get correct version number e.g. 1.8u262 -> 8u262
        String versionText1 = text.startsWith("1.") ? text.replace("1.", "") : text;
        if (versionText1.contains("_")) {
            versionText1 = versionText1.replace("_", ".");
        }
        if (versionText1.matches("[0-9]+u[0-9]+.*")) {
            versionText1 = versionText1.replace("u", ".0.");
        }

        final Matcher           semverMatcher = SEM_VER_PATTERN.matcher(versionText1);
        final List<MatchResult> results       = semverMatcher.results().collect(Collectors.toList());

        if (results.isEmpty()) {
            parsingResult.setError1(new Error("Invalid semver: " + versionText1));
            return parsingResult;
        }

        MatchResult result = results.get(0);

        String metadata1 = null != result.group(12) ? result.group(12) : "";
        String pre1      = null != result.group(9)  ? result.group(9)  : "";

        final Matcher           optMatcher = OPT_PATTERN.matcher(metadata1);
        final List<MatchResult> optResults = optMatcher.results().collect(Collectors.toList());

        String build1 = "";
        String opt1   = "";
        if (!optResults.isEmpty()) {
            MatchResult optResult = optResults.get(0);
            build1 = null != optResult.group(2) ? optResult.group(2) : "";
            opt1   = null != optResult.group(5) ? optResult.group(5) : "";
        }

        if (pre1.equals("ea.0")) { pre1 = "ea"; }
        if (pre1.startsWith("b") && metadata1.isEmpty()) {
            metadata1 = pre1;
            pre1 = "";
        }

        VersionNumber versionNumber1 = new VersionNumber();

        Comparison comparison1;
        if (null == result.group(1)) {
            comparison1 = Comparison.EQUAL;
        } else {
            comparison1 = Comparison.fromText(result.group(1));
        }

        // 1st number
        try {
            if (null == result.group(2)) {
                parsingResult.setError1(new Error("Feature version cannot be null"));
                return parsingResult;
            }
            versionNumber1.setFeature(Integer.parseInt(result.group(2)));
        } catch (NumberFormatException e) {
            parsingResult.setError1(new Error("Error when parsing feature version " + result.group(2) + ": " + e));
            return parsingResult;
        }

        // 2nd number
        try {
            if (null == result.group(3)) {
                versionNumber1.setInterim(0);
            } else {
                versionNumber1.setInterim(Integer.parseInt(Helper.trimPrefix(result.group(3), "\\.")));
            }
        } catch (NumberFormatException e) {
            parsingResult.setError1(new Error("Error when parsing interim version " + result.group(3) + ": " + e));
            return parsingResult;
        }

        // 3rd number
        try {
            if (null == result.group(4)) {
                versionNumber1.setUpdate(0);
            } else {
                versionNumber1.setUpdate(Integer.parseInt(Helper.trimPrefix(result.group(4), "\\.")));
            }
        } catch (NumberFormatException e) {
            parsingResult.setError1(new Error("Error when parsing update version " + result.group(4) + ": " + e));
            return parsingResult;
        }

        // 4th number
        try {
            if (null == result.group(5)) {
                versionNumber1.setPatch(0);
            } else {
                versionNumber1.setPatch(Integer.parseInt(Helper.trimPrefix(result.group(5), "\\.")));
            }
        } catch (NumberFormatException e) {
            parsingResult.setError1(new Error("Error when parsing patch version " + result.group(5) + ": " + e));
            return parsingResult;
        }

        // 5th number
        try {
            if (null == result.group(6)) {
                versionNumber1.setFifth(0);
            } else {
                versionNumber1.setFifth(Integer.parseInt(Helper.trimPrefix(result.group(6), "\\.")));
            }
        } catch (NumberFormatException e) {
            parsingResult.setError1(new Error("Error when parsing fifth number " + result.group(6) + ": " + e));
            return parsingResult;
        }

        // 6th number
        try {
            if (null == result.group(7)) {
                versionNumber1.setSixth(0);
            } else {
                versionNumber1.setSixth(Integer.parseInt(Helper.trimPrefix(result.group(7), "\\.")));
            }
        } catch (NumberFormatException e) {
            parsingResult.setError1(new Error("Error when parsing sixth number " + result.group(7) + ": " + e));
            return parsingResult;
        }

        // Validate prerelease
        Error err1;
        if (!pre1.isEmpty()) {
            String[] eparts = pre1.split("\\.");
            /*
            if (eparts.length > 0 && (eparts[0].equalsIgnoreCase("-ea") ||
                                      eparts[0].equalsIgnoreCase("ea")  ||
                                      eparts[0].equalsIgnoreCase("beta"))) {
                pre1 = "ea";
            }
            */
            if (eparts.length > 0 && eparts[0].length() > 0 && eparts[0].matches("[a-zA-Z]+")) {
                pre1 = "ea";
                opt1 = eparts[0].equals("ea") ? opt1 : eparts[0];
            }
            if (eparts.length > 1 && Helper.isPositiveInteger(eparts[1])) {
                metadata1 = eparts[1];
            }
            err1 = validatePrerelease(pre1);
            if (null != err1) {
                parsingResult.setError1(err1);
                return parsingResult;
            }
        }

        // Validate metadata
        if (null != metadata1 && !metadata1.isEmpty()) {
            err1 = validateMetadata(metadata1);
            if (null != err1) {
                parsingResult.setError1(err1);
                return parsingResult;
            }
        }
        Semver semVer1 = new Semver(versionNumber1, pre1, metadata1);
        semVer1.setBuild(build1);
        semVer1.setOpt(opt1);
        semVer1.setComparison(comparison1);
        parsingResult.setSemver1(semVer1);

        Predicate<Semver> filter = null;

        // ******************** Parsing 2nd Semver ****************************
        if (result.groupCount() == 27 && null != result.group(14)) {
            String metadata2 = null != result.group(26) ? result.group(26) : "";
            String pre2      = null != result.group(23) ? result.group(23) : "";

            optMatcher.reset(metadata2);
            optResults.clear();
            optResults.addAll(optMatcher.results().collect(Collectors.toList()));

            String build2 = "";
            String opt2   = "";
            if (!optResults.isEmpty()) {
                MatchResult optResult = optResults.get(0);
                build2 = null != optResult.group(2) ? optResult.group(2) : "";
                opt2   = null != optResult.group(5) ? optResult.group(5) : "";
            }

            if (pre2.equals("ea.0")) { pre2 = "ea"; }
            if (pre2.startsWith("b") && metadata2.isEmpty()) {
                metadata2 = pre2;
                pre2 = "";
            }

            VersionNumber versionNumber2 = new VersionNumber();

            Comparison comparison2;
            if (null == result.group(15)) {
                comparison2 = Comparison.EQUAL;
            } else {
                comparison2 = Comparison.fromText(result.group(15));
            }

            // 1st number
            boolean oldFormat;
            try {
                if (null == result.group(16)) {
                    parsingResult.setError2(new Error("Feature version cannot be null"));
                    return parsingResult;
                }
                oldFormat = Integer.parseInt(result.group(16)) == 1;
                versionNumber2.setFeature(Integer.parseInt(result.group(16)));
            } catch (NumberFormatException e) {
                parsingResult.setError2(new Error("Error when parsing feature version " + result.group(16) + ": " + e));
                return parsingResult;
            }

            // 2nd number
            try {
                if (null == result.group(17)) {
                    versionNumber2.setInterim(0);
                } else {
                    versionNumber2.setInterim(Integer.parseInt(Helper.trimPrefix(result.group(17), "\\.")));
                }
            } catch (NumberFormatException e) {
                parsingResult.setError2(new Error("Error when parsing interim version " + result.group(17) + ": " + e));
                return parsingResult;
            }

            // 3rd number
            try {
                if (null == result.group(18)) {
                    versionNumber2.setUpdate(0);
                } else {
                    versionNumber2.setUpdate(Integer.parseInt(Helper.trimPrefix(result.group(18), "\\.")));
                }
            } catch (NumberFormatException e) {
                parsingResult.setError2(new Error("Error when parsing update version " + result.group(18) + ": " + e));
                return parsingResult;
            }

            // 4th number
            try {
                if (null == result.group(19)) {
                    versionNumber2.setPatch(0);
                } else {
                    versionNumber2.setPatch(Integer.parseInt(Helper.trimPrefix(result.group(19), "\\.")));
                }
            } catch (NumberFormatException e) {
                parsingResult.setError2(new Error("Error when parsing patch version " + result.group(19) + ": " + e));
                return parsingResult;
            }

            // 5th number
            try {
                if (null == result.group(20)) {
                    versionNumber2.setFifth(0);
                } else {
                    versionNumber2.setFifth(Integer.parseInt(Helper.trimPrefix(result.group(20), "\\.")));
                }
            } catch (NumberFormatException e) {
                parsingResult.setError2(new Error("Error when parsing fifth number " + result.group(20) + ": " + e));
                return parsingResult;
            }

            // 6th number
            try {
                if (null == result.group(21)) {
                    versionNumber2.setSixth(0);
                } else {
                    versionNumber2.setSixth(Integer.parseInt(Helper.trimPrefix(result.group(21), "\\.")));
                }
            } catch (NumberFormatException e) {
                parsingResult.setError2(new Error("Error when parsing sixth number " + result.group(21) + ": " + e));
                return parsingResult;
            }

            // Remove leading "1." to get correct version number e.g. 1.8u262 -> 8u262
            if (oldFormat) {
                versionNumber2.setFeature(versionNumber2.getInterim().getAsInt());
                versionNumber2.setInterim(versionNumber2.getUpdate().getAsInt());
                versionNumber2.setUpdate(versionNumber2.getPatch().getAsInt());
                versionNumber2.setPatch(0);
            }

            // Validate prerelease
            Error err2;
            if (!pre2.isEmpty()) {
                String[] eparts = pre2.split("\\.");
                /*
                if (eparts.length > 0 && (eparts[0].equalsIgnoreCase("-ea") ||
                                          eparts[0].equalsIgnoreCase("ea")  ||
                                          eparts[0].equalsIgnoreCase("beta"))) {
                    pre2 = "ea";
                }
                */
                if (eparts.length > 0 && (eparts[0].matches("[a-zA-Z]+") && eparts[0].length() > 0)) {
                    pre2 = "ea";
                    opt2 = eparts[0].equals("ea") ? opt2 : eparts[0];
                }
                if (eparts.length > 1 && Helper.isPositiveInteger(eparts[1])) {
                    metadata2 = eparts[1];
                }
                err2 = validatePrerelease(pre2);
                if (null != err2) {
                    parsingResult.setError2(err2);
                    return parsingResult;
                }
            }

            // Validate metadata
            if (null != metadata2 && !metadata2.isEmpty()) {
                err2 = validateMetadata(metadata2);
                if (null != err2) {
                    parsingResult.setError2(err2);
                    return parsingResult;
                }
            }
            Semver semVer2 = new Semver(versionNumber2, pre2, metadata2);
            semVer2.setBuild(build2);
            semVer2.setOpt(opt2);
            semVer2.setComparison(comparison2);

            // Define filter
            switch(comparison1) {
                case LESS_THAN          -> filter = semVer -> semVer.lessThan(semVer1);
                case LESS_THAN_OR_EQUAL -> filter = semVer -> (semVer.lessThan(semVer1) || semVer.equalTo(semVer1));
                case GREATER_THAN -> {
                    switch (comparison2) {
                        case LESS_THAN -> filter = semVer -> semVer.greaterThan(semVer1) && semVer.lessThan(semVer2);
                        case LESS_THAN_OR_EQUAL -> filter = semVer -> semVer.greaterThan(semVer1) && (semVer.lessThan(semVer2) || semVer.equalTo(semVer2));
                        default -> filter = semVer -> semVer.greaterThan(semVer1);
                    }
                }
                case GREATER_THAN_OR_EQUAL -> {
                    switch (comparison2) {
                        case LESS_THAN -> filter = semVer -> (semVer.equalTo(semVer1) || semVer.greaterThan(semVer1)) && semVer.lessThan(semVer2);
                        case LESS_THAN_OR_EQUAL -> filter = semVer -> (semVer.equalTo(semVer1) || semVer.greaterThan(semVer1)) && (semVer.lessThan(semVer2) || semVer.equalTo(semVer2));
                        default -> filter = semVer -> (semVer.equalTo(semVer1) || semVer.greaterThan(semVer1));
                    }
                }
            }
            parsingResult.setFilter(filter);

            parsingResult.setSemver2(semVer2);
            return parsingResult;
        }

        // Define filter
        switch(comparison1) {
            case LESS_THAN            -> filter = semVer -> semVer.lessThan(semVer1);
            case LESS_THAN_OR_EQUAL   -> filter = semVer -> (semVer.lessThan(semVer1) || semVer.equalTo(semVer1));
            case GREATER_THAN         -> filter = semVer -> semVer.greaterThan(semVer1);
            case GREATER_THAN_OR_EQUAL-> filter = semVer -> (semVer.equalTo(semVer1) || semVer.greaterThan(semVer1));
        }
        parsingResult.setFilter(filter);

        return parsingResult;
    }

    private static Error validatePrerelease(final String preRelease) {
        String[] eparts = preRelease.split("\\.");
        for (String p : eparts) {
            if (p.matches("[0-9]+")) {
                if (p.length() > 0 && p.startsWith("0")) {
                    return new Error("Segment starts with 0: " + p);
                }
            } else if (!p.matches("[0-9A-Za-z-]+")) {
                return new Error("Invalid preRelease: " + preRelease);
            }
        }
        return null;
    }

    private static Error validateMetadata(final String metadata) {
        String[] eparts = metadata.split("\\.");
        for (String p : eparts) {
            if (!p.matches("[0-9A-Za-z-]+")) {
                return new Error("Invalid metadata: " + metadata);
            }
        }
        return null;
    }
}
