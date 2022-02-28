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


public enum TermOfSupport implements Api {
    STS("short term stable", "sts"),
    MTS("mid term stable", "mts"),
    LTS("long term stable", "lts"),
    NONE("-", ""),
    NOT_FOUND("", "");

    private final String uiString;
    private final String apiString;


    TermOfSupport(final String uiString, final String apiString) {
        this.uiString = uiString;
        this.apiString = apiString;
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }

    @Override public TermOfSupport getDefault() { return TermOfSupport.NONE; }

    @Override public TermOfSupport getNotFound() { return TermOfSupport.NOT_FOUND; }

    @Override public TermOfSupport[] getAll() { return values(); }

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

    public static TermOfSupport fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        return switch (text) {
            case "long_term_stable", "LongTermStable", "lts", "LTS", "Lts" -> LTS;
            case "mid_term_stable", "MidTermStable", "mts", "MTS", "Mts" -> MTS;
            case "short_term_stable", "ShortTermStable", "sts", "STS", "Sts" -> STS;
            default -> NOT_FOUND;
        };
    }

    public static List<TermOfSupport> getAsList() { return Arrays.asList(values()); }
}
