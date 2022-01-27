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

package eu.hansolo.jdktools.scopes;

import java.util.List;
import java.util.stream.Collectors;

import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.EMPTY_SQUARE_BRACKETS;
import static eu.hansolo.jdktools.Constants.QUOTES;
import static eu.hansolo.jdktools.Constants.QUOTES_COMMA_QUOTES;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_CLOSE_QUOTES;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_OPEN_QUOTES;


public class ScopeConfig {
    private String       name;
    private List<String> distributions;
    private List<String> basicScopes;
    private List<String> downloadScopes;
    private List<String> usageScopes;
    private List<String> buildScopes;
    private String       match;
    private String       version;
    private List<String> architectures;
    private List<String> archiveTypes;
    private List<String> packageTypes;
    private List<String> operatingSystems;
    private List<String> libcTypes;
    private List<String> releaseStatus;
    private List<String> termsOfSupport;
    private String       bitness;
    private String       javafxBundled;
    private String       directlyDownloadable;
    private String       signatureAvailable;
    private String       latest;


    public String getName() { return null == name ? "" : name; }
    public void setName(final String name) { this.name = name; }

    public List<String> getDistributions() { return null == distributions ? List.of() : distributions; }
    public void setDistributions(final List<String> distributions) { this.distributions = distributions; }

    public List<String> getBasicScopes() { return null == basicScopes ? List.of() : basicScopes; }
    public void setBasicScopes(final List<String> basicScopes) { this.basicScopes = basicScopes; }

    public List<String> getDownloadScopes() { return null == downloadScopes ? List.of() : downloadScopes; }
    public void setDownloadScopes(final List<String> downloadScopes) { this.downloadScopes = downloadScopes; }

    public List<String> getUsageScopes() { return null == usageScopes ? List.of() : usageScopes; }
    public void setUsageScopes(final List<String> usageScopes) { this.usageScopes = usageScopes; }

    public List<String> getBuildScopes() { return null == buildScopes ? List.of() : buildScopes; }
    public void setBuildScopes(final List<String> buildScopes) { this.buildScopes = buildScopes; }

    public String getMatch() { return null == match ? "": match; }
    public void setMatch(final String match) { this.match = match; }

    public String getVersion() { return null == version ? "" : version; }
    public void setVersion(final String version) { this.version = version; }

    public List<String> getArchitectures() { return null == architectures ? List.of() : architectures; }
    public void setArchitectures(final List<String> architectures) { this.architectures = architectures; }

    public List<String> getArchiveTypes() { return null == archiveTypes ? List.of() : archiveTypes; }
    public void setArchiveTypes(final List<String> archiveTypes) { this.archiveTypes = archiveTypes; }

    public List<String> getPackageTypes() { return null == packageTypes ? List.of() : packageTypes; }
    public void setPackageTypes(final List<String> packageTypes) { this.packageTypes = packageTypes; }

    public List<String> getOperatingSystems() { return null == operatingSystems ? List.of() : operatingSystems; }
    public void setOperatingSystems(final List<String> operatingSystems) { this.operatingSystems = operatingSystems; }

    public List<String> getLibcTypes() { return null == libcTypes ? List.of() : libcTypes; }
    public void setLibcTypes(final List<String> libcTypes) { this.libcTypes = libcTypes; }

    public List<String> getReleaseStatus() { return null == releaseStatus ? List.of() : releaseStatus; }
    public void setReleaseStatus(final List<String> releaseStatus) { this.releaseStatus = releaseStatus; }

    public List<String> getTermsOfSupport() { return null == termsOfSupport ? List.of() : termsOfSupport; }
    public void setTermsOfSupport(final List<String> termsOfSupport) { this.termsOfSupport = termsOfSupport; }

    public String getBitness() { return null == bitness ? "" : bitness; }
    public void setBitness(final String bitness) { this.bitness = bitness; }

    public String getJavafxBundled() { return null == javafxBundled ? "" : javafxBundled; }
    public void setJavafxBundled(final String javafxBundled) { this.javafxBundled = javafxBundled; }

    public String getDirectlyDownloadable() { return null == directlyDownloadable ? "" : directlyDownloadable; }
    public void setDirectlyDownloadable(final String directlyDownloadable) { this.directlyDownloadable = directlyDownloadable; }

    public String getSignatureAvailable() { return null == signatureAvailable ? "" : signatureAvailable; }
    public void setSignatureAvailable(final String signatureAvailable) { this.signatureAvailable = signatureAvailable; }

    public String getLatest() { return null == latest ? "" : latest; }
    public void setLatest(final String latest) { this.latest = latest; }

    @Override public String toString() {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append(CURLY_BRACKET_OPEN)
                  .append(QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(getName()).append(QUOTES).append(COMMA)
                  .append(QUOTES).append("distribution").append(QUOTES).append(COLON).append(getDistributions().isEmpty() ? EMPTY_SQUARE_BRACKETS : getDistributions().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES, SQUARE_BRACKET_OPEN_QUOTES, SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("basic_scope").append(QUOTES).append(COLON).append(getBasicScopes().isEmpty() ? EMPTY_SQUARE_BRACKETS : getBasicScopes().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("build_scope").append(QUOTES).append(COLON).append(getBuildScopes().isEmpty() ? EMPTY_SQUARE_BRACKETS : getBuildScopes().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("download_scope").append(QUOTES).append(COLON).append(getDownloadScopes().isEmpty() ? EMPTY_SQUARE_BRACKETS : getDownloadScopes().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("usage_scope").append(QUOTES).append(COLON).append(getUsageScopes().isEmpty() ? EMPTY_SQUARE_BRACKETS : getUsageScopes().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("match").append(QUOTES).append(COLON).append(QUOTES).append(getMatch()).append(QUOTES).append(COMMA)
                  .append(QUOTES).append("version").append(QUOTES).append(COLON).append(QUOTES).append(getVersion()).append(QUOTES).append(COMMA)
                  .append(QUOTES).append("architecture").append(QUOTES).append(COLON).append(getArchitectures().isEmpty() ? EMPTY_SQUARE_BRACKETS : getArchitectures().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("archive_type").append(QUOTES).append(COLON).append(getArchiveTypes().isEmpty() ? EMPTY_SQUARE_BRACKETS : getArchiveTypes().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("package_type").append(QUOTES).append(COLON).append(getPackageTypes().isEmpty() ? EMPTY_SQUARE_BRACKETS : getPackageTypes().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("operating_system").append(QUOTES).append(COLON).append(getOperatingSystems().isEmpty() ? EMPTY_SQUARE_BRACKETS : getOperatingSystems().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("lib_c_type").append(QUOTES).append(COLON).append(getLibcTypes().isEmpty() ? EMPTY_SQUARE_BRACKETS : getLibcTypes().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("release_status").append(QUOTES).append(COLON).append(getReleaseStatus().isEmpty() ? EMPTY_SQUARE_BRACKETS : getReleaseStatus().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("term_of_support").append(QUOTES).append(COLON).append(getTermsOfSupport().isEmpty() ? EMPTY_SQUARE_BRACKETS : getTermsOfSupport().stream().collect(Collectors.joining(QUOTES_COMMA_QUOTES,SQUARE_BRACKET_OPEN_QUOTES,SQUARE_BRACKET_CLOSE_QUOTES))).append(COMMA)
                  .append(QUOTES).append("bitness").append(QUOTES).append(COLON).append(QUOTES).append(getBitness()).append(QUOTES).append(COMMA)
                  .append(QUOTES).append("javafx_bundled").append(QUOTES).append(COLON).append(QUOTES).append(getJavafxBundled()).append(QUOTES).append(COMMA)
                  .append(QUOTES).append("directly_downloadable").append(QUOTES).append(COLON).append(QUOTES).append(getDirectlyDownloadable()).append(QUOTES).append(COMMA)
                  .append(QUOTES).append("signature_available").append(QUOTES).append(COLON).append(QUOTES).append(getSignatureAvailable()).append(QUOTES).append(COMMA)
                  .append(QUOTES).append("latest").append(QUOTES).append(COLON).append(QUOTES).append(getLatest()).append(QUOTES)
                  .append("}");
        return msgBuilder.toString();
    }
}
