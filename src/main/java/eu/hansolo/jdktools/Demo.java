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
import eu.hansolo.jdktools.versioning.Semver;
import eu.hansolo.jdktools.versioning.VersionNumber;


public class Demo {

    public Demo() {
        String        versionString = "8u322-ea+12";
        Semver        semver        = Semver.fromText(versionString).getSemver1();
        VersionNumber versionNumber = semver.getVersionNumber();
        System.out.println("Version text        : " + versionString);
        System.out.println("Standardized version: " + versionNumber.toString(OutputFormat.REDUCED_COMPRESSED, true, true));
        System.out.println("Feature version     : " + versionNumber.getFeature().getAsInt());
        System.out.println("Update version      : " + versionNumber.getUpdate().getAsInt());
        System.out.println("Build               : " + versionNumber.getBuild().getAsInt());
        System.out.println("Release status      : " + versionNumber.getReleaseStatus().get().getUiString());
    }

    public static void main(String[] args) {
        new Demo();
    }
}
