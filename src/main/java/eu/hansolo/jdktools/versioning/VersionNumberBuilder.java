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

package eu.hansolo.jdktools.versioning;

import eu.hansolo.jdktools.ReleaseStatus;


public class VersionNumberBuilder {
    private VersionNumber versionNumber;


    protected VersionNumberBuilder(final Integer featureNumber) {
        if (null == featureNumber) { throw new IllegalArgumentException("featureNumber cannot be null"); }
        if (featureNumber <= 0) { throw new IllegalArgumentException("featureNumber cannot <= 0"); }
        versionNumber = new VersionNumber(featureNumber);
    }


    public static VersionNumberBuilder create(final Integer featureNumber) throws IllegalArgumentException {
        if (null == featureNumber) { throw new IllegalArgumentException("Feature version cannot be null"); }
        if (0 >= featureNumber) { throw new IllegalArgumentException("Feature version cannot be smaller than 0 (" + featureNumber + ")"); }
        return new VersionNumberBuilder(featureNumber);
    }

    public VersionNumberBuilder interimNumber(final Integer interimNumber) throws IllegalArgumentException {
        if (null != interimNumber && 0 > interimNumber) { throw new IllegalArgumentException("Interim version cannot be smaller than 0"); }
        versionNumber.setInterim(interimNumber);
        return this;
    }

    public VersionNumberBuilder updateNumber(final Integer updateNumber) throws IllegalArgumentException {
        if (null != updateNumber && 0 > updateNumber) { throw new IllegalArgumentException("Update version cannot be smaller than 0"); }
        versionNumber.setUpdate(updateNumber);
        return this;
    }

    public VersionNumberBuilder patchNumber(final Integer patchNumber) throws IllegalArgumentException {
        if (null != patchNumber && 0 > patchNumber) { throw new IllegalArgumentException("Patch version cannot be smaller than 0"); }
        versionNumber.setPatch(patchNumber);
        return this;
    }

    public VersionNumberBuilder fifthNumber(final Integer fifthNumber) throws IllegalArgumentException {
        if (null != fifthNumber && 0 > fifthNumber) { throw new IllegalArgumentException("Fifth version cannot be smaller than 0"); }
        versionNumber.setFifth(fifthNumber);
        return this;
    }

    public VersionNumberBuilder sixthNumber(final Integer sixthNumber) throws IllegalArgumentException {
        if (null != sixthNumber && 0 > sixthNumber) { throw new IllegalArgumentException("Sixth version cannot be smaller than 0"); }
        versionNumber.setSixth(sixthNumber);
        return this;
    }

    public VersionNumberBuilder buildNumber(final Integer buildNumber) throws IllegalArgumentException {
        if (null != buildNumber && 0 > buildNumber) { throw new IllegalArgumentException("Build version cannot be smaller than 0"); }
        versionNumber.setBuild(buildNumber);
        return this;
    }

    public VersionNumberBuilder releaseStatus(final ReleaseStatus releaseStatus) {
        if (null == releaseStatus) { throw new IllegalArgumentException("Release status cannot be null"); }
        versionNumber.setReleaseStatus(releaseStatus);
        return this;
    }

    public VersionNumber build() {
        return versionNumber;
    }
}
