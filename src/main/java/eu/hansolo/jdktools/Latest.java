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


public enum Latest implements Api {
    ALL_OF_VERSION("all of version", "all_of_version"),
    OVERALL("overall", "overall"),
    PER_DISTRIBUTION("per distribution", "per_distro"),
    PER_VERSION("per version", "per_version"),
    AVAILABLE("available", "available"),
    NONE("-", ""),
    NOT_FOUND("", "");

    private final String uiString;
    private final String apiString;

    Latest(final String uiString, final String apiString) {
        this.uiString  = uiString;
        this.apiString = apiString;
    }


    @Override public String getUiString() {
        return uiString;
    }

    @Override public String getApiString() { return apiString; }

    @Override public Latest getDefault() { return Latest.NONE; }

    @Override public Latest getNotFound() { return Latest.NOT_FOUND; }

    @Override public Latest[] getAll() { return values(); }

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
     * Returns Latest parsed from a given text
     * @param text Name of the latest to parse usually the api_string of a latest e.g. 'available'
     * @return Latest parsed from a given text
     */
    public static Latest fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch (text) {
            case "per_distro", "per-distro", "per-distribution", "per_distribution", "perdistro", "PER_DISTRO", "PER-DISTRO", "PER_DISTRIBUTION", "PER-DISTRIBUTION", "PERDISTRO" -> { return PER_DISTRIBUTION; }
            case "overall", "OVERALL", "in_general", "in-general", "IN_GENERAL", "IN-GENERAL"                                                                                     -> { return OVERALL; }
            case "per_version", "per-version", "perversion", "PER_VERSION", "PER-VERSION", "PERVERSION"                                                                           -> { return PER_VERSION; }
            case "available", "AVAILABLE", "Available"                                                                                                                            -> { return AVAILABLE; }
            case "all_of_version", "ALL_OF_VERSION"                                                                                                                               -> { return ALL_OF_VERSION; }
            default                                                                                                                                                               -> { return NOT_FOUND; }
        }
    }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<Latest> getAsList() { return Arrays.asList(values()); }
}
