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


public enum DownloadScope implements Scope {
    DIRECTLY("Directly downloadable", "directly_downloadable"),
    NOT_DIRECTLY("Not directly downloadable", "not_directly_downloadable");

    private final String uiString;
    private final String apiString;


    DownloadScope(final String uiString, final String apiString) {
        this.uiString  = uiString;
        this.apiString = apiString;
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }


    public static Scope fromText(final String text) {
        switch(text) {
            case "directly":
            case "directly_downloadable":
                return DIRECTLY;
            case "not_directly":
            case "not_directly_downloadable":
                return NOT_DIRECTLY;
            default:
                return NOT_FOUND;
        }
    }

    public static List<DownloadScope> getAsList() { return Arrays.asList(values()); }
}
