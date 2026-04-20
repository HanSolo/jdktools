package eu.hansolo.jdktools.versioning;

import eu.hansolo.jdktools.Architecture;
import eu.hansolo.jdktools.ArchiveType;
import eu.hansolo.jdktools.OperatingSystem;

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JavaVersion implements Comparable<JavaVersion> {
    private static final Pattern VERSION_NUMBER_PATTERN = Pattern.compile("^([1-9]\\d*)(\\.(\\d+)\\.(\\d+)(?:\\.(\\d+))?)((-[a-zA-Z]+)(\\.(\\d+))?)?(\\+(fx|crac|r\\d+|\\d+\\.r\\d+|\\d+))?$");
    private static final Pattern INT_PATTERN            = Pattern.compile("\\d+");
    private              int     feature;
    private              int     interim;
    private              int     update;
    private              int     patch;
    private              String  pre;
    private              String  meta;
    private              boolean earlyAccess;
    private              boolean graal;
    private              boolean fx;
    private              boolean crac;
    private              int     buildNumber;
    private              int     target;


    // ******************** Constructors **************************************
    public JavaVersion() {
        this(1, 0, 0, 0, "", "");
    }
    public JavaVersion(JavaVersion javaVersion) {
        this(javaVersion.getFeature(), javaVersion.getInterim(), javaVersion.getUpdate(), javaVersion.getPatch(), javaVersion.getPre(), javaVersion.getMeta());
    }
    public JavaVersion(final int feature) {
        this(feature, 0, 0, 0, "", "");
    }
    public JavaVersion(final int feature, final int interim, final int update) {
        this(feature, interim, update, 0, "","");
    }
    public JavaVersion(final int feature, final int interim, final int update, final int patch) throws IllegalArgumentException {
        this(feature, interim, update, patch, "", "");
    }
    public JavaVersion(final int feature, final int interim, final int update, final int patch, final String pre, final String meta) {
        this(feature, interim, update, patch, pre, meta, (null != pre && !pre.isEmpty()), false, 0, false, false, 0);
    }
    JavaVersion(final int feature, final int interim, final int update, final int patch, final String pre, final String meta, final boolean earlyAccess, final boolean graal, final int target, final boolean fx, final boolean crac, final int buildNumber) {
        if (feature < 1) { throw new IllegalArgumentException("Feature version must be greater than 0"); }
        if (interim < 0) { throw new IllegalArgumentException("Interim version cannot be smaller than 0"); }
        if (update  < 0) { throw new IllegalArgumentException("Update version cannot be smaller than 0"); }
        if (patch   < 0) { throw new IllegalArgumentException("Patch version cannot be smaller than 0"); }

        this.feature = feature;
        this.interim = interim;
        this.update  = update;
        this.patch   = patch;
        this.pre     = pre  == null || pre.isEmpty()  ? "" : pre.replaceFirst("-", "");
        this.meta    = meta == null || meta.isEmpty() ? "" : meta.replaceFirst("\\+", "");

        // Additional information
        this.earlyAccess = earlyAccess;
        this.graal       = graal;
        this.target      = target;
        this.fx          = fx;
        this.crac        = crac;
        this.buildNumber = buildNumber;

        if (this.pre.isEmpty()) {
            if (this.earlyAccess) {
                this.pre = this.buildNumber > 0 ? "ea." + this.buildNumber : "ea";
            }
        }

        if (this.meta.isEmpty()) {
            if (this.graal) {
                this.meta = this.buildNumber > 0 ? this.buildNumber + "." + (this.target > 0 ? "r" + target : "r") : (this.target > 0 ? "r" + target : "r");
            } else if (this.buildNumber > 0 && this.pre.isEmpty()) {
                this.meta = Integer.toString(this.buildNumber);
            } else if (this.fx) {
                this.meta = "fx";
            } else if (this.crac) {
                this.meta = "crac";
            }
        }
    }
    


    // ******************** Methods *******************************************
    public int getFeature() { return this.feature; }
    public void setFeature(final Integer feature) throws IllegalArgumentException {
        if (feature < 1) { throw new IllegalArgumentException("Feature version must be greater than 0 (" + feature + ")"); }
        this.feature = feature;
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
        this.earlyAccess = !pre.isEmpty();
    }

    public String getMeta() { return this.meta; }
    public void setMeta(final String meta) {
        this.meta = meta == null || meta.isEmpty() ? "" : meta.replaceFirst("\\+", "");
    }

    public SimpleMajorVersion getMajorVersion() { return new SimpleMajorVersion(this.feature); }

    public String getNormalizedJavaVersion() {
        return new StringBuilder().append(feature).append(".")
                                  .append(interim).append(".")
                                  .append(update).append(".")
                                  .append(patch)
                                  .append(this.pre.isEmpty()  ? "" : ("-" + this.pre))
                                  .append(this.meta.isEmpty() ? "" : ("+" + this.meta))
                                  .toString();
    }

    public static JavaVersion fromText(final String text) throws IllegalArgumentException {
        return fromText(text, 0, true);
    }

    // Additional convenience methods
    public boolean isEarlyAccess() { return this.earlyAccess; }

    public boolean isGraalVM() { return this.graal; }

    public int getTarget() { return this.target; }

    public boolean hasFX() { return this.fx; }

    public boolean hasCRaC() { return this.crac; }

    public int getBuildNumber() { return this.buildNumber; }

    /**
     * Returns a version number parsed from the given text. If the matcher finds more than 1 result, the
     * resultToMatch variable will be taken into account. For example if the given text matches 2 times,
     * the resultToMatch variable defines which result should be taken to parse the version number.
     * @param text           Text to parse
     * @param resultToMatch  The result that should be taken for parsing if there are more than 1
     * @return Returns a version number parsed from the given text
     * @throws IllegalArgumentException Throws IllegalArgumentException in case the given text was null or empty
     */
    public static JavaVersion fromText(final String text, final int resultToMatch) throws IllegalArgumentException {
        return fromText(text, resultToMatch, true);
    }
    public static JavaVersion fromText(final String text, final int resultToMatch, final boolean isJavaVersion) throws IllegalArgumentException {
        if (null == text || text.isEmpty()) {
            throw new IllegalArgumentException("No version number can be parsed because given text is null or empty.");
        }

        // Remove things like cpu architecture, operating system and file endings
        String tmp = text;
        for (ArchiveType archiveType : ArchiveType.getAsList()) {
            for (String fileEnding : archiveType.getFileEndings()) {
                if (fileEnding.isEmpty() || fileEnding.equals("-") || fileEnding.equals("_")) { continue; }
                tmp = tmp.replaceAll(Pattern.quote(fileEnding), "");
            }
        }
        for (OperatingSystem operatingSystem : OperatingSystem.getAsList()) {
            for (String acronym : OperatingSystem.getAcronyms(operatingSystem)) {
                if (acronym.isEmpty()) { continue; }
                tmp = tmp.replaceAll("\\-" + acronym, "");
                tmp = tmp.replaceAll("_" + acronym, "");
            }
        }
        for (Architecture architecture : Architecture.getAsList()) {
            for (String acronym : Architecture.getAcronyms(architecture)) {
                if (acronym.isEmpty()) { continue; }
                tmp = tmp.replaceAll("\\-" + acronym, "");
                tmp = tmp.replaceAll("_" + acronym, "");
            }
        }

        // Remove leading "1." to get correct version number e.g. 1.8u262 -> 8u262
        String version = tmp;
        String[] tmpParts = tmp.split("\\.");
        if (isJavaVersion && tmpParts.length > 1) {
            if (tmpParts[0].equals("1") && Integer.parseInt(tmpParts[1].substring(0, 1)) <= 8) {
                version = tmp.startsWith("1.") ? tmp.replace("1.", "") : tmp;
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
                boolean earlyAccess = !pre.isEmpty();
                boolean graal       = false;
                int     target      = 0;
                boolean fx          = false;
                boolean crac        = false;

                if (result.group(6) != null && !result.group(6).isEmpty()) {
                    pre   = result.group(6) != null ? result.group(6)                  : "";  // -ea.23
                    build = result.group(9) != null ? Integer.valueOf(result.group(9)) : 0;   // 23
                }

                if (result.group(10) != null && !result.group(10).isEmpty()) {
                    meta         = result.group(10); // +fx, +crac, +r25, +1.r17, +1
                    String  feat = result.group(11) != null ? result.group(11) : ""; // 1, fx, crac, r25, 1.r17, 1
                    build        = (build == 0 && INT_PATTERN.matcher(feat).matches()) ? Integer.parseInt(feat) : 0;
                    crac         = feat.equals("crac");
                    fx           = feat.equals("fx");
                    graal        = feat.matches("r\\d+");
                    target       = graal ? Integer.valueOf(feat.substring(1)) : 0;
                    boolean comb = feat.matches("\\d+\\.r\\d+");
                    if (comb) {
                        target = Integer.valueOf(feat.substring(feat.indexOf('r') + 1));
                    }
                }
                return new JavaVersion(feature, interim, update, patch, pre, meta, earlyAccess, graal, target, fx, crac, build);
            } else {
                System.out.println("No valid JavaVersion found");
                return null;
            }
        }
        System.out.println("No valid JavaVersion found");
        return null;
    }

    @Override public boolean equals(final Object obj) {
        if (obj == JavaVersion.this) { return true; }
        if (!(obj instanceof JavaVersion)) { return false; }
        JavaVersion other = (JavaVersion) obj;
        boolean isEqual;
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
            isEqual = this.fx == other.hasFX();
            if (isEqual) {
                isEqual = this.graal == other.isGraalVM();
                if (isEqual) {
                    isEqual = this.target == other.getTarget();
                } else {
                    isEqual = false;
                }
                if (isEqual) {
                    isEqual = this.crac == other.hasCRaC();
                }
            }
        }

        return isEqual;
    }

    @Override public int hashCode() {
        return Objects.hash(feature, interim, update, patch, graal, fx, crac, target);
    }

    @Override public String toString() {
        return toString(false, true);
    }
    public String toString(final boolean javaFormat, final boolean includePreAndMeta) {
        final StringBuilder versionBuilder = new StringBuilder().append(feature).append(".").append(interim).append(".").append(update);
        if (javaFormat)        { versionBuilder.append(".").append(patch); }
        if (includePreAndMeta) {
            if (!this.pre.isEmpty())  { versionBuilder.append("-").append(this.pre); }
            if (!this.meta.isEmpty()) { versionBuilder.append("+").append(this.meta); }
        }
        return versionBuilder.toString();
    }

    @Override public int compareTo(final JavaVersion otherJavaVersion) {
        final int equal       = 0;
        final int smallerThan = -1;
        final int largerThan  = 1;
        int ret;
        if (feature > otherJavaVersion.getFeature()) {
            ret = largerThan;
        } else if (feature < otherJavaVersion.getFeature()) {
            ret = smallerThan;
        } else {
            if (interim > otherJavaVersion.getInterim()) {
                ret = largerThan;
            } else if (interim < otherJavaVersion.getInterim()) {
                ret = smallerThan;
            } else {
                if (update > otherJavaVersion.getUpdate()) {
                    ret = largerThan;
                } else if (update < otherJavaVersion.getUpdate()) {
                    ret = smallerThan;
                } else {
                    if (patch > otherJavaVersion.getPatch()) {
                        ret = largerThan;
                    } else if (patch < otherJavaVersion.getPatch()) {
                        ret = smallerThan;
                    } else {
                        if (buildNumber > otherJavaVersion.getBuildNumber()) {
                            ret = largerThan;
                        } else if (buildNumber < otherJavaVersion.getBuildNumber()) {
                            ret = smallerThan;
                        } else {
                            ret = equal;
                        }
                    }
                }
            }
        }
        return ret;
    }

    public boolean isLessThan(final JavaVersion versionNumber) { return compareTo(versionNumber) < 0; }
    public boolean isLessThanOrEqualTo(final JavaVersion versionNumber) { return compareTo(versionNumber) <= 0; }
    public boolean isGreaterThan(final JavaVersion versionNumber) { return compareTo(versionNumber) > 0; }
    public boolean isGreaterThanOrEqualTo(final JavaVersion versionNumber) { return compareTo(versionNumber) >= 0; }

    public boolean isSmallerThan(final JavaVersion versionNumber) {
        return compareTo(versionNumber) < 0;
    }
    public boolean isSmallerOrEqualThan(final JavaVersion versionNumber) {
        return compareTo(versionNumber) <= 0;
    }
    public boolean isLargerOrEqualThan(final JavaVersion versionNumber) {
        return compareTo(versionNumber) >= 0;
    }
    public boolean isLargerThan(final JavaVersion versionNumber) {
        return compareTo(versionNumber) > 0;
    }
}
