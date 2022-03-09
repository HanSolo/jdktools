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

import java.util.Arrays;
import java.util.List;


public enum QualityScope implements Scope {
    TCK_TESTED("TCK tested", "tck_tested"),
    AQAVIT_CERTIFIED("AQAVIT certified", "aqavit_certified");

    private final String uiString;
    private final String apiString;


    QualityScope(final String uiString, final String apiString) {
        this.uiString  = uiString;
        this.apiString = apiString;
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }


    /**
     * Return Scope parsed from given text
     * @param text Name of the scope to parse usually the api_string of a scope e.g. 'tck_tested'
     * @return Scope parsed from given text
     */
    public static Scope fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch(text) {
            case "tck_tested"      :
            case "TCK_TESTED"      : 
            case "tckTested"       : return TCK_TESTED;
            case "aqavit_certified":
            case "AQAVIT_CERTIFIED":
            case "aqavitCertified" : return AQAVIT_CERTIFIED;
            default                : return NOT_FOUND;
        }
    }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<QualityScope> getAsList() { return Arrays.asList(values()); }
}
