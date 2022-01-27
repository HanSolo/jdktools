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

import eu.hansolo.jdktools.Constants;
import eu.hansolo.jdktools.TermOfSupport;
import eu.hansolo.jdktools.versioning.VersionNumber;


public class Helper {

    private Helper(){}

    public static final String trimPrefix(final String text, final String prefix) {
        return text.replaceFirst(prefix, "");
    }

    public static final boolean isPositiveInteger(final String text) {
        if (null == text || text.isEmpty()) { return false; }
        return Constants.POSITIVE_INTEGER_PATTERN.matcher(text).matches();
    }


    public static final boolean isReleaseTermOfSupport(final int featureVersion, final TermOfSupport termOfSupport) {
        switch(termOfSupport) {
            case LTS: return isLTS(featureVersion);
            case MTS: return isMTS(featureVersion);
            case STS: return isSTS(featureVersion);
            default : return false;
        }
    }
    public static final boolean isSTS(final int featureVersion) {
        if (featureVersion < 9) { return false; }
        return switch (featureVersion) {
            case 9, 10 -> true;
            default -> !isLTS(featureVersion);
        };
    }
    public static final boolean isMTS(final int featureVersion) {
        if (featureVersion < 13) { return false; }
        return (!isLTS(featureVersion)) && featureVersion % 2 != 0;
    }
    public static final boolean isLTS(final int featureVersion) {
        if (featureVersion < 1) { throw new IllegalArgumentException("Feature version number cannot be smaller than 1"); }
        if (featureVersion <= 8) { return true; }
        if (featureVersion < 11) { return false; }
        if (featureVersion < 17) { return ((featureVersion - 11.0) / 6.0) % 1 == 0; }
        return ((featureVersion - 17.0) / 4.0) % 1 == 0;
    }

    public static final TermOfSupport getTermOfSupport(final VersionNumber versionNumber, final boolean isZulu) {
        TermOfSupport termOfSupport = getTermOfSupport(versionNumber);
        return switch (termOfSupport) {
            case LTS, STS -> termOfSupport;
            case MTS -> isZulu ? termOfSupport : TermOfSupport.STS;
            default -> TermOfSupport.NOT_FOUND;
        };
    }
    public static final TermOfSupport getTermOfSupport(final VersionNumber versionNumber) {
        if (!versionNumber.getFeature().isPresent() || versionNumber.getFeature().isEmpty()) {
            throw new IllegalArgumentException("VersionNumber need to have a feature version");
        }
        return getTermOfSupport(versionNumber.getFeature().getAsInt());
    }
    public static final TermOfSupport getTermOfSupport(final int featureVersion) {
        if (featureVersion < 1) { throw new IllegalArgumentException("Feature version number cannot be smaller than 1"); }
        if (isLTS(featureVersion)) {
            return TermOfSupport.LTS;
        } else if (isMTS(featureVersion)) {
            return TermOfSupport.MTS;
        } else if (isSTS(featureVersion)) {
            return TermOfSupport.STS;
        } else {
            return TermOfSupport.NOT_FOUND;
        }
    }
}
