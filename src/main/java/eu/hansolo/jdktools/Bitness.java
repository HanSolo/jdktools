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


public enum Bitness implements Api {
    BIT_32("32 Bit", "32", 32),
    BIT_64("64 Bit", "64", 64),
    NONE("-", "", 0),
    NOT_FOUND("", "", 0);

    private final String uiString;
    private final String apiString;
    private final int    bits;


    Bitness(final String uiString, final String apiString, final int bits) {
        this.uiString  = uiString;
        this.apiString = apiString;
        this.bits      = bits;
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }

    @Override public Bitness getDefault() { return Bitness.NONE; }

    @Override public Bitness getNotFound() { return Bitness.NOT_FOUND; }

    @Override public Bitness[] getAll() { return values(); }

    @Override public String toString(final OutputFormat outputFormat) {
        StringBuilder msgBuilder = new StringBuilder();
        switch(outputFormat) {
            case FULL, REDUCED, REDUCED_ENRICHED -> {
                msgBuilder.append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                          .append(INDENTED_QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("bits").append(QUOTES).append(COLON).append(bits).append(NEW_LINE)
                          .append(CURLY_BRACKET_CLOSE);
            }
            default -> {
                msgBuilder.append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("bits").append(QUOTES).append(COLON).append(bits)
                          .append(CURLY_BRACKET_CLOSE);
            }
        }
        return msgBuilder.toString();
    }

    @Override public String toString() { return toString(OutputFormat.FULL_COMPRESSED); }

    public int getAsInt() { return bits; }

    public String getAsString() { return Integer.toString(bits); }

    public static Bitness fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        return switch (text) {
            case "32", "32bit", "32Bit", "32BIT" -> BIT_32;
            case "64", "64bit", "64Bit", "64BIT" -> BIT_64;
            case "" -> NONE;
            default -> NOT_FOUND;
        };
    }

    public static Bitness fromInt(final Integer bits) {
        return switch (bits) {
            case 32 -> BIT_32;
            case 64 -> BIT_64;
            default -> NOT_FOUND;
        };
    }

    public static List<Bitness> getAsList() { return Arrays.asList(values()); }
}
