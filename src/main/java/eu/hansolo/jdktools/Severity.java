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
import java.util.Objects;

import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.COMMA_NEW_LINE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.INDENTED_QUOTES;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;


public enum Severity implements Api {
    LOW("LOW", "LOW", 0.1, 3.9),
    MEDIUM("MEDIUM", "MEDIUM", 4.0, 6.9),
    HIGH("HIGH", "HIGH", 7.0, 8.9),
    CRITICAL("CRITICAL", "CRITICAL", 9.0, 10.0),
    NONE("-", "", 0, 0),
    NOT_FOUND("", "", 0, 0);

    private final String uiString;
    private final String apiString;
    private final double minScore;
    private final double maxScore;


    Severity(final String uiString, final String apiString, final double minScore, final double maxScore) {
        this.uiString  = uiString;
        this.apiString = apiString;
        this.minScore  = minScore;
        this.maxScore  = maxScore;
    }

    public double getMinScore() { return minScore; }

    public double getMaxScore() { return maxScore; }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }

    @Override public Severity getDefault() { return Severity.NONE; }

    @Override public Severity getNotFound() { return Severity.NOT_FOUND; }

    @Override public Severity[] getAll() { return values(); }

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


    /**
     * Returns Severity parsed from a given text
     * @param text Name of the severity to parse usually the api_string of a severity e.g. 'low'
     * @return Severity parsed from a given text
     */
    public static Severity fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch (text) {
            case "low":
            case "LOW":
            case "Low":
                return LOW;
            case "medium":
            case "MEDIUM":
            case "Medium":
                return MEDIUM;
            case "high":
            case "HIGH":
            case "High":
                return HIGH;
            case "critical":
            case "CRITICAL":
            case "Critical":
                return CRITICAL;
            default:
                return NOT_FOUND;
        }
    }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<Severity> getAsList() { return Arrays.asList(values()); }
}
