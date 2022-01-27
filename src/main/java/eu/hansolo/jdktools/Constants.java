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

import java.util.regex.Pattern;


public class Constants {
    public static final String SQUARE_BRACKET_OPEN  = "[";
    public static final String SQUARE_BRACKET_CLOSE = "]";
    public static final String CURLY_BRACKET_OPEN   = "{";
    public static final String CURLY_BRACKET_CLOSE  = "}";
    public static final String INDENTED_QUOTES      = "  \"";
    public static final String QUOTES               = "\"";
    public static final String COLON                = ":";
    public static final String COMMA                = ",";
    public static final String SLASH                = "/";
    public static final String NEW_LINE             = "\n";
    public static final String COMMA_NEW_LINE       = ",\n";
    public static final String INDENT               = "  ";

    public static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile("\\+?\\d+");
}
