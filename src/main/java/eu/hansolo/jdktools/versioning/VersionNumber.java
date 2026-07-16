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

import eu.hansolo.jdktools.Architecture;
import eu.hansolo.jdktools.ArchiveType;
import eu.hansolo.jdktools.OperatingSystem;
import eu.hansolo.jdktools.ReleaseStatus;
import eu.hansolo.jdktools.util.Helper;
import eu.hansolo.jdktools.util.OutputFormat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VersionNumber implements Comparable<VersionNumber> {
    public static final Pattern       VERSION_NO_PATTERN   = Pattern.compile("([1-9]\\d*)((u(\\d+))|(\\.?(\\d+)?\\.?(\\d+)?\\.?(\\d+)?\\.?(\\d+)?\\.(\\d+)))?(([_b])(\\d+))?(-([a-zA-Z][a-zA-Z0-9]*(?:\\.\\d+)*|\\d+(?:\\.\\d+)*))?(\\+([a-zA-Z0-9_]+))?");
    public static final Pattern       BUILD_NUMBER_PATTERN = Pattern.compile("\\+?([bB])([0-9]+)");
    public static final Pattern       BUILD_PATTERN        = Pattern.compile("\\d+");
    public static final Pattern       LEADING_INT_PATTERN  = Pattern.compile("^[0-9]*");
    private             Integer       feature;
    private             Integer       interim;
    private             Integer       update;
    private             Integer       patch;
    private             Integer       fifth;
    private             Integer       sixth;
    private             Integer       build;
    private             ReleaseStatus releaseStatus;
    private             String        meta;


    // ******************** Constructors **************************************
    public VersionNumber() {
        this.feature       = 1;
        this.interim       = null;
        this.update        = null;
        this.patch         = null;
        this.fifth         = null;
        this.sixth         = null;
        this.build         = null;
        this.releaseStatus = ReleaseStatus.NONE;
        this.meta          = "";
    }
    public VersionNumber(VersionNumber versionNumber) {
        this(versionNumber.getFeatureNumber(), versionNumber.getInterimNumber(), versionNumber.getUpdateNumber(), versionNumber.getPatchNumber(), versionNumber.getFifthNumber(), versionNumber.getSixthNumber(), versionNumber.getBuildNumber(), versionNumber.getReleaseStatusValue(), versionNumber.getMeta());
    }
    public VersionNumber(final Integer feature) {
        this(feature, 0, 0, 0, 0, 0, null, ReleaseStatus.NONE, "");
    }
    public VersionNumber(final Integer feature, final Integer interim) {
        this(feature, interim, 0, 0, 0, 0, null, ReleaseStatus.NONE, "");
    }
    public VersionNumber(final Integer feature, final Integer interim, final Integer update) {
        this(feature, interim, update, 0, 0, 0, null, ReleaseStatus.NONE, "");
    }
    public VersionNumber(final Integer feature, final Integer interim, final Integer update, final Integer patch) throws IllegalArgumentException {
        this(feature, interim, update, patch, 0, 0, null, ReleaseStatus.NONE, "");
    }
    public VersionNumber(final Integer feature, final Integer interim, final Integer update, final Integer patch, final Integer build) throws IllegalArgumentException {
        this(feature, interim, update, patch, 0, 0, build, ReleaseStatus.NONE, "");
    }
    public VersionNumber(final Integer feature, final Integer interim, final Integer update, final Integer patch, final Integer fifth, final Integer sixth) {
        this(feature, interim, update, patch, fifth, sixth, null, ReleaseStatus.NONE, "");
    }
    public VersionNumber(final Integer feature, final Integer interim, final Integer update, final Integer patch, final Integer fifth, final Integer sixth, final Integer build, final ReleaseStatus releaseStatus) throws IllegalArgumentException {
        this(feature, interim, update, patch, fifth, sixth, build, releaseStatus, "");
    }
    public VersionNumber(final Integer feature, final Integer interim, final Integer update, final Integer patch, final Integer fifth, final Integer sixth, final Integer build, final ReleaseStatus releaseStatus, final String meta) throws IllegalArgumentException {
        Objects.requireNonNull(feature, "Feature version cannot be null");
        if (0 >= feature)                     { throw new IllegalArgumentException("Feature version must be greater than 0"); }
        if (null != interim  && 0 > interim)  { throw new IllegalArgumentException("Interim version cannot be smaller than 0"); }
        if (null != update   && 0 > update)   { throw new IllegalArgumentException("Update version cannot be smaller than 0"); }
        if (null != patch    && 0 > patch)    { throw new IllegalArgumentException("Patch version cannot be smaller than 0"); }
        if (null != fifth    && 0 > fifth)    { throw new IllegalArgumentException("Fifth number cannot be smaller than 0"); }
        if (null != sixth    && 0 > sixth)    { throw new IllegalArgumentException("Sixth number cannot be smaller than 0"); }
        if (null != build    && 0 > build)    { throw new IllegalArgumentException("Build number cannot be smaller than 0"); }
        this.feature       = feature;
        this.interim       = null == interim       ? 0 : interim;
        this.update        = null == update        ? 0 : update;
        this.patch         = null == patch         ? 0 : patch;
        this.fifth         = null == fifth         ? 0 : fifth;
        this.sixth         = null == sixth         ? 0 : sixth;
        this.build         = build;
        this.releaseStatus = null == releaseStatus ? ReleaseStatus.NONE : releaseStatus;
        this.meta          = null == meta          ? "" : meta;
    }
    @Deprecated public VersionNumber(final OptionalInt feature, final OptionalInt interim, final OptionalInt update, final OptionalInt patch) {
        this(feature, interim, update, patch, OptionalInt.of(0), OptionalInt.of(0), OptionalInt.empty(), Optional.empty());
    }
    @Deprecated public VersionNumber(final OptionalInt feature, final OptionalInt interim, final OptionalInt update, final OptionalInt patch, final OptionalInt fifth, final OptionalInt sixth) {
        this(feature, interim, update, patch, fifth, sixth, OptionalInt.empty(), Optional.empty());
    }
    @Deprecated public VersionNumber(final OptionalInt feature, final OptionalInt interim, final OptionalInt update, final OptionalInt patch, final OptionalInt fifth, final OptionalInt sixth, final OptionalInt build, final Optional<ReleaseStatus> releaseStatus) {
        if (null == feature)                                                     { throw new IllegalArgumentException("Feature version cannot be null"); }
        if (feature.isPresent()  && 0 >= feature.getAsInt())                    { throw new IllegalArgumentException("Feature version must be greater than 0"); }
        if (null != interim  && interim.isPresent()  && 0 > interim.getAsInt()) { throw new IllegalArgumentException("Interim version cannot be smaller than 0"); }
        if (null != update   && update.isPresent()   && 0 > update.getAsInt())  { throw new IllegalArgumentException("Update version cannot be smaller than 0"); }
        if (null != patch    && patch.isPresent()    && 0 > patch.getAsInt())   { throw new IllegalArgumentException("Patch version cannot be smaller than 0"); }
        if (null != fifth    && fifth.isPresent()    && 0 > fifth.getAsInt())   { throw new IllegalArgumentException("Fifth number cannot be smaller than 0"); }
        if (null != sixth    && sixth.isPresent()    && 0 > sixth.getAsInt())   { throw new IllegalArgumentException("Sixth number cannot be smaller than 0"); }
        if (null != build    && build.isPresent()    && 0 > build.getAsInt())   { throw new IllegalArgumentException("Build number cannot be smaller than 0"); }
        this.feature       = (null == feature || feature.isEmpty())           ? null               : feature.getAsInt();
        this.interim       = (null == interim)                                ? Integer.valueOf(0) : (interim.isEmpty() ? null : interim.getAsInt());
        this.update        = (null == update)                                 ? Integer.valueOf(0) : (update.isEmpty()  ? null : update.getAsInt());
        this.patch         = (null == patch)                                  ? Integer.valueOf(0) : (patch.isEmpty()   ? null : patch.getAsInt());
        this.fifth         = (null == fifth)                                  ? Integer.valueOf(0) : (fifth.isEmpty()   ? null : fifth.getAsInt());
        this.sixth         = (null == sixth)                                  ? Integer.valueOf(0) : (sixth.isEmpty()   ? null : sixth.getAsInt());
        this.build         = (null == build || build.isEmpty())               ? null               : build.getAsInt();
        this.releaseStatus = (null == releaseStatus || releaseStatus.isEmpty()) ? ReleaseStatus.NONE : releaseStatus.get();
    }


    // ******************** Methods *******************************************
    /** @deprecated Use {@link #getFeatureNumber()} instead. */
    @Deprecated public OptionalInt getFeature() { return feature == null ? OptionalInt.empty() : OptionalInt.of(feature); }
    public Integer getFeatureNumber() { return feature; }
    public void setFeature(final Integer feature) throws IllegalArgumentException {
        if (null == feature) { throw new IllegalArgumentException("Feature version cannot be null"); }
        if (0 >= feature) { throw new IllegalArgumentException("Feature version must be greater than 0 (" + feature + ")"); }
        this.feature = feature;
    }

    /** @deprecated Use {@link #getInterimNumber()} instead. */
    @Deprecated public OptionalInt getInterim() { return interim == null ? OptionalInt.empty() : OptionalInt.of(interim); }
    public Integer getInterimNumber() { return interim; }
    public void setInterim(final Integer interim) throws IllegalArgumentException {
        if (null != interim && 0 > interim) { throw new IllegalArgumentException("Interim version cannot be smaller than 0"); }
        this.interim = interim;
    }

    /** @deprecated Use {@link #getUpdateNumber()} instead. */
    @Deprecated public OptionalInt getUpdate() { return update == null ? OptionalInt.empty() : OptionalInt.of(update); }
    public Integer getUpdateNumber() { return update; }
    public void setUpdate(final Integer update) throws IllegalArgumentException {
        if (null != update && 0 > update) { throw new IllegalArgumentException("Update version cannot be smaller than 0"); }
        this.update = update;
    }

    /** @deprecated Use {@link #getPatchNumber()} instead. */
    @Deprecated public OptionalInt getPatch() { return patch == null ? OptionalInt.empty() : OptionalInt.of(patch); }
    public Integer getPatchNumber() { return patch; }
    public void setPatch(final Integer patch) throws IllegalArgumentException {
        if (null != patch && 0 > patch) { throw new IllegalArgumentException("Patch version cannot be smaller than 0"); }
        this.patch = patch;
    }

    /** @deprecated Use {@link #getFifthNumber()} instead. */
    @Deprecated public OptionalInt getFifth() { return fifth == null ? OptionalInt.empty() : OptionalInt.of(fifth); }
    public Integer getFifthNumber() { return fifth; }
    public void setFifth(final Integer fifth) throws IllegalArgumentException {
        if (null != fifth && 0 > fifth) { throw new IllegalArgumentException("Fifth number cannot be smaller than 0"); }
        this.fifth = fifth;
    }

    /** @deprecated Use {@link #getSixthNumber()} instead. */
    @Deprecated public OptionalInt getSixth() { return sixth == null ? OptionalInt.empty() : OptionalInt.of(sixth); }
    public Integer getSixthNumber() { return sixth; }
    public void setSixth(final Integer sixth) throws IllegalArgumentException {
        if (null != sixth && 0 > sixth) { throw new IllegalArgumentException("Sixth number cannot be smaller than 0"); }
        this.sixth = sixth;
    }

    /** @deprecated Use {@link #getBuildNumber()} instead. */
    @Deprecated public OptionalInt getBuild() { return build == null ? OptionalInt.empty() : OptionalInt.of(build); }
    public Integer getBuildNumber() { return build; }
    public void setBuild(final Integer build) throws IllegalArgumentException {
        if (null != build && 0 >= build) {
            this.build = null;
        } else {
            this.build = build;
        }
    }

    /** @deprecated Use {@link #getReleaseStatusValue()} instead. */
    @Deprecated
    public Optional<ReleaseStatus> getReleaseStatus() { return releaseStatus == ReleaseStatus.NONE ? Optional.empty() : Optional.of(releaseStatus); }
    public ReleaseStatus getReleaseStatusValue() { return releaseStatus; }
    public void setReleaseStatus(final ReleaseStatus releaseStatus) {
        this.releaseStatus = null == releaseStatus ? ReleaseStatus.NONE : releaseStatus;
    }

    public String getMeta() { return this.meta; }
    public void setMeta(final String meta) { this.meta = null == meta ? "" : meta; }

    public SimpleMajorVersion getMajorVersion() { return new SimpleMajorVersion(feature != null ? feature : 0); }

    public String getNormalizedVersionNumber() {
        StringBuilder versionBuilder = new StringBuilder();
        if (feature != null) {
            versionBuilder.append(feature);
        } else {
            throw new IllegalArgumentException("Feature version number cannot be null");
        }
        versionBuilder.append(".").append(interim != null ? interim : 0);
        versionBuilder.append(".").append(update  != null ? update  : 0);
        versionBuilder.append(".").append(patch   != null ? patch   : 0);
        versionBuilder.append(".").append(fifth   != null ? fifth   : 0);
        versionBuilder.append(".").append(sixth   != null ? sixth   : 0);
        return versionBuilder.toString();
    }

    public static VersionNumber fromText(final String text) throws IllegalArgumentException {
        return fromText(text, 0, true);
    }
    public static VersionNumber fromText(final String text, final boolean isJavaVersion) throws IllegalArgumentException {
        return fromText(text, 0, isJavaVersion);
    }
    /**
     * Returns a version number parsed from the given text. If the matcher finds more than 1 result, the
     * resultToMatch variable will be taken into account. For example if the given text matches 2 times,
     * the resultToMatch variable defines which result should be taken to parse the version number.
     * @param text           Text to parse
     * @param resultToMatch  The result that should be taken for parsing if there are more than 1
     * @return Returns a version number parsed from the given text
     * @throws IllegalArgumentException Throws IllegalArgumentException in case the given text was null or empty
     */
    public static VersionNumber fromText(final String text, final int resultToMatch) throws IllegalArgumentException {
        return fromText(text, resultToMatch, true);
    }
    public static VersionNumber fromText(final String text, final int resultToMatch, final boolean isJavaVersion) throws IllegalArgumentException {
        if (null == text || text.isEmpty()) {
            throw new IllegalArgumentException("No version number can be parsed because given text is null or empty.");
        }

        // Remove things like cpu architecture, operating system and file endings
        AtomicReference<String> tmp = new AtomicReference<>(text);
        ArchiveType.getAsList().forEach(archiveType -> {
            archiveType.getFileEndings().forEach(fileEnding -> {
                if (fileEnding.isEmpty() || fileEnding.equals("-") || fileEnding.equals("_")) { return; }
                tmp.set(tmp.get().replaceAll(Pattern.quote(fileEnding), ""));
            });
        });
        OperatingSystem.getAsList().forEach(operatingSystem -> {
            OperatingSystem.getAcronyms(operatingSystem).forEach(acronym -> {
                if (acronym.isEmpty()) { return; }
                tmp.set(tmp.get().replaceAll("\\-" + acronym, ""));
                tmp.set(tmp.get().replaceAll("_" + acronym, ""));
            });
        });
        Architecture.getAsList().forEach(architecture -> {
            Architecture.getAcronyms(architecture).forEach(acronym -> {
                if (acronym.isEmpty()) { return; }
                tmp.set(tmp.get().replaceAll("\\-" + acronym, ""));
                tmp.set(tmp.get().replaceAll("_" + acronym, ""));
            });
        });

        // Streamline text to be more compatible to semver by replacing comming findings e.g. .bN -> +bN
        tmp.set(tmp.get().replaceAll("\\-beta", "-ea"));
        tmp.set(tmp.get().replaceAll("\\-BETA", "-ea"));
        tmp.set(tmp.get().replaceAll("_ea", "-ea"));
        tmp.set(tmp.get().replaceAll("_b([0-9])", "+b$1"));
        tmp.set(tmp.get().replaceAll("\\-b([0-9])", "+b$1"));
        tmp.set(tmp.get().replaceAll("\\.b([0-9])", "+b$1"));
        tmp.set(tmp.get().replaceAll("([0-9])b", "$1+b"));
        tmp.set(tmp.get().replaceAll("\\-([0-9]+)$", "+$1"));
        tmp.set(tmp.get().replaceAll("_openj9.*", ""));
        tmp.set(tmp.get().replaceAll("\\-openj9.*", ""));
        tmp.set(tmp.get().replaceAll("\\-LTS|\\-lts", ""));
        // Normalize: swap +build-preRelease → -preRelease+build so the regex always sees preRelease first.
        // Only swap for known pre-release keywords to avoid misidentifying OS/libc suffixes (e.g. -musl).
        tmp.set(tmp.get().replaceAll("(?i)(\\+[a-zA-Z0-9_]+)(-(ea|beta|pre|alpha)(?:[.][0-9]+)*)", "$2$1"));

        //System.out.println("stripped: " + tmp.get());

        // Remove leading "1." to get correct version number e.g. 1.8u262 -> 8u262
        String   version  = tmp.get();
        String[] tmpParts = tmp.get().split("\\.");
        if (isJavaVersion && tmpParts.length > 1) {
            if (tmpParts[0].equals("1") && Integer.parseInt(tmpParts[1].substring(0, 1)) <= 8) {
                version = tmp.get().startsWith("1.") ? tmp.get().replace("1.", "") : tmp.get();
            }
        }
        //String version = tmp.get().startsWith("1.") ? tmp.get().replace("1.", "") : tmp.get();

        final Matcher           versionNoMatcher = VERSION_NO_PATTERN.matcher(version);
        final List<MatchResult> results          = versionNoMatcher.results().toList();
        final int               noOfResults      = results.size();
        final int               resultToTake     = noOfResults > resultToMatch ? resultToMatch : 0;
        List<VersionNumber>     numbersFound     = new ArrayList<>();
        if (noOfResults > 0) {
            MatchResult result = results.get(resultToTake);
            VersionNumber versionNumber = new VersionNumber(Integer.valueOf(result.group(1)));
            if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(7) && null != result.group(9) && null != result.group(10) && null != result.group(11) && null != result.group(12) && null != result.group(13) && null != result.group(14) && null != result.group(15)) {
                //System.out.println("match: 1, 2, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(9)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(7) && null != result.group(10) && null != result.group(11) && null != result.group(12) && null != result.group(13) && null != result.group(14) && null != result.group(15) && null != result.group(16)) {
                //System.out.println("match: 1, 2, 5, 6, 7, 10, 11, 12, 13, 14, 15, 16");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(7) && null != result.group(8) && null != result.group(9) && null != result.group(10) && null != result.group(14) && null != result.group(15) && null != result.group(16)) {
                //System.out.println("match: 1, 2, 5, 6, 7, 8, 9, 10, 14, 15, 16");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(8)));
                versionNumber.setFifth(getPositiveIntFromText(result.group(9)));
                versionNumber.setSixth(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(7) && null != result.group(8) && null != result.group(10) && null != result.group(14) && null != result.group(15) && null != result.group(16)) {
                //System.out.println("match: 1, 2, 5, 6, 7, 8, 10, 14, 15, 16");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(8)));
                versionNumber.setFifth(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(10) && null != result.group(11) && null != result.group(12) && null != result.group(13) && null != result.group(14) && null != result.group(15) && null != result.group(16)) {
                //System.out.println("match: 1, 2, 5, 10, 11, 12, 13, 14, 15, 16");
                versionNumber.setInterim(getPositiveIntFromText(result.group(10)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(13)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(7) && null != result.group(10) && null != result.group(14) && null != result.group(15) && null != result.group(16)) {
                //System.out.println("match: 1, 2, 5, 6, 7, 10, 14, 15, 16");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(10) && null != result.group(14) && null != result.group(15) && null != result.group(16)) {
                //System.out.println("match: 1, 2, 5, 6, 10, 14, 15, 16");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(7) && null != result.group(8) && null != result.group(9) && null != result.group(10)) {
                //System.out.println("match: 1, 2, 5, 6, 7, 8, 9, 10");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(8)));
                versionNumber.setFifth(getPositiveIntFromText(result.group(9)));
                versionNumber.setSixth(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(7) && null != result.group(8) && null != result.group(10)) {
                //System.out.println("match: 1, 2, 5, 6, 7, 8, 10");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(8)));
                versionNumber.setFifth(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(3) && null != result.group(4) && null != result.group(14) && null != result.group(15) && null != result.group(16)) {
                //System.out.println("match: 1, 2, 3, 4, 14, 15, 16");
                versionNumber.setInterim(0);
                versionNumber.setUpdate(getPositiveIntFromText(result.group(4)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(3) && null != result.group(4) && null != result.group(11) && null != result.group(12) && null != result.group(13)) {
                //System.out.println("match: 1, 2, 3, 4, 11, 12, 13");
                versionNumber.setInterim(0);
                versionNumber.setUpdate(getPositiveIntFromText(result.group(4)));
            } /*else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(10) && null != result.group(14) && null != result.group(15) && null != result.group(16)) {
                //System.out.println("match: 1, 2, 5, 6, 10, 14, 15, 16");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6), version));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(10), version));
            } */ else if (null != result.group(1) && null != result.group(6) && null != result.group(7) && null != result.group(10)) {
                //System.out.println("match: 1, 6, 7, 10");
                versionNumber.setFeature(getPositiveIntFromText(result.group(1)));
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(10) && null != result.group(11) && null != result.group(12) && null != result.group(13)) {
                //System.out.println("match: 1, 2, 5, 10, 11, 12, 13");
                versionNumber.setInterim(0);
                versionNumber.setUpdate(getPositiveIntFromText(result.group(13)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(7) && null != result.group(10)) {
                //System.out.println("match: 1, 2, 5, 6, 7, 10");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(7)));
                versionNumber.setPatch(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(6) && null != result.group(10)) {
                //System.out.println("match: 1, 2, 5, 6, 10");
                versionNumber.setInterim(getPositiveIntFromText(result.group(6)));
                versionNumber.setUpdate(getPositiveIntFromText(result.group(10)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(3) && null != result.group(4)) {
                //System.out.println("match: 1, 2, 3, 4");
                versionNumber.setInterim(0);
                versionNumber.setUpdate(getPositiveIntFromText(result.group(4)));
            } else if (null != result.group(1) && null != result.group(2) && null != result.group(5) && null != result.group(10)) {
                //System.out.println("match: 1, 2, 5, 10");
                versionNumber.setInterim(getPositiveIntFromText(result.group(10)));
            }

            // Parse release status, build and meta
            // New pattern groups: 14 = -preRelease block, 15 = preRelease content, 16 = +build block, 17 = build content
            versionNumber.setReleaseStatus(ReleaseStatus.GA);
            versionNumber.setMeta("");

            if (null != result.group(14)) {
                // Pre-release section present, e.g. -ea, -ea.25.3.1.2, -23.1.3, -DEBUG
                final String preRelease = result.group(15);
                final int    dotIndex   = preRelease.indexOf('.');
                final String identifier = dotIndex >= 0 ? preRelease.substring(0, dotIndex) : preRelease;
                final String suffix     = dotIndex >= 0 ? preRelease.substring(dotIndex + 1) : "";
                if (!identifier.isEmpty() && Character.isLetter(identifier.charAt(0))) {
                    // Any alphabetic identifier (ea, beta, pre, DEBUG, etc.) → EA
                    versionNumber.setReleaseStatus(ReleaseStatus.EA);
                    if (!suffix.isEmpty()) {
                        if (Helper.isPositiveInteger(suffix)) {
                            // Single integer suffix → build number (e.g. -ea.28)
                            versionNumber.setBuild(Integer.parseInt(suffix));
                        } else {
                            // Multi-part dot-separated suffix → meta (e.g. -ea.25.3.1.2)
                            versionNumber.setMeta(suffix);
                        }
                    }
                } else {
                    // Numeric identifier: entire pre-release content goes to meta (e.g. -23.1.3)
                    versionNumber.setMeta(preRelease);
                }
            }

            if (null != result.group(16)) {
                // Build metadata section present, e.g. +7, +b25
                final Matcher           buildMatcher = BUILD_PATTERN.matcher(result.group(17));
                final List<MatchResult> buildResults = buildMatcher.results().toList();
                if (!buildResults.isEmpty()) {
                    versionNumber.setBuild(Integer.parseInt(buildResults.get(0).group()));
                }
            }

            // No Semver build found, try things like "b01" etc.
            if (versionNumber.getBuildNumber() == null) {
                final Matcher           buildNumberMatcher = BUILD_NUMBER_PATTERN.matcher(version);
                final List<MatchResult> buildNumberResults = buildNumberMatcher.results().toList();
                if (!buildNumberResults.isEmpty()) {
                    final MatchResult buildNumberResult = buildNumberResults.get(0);
                    if (null != buildNumberResult.group(2)) {
                        versionNumber.setBuild(Integer.parseInt(buildNumberResult.group(2)));
                    }
                }
            }

            if (versionNumber.getInterimNumber() == null) {
                versionNumber.setInterim(0);
            }
            if (versionNumber.getUpdateNumber() == null) {
                versionNumber.setUpdate(0);
            }
            if (versionNumber.getPatchNumber() == null) {
                versionNumber.setPatch(0);
            }
            if (versionNumber.getFifthNumber() == null) {
                versionNumber.setFifth(0);
            }
            if (versionNumber.getSixthNumber() == null) {
                versionNumber.setSixth(0);
            }
            numbersFound.add(versionNumber);
        }

        if (numbersFound.isEmpty()) {
            throw new IllegalArgumentException("No suitable version number found in String: " + text);
        } else {
            return numbersFound.stream().max(Comparator.comparingInt(VersionNumber::numbersAvailable)).get();
        }
    }

    /**
     * Returns the numbers that are available in the version number
     * e.g. Feature                                  (Number 1)
     *      Feature.Interim                          (Number 2)
     *      Feature.Interim.Update                   (Number 3)
     *      Feature.Interim.Update.Patch             (Number 4)
     *      Feature.Interim.Update.Patch.Fifth       (Number 5)
     *      Feature.Interim.Update.Patch.Fifth.Sixth (Number 6)
     * @return the numbers that are available in the version number
     */
    public int numbersAvailable() {
        return 1 + (interim != null ? 1 : 0) + (update != null ? 1 : 0) + (patch != null ? 1 : 0) + (fifth != null ? 1 : 0) + (sixth != null ? 1 : 0);
    }

    private static Integer getPositiveIntFromText(final String text) {
        if (Helper.isPositiveInteger(text)) {
            return Integer.valueOf(text);
        } else {
            //LOGGER.info("Given text {} did not contain positive integer. Full text to parse was: {}", text, fullTextToParse);
            return -1;
        }
    }

    /*
    private static Integer getLeadingIntFromText(final String text) {
        if (null == text || text.isEmpty()) { return -1; }
        Matcher matcher = LEADING_INT_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(0).isEmpty() ? -1 : Integer.parseInt(matcher.group(0));
        } else {
            //LOGGER.debug("Given text {} did not start with integer. Full text to parse was: {}", text, fullTextToParse);
            return -1;
        }
    }
    */

    /**
     * Returns 0 if given version number is equal to this. But with just a number like 11, it will
     * also return 0 for values like 11.0.2, 11.4.0 etc. This is used in the DiscoService to make sure
     * to filter results for version numbers.
     * @param otherVersionNumber Version number to compare to
     * @return 0 if given version number is equal to this. But also returns 0 if only feature number is equal to given feature number
     */
    public int compareForFilterTo(final VersionNumber otherVersionNumber) {
        int comparisonResult = 0;
        if (feature == null || otherVersionNumber.getFeatureNumber() == null) { return comparisonResult; }
        String[] version1Splits = toString().split("\\.");
        String[] version2Splits = otherVersionNumber.toString().split("\\.");
        int maxLengthOfVersionSplits = Math.min(version1Splits.length, version2Splits.length);

        for (int i = 0; i < maxLengthOfVersionSplits; i++) {
            Integer v1      = i < version1Splits.length ? Integer.parseInt(version1Splits[i]) : 0;
            Integer v2      = i < version2Splits.length ? Integer.parseInt(version2Splits[i]) : 0;
            int     compare = v1.compareTo(v2);
            if (compare != 0) {
                comparisonResult = compare;
                break;
            }
        }
        return comparisonResult;
    }

    @Override public int hashCode() {
        if (feature == null) { throw new IllegalArgumentException("feature cannot be null or empty"); }
        return Objects.hash(feature, interim == null ? 0 : interim, update == null ? 0 : update, patch == null ? 0 : patch);
    }

    @Override public boolean equals(final Object obj) {
        if (obj == VersionNumber.this) { return true; }
        if (!(obj instanceof VersionNumber)) { return false; }
        if (feature == null) { throw new IllegalArgumentException("feature cannot be null or empty"); }
        VersionNumber other = (VersionNumber) obj;
        if (other.feature == null) { throw new IllegalArgumentException("feature cannot be null or empty"); }
        boolean isEqual;
        if (feature.equals(other.feature)) {
            if (interim != null) {
                if (other.interim != null) {
                    if (interim.equals(other.interim)) {
                        if (update != null) {
                            if (other.update != null) {
                                if (update.equals(other.update)) {
                                    if (patch != null) {
                                        if (other.patch != null) {
                                            if (patch.equals(other.patch)) {
                                                if (fifth != null) {
                                                    if (other.fifth != null) {
                                                        if (fifth.equals(other.fifth)) {
                                                            if (sixth != null) {
                                                                if (other.sixth != null) {
                                                                    isEqual = sixth.equals(other.sixth);
                                                                } else {
                                                                    isEqual = false;
                                                                }
                                                            } else {
                                                                isEqual = true;
                                                            }
                                                        } else {
                                                            isEqual = false;
                                                        }
                                                    } else {
                                                        isEqual = false;
                                                    }
                                                } else {
                                                    isEqual = true;
                                                }
                                            } else {
                                                isEqual = false;
                                            }
                                        } else {
                                            isEqual = false;
                                        }
                                    } else {
                                        isEqual = true;
                                    }
                                } else {
                                    isEqual = false;
                                }
                            } else {
                                isEqual = false;
                            }
                        } else {
                            isEqual = true;
                        }
                    } else {
                        isEqual = false;
                    }
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = true;
            }
        } else {
            isEqual = false;
        }
        if (isEqual && ReleaseStatus.EA == releaseStatus && build != null &&
            ReleaseStatus.EA == other.releaseStatus && other.build != null) {
            isEqual = build.equals(other.build);
        }
        return isEqual;
    }

    public static boolean equalsExceptBuild(final VersionNumber v1, final VersionNumber v2) {
        VersionNumber v1Copy = new VersionNumber(v1);
        v1Copy.setBuild(null);
        VersionNumber v2Copy = new VersionNumber(v2);
        v2Copy.setBuild(null);
        return v1Copy.equals(v2Copy);
    }
    public static boolean equalsIncludingBuild(final VersionNumber v1, final VersionNumber v2) { return v1.compareTo(v2) == 0; }

    public String toStringInclBuild(final boolean javaFormat) {
        return toString(OutputFormat.REDUCED, javaFormat, true);
    }

    public String toString(final OutputFormat outputFormat, final boolean javaFormat, final boolean includeReleaseStatusAndBuild) {
        return toString(outputFormat, javaFormat, includeReleaseStatusAndBuild, false);
    }
    public String toString(final OutputFormat outputFormat, final boolean javaFormat, final boolean includeReleaseStatusAndBuild, final boolean includeMeta) {
        String pre      = ReleaseStatus.EA == this.releaseStatus ? "-ea" : "";
        String buildStr = (this.build != null && this.build > 0) ? ("+" + this.build) : "";
        if (includeMeta && !meta.isEmpty()) { pre += ReleaseStatus.EA == this.releaseStatus ? ("." + meta) : "-" + meta; }
        StringBuilder versionBuilder = new StringBuilder();
        switch(outputFormat) {
            case REDUCED:
            case REDUCED_COMPRESSED: // e.g. 25.0.0.0 -> 25
                if (feature != null) { versionBuilder.append(feature); }
                if (sixth != null && sixth != 0) {
                    if (interim != null) { versionBuilder.append(".").append(interim); }
                    if (update  != null) { versionBuilder.append(".").append(update); }
                    if (patch   != null) { versionBuilder.append(".").append(patch); }
                    if (!javaFormat) {
                        if (fifth != null) {
                            versionBuilder.append(".").append(fifth);
                            versionBuilder.append(".").append(sixth);
                        }
                    }
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else if (fifth != null && fifth != 0) {
                    if (interim != null) { versionBuilder.append(".").append(interim); }
                    if (update  != null) { versionBuilder.append(".").append(update); }
                    if (patch   != null) { versionBuilder.append(".").append(patch); }
                    if (!javaFormat) { versionBuilder.append(".").append(fifth); }
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }

                    return versionBuilder.toString();
                } else if (patch != null && patch != 0) {
                    if (interim != null) {
                        versionBuilder.append(".").append(interim);
                        if (update  != null) {
                            versionBuilder.append(".").append(update);
                            versionBuilder.append(".").append(patch);
                        }
                    }
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else if (update != null && update != 0) {
                    if (interim != null) {
                        versionBuilder.append(".").append(interim);
                        versionBuilder.append(".").append(update);
                    }
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else if (interim != null && interim != 0) {
                    versionBuilder.append(".").append(interim);
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else {
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                }
            case FULL_COMPRESSED: // e.g. 25.0.0
                if (feature != null) { versionBuilder.append(feature); }
                if (sixth != null && sixth != 0) {
                    if (interim != null) {
                        versionBuilder.append(".").append(interim);
                        if (update != null) {
                            versionBuilder.append(".").append(update);
                            if (patch != null) {
                                versionBuilder.append(".").append(patch);
                            }
                        }
                    }

                    if (!javaFormat) {
                        if (fifth != null) {
                            versionBuilder.append(".").append(fifth);
                            versionBuilder.append(".").append(sixth);
                        }
                    }
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else if (fifth != null && fifth != 0) {
                    if (interim != null) {
                        versionBuilder.append(".").append(interim);
                        if (update  != null) {
                            versionBuilder.append(".").append(update);
                            if (patch   != null) {
                                versionBuilder.append(".").append(patch);
                            }
                        }
                    }
                    if (!javaFormat) { versionBuilder.append(".").append(fifth); }
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else if (patch != null && patch != 0) {
                    if (interim != null) { versionBuilder.append(".").append(interim); }
                    if (update  != null) { versionBuilder.append(".").append(update); }
                    versionBuilder.append(".").append(patch);
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else if (update != null) {
                    if (interim != null) { versionBuilder.append(".").append(interim); }
                    versionBuilder.append(".").append(update);
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else if (interim != null) {
                    versionBuilder.append(".").append(interim);
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                } else {
                    if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                    return versionBuilder.toString();
                }
            default:
                if (feature != null) {
                    versionBuilder.append(feature);
                    if (interim != null) {
                        versionBuilder.append(".").append(interim);
                        if (update != null) {
                            versionBuilder.append(".").append(update);
                            if (patch != null) {
                                versionBuilder.append(".").append(patch);
                            }
                        }
                    }
                }
                if (!javaFormat) {
                    if (fifth  != null) { versionBuilder.append(".").append(fifth); }
                    if (sixth  != null) { versionBuilder.append(".").append(sixth); }
                }
                if (includeReleaseStatusAndBuild) { versionBuilder.append(pre).append(buildStr); }
                return versionBuilder.toString();
        }
    }

    @Override public String toString() {
        return toString(OutputFormat.FULL, true, true);
    }

    @Override public int compareTo(final VersionNumber otherVersionNumber) {
        final int equal       = 0;
        final int smallerThan = -1;
        final int largerThan  = 1;
        int ret;

        if (feature != null && otherVersionNumber.feature != null) {
            if (feature > otherVersionNumber.feature) {
                ret = largerThan;
            } else if (feature < otherVersionNumber.feature) {
                ret = smallerThan;
            } else {
                if (interim != null && otherVersionNumber.interim != null) {
                    if (interim > otherVersionNumber.interim) {
                        ret = largerThan;
                    } else if (interim < otherVersionNumber.interim) {
                        ret = smallerThan;
                    } else {
                        if (update != null && otherVersionNumber.update != null) {
                            if (update > otherVersionNumber.update) {
                                ret = largerThan;
                            } else if (update < otherVersionNumber.update) {
                                ret = smallerThan;
                            } else {
                                if (patch != null && otherVersionNumber.patch != null) {
                                    if (patch > otherVersionNumber.patch) {
                                        ret = largerThan;
                                    } else if (patch < otherVersionNumber.patch) {
                                        ret = smallerThan;
                                    } else {
                                        if (fifth != null && otherVersionNumber.fifth != null) {
                                            if (fifth > otherVersionNumber.fifth) {
                                                ret = largerThan;
                                            } else if (fifth < otherVersionNumber.fifth) {
                                                ret = smallerThan;
                                            } else {
                                                if (sixth != null && otherVersionNumber.sixth != null) {
                                                    if (sixth > otherVersionNumber.sixth) {
                                                        ret = largerThan;
                                                    } else if (sixth < otherVersionNumber.sixth) {
                                                        ret = smallerThan;
                                                    } else {
                                                        ReleaseStatus thisStatus  = releaseStatus == ReleaseStatus.NONE                         ? ReleaseStatus.GA : releaseStatus;
                                                        ReleaseStatus otherStatus = otherVersionNumber.releaseStatus == ReleaseStatus.NONE ? ReleaseStatus.GA : otherVersionNumber.releaseStatus;

                                                        if (ReleaseStatus.GA == thisStatus && ReleaseStatus.EA == otherStatus) {
                                                            ret = largerThan;
                                                        } else if (ReleaseStatus.EA == thisStatus && ReleaseStatus.GA == otherStatus) {
                                                            ret = smallerThan;
                                                        } else if (thisStatus == otherStatus) {
                                                            // Either both GA or both EA
                                                            int thisBuild  = build != null                  ? build                  : 0;
                                                            int otherBuild = otherVersionNumber.build != null ? otherVersionNumber.build : 0;

                                                            ret = Integer.compare(thisBuild, otherBuild);
                                                        } else {
                                                            ret = equal;
                                                        }
                                                    }
                                                } else if (sixth != null && otherVersionNumber.sixth == null) {
                                                    ret = largerThan;
                                                } else if (sixth == null && otherVersionNumber.sixth != null) {
                                                    ret = smallerThan;
                                                } else {
                                                    ret = equal;
                                                }
                                            }
                                        } else if (fifth != null && otherVersionNumber.fifth == null) {
                                            ret = largerThan;
                                        } else if (fifth == null && otherVersionNumber.fifth != null) {
                                            ret = smallerThan;
                                        } else {
                                            ret= equal;
                                        }
                                    }
                                } else if (patch != null && otherVersionNumber.patch == null) {
                                    ret = largerThan;
                                } else if (patch == null && otherVersionNumber.patch != null) {
                                    ret = smallerThan;
                                } else {
                                    ret = equal;
                                }
                            }
                        } else if (update != null && otherVersionNumber.update == null) {
                            ret = largerThan;
                        } else if (update == null && otherVersionNumber.update != null) {
                            ret = smallerThan;
                        } else {
                            ret = equal;
                        }
                    }
                } else if (interim != null && otherVersionNumber.interim == null) {
                    ret = largerThan;
                } else if (interim == null && otherVersionNumber.interim != null) {
                    ret = smallerThan;
                } else {
                    ret = equal;
                }
            }
        } else if (feature != null && otherVersionNumber.feature == null) {
            ret = largerThan;
        } else if (feature == null && otherVersionNumber.feature != null) {
            ret = smallerThan;
        } else {
            ret = equal;
        }
        if (ret == equal) {
            if (ReleaseStatus.EA == releaseStatus && build != null &&
                ReleaseStatus.EA == otherVersionNumber.releaseStatus && otherVersionNumber.build != null) {
                ret = Integer.compare(build, otherVersionNumber.build);
            } else if (ReleaseStatus.EA == releaseStatus && build != null && ReleaseStatus.EA == otherVersionNumber.releaseStatus && otherVersionNumber.build == null) {
                ret = largerThan;
            } else if (ReleaseStatus.EA == releaseStatus && build == null &&
                       ReleaseStatus.EA == otherVersionNumber.releaseStatus && otherVersionNumber.build != null) {
                ret = smallerThan;
            }
        }
        return ret;
    }

    public boolean isLessThan(final VersionNumber versionNumber) { return compareTo(versionNumber) < 0; }
    public boolean isLessThanOrEqualTo(final VersionNumber versionNumber) { return compareTo(versionNumber) <= 0; }
    public boolean isGreaterThan(final VersionNumber versionNumber) { return compareTo(versionNumber) > 0; }
    public boolean isGreaterThanOrEqualTo(final VersionNumber versionNumber) { return compareTo(versionNumber) >= 0; }

    public boolean isSmallerThan(final VersionNumber versionNumber) {
        return compareTo(versionNumber) < 0;
    }
    public boolean isSmallerOrEqualThan(final VersionNumber versionNumber) {
        return compareTo(versionNumber) <= 0;
    }
    public boolean isLargerOrEqualThan(final VersionNumber versionNumber) {
        return compareTo(versionNumber) >= 0;
    }
    public boolean isLargerThan(final VersionNumber versionNumber) {
        return compareTo(versionNumber) > 0;
    }
}