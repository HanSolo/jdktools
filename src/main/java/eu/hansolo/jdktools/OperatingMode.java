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


public enum OperatingMode implements Api {
    NATIVE("Native", "native"),
    EMULATED("Emulated", "emulated"),
    NONE("-", ""),
    NOT_FOUND("", "");

    private final String  uiString;
    private final String  apiString;


    OperatingMode(final String uiString, final String apiString) {
        this.uiString  = uiString;
        this.apiString = apiString;
    }


    public String getUiString() {
        return this.uiString;
    }

    public String getApiString() {
        return this.apiString;
    }

    public OperatingMode getDefault() {
        return NONE;
    }

    public OperatingMode getNotFound() {
        return NOT_FOUND;
    }

    public OperatingMode[] getAll() {
        return values();
    }


    /**
     * Returns OperatingMode parsed from a given text
     * @param text Name of the operating mode to parse usually the api_string of a operating mode e.g. 'emulated'
     * @return OperatingMode parsed from a given text
     */
    public static OperatingMode fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        return switch (text) {
            case "native", "NATIVE", "Native"       -> NATIVE;
            case "emulated", "EMULATED", "Emulated" -> EMULATED;
            default                                 -> NOT_FOUND;
        };
    }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<OperatingMode> getAsList() {
        return Arrays.asList(values());
    }

    public String toString(final OutputFormat outputFormat) {
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

    @Override public String toString() {
        return toString(OutputFormat.REDUCED_COMPRESSED);
    }
}
