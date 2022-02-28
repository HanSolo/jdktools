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
import eu.hansolo.jdktools.TermOfSupport;
import eu.hansolo.jdktools.util.Helper;
import eu.hansolo.jdktools.util.OutputFormat;

import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.COMMA_NEW_LINE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.INDENTED_QUOTES;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;


public class SimpleMajorVersion {
    public  static final String        FIELD_MAJOR_VERSION     = "major_version";
    public  static final String        FIELD_TERM_OF_SUPPORT   = "term_of_support";
    public  static final String        FIELD_MAINTAINED        = "maintained";
    public  static final String        FIELD_EARLY_ACCESS_ONLY = "early_access_only";
    public  static final String        FIELD_RELEASE_STATUS    = "release_status";
    public  static final String        FIELD_SCOPE             = "scope";
    public  static final String        FIELD_VERSIONS          = "versions";
    private        final int           featureVersion;
    private        final TermOfSupport termOfSupport;
    private              ReleaseStatus releaseStatus;


    public SimpleMajorVersion(final int majorVersion) {
        this(majorVersion, Helper.getTermOfSupport(majorVersion), ReleaseStatus.GA);
    }
    public SimpleMajorVersion(final int featureVersion, final TermOfSupport termOfSupport, final ReleaseStatus releaseStatus) {
        if (featureVersion <= 0) { throw new IllegalArgumentException("Major version cannot be <= 0"); }
        this.featureVersion = featureVersion;
        this.termOfSupport  = termOfSupport;
        this.releaseStatus  = releaseStatus;
    }


    public int getAsInt() { return featureVersion; }

    // Term of support
    public TermOfSupport getTermOfSupport() { return termOfSupport; }

    // VersionNumber
    public VersionNumber getVersionNumber() { return new VersionNumber(featureVersion); }

    // Release Status
    public ReleaseStatus getReleaseStatus() { return releaseStatus; }
    public void setReleaseStatus(final ReleaseStatus releaseStatus) { this.releaseStatus = releaseStatus; }

    public String toString(final OutputFormat outputFormat) {
        if (OutputFormat.FULL_COMPRESSED == outputFormat) {
                return new StringBuilder().append(CURLY_BRACKET_OPEN)
                                          .append(QUOTES).append(FIELD_MAJOR_VERSION).append(QUOTES).append(COLON).append(featureVersion).append(COMMA)
                                          .append(QUOTES).append(FIELD_TERM_OF_SUPPORT).append(QUOTES).append(COLON).append(QUOTES).append(termOfSupport.getApiString()).append(QUOTES).append(COMMA)
                                          .append(QUOTES).append(FIELD_RELEASE_STATUS).append(QUOTES).append(COLON).append(releaseStatus.getApiString())
                                          .append(CURLY_BRACKET_CLOSE)
                                          .toString();
        } else {
                return new StringBuilder().append(CURLY_BRACKET_OPEN)
                                          .append(NEW_LINE)
                                          .append(INDENTED_QUOTES).append(FIELD_MAJOR_VERSION).append(QUOTES).append(COLON).append(featureVersion).append(COMMA_NEW_LINE)
                                          .append(INDENTED_QUOTES).append(FIELD_TERM_OF_SUPPORT).append(QUOTES).append(COLON).append(QUOTES).append(termOfSupport.getApiString()).append(QUOTES).append(COMMA_NEW_LINE)
                                          .append(INDENTED_QUOTES).append(FIELD_RELEASE_STATUS).append(QUOTES).append(COLON).append(releaseStatus.getApiString()).append(NEW_LINE)
                                          .append(CURLY_BRACKET_CLOSE)
                                          .toString();
        }
    }

    @Override public String toString() {
        return toString(OutputFormat.FULL_COMPRESSED);
    }
}
