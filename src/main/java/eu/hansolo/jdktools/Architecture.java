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


public enum Architecture implements Api {
    AARCH64("AARCH64", "aarch64", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.ARM64); }
    },
    AARCH32("AARCH32", "aarch32", Bitness.BIT_32, false) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.ARM, Architecture.ARM32); }
    },
    ARM("ARM", "arm", Bitness.BIT_32, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.ARM32, Architecture.AARCH32); }
    },
    ARM32("ARM32", "arm32", Bitness.BIT_32, false) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.ARM, Architecture.AARCH32); }
    },
    ARMHF("ARMHF", "armhf", Bitness.BIT_32, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    ARMEL("ARMEL", "armel", Bitness.BIT_32, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    ARM64("ARM64", "arm64", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.AARCH64); }
    },
    MIPS("MIPS", "mips", Bitness.BIT_32, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    MIPSEL("MIPS EL", "mipsel", Bitness.BIT_32, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    PPC("Power PC", "ppc", Bitness.BIT_32, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    PPC64("PPC64", "ppc64", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    PPC64LE("PPC64LE", "ppc64le", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    RISCV64("RISCv64", "riscv64", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    S390X("S390X", "s390x", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    SPARC("Sparc", "sparc", Bitness.BIT_32, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    SPARCV9("Sparc V9", "sparcv9", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    X64("X64", "x64", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.AMD64, Architecture.X86_64); }
    },
    X32("X32", "x32", Bitness.BIT_32, false) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.X86, Architecture.I386, Architecture.I586, Architecture.I686); }
    },
    I386("I386", "i386", Bitness.BIT_32, false) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.X86, Architecture.X32, Architecture.I586, Architecture.I686); }
    },
    I586("I586", "i386", Bitness.BIT_32, false) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.X86, Architecture.X32, Architecture.I386, Architecture.I686); }
    },
    I686("I686", "i386", Bitness.BIT_32, false) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.X86, Architecture.X32, Architecture.I386, Architecture.I686); }
    },
    X86("X86", "x86", Bitness.BIT_32, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.X32, Architecture.I386, Architecture.I586, Architecture.I686); }
    },
    X86_64("X86_64", "x86_64", Bitness.BIT_64, false) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.X64, Architecture.AMD64); }
    },
    AMD64("AMD64", "amd64", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(Architecture.X64, Architecture.X86_64); }
    },
    IA64("IA-64", "ia64", Bitness.BIT_64, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    NONE("-", "", Bitness.NONE, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    },
    NOT_FOUND("", "", Bitness.NOT_FOUND, true) {
        @Override public List<Architecture> getSynonyms() { return List.of(); }
    };

    private final String   uiString;
    private final String   apiString;
    private final Bitness  bitness;
    private final boolean standard;


    Architecture(final String uiString, final String apiString, final Bitness bitness, final boolean standard) {
        this.uiString  = uiString;
        this.apiString = apiString;
        this.bitness   = bitness;
        this.standard  = standard;
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }

    @Override public Architecture getDefault() { return Architecture.NONE; }

    @Override public Architecture getNotFound() { return Architecture.NOT_FOUND; }

    @Override public Architecture[] getAll() { return Arrays.stream(values()).filter(Architecture::isStandard).toArray(Architecture[]::new); }

    @Override public String toString(final OutputFormat outputFormat) {
        StringBuilder msgBuilder = new StringBuilder();
        switch(outputFormat) {
            case FULL, REDUCED, REDUCED_ENRICHED ->
                msgBuilder.append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                          .append(INDENTED_QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("bitness").append(QUOTES).append(COLON).append(QUOTES).append(bitness.getApiString()).append(QUOTES).append(NEW_LINE)
                          .append(CURLY_BRACKET_CLOSE);
            default ->
                msgBuilder.append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("bitness").append(QUOTES).append(COLON).append(QUOTES).append(bitness.getApiString()).append(QUOTES)
                          .append(CURLY_BRACKET_CLOSE);
        }
        return msgBuilder.toString();
    }

    @Override public String toString() { return toString(OutputFormat.FULL_COMPRESSED); }

    public static Architecture fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        return switch (text) {
            case "aarch64", "AARCH64" -> AARCH64;
            case "amd64", "AMD64", "_amd64" -> AMD64;
            case "aarch32", "AARCH32", "arm32", "ARM32", "armv6", "ARMV6", "armv7l", "ARMV7L", "armv7", "ARMV7", "arm", "ARM" -> ARM;
            case "armel", "ARMEL" -> ARMEL;
            case "armhf", "ARMHF" -> ARMHF;
            case "arm64", "ARM64", "armv8", "ARMV8" -> ARM64;
            case "mips", "MIPS" -> MIPS;
            case "mipsel", "MIPSEL" -> MIPSEL;
            case "ppc", "PPC" -> PPC;
            case "ppc64el", "PPC64EL", "ppc64le", "PPC64LE" -> PPC64LE;
            case "ppc64", "PPC64" -> PPC64;
            case "riscv64", "RISCV64" -> RISCV64;
            case "s390", "s390x", "S390X" -> S390X;
            case "sparc", "SPARC" -> SPARC;
            case "sparcv9", "SPARCV9" -> SPARCV9;
            case "x64", "X64", "x86-64", "X86-64", "x86_64", "X86_64", "x86lx64", "X86LX64" -> X64;
            case "x32", "x86", "X86", "286", "386", "486", "586", "686", "i386", "i486", "i586", "i686", "x86-32", "x86lx32", "X86LX32" -> X86;
            case "ia64", "IA64", "ia-64", "IA-64" -> IA64;
            default -> NOT_FOUND;
        };
    }

    public Bitness getBitness() { return bitness; }

    public boolean isStandard() { return standard; }

    public static List<Architecture> getAsList() { return Arrays.asList(values()); }

    public abstract List<Architecture> getSynonyms();
}
