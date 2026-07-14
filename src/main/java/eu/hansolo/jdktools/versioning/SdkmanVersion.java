package eu.hansolo.jdktools.versioning;

import eu.hansolo.jdktools.Architecture;
import eu.hansolo.jdktools.ArchiveType;
import eu.hansolo.jdktools.OperatingSystem;
import eu.hansolo.jdktools.ReleaseStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SdkmanVersion implements Comparable<SdkmanVersion> {
    private static final Pattern SDKMAN_VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(-(crac|fx))?(\\+ea)?(\\+(\\d+))?(\\.(\\d+))?((\\.|\\+)r(\\d+))?$");
    private static final Pattern INT_PATTERN            = Pattern.compile("\\d+");
    private static final Pattern ARCHIVE_TYPE_PATTERN;
    private static final Pattern OS_ACRONYM_PATTERN;
    private static final Pattern ARCH_ACRONYM_PATTERN;

    static {
        final List<String> archiveEndings = new ArrayList<>();
        for (ArchiveType archiveType : ArchiveType.getAsList()) {
            for (String fileEnding : archiveType.getFileEndings()) {
                if (!fileEnding.isEmpty() && !fileEnding.equals("-") && !fileEnding.equals("_")) {
                    archiveEndings.add(fileEnding);
                }
            }
        }
        archiveEndings.sort(Comparator.comparingInt(String::length).reversed());
        final StringBuilder archiveAlts = new StringBuilder();
        for (String ending : archiveEndings) {
            if (!archiveAlts.isEmpty()) { archiveAlts.append('|'); }
            archiveAlts.append(Pattern.quote(ending));
        }
        ARCHIVE_TYPE_PATTERN = !archiveAlts.isEmpty() ? Pattern.compile(archiveAlts.toString()) : null;

        final List<String> osAcronyms = new ArrayList<>();
        for (OperatingSystem os : OperatingSystem.getAsList()) {
            for (String acronym : OperatingSystem.getAcronyms(os)) {
                if (!acronym.isEmpty()) { osAcronyms.add(acronym); }
            }
        }
        osAcronyms.sort(Comparator.comparingInt(String::length).reversed());
        final StringBuilder osAlts = new StringBuilder();
        for (String acronym : osAcronyms) {
            if (!osAlts.isEmpty()) { osAlts.append('|'); }
            osAlts.append(Pattern.quote(acronym));
        }
        OS_ACRONYM_PATTERN = !osAlts.isEmpty() ? Pattern.compile("[-_](?:" + osAlts + ")") : null;

        final List<String> archAcronyms = new ArrayList<>();
        for (Architecture arch : Architecture.getAsList()) {
            for (String acronym : Architecture.getAcronyms(arch)) {
                if (!acronym.isEmpty()) { archAcronyms.add(acronym); }
            }
        }
        archAcronyms.sort(Comparator.comparingInt(String::length).reversed());
        final StringBuilder archAlts = new StringBuilder();
        for (String acronym : archAcronyms) {
            if (!archAlts.isEmpty()) { archAlts.append('|'); }
            archAlts.append(Pattern.quote(acronym));
        }
        ARCH_ACRONYM_PATTERN = !archAlts.isEmpty() ? Pattern.compile("[-_](?:" + archAlts + ")") : null;
    }

    private              int                feature;
    private              int                interim;
    private              int                update;
    private              int                patch;
    private              String             pre;
    private              String             meta;
    private              boolean            earlyAccess;
    private              boolean            graal;
    private              boolean            fx;
    private              boolean            crac;
    private              int                buildNumber;
    private              int                target;
    private              SimpleMajorVersion majorVersion;


    // ******************** Constructors **************************************
    public SdkmanVersion() {
        this(1, 0, 0, 0, "", "");
    }
    public SdkmanVersion(SdkmanVersion sdkmanVersion) {
        this(sdkmanVersion.getFeature(), sdkmanVersion.getInterim(), sdkmanVersion.getUpdate(), sdkmanVersion.getPatch(), sdkmanVersion.getPre(), sdkmanVersion.getMeta());
    }
    public SdkmanVersion(final int feature) {
        this(feature, 0, 0, 0, "", "");
    }
    public SdkmanVersion(final int feature, final int interim, final int update) {
        this(feature, interim, update, 0, "","");
    }
    public SdkmanVersion(final int feature, final int interim, final int update, final int patch) throws IllegalArgumentException {
        this(feature, interim, update, patch, "", "");
    }
    public SdkmanVersion(final int feature, final int interim, final int update, final int patch, final String pre, final String meta) {
        this(feature, interim, update, patch, pre, meta, (null != pre && !pre.isEmpty()), false, 0, false, false, 0);
    }
    SdkmanVersion(final int feature, final int interim, final int update, final int patch, final String pre, final String meta, final boolean earlyAccess, final boolean graal, final int target, final boolean fx, final boolean crac, final int buildNumber) {
        if (feature < 1) { throw new IllegalArgumentException("Feature version must be greater than 0"); }
        if (interim < 0) { throw new IllegalArgumentException("Interim version cannot be smaller than 0"); }
        if (update  < 0) { throw new IllegalArgumentException("Update version cannot be smaller than 0"); }
        if (patch   < 0) { throw new IllegalArgumentException("Patch version cannot be smaller than 0"); }

        this.feature = feature;
        this.interim = interim;
        this.update  = update;
        this.patch   = patch;
        this.pre     = pre  == null ? "" : pre.replaceFirst("-", "");
        this.meta    = meta == null ? "" : meta.replaceFirst("\\+", "");

        // Additional information
        this.earlyAccess  = earlyAccess;
        this.graal        = graal;
        this.target       = target;
        this.fx           = fx;
        this.crac         = crac;
        this.buildNumber  = buildNumber;
        this.majorVersion = new SimpleMajorVersion(feature);

        if (pre.isEmpty()) {
            if (this.fx && this.crac) {
                this.pre = "fx.crac";
            } else if (this.fx) {
                this.pre = "fx";
            } else if (this.crac) {
                this.pre = "crac";
            }
        }

        if (this.meta.isEmpty()) {
            if (this.earlyAccess) {
                this.meta = "ea";
            }
            if (this.patch > 0) {
                this.meta += this.meta.isEmpty() ? this.patch : ("." + this.patch);
            }
            if (this.buildNumber > 0) {
                this.meta += (this.meta.isEmpty() ? this.buildNumber : ("+" + this.buildNumber));
            }
            if (this.graal && this.target > 0) {
                this.meta += (this.meta.isEmpty() ? ("r" + this.target) : (".r" + this.target));
            }
        }
    }
    


    // ******************** Methods *******************************************
    public int getFeature() { return this.feature; }
    public void setFeature(final Integer feature) throws IllegalArgumentException {
        if (feature < 1) { throw new IllegalArgumentException("Feature version must be greater than 0 (" + feature + ")"); }
        this.feature      = feature;
        this.majorVersion = new SimpleMajorVersion(this.feature);
    }

    public int getInterim() { return this.interim; }
    public void setInterim(final Integer interim) throws IllegalArgumentException {
        if (interim < 0) { throw new IllegalArgumentException("Interim version cannot be smaller than 0"); }
        this.interim = interim;
    }

    public int getUpdate() { return this.update; }
    public void setUpdate(final Integer update) throws IllegalArgumentException {
        if (update < 0) { throw new IllegalArgumentException("Update version cannot be smaller than 0"); }
        this.update = update;
    }

    public int getPatch() { return this.patch; }
    public void setPatch(final Integer patch) throws IllegalArgumentException {
        if (patch < 0) { throw new IllegalArgumentException("Patch version cannot be smaller than 0"); }
        this.patch = patch;
    }

    public String getPre() { return this.pre; }
    public void setPre(final String pre) {
        this.pre         = (pre == null || pre.isEmpty()) ? "" : pre.replaceFirst("-", "");
        this.earlyAccess = !this.pre.isEmpty();
    }

    public String getMeta() { return this.meta; }
    public void setMeta(final String meta) {
        this.meta = meta == null || meta.isEmpty() ? "" : meta.replaceFirst("\\+", "");
    }

    public SimpleMajorVersion getMajorVersion() { return this.majorVersion; }

    public String getNormalizedJavaVersion() {
        return new StringBuilder().append(feature).append(".")
                                  .append(interim).append(".")
                                  .append(update).append(".")
                                  .append(patch)
                                  .append(this.pre.isEmpty()  ? "" : ("-" + this.pre))
                                  .append(this.meta.isEmpty() ? "" : ("+" + this.meta))
                                  .toString();
    }

    // Additional convenience methods
    public boolean isEarlyAccess() { return this.earlyAccess; }

    public boolean isGraalVM() { return this.graal; }

    public int getTarget() { return this.target; }

    public boolean hasFX() { return this.fx; }

    public boolean hasCRaC() { return this.crac; }

    public int getBuildNumber() { return this.buildNumber; }

    public static SdkmanVersion fromText(final String text) throws IllegalArgumentException { return fromText(text, true); }
    private static SdkmanVersion fromText(final String text, final boolean isJavaVersion) throws IllegalArgumentException {
        if (null == text || text.isEmpty()) {
            throw new IllegalArgumentException("No version number can be parsed because given text is null or empty.");
        }

        // Remove things like cpu architecture, operating system and file endings
        String tmp = text;
        if (ARCHIVE_TYPE_PATTERN != null) { tmp = ARCHIVE_TYPE_PATTERN.matcher(tmp).replaceAll(""); }
        if (OS_ACRONYM_PATTERN   != null) { tmp = OS_ACRONYM_PATTERN.matcher(tmp).replaceAll(""); }
        if (ARCH_ACRONYM_PATTERN != null) { tmp = ARCH_ACRONYM_PATTERN.matcher(tmp).replaceAll(""); }

        // Remove leading "1." to get correct version number e.g. 1.8u262 -> 8u262
        String version = tmp;
        String[] tmpParts = tmp.split("\\.");
        if (isJavaVersion && tmpParts.length > 1) {
            if (tmpParts[0].equals("1") && !tmpParts[1].isEmpty() && Character.isDigit(tmpParts[1].charAt(0)) && Integer.parseInt(tmpParts[1].substring(0, 1)) <= 8) {
                version = tmp.startsWith("1.") ? tmp.replaceFirst("^1\\.", "") : tmp;
            }
        }
        version = version.replace("u", ".0.");

        final Matcher sdkmanVersionMatcher = SDKMAN_VERSION_PATTERN.matcher(version);
        if (sdkmanVersionMatcher.matches()) {
            int     feature     = Integer.parseInt(sdkmanVersionMatcher.group(1));
            int     interim     = Integer.parseInt(sdkmanVersionMatcher.group(2));
            int     update      = Integer.parseInt(sdkmanVersionMatcher.group(3));
            int     patch       = sdkmanVersionMatcher.group(8)  != null ? Integer.parseInt(sdkmanVersionMatcher.group(8))  : 0;
            int     build       = sdkmanVersionMatcher.group(10) != null ? Integer.parseInt(sdkmanVersionMatcher.group(10)) : 0;
            boolean earlyAccess = sdkmanVersionMatcher.group(6) != null;
            boolean crac        = false;
            boolean fx          = false;
            if (sdkmanVersionMatcher.group(5) != null) {
                switch (sdkmanVersionMatcher.group(5)) {
                    case "crac" -> crac = true;
                    case "fx"   -> fx   = true;
                }
            }
            boolean graal       = sdkmanVersionMatcher.group(11) != null;
            int     target      = sdkmanVersionMatcher.group(13) != null ? Integer.parseInt(sdkmanVersionMatcher.group(13)) : 0;
            String  pre         = sdkmanVersionMatcher.group(4) != null ? sdkmanVersionMatcher.group(4).substring(1) : ""; // -crac, -fx
            String  meta        = version.indexOf("+") != -1 ? version.substring(version.indexOf("+") + 1) : "";  // +ea.5 Group 6, 7, 11

            return new SdkmanVersion(feature, interim, update, patch, pre, meta, earlyAccess, graal, target, fx, crac, build);
        }
        System.out.println("No valid JavaVersion found");
        return null;
    }

    public VersionNumber toVersionNumber() {
        return VersionNumberBuilder.create(feature).interimNumber(interim).updateNumber(update).patchNumber(patch).buildNumber(buildNumber).releaseStatus(earlyAccess ? ReleaseStatus.EA : ReleaseStatus.GA).build();
    }

    @Override public boolean equals(final Object obj) {
        if (obj == SdkmanVersion.this) { return true; }
        if (!(obj instanceof SdkmanVersion)) { return false; }
        SdkmanVersion other = (SdkmanVersion) obj;
        boolean       isEqual;
        if (this.feature == other.getFeature()) {
            //System.out.println("Feature: " + this.feature + " == " + other.getFeature());
            if (this.interim == other.getInterim()) {
                //System.out.println("Interim: " + this.interim + " == " + other.getInterim());
                if (this.update == other.getUpdate()) {
                    //System.out.println("Update: " + this.update + " == " + other.getUpdate());
                    if (this.patch == other.getPatch()) {
                        //System.out.println("Patch: " + this.patch + " == " + other.getPatch());
                        if (this.buildNumber == other.getBuildNumber()) {
                            //System.out.println("Build: " + this.buildNumber + " == " + other.getBuildNumber());
                            isEqual = true;
                        } else {
                            //System.out.println("Build: " + this.buildNumber + " != " + other.getBuildNumber());
                            isEqual = false;
                        }
                    } else {
                        //System.out.println("Patch: " + this.patch + " != " + other.getPatch());
                        isEqual = false;
                    }
                } else {
                    //System.out.println("Update: " + this.update + " != " + other.getUpdate());
                    isEqual = false;
                }
            } else {
                //System.out.println("Interim: " + this.interim + " != " + other.getInterim());
                isEqual = false;
            }
        } else {
            //System.out.println("Feature: " + this.feature + " != " + other.getFeature());
            isEqual = false;
        }

        if (isEqual) {
            isEqual = (this.fx     == other.hasFX()) &&
                      (this.crac   == other.hasCRaC()) &&
                      (this.graal  == other.isGraalVM()) &&
                      (this.target == other.getTarget());
        }

        return isEqual;
    }

    @Override public int hashCode() {
        return Objects.hash(feature, interim, update, patch, graal, fx, crac, target, buildNumber);
    }

    @Override public String toString() {
        return toString(false, true);
    }
    public String toString(final boolean javaFormat, final boolean includePreAndMeta) {
        final StringBuilder versionBuilder = new StringBuilder().append(feature).append(".").append(interim).append(".").append(update);
        if (javaFormat) { versionBuilder.append(".").append(patch); }
        if (includePreAndMeta) {
            if (!this.pre.isEmpty())  { versionBuilder.append("-").append(this.pre); }
            //if (!this.meta.isEmpty()) { versionBuilder.append("+").append(this.meta); }
            if (!this.meta.isEmpty()) { versionBuilder.append("+").append(this.meta); }
        }
        return versionBuilder.toString();
    }

    @Override public int compareTo(final SdkmanVersion otherSdkmanVersion) {
        final int equal       = 0;
        final int smallerThan = -1;
        final int largerThan  = 1;
        int ret;
        if (feature > otherSdkmanVersion.getFeature()) {
            ret = largerThan;
        } else if (feature < otherSdkmanVersion.getFeature()) {
            ret = smallerThan;
        } else {
            if (interim > otherSdkmanVersion.getInterim()) {
                ret = largerThan;
            } else if (interim < otherSdkmanVersion.getInterim()) {
                ret = smallerThan;
            } else {
                if (update > otherSdkmanVersion.getUpdate()) {
                    ret = largerThan;
                } else if (update < otherSdkmanVersion.getUpdate()) {
                    ret = smallerThan;
                } else {
                    if (patch > otherSdkmanVersion.getPatch()) {
                        ret = largerThan;
                    } else if (patch < otherSdkmanVersion.getPatch()) {
                        ret = smallerThan;
                    } else {
                        if (buildNumber > otherSdkmanVersion.getBuildNumber()) {
                            ret = largerThan;
                        } else if (buildNumber < otherSdkmanVersion.getBuildNumber()) {
                            ret = smallerThan;
                        } else {
                            int cmp = Boolean.compare(this.graal, otherSdkmanVersion.isGraalVM());
                            if (cmp != 0) {
                                ret = cmp < 0 ? smallerThan : largerThan;
                            } else {
                                cmp = Integer.compare(this.target, otherSdkmanVersion.getTarget());
                                if (cmp != 0) {
                                    ret = cmp < 0 ? smallerThan : largerThan;
                                } else {
                                    cmp = Boolean.compare(this.fx, otherSdkmanVersion.hasFX());
                                    if (cmp != 0) {
                                        ret = cmp < 0 ? smallerThan : largerThan;
                                    } else {
                                        cmp = Boolean.compare(this.crac, otherSdkmanVersion.hasCRaC());
                                        ret = cmp < 0 ? smallerThan : (cmp > 0 ? largerThan : equal);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    public boolean isLessThan(final SdkmanVersion versionNumber) { return compareTo(versionNumber) < 0; }
    public boolean isLessThanOrEqualTo(final SdkmanVersion versionNumber) { return compareTo(versionNumber) <= 0; }
    public boolean isGreaterThan(final SdkmanVersion versionNumber) { return compareTo(versionNumber) > 0; }
    public boolean isGreaterThanOrEqualTo(final SdkmanVersion versionNumber) { return compareTo(versionNumber) >= 0; }
    public boolean isEqualTo(final SdkmanVersion versionNumber) { return compareTo(versionNumber) == 0; }
    public boolean isSmallerThan(final SdkmanVersion versionNumber) {
        return compareTo(versionNumber) < 0;
    }
    public boolean isSmallerOrEqualThan(final SdkmanVersion versionNumber) {
        return compareTo(versionNumber) <= 0;
    }
    public boolean isLargerOrEqualThan(final SdkmanVersion versionNumber) {
        return compareTo(versionNumber) >= 0;
    }
    public boolean isLargerThan(final SdkmanVersion versionNumber) {
        return compareTo(versionNumber) > 0;
    }
}
