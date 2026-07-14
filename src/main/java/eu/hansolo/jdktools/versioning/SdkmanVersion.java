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
    private static final Pattern            VERSION_NUMBER_PATTERN = Pattern.compile("^([1-9]\\d*)(\\.(\\d+)\\.(\\d+)(?:\\.(\\d+))?)(\\-(fx)(\\.crac)?)?(\\-(crac)(\\.fx)?)?(\\+ea(\\.(\\d+))?(\\.r(\\d+))?)?(\\+(\\d+)(\\.r(\\d+))?)?(\\+r(\\d+))?$");
    private static final Pattern            INT_PATTERN            = Pattern.compile("\\d+");
    private static final Pattern            ARCHIVE_TYPE_PATTERN;
    private static final Pattern            OS_ACRONYM_PATTERN;
    private static final Pattern            ARCH_ACRONYM_PATTERN;

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
        this.pre     = pre  == null ? "" : pre;//pre.replaceFirst("-", "");
        this.meta    = meta == null ? "" : meta;//meta.replaceFirst("\\+", "");

        // Additional information
        this.earlyAccess  = earlyAccess;
        this.graal        = graal;
        this.target       = target;
        this.fx           = fx;
        this.crac         = crac;
        this.buildNumber  = buildNumber;
        this.majorVersion = new SimpleMajorVersion(feature);

        boolean hasBuild = buildNumber != 0;
        if (this.meta.isEmpty()) {
            boolean       hasMetaData = fx | crac;
            StringBuilder metaBuilder = new StringBuilder();
            if (hasMetaData) {
                if (fx && crac) {
                    metaBuilder.append("-crac.fx");
                } else if (fx) {
                    metaBuilder.append("-fx");
                } else if (crac) {
                    metaBuilder.append("-crac");
                }
            }
            this.meta = metaBuilder.toString();
        }

        if (this.pre.isEmpty()) {
            StringBuilder preBuilder = new StringBuilder();
            if (earlyAccess) {
                preBuilder.append("+ea");
                preBuilder.append(hasBuild ? "." + buildNumber : "");
                preBuilder.append(graal ? ".r" + target : "");
            } else if (hasBuild) {
                preBuilder.append("+").append(buildNumber);
                preBuilder.append(graal ? ".r" + target : "");
            } else if (graal) {
                preBuilder.append("+r").append(target);
            }
            this.pre = preBuilder.toString();
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

        final Matcher matcher = VERSION_NUMBER_PATTERN.matcher(version);

        if (matcher.find()) {
            final MatchResult result = matcher.toMatchResult();
            if (null != result.group(1)) {
                int    feature      = Integer.parseInt(result.group(1));
                int    interim      = result.group(3)  != null ? Integer.parseInt(result.group(3)) : 0;
                int    update       = result.group(4)  != null ? Integer.parseInt(result.group(4)) : 0;
                int    patch        = result.group(5)  != null ? Integer.parseInt(result.group(5)) : 0;
                String  pre         = "";
                String  meta        = "";
                int     build       = 0;
                boolean earlyAccess = false;
                boolean graal       = false;
                int     target      = 0;
                boolean fx          = false;
                boolean crac        = false;

                if (result.group(6) != null) {
                    meta = result.group(6);
                    fx   = result.group(7) != null;
                    crac = result.group(8) != null;
                } else if (result.group(9) != null) {
                    meta = result.group(9);
                    crac = result.group(10) != null;
                    fx   = result.group(11) != null;
                }
                earlyAccess = result.group(12) != null;

                if (result.group(13) != null) {
                    pre   = result.group(13);
                    build = result.group(14) != null ? Integer.parseInt(result.group(14)) : 0;
                }

                if (result.group(17) != null) {
                    build = result.group(18) != null ? Integer.parseInt(result.group(18)) : 0;
                }

                if (result.group(15) != null && result.group(16) != null) {
                    graal  = true;
                    target = Integer.parseInt(result.group(16));
                } else if (result.group(19) != null && result.group(20) != null) {
                    graal  = true;
                    target = Integer.parseInt(result.group(20));
                } else if (result.group(21) != null) {
                    graal = true;
                    target = Integer.parseInt(result.group(22));
                }

                boolean hasBuild    = build != 0;
                boolean hasMetaData = fx | crac;
                StringBuilder metaBuilder = new StringBuilder();
                if (hasMetaData) {
                    if (fx && crac) {
                        metaBuilder.append("-crac.fx");
                    } else if (fx) {
                        metaBuilder.append("-fx");
                    } else if (crac) {
                        metaBuilder.append("-crac");
                    }
                }
                meta = metaBuilder.toString();

                StringBuilder preBuilder = new StringBuilder();
                if (earlyAccess) {
                    preBuilder.append("+ea");
                    preBuilder.append(hasBuild ? "." + build : "");
                    preBuilder.append(graal ? ".r" + target : "");
                } else if (hasBuild) {
                    preBuilder.append("+").append(build);
                    preBuilder.append(graal ? ".r" + target : "");
                } else if (graal) {
                    preBuilder.append("+r").append(target);
                }
                pre = preBuilder.toString();

                return new SdkmanVersion(feature, interim, update, patch, pre, meta, earlyAccess, graal, target, fx, crac, build);
            } else {
                System.out.println("No valid Sdkman Version found (" +  text + ")");
                return null;
            }
        }
        System.out.println("No valid Sdkman Version found (" +  text + ")");
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
            if (this.interim == other.getInterim()) {
                if (this.update == other.getUpdate()) {
                    if (this.patch == other.getPatch()) {
                        if (this.buildNumber == other.getBuildNumber()) {
                            isEqual = true;
                        } else {
                            isEqual = false;
                        }
                    } else {
                        isEqual = false;
                    }
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = false;
            }
        } else {
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
            if (!this.meta.isEmpty()) { versionBuilder.append(this.meta); }
            if (!this.pre.isEmpty())  { versionBuilder.append(this.pre); }
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
