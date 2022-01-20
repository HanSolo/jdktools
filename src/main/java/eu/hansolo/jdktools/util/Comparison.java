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

package eu.hansolo.jdktools.util;

public enum Comparison {
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    EQUAL("="),
    GREATER_THAN_OR_EQUAL(">="),
    GREATER_THAN(">"),
    RANGE_INCLUDING("..."),
    RANGE_EXCLUDING_TO("..<"),
    RANGE_EXCLUDING_FROM(">.."),
    RANGE_EXCLUDING(">.<");

    private final String operator;

    Comparison(final String operator) {
        this.operator = operator;
    }

    public String getOperator() { return operator; }

    public static Comparison fromText(final String text) {
        switch (text) {
            case "<"  : return LESS_THAN;
            case "<=" : return LESS_THAN_OR_EQUAL;
            case "="  : return EQUAL;
            case ">=" : return GREATER_THAN_OR_EQUAL;
            case ">"  : return GREATER_THAN;
            case "...": return RANGE_INCLUDING;
            case "..<": return RANGE_EXCLUDING_TO;
            case ">..": return RANGE_EXCLUDING_FROM;
            case ">.<": return RANGE_EXCLUDING;
            default   : return EQUAL;
        }
    }
}
