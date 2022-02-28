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
            case FULL:
            case REDUCED:
            case REDUCED_ENRICHED:
                msgBuilder.append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                          .append(INDENTED_QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(NEW_LINE)
                          .append(CURLY_BRACKET_CLOSE);
                break;
            default:
                msgBuilder.append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES)
                          .append(CURLY_BRACKET_CLOSE);
                break;
        }
        return msgBuilder.toString();
    }

    @Override public String toString() { return toString(OutputFormat.FULL_COMPRESSED); }

    public static ArchiveType fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch (text) {
            case "apk":
            case ".apk":
            case "APK":
                return APK;
            case "bin":
            case ".bin":
            case "BIN":
                return BIN;
            case "cab":
            case ".cab":
            case "CAB":
                return CAB;
            case "deb":
            case ".deb":
            case "DEB":
                return DEB;
            case "dmg":
            case ".dmg":
            case "DMG":
                return DMG;
            case "exe":
            case ".exe":
            case "EXE":
                return EXE;
            case "msi":
            case ".msi":
            case "MSI":
                return MSI;
            case "pkg":
            case ".pkg":
            case "PKG":
                return PKG;
            case "rpm":
            case ".rpm":
            case "RPM":
                return RPM;
            case "src.tar.gz":
            case ".src.tar.gz":
            case "source.tar.gz":
            case "SRC.TAR.GZ":
            case "src_tar":
            case "SRC_TAR":
                return SRC_TAR;
            case "tar.Z":
            case ".tar.Z":
            case "TAR.Z":
            case "tar.z":
                return TAR_Z;
            case "tar.gz":
            case ".tar.gz":
            case "TAR.GZ":
                return TAR_GZ;
            case "tgz":
            case ".tgz":
            case "TGZ":
                return TGZ;
            case "tar":
            case ".tar":
            case "TAR":
                return TAR;
            case "zip":
            case ".zip":
            case "ZIP":
                return ZIP;
            default:
                return NOT_FOUND;
        }
    }

    public List<String> getFileEndings() { return fileEndings; }

    public static ArchiveType getFromFileName(final String fileName) {
        if (null == fileName || fileName.isEmpty()) { return ArchiveType.NONE; }
        for (ArchiveType archiveType : values()) {
            for (String ending : archiveType.getFileEndings()) {
                if (fileName.toLowerCase().endsWith(ending)) { return archiveType; }
            }
        }
        return ArchiveType.NONE;
    }

    public static List<ArchiveType> getAsList() { return Arrays.asList(values()); }
}
