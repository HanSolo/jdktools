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


public enum HashAlgorithm implements Api {
    MD5("MSD5", "md5"),
    SHA1("SHA1", "sha1"),
    SHA256("SHA256", "sha256"),
    SHA224("SHA224", "sha224"),
    SHA384("SHA384", "sha384"),
    SHA512("SHA512", "sha512"),
    SHA3_256("SHA-3 256", "sha3_256"),
    NONE("-", ""),
    NOT_FOUND("", "");

    private final String uiString;
    private final String apiString;


    HashAlgorithm(final String uiString, final String apiString) {
        this.uiString  = uiString;
        this.apiString = apiString;
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }

    @Override public HashAlgorithm getDefault() { return HashAlgorithm.NONE; }

    @Override public HashAlgorithm getNotFound() { return HashAlgorithm.NOT_FOUND; }

    @Override public HashAlgorithm[] getAll() { return values(); }

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
     * Returns HashAlgorithm parsed from a given text
     * @param text Name of the hash algorithm to parse usually the api_string of a hash algorithm e.g. 'sha256'
     * @return HashAlgorithm parsed from a given text
     */
    public static HashAlgorithm fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch(text) {
            case "md5":
            case "MD5":
            case "md-5":
            case "md_5":
            case "MD-5":
            case "MD_5":
                return MD5;
            case "sha1":
            case "SHA1":
            case "sha-1":
            case "SHA-1":
            case "sha_1":
            case "SHA_1":
                return SHA1;
            case "sha256":
            case "SHA256":
            case "sha_256":
            case "SHA_256":
            case "sha-256":
            case "SHA-256":
                return SHA256;
            case "sha224":
            case "SHA224":
            case "sha_224":
            case "SHA_224":
            case "sha-224":
            case "SHA-224":
                return SHA224;
            case "sha384":
            case "SHA384":
            case "sha_384":
            case "SHA_384":
            case "sha-384":
            case "SHA-384":
                return SHA384;
            case "sha512":
            case "SHA512":
            case "sha_512":
            case "SHA_512":
            case "sha-512":
            case "SHA-512":
                return SHA512;
            case "sha3_256":
            case "SHA3_256":
            case "sha-3-256":
            case "SHA-3-256":
            case "sha_3_256":
            case "SHA_3_256":
                return SHA3_256;
            default:
                return NOT_FOUND;
        }
    }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<HashAlgorithm> getAsList() { return Arrays.asList(values()); }
}
