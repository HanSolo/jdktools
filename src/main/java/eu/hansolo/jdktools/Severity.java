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


public enum Severity implements Api {
    LOW(SeverityName.LOW.name(), SeverityName.LOW.name().toLowerCase(), 0.0, 3.9, 0.1, 3.9, 0.1, 3.9, 2),
    MEDIUM(SeverityName.MEDIUM.name(), SeverityName.MEDIUM.name().toLowerCase(), 4.0, 6.9, 4.0, 6.9, 4.0, 6.9,3),
    HIGH(SeverityName.HIGH.name(), SeverityName.HIGH.name().toLowerCase(), 7.0, 10.0, 7.0, 8.9, 7.0, 8.9,4),
    CRITICAL(SeverityName.CRITICAL.name(), SeverityName.CRITICAL.name().toLowerCase(), 10.0, 10.0, 9.0, 10.0, 9.0, 10.0,5),
    NONE("-", "", 0, 0, 0, 0, 0, 0,1),
    NOT_FOUND("", "", 0, 0, 0, 0, 0, 0,0);

    private final String  uiString;
    private final String  apiString;
    private final double  minScoreV2;
    private final double  maxScoreV2;
    private final double  minScoreV3;
    private final double  maxScoreV3;
    private final double  minScoreV4;
    private final double  maxScoreV4;
    private final Integer order;


    Severity(final String uiString, final String apiString, final double minScoreV2, final double maxScoreV2, final double minScoreV3, final double maxScoreV3, final double minScoreV4, final double maxScoreV4, final Integer order) {
        this.uiString   = uiString;
        this.apiString  = apiString;
        this.minScoreV2 = minScoreV2;
        this.maxScoreV2 = maxScoreV2;
        this.minScoreV3 = minScoreV3;
        this.maxScoreV3 = maxScoreV3;
        this.minScoreV4 = minScoreV4;
        this.maxScoreV4 = maxScoreV4;
        this.order      = order;
    }

    public double getMinScoreV2() { return minScoreV2; }
    public double getMaxScoreV2() { return maxScoreV2; }

    public double getMinScoreV3() { return minScoreV3; }
    public double getMaxScoreV3() { return maxScoreV3; }

    public double getMinScoreV4() { return minScoreV4; }
    public double getMaxScoreV4() { return maxScoreV4; }

    public int getOrder() { return order; }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }

    @Override public Severity getDefault() { return Severity.NONE; }

    @Override public Severity getNotFound() { return Severity.NOT_FOUND; }

    @Override public Severity[] getAll() { return values(); }

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
     * Returns Severity parsed from a given text
     * @param text Name of the severity to parse usually the api_string of a severity e.g. 'low'
     * @return Severity parsed from a given text
     */
    public static Severity fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch (text) {
            case "low", "LOW", "Low"                -> { return LOW; }
            case "medium", "MEDIUM", "Medium"       -> { return MEDIUM; }
            case "high", "HIGH", "High"             -> { return HIGH; }
            case "critical", "CRITICAL", "Critical" -> { return CRITICAL; }
            default                                 -> { return NOT_FOUND; }
        }
    }

    /**
     * Returns a Severity parsed from the given score and cvss version
     * @param score The CVSS score (0.0 - 10.0)
     * @param cvss  The CVSS version (CVSS2, CVSS3, CVSS4)
     * @return Severity parsed from a given score and cvss version
     */
    public static Severity fromScore(final double score, final CVSS cvss) {
        switch (cvss) {
            case CVSSV2 -> {
                if (score >= 0 && score <= 3.9) {
                    return Severity.LOW;
                } else if (score > 3.9 && score <= 6.9) {
                    return Severity.MEDIUM;
                } else if (score > 6.9 && score <= 10.0) {
                    return Severity.HIGH;
                } else {
                    return Severity.NOT_FOUND;
                }
            }
            case CVSSV3, CVSSV4 -> {
                if (score <= 0) {
                    return Severity.NONE;
                } else if (score > 0 && score <= 3.9) {
                    return Severity.LOW;
                } else if (score > 3.9 && score <= 6.9) {
                    return Severity.MEDIUM;
                } else if (score > 6.9 && score < 8.9) {
                    return Severity.HIGH;
                } else if (score > 8.9 && score <= 10.0) {
                    return Severity.CRITICAL;
                } else {
                    return Severity.NOT_FOUND;
                }
            }
        }
        return Severity.NOT_FOUND;
    }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<Severity> getAsList() { return Arrays.asList(values()); }

    public int compareToSeverity(final Severity other) {
        return order.compareTo(other.order);
    }
}
