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


public enum ArchiveType implements Api {
    APK("apk", "apk", ".apk"),
    BIN("bin", "bin", ".bin"),
    CAB("cab", "cab",".cab"),
    DEB("deb", "deb",".deb"),
    DMG("dmg", "dmg",".dmg"),
    MSI("msi", "msi",".msi"),
    PKG("pkg", "pkg",".pkg"),
    RPM("rpm", "rpm",".rpm"),
    SRC_TAR("src.tar.gz", "src_tar",".src.tar.gz", ".source.tar.gz", ".source.tar.gz"),
    TAR("tar", "tar", ".tar"),
    TAR_GZ("tar.gz", "tar.gz", ".tar.gz"),
    TAR_XZ("tar.xz", "tar.xz", ".tar.xz"),
    TGZ("tgz", "tgz", ".tgz"),
    TAR_Z("tar.Z", "tar.z", ".tar.Z"),
    ZIP("zip", "zip", ".zip"),
    EXE("exe", "exe", ".exe"),
    NONE("-", "", "-"),
    NOT_FOUND("", "", "");

    private final String       uiString;
    private final String       apiString;
    private final List<String> fileEndings;


    ArchiveType(final String uiString, final String apiString, final String... fileEndings) {
        this.uiString    = uiString;
        this.apiString   = apiString;
        this.fileEndings = List.of(fileEndings);
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }


    @Override public ArchiveType getDefault() { return ArchiveType.NONE; }

    @Override public ArchiveType getNotFound() { return ArchiveType.NOT_FOUND; }

    @Override public ArchiveType[] getAll() { return values(); }

    @Override public String toString(final OutputFormat outputFormat) {
        StringBuilder msgBuilder = new StringBuilder();
        switch(outputFormat) {
            case FULL, REDUCED, REDUCED_ENRICHED ->
                msgBuilder.append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                          .append(INDENTED_QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(NEW_LINE)
                          .append(CURLY_BRACKET_CLOSE);
            default ->
                msgBuilder.append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES)
                          .append(CURLY_BRACKET_CLOSE);
        }
        return msgBuilder.toString();
    }

    @Override public String toString() { return toString(OutputFormat.FULL_COMPRESSED); }

    /**
     * Returns ArchiveType parsed from a given text
     * @param text Name of the archive type to parse usually the api_string of an archive type e.g. 'tar.gz'
     * @return ArchiveType parsed from a given text
     */
    public static ArchiveType fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        return switch (text) {
            case "apk", ".apk", "APK" -> APK;
            case "bin", ".bin", "BIN" -> BIN;
            case "cab", ".cab", "CAB" -> CAB;
            case "deb", ".deb", "DEB" -> DEB;
            case "dmg", ".dmg", "DMG" -> DMG;
            case "exe", ".exe", "EXE" -> EXE;
            case "msi", ".msi", "MSI" -> MSI;
            case "pkg", ".pkg", "PKG" -> PKG;
            case "rpm", ".rpm", "RPM" -> RPM;
            case "src.tar.gz", ".src.tar.gz", "source.tar.gz", "SRC.TAR.GZ", "src_tar", "SRC_TAR" -> SRC_TAR;
            case "tar.Z", ".tar.Z", "TAR.Z", "tar.z" -> TAR_Z;
            case "tar.gz", ".tar.gz", "TAR.GZ" -> TAR_GZ;
            case "tar.xz", ".tar.xz", "TAR.XZ" -> TAR_XZ;
            case "tgz", ".tgz", "TGZ" -> TGZ;
            case "tar", ".tar", "TAR" -> TAR;
            case "zip", ".zip", "ZIP" -> ZIP;
            default -> NOT_FOUND;
        };
    }

    public List<String> getFileEndings() { return fileEndings; }

    /**
     * Returns ArchiveType parsed from a given filename
     * @param filename Filename from which the archive type should be parsed e.g. 'zulu-18.jdk.tar.gz'
     * @return ArchiveType parsed from a given filename
     */
    public static ArchiveType getFromFileName(final String filename) {
        if (null == filename || filename.isEmpty()) { return ArchiveType.NONE; }
        for (ArchiveType archiveType : values()) {
            for (String ending : archiveType.getFileEndings()) {
                if (filename.toLowerCase().endsWith(ending)) { return archiveType; }
            }
        }
        return ArchiveType.NONE;
    }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<ArchiveType> getAsList() { return Arrays.asList(values()); }
}
