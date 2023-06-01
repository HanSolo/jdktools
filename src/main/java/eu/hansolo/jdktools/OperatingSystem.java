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

package eu.hansolo.jdktools;

import eu.hansolo.jdktools.util.OutputFormat;

import java.util.Arrays;
import java.util.List;

import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.COMMA_NEW_LINE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.INDENTED_QUOTES;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;


public enum OperatingSystem implements Api {
    ALPINE_LINUX("Alpine Linux", "linux", LibCType.MUSL) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(OperatingSystem.LINUX, OperatingSystem.LINUX_MUSL); }
    },
    LINUX("Linux", "linux", LibCType.GLIBC) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    },
    LINUX_MUSL("Linux Musl", "linux", LibCType.MUSL) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(OperatingSystem.LINUX, OperatingSystem.ALPINE_LINUX); }
    },
    FREE_BSD("FreeBSD", "free_bsd", LibCType.LIBC) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    },
    MACOS("Mac OS", "macos", LibCType.LIBC) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    },
    WINDOWS("Windows", "windows", LibCType.C_STD_LIB) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    },
    SOLARIS("Solaris", "solaris", LibCType.LIBC) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    },
    QNX("QNX", "qnx", LibCType.LIBC) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    },
    AIX("AIX", "aix", LibCType.LIBC) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    },
    NONE("-", "", LibCType.NONE) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    },
    NOT_FOUND("", "", LibCType.NOT_FOUND) {
        @Override public List<OperatingSystem> getSynonyms() { return List.of(); }
    };

    private final String   uiString;
    private final String   apiString;
    private final LibCType libCType;


    OperatingSystem(final String uiString, final String apiString, final LibCType libCType) {
        this.uiString  = uiString;
        this.apiString = apiString;
        this.libCType  = libCType;
    }


    @Override public String getUiString() { return uiString; }

    public String getApiString() { return apiString; }

    @Override public OperatingSystem getDefault() { return OperatingSystem.NONE; }

    @Override public OperatingSystem getNotFound() { return OperatingSystem.NOT_FOUND; }

    @Override public OperatingSystem[] getAll() { return values(); }

    @Override public String toString(final OutputFormat outputFormat) {
        StringBuilder msgBuilder = new StringBuilder();
        switch(outputFormat) {
            case FULL, REDUCED, REDUCED_ENRICHED ->
                msgBuilder.append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                          .append(INDENTED_QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("lib_c_type").append(QUOTES).append(COLON).append(QUOTES).append(libCType.getApiString()).append(QUOTES).append(NEW_LINE)
                          .append(CURLY_BRACKET_CLOSE);
            default ->
                msgBuilder.append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("lib_c_type").append(QUOTES).append(COLON).append(QUOTES).append(libCType.getApiString()).append(QUOTES)
                          .append(CURLY_BRACKET_CLOSE);
        }
        return msgBuilder.toString();
    }

    @Override public String toString() { return toString(OutputFormat.FULL_COMPRESSED); }


    /**
     * Returns OperatingSystem parsed from a given text
     * @param text Name of the operating system to parse usually the api_string of a operating system e.g. 'windows'
     * @return OperatingSystem parsed from a given text
     */
    public static OperatingSystem fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        return switch (text) {
            case "-linux", "linux", "Linux", "LINUX", "unix", "UNIX", "Unix", "-unix" -> LINUX;
            case "-linux-musl", "-linux_musl", "Linux-Musl", "linux-musl", "Linux_Musl", "LINUX_MUSL", "linux_musl", "alpine", "ALPINE", "Alpine", "alpine-linux", "ALPINE-LINUX", "alpine_linux", "Alpine_Linux", "ALPINE_LINUX", "Alpine Linux", "alpine linux", "ALPINE LINUX" -> ALPINE_LINUX;
            case "free_bsd", "FREE_BSD", "FREE BSD", "free bsd", "freebsd", "FreeBSD", "freeBSD" -> FREE_BSD;
            case "-solaris", "solaris", "SOLARIS", "Solaris" -> SOLARIS;
            case "-qnx", "qnx", "QNX" -> QNX;
            case "-aix", "aix", "AIX" -> AIX;
            case "darwin", "-darwin", "-macosx", "-MACOSX", "MacOS", "Mac OS", "mac_os", "Mac_OS", "mac-os", "Mac-OS", "mac", "MAC", "macos", "MACOS", "osx", "OSX", "macosx", "MACOSX", "Mac OSX", "mac osx" -> MACOS;
            case "-win", "windows", "Windows", "WINDOWS", "win", "Win", "WIN" -> WINDOWS;
            default -> NOT_FOUND;
        };
    }

    public LibCType getLibCType() { return libCType; }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<OperatingSystem> getAsList() { return Arrays.asList(values()); }

    public abstract List<OperatingSystem> getSynonyms();
}
