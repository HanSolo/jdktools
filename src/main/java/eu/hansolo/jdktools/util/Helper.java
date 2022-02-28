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

package eu.hansolo.jdktools.util;

import eu.hansolo.jdktools.Architecture;
import eu.hansolo.jdktools.Constants;
import eu.hansolo.jdktools.OperatingMode;
import eu.hansolo.jdktools.OperatingSystem;
import eu.hansolo.jdktools.TermOfSupport;
import eu.hansolo.jdktools.versioning.VersionNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Helper {

    private static final String[] DETECT_ALPINE_CMDS       = { "/bin/sh", "-c", "cat /etc/os-release | grep 'NAME=' | grep -ic 'Alpine'" };
    private static final String[] UX_DETECT_ARCH_CMDS      = { "/bin/sh", "-c", "uname -m" };
    private static final String[] MAC_DETECT_ROSETTA2_CMDS = { "/bin/sh", "-c", "sysctl -in sysctl.proc_translated" };
    private static final String[] WIN_DETECT_ARCH_CMDS     = { "cmd.exe", "/c", "SET Processor" };
    private static final Pattern  ARCHITECTURE_PATTERN     = Pattern.compile("(PROCESSOR_ARCHITECTURE)=([a-zA-Z0-9_\\-]+)");
    private static final Matcher  ARCHITECTURE_MATCHER     = ARCHITECTURE_PATTERN.matcher("");

    public record OsArcMode(OperatingSystem operatingSystem, Architecture architecture, OperatingMode operatingMode) {}


    private Helper(){}

    public static final String trimPrefix(final String text, final String prefix) {
        return text.replaceFirst(prefix, "");
    }

    public static final boolean isPositiveInteger(final String text) {
        if (null == text || text.isEmpty()) { return false; }
        return Constants.POSITIVE_INTEGER_PATTERN.matcher(text).matches();
    }


    public static final boolean isReleaseTermOfSupport(final int featureVersion, final TermOfSupport termOfSupport) {
        switch(termOfSupport) {
            case LTS: return isLTS(featureVersion);
            case MTS: return isMTS(featureVersion);
            case STS: return isSTS(featureVersion);
            default : return false;
        }
    }
    public static final boolean isSTS(final int featureVersion) {
        if (featureVersion < 9) { return false; }
        switch(featureVersion) {
            case 9 :
            case 10: return true;
            default: return !isLTS(featureVersion);
        }
    }
    public static final boolean isMTS(final int featureVersion) {
        if (featureVersion < 13) { return false; }
        return (!isLTS(featureVersion)) && featureVersion % 2 != 0;
    }
    public static final boolean isLTS(final int featureVersion) {
        if (featureVersion < 1) { throw new IllegalArgumentException("Feature version number cannot be smaller than 1"); }
        if (featureVersion <= 8) { return true; }
        if (featureVersion < 11) { return false; }
        if (featureVersion < 17) { return ((featureVersion - 11.0) / 6.0) % 1 == 0; }
        return ((featureVersion - 17.0) / 4.0) % 1 == 0;
    }

    public static final TermOfSupport getTermOfSupport(final VersionNumber versionNumber, final boolean isZulu) {
        TermOfSupport termOfSupport = getTermOfSupport(versionNumber);
        switch(termOfSupport) {
            case LTS:
            case STS: return termOfSupport;
            case MTS: return isZulu ? termOfSupport : TermOfSupport.STS;
            default : return TermOfSupport.NOT_FOUND;
        }
    }
    public static final TermOfSupport getTermOfSupport(final VersionNumber versionNumber) {
        if (!versionNumber.getFeature().isPresent() || versionNumber.getFeature().isEmpty()) {
            throw new IllegalArgumentException("VersionNumber need to have a feature version");
        }
        return getTermOfSupport(versionNumber.getFeature().getAsInt());
    }
    public static final TermOfSupport getTermOfSupport(final int featureVersion) {
        if (featureVersion < 1) { throw new IllegalArgumentException("Feature version number cannot be smaller than 1"); }
        if (isLTS(featureVersion)) {
            return TermOfSupport.LTS;
        } else if (isMTS(featureVersion)) {
            return TermOfSupport.MTS;
        } else if (isSTS(featureVersion)) {
            return TermOfSupport.STS;
        } else {
            return TermOfSupport.NOT_FOUND;
        }
    }

    public static final OperatingSystem getOperatingSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (os.contains("apple") || os.contains("mac")) {
            return OperatingSystem.MACOS;
        } else if (os.contains("nix") || os.contains("nux")) {
            try {
                final ProcessBuilder processBuilder = new ProcessBuilder(DETECT_ALPINE_CMDS);
                final Process        process        = processBuilder.start();
                final String         result         = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
                return null == result ? OperatingSystem.LINUX : result.equals("1") ? OperatingSystem.ALPINE_LINUX : OperatingSystem.LINUX;
            } catch (IOException e) {
                e.printStackTrace();
                return OperatingSystem.LINUX;
            }
        } else if (os.contains("sunos")) {
            return OperatingSystem.SOLARIS;
        } else {
            return OperatingSystem.NOT_FOUND;
        }
    }

    public static final Architecture getArchitecture() {
        final String arch = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
        if (arch.contains("sparc")) return Architecture.SPARC;
        if (arch.contains("amd64") || arch.contains("86_64")) return Architecture.AMD64;
        if (arch.contains("86")) return Architecture.X86;
        if (arch.contains("s390x")) return Architecture.S390X;
        if (arch.contains("ppc64")) return Architecture.PPC64;
        if (arch.contains("arm") && arch.contains("64")) return Architecture.AARCH64;
        if (arch.contains("arm")) return Architecture.ARM;
        if (arch.contains("aarch64")) return Architecture.AARCH64;
        return Architecture.NOT_FOUND;
    }

    public static final OsArcMode getOperaringSystemArchitectureOperatingMode() {
        final OperatingSystem operatingSystem = getOperatingSystem();
        try {
            final ProcessBuilder processBuilder = OperatingSystem.WINDOWS == operatingSystem ? new ProcessBuilder(WIN_DETECT_ARCH_CMDS) : new ProcessBuilder(UX_DETECT_ARCH_CMDS);
            final Process        process        = processBuilder.start();
            final String         result         = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
            switch(operatingSystem) {
                case WINDOWS:
                    ARCHITECTURE_MATCHER.reset(result);
                    final List<MatchResult> results     = ARCHITECTURE_MATCHER.results().collect(Collectors.toList());
                    final int               noOfResults = results.size();
                    if (noOfResults > 0) {
                        final MatchResult   res = results.get(0);
                        return new OsArcMode(operatingSystem, Architecture.fromText(res.group(2)), OperatingMode.NATIVE);
                    } else {
                        return new OsArcMode(operatingSystem, Architecture.NOT_FOUND, OperatingMode.NOT_FOUND);
                    }
                case MACOS:
                    Architecture architecture = Architecture.fromText(result);
                    final ProcessBuilder processBuilder1 = new ProcessBuilder(MAC_DETECT_ROSETTA2_CMDS);
                    final Process        process1        = processBuilder1.start();
                    final String         result1         = new BufferedReader(new InputStreamReader(process1.getInputStream())).lines().collect(Collectors.joining("\n"));
                    return new OsArcMode(operatingSystem, architecture, result1.equals("1") ? OperatingMode.EMULATED : OperatingMode.NATIVE);
                case LINUX:
                    return new OsArcMode(operatingSystem, Architecture.fromText(result), OperatingMode.NATIVE);
                }

            // If not found yet try via system property
            final String arch = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
            if (arch.contains("sparc"))                           { return new OsArcMode(operatingSystem, Architecture.SPARC, OperatingMode.NATIVE); }
            if (arch.contains("amd64") || arch.contains("86_64")) { return new OsArcMode(operatingSystem, Architecture.AMD64, OperatingMode.NATIVE); }
            if (arch.contains("86"))                              { return new OsArcMode(operatingSystem, Architecture.X86, OperatingMode.NATIVE); }
            if (arch.contains("s390x"))                           { return new OsArcMode(operatingSystem, Architecture.S390X, OperatingMode.NATIVE); }
            if (arch.contains("ppc64"))                           { return new OsArcMode(operatingSystem, Architecture.PPC64, OperatingMode.NATIVE); }
            if (arch.contains("arm") && arch.contains("64"))      { return new OsArcMode(operatingSystem, Architecture.AARCH64, OperatingMode.NATIVE); }
            if (arch.contains("arm"))                             { return new OsArcMode(operatingSystem, Architecture.ARM, OperatingMode.NATIVE); }
            if (arch.contains("aarch64"))                         { return new OsArcMode(operatingSystem, Architecture.AARCH64, OperatingMode.NATIVE); }
            return new OsArcMode(operatingSystem, Architecture.NOT_FOUND, OperatingMode.NATIVE);
        } catch (IOException e) {
            e.printStackTrace();
            return new OsArcMode(operatingSystem, Architecture.NOT_FOUND, OperatingMode.NATIVE);
        }
    }
}
