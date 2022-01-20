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


public enum BuildScope implements Scope {
    BUILD_OF_OPEN_JDK("Build of OpenJDK", "build_of_openjdk"),
    BUILD_OF_GRAALVM("Build of GraalVM", "build_of_graalvm");

    private final String uiString;
    private final String apiString;


    BuildScope(final String uiString, final String apiString) {
        this.uiString  = uiString;
        this.apiString = apiString;
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }


    public static Scope fromText(final String text) {
        switch(text) {
            case "build_of_openjdk": return BUILD_OF_OPEN_JDK;
            case "build_of_graalvm": return BUILD_OF_GRAALVM;
            default                : return NOT_FOUND;
        }
    }

    public static List<BuildScope> getAsList() { return Arrays.asList(values()); }
}