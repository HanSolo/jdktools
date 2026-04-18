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
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JavaVersion implements Comparable<JavaVersion> {
    public static final Pattern VERSION_NO_PATTERN   = Pattern.compile("^([1-9]\\d*)(\\.?(\\d+)?\\.?(\\d+)?\\.?(\\d+)?)(\\-ea(\\.?(\\d+)?))?(\\+(fx|crac|r\\d+|\\d+\\.r\\d+|\\d+))?");
    private             int     feature; // major
    private             int     interim; // minor
    private             int     update;  // patch
    private             int     patch;
    private             String  pre;
    private             String  meta;
    private             boolean graalVM;
    private             boolean fx;
    private             boolean crac;
    private             int     buildNumber;


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
    public JavaVersion(final int feature, final int interim, final int update, final int patch, final String pre,  final String meta) {
        Objects.requireNonNull(feature, "Feature version cannot be null");
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

        // TODO: Check pre and meta with Regex for graalvm, fx, crac and build number and set the variables

        // Additional information
        this.graalVM     = this.pre.isEmpty() ? false : ;
        this.fx          = this.pre.isEmpty() ? false : ;
        this.crac        = this.pre.isEmpty() ? false : ;
        this.buildNumber = this.pre.isEmpty() ?   0   : ;
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
        this.pre = pre == null || pre.isEmpty() ? "" : pre.replaceFirst("-", "");

        // TODO: Check with Regex for graalvm, fx, crac and build number and set the variables
    }

    public String getMeta() { return this.meta; }
    public void setMeta(final String meta) {
        this.meta = meta == null || meta.isEmpty() ? "" : meta.replaceFirst("\\+", "");

        // TODO: Check with Regex for graalvm, fx, crac and build number and set the variables
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
    public boolean isGraalVM() { return this.graalVM; }

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
        tmp.set(tmp.get().replaceAll("_b", "+b"));
        tmp.set(tmp.get().replaceAll("\\-b", "+b"));
        tmp.set(tmp.get().replaceAll("\\.b", "+b"));
        tmp.set(tmp.get().replaceAll("([0-9])b", "$1+b"));
        tmp.set(tmp.get().replaceAll("(ea|EA)\\.([0-9]+)$", "ea+b$2"));
        tmp.set(tmp.get().replaceAll("\\-([0-9]+)$", "+$1"));
        tmp.set(tmp.get().replaceAll("_openj9.*", ""));
        tmp.set(tmp.get().replaceAll("\\-openj9.*", ""));
        tmp.set(tmp.get().replaceAll("\\-LTS|\\-lts", ""));

        //System.out.println("stripped: " + tmp.get());

        // Remove leading "1." to get correct version number e.g. 1.8u262 -> 8u262
        String version = tmp.get();
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
        if (noOfResults > 0) {
            MatchResult result = results.get(resultToTake);
            if (null != result.group(1)) {
                int feature    = Integer.valueOf(result.group(1));
                int interim    = result.group(3)  != null ? Integer.valueOf(result.group(3)) : 0;
                int update     = result.group(4)  != null ? Integer.valueOf(result.group(4)) : 0;
                int patch      = result.group(5)  != null ? Integer.valueOf(result.group(5)) : 0;

                String pre     = result.group(6)  != null ? result.group(6) : "";  // -ea
                String build   = result.group(7)  != null ? result.group(7) : "";  // .23
                int    bNumber = result.group(8)  != null ? Integer.valueOf(result.group(8)) : 0;

                // TODO: Set build in JavaVersion -> parse pre in separate method
                System.out.println("build: " + bNumber);
                String meta    = "";
                if (result.group(9) != null) {
                    meta           = result.group(9); // +fx, +crac, +r25, +1.r17, +1
                    String  feat   = result.group(10) != null ? result.group(10) : ""; // fx, crac, r25, 1.r17, 1
                    boolean crac   = feat.equals("crac");
                    boolean fx     = feat.equals("fx");
                    boolean graal  = feat.matches("r\\d+");
                    int     target = graal ? Integer.valueOf(feat.substring(1)) : -1;
                    boolean comb   = feat.matches("\\d+\\.r\\d+");
                    if (comb) {
                        patch  = Integer.valueOf(feat.substring(0, feat.indexOf('.')));
                        target = Integer.valueOf(feat.substring(feat.indexOf('r') + 1));
                    }
                    // TODO: Set these values in the JavaVersion -> parse meta in separate method
                    System.out.println("crac  : " + crac);
                    System.out.println("fx    : " + fx);
                    System.out.println("graal : " + graal);
                    System.out.println("target: " + target);
                    System.out.println("patch : " + patch);
                }

                return new JavaVersion(feature, interim, update, patch, pre, meta);
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
        if (feature == other.getFeature()) {
            if (interim == other.getInterim()) {
                if (update == other.getUpdate()) {
                    if (patch == other.getPatch()) {
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
        return isEqual;
    }

    public String toString(final boolean javaFormat, final boolean includePreAndMeta) {
        final StringBuilder versionBuilder = new StringBuilder().append(feature).append(".").append(interim).append(".").append(update);
        if (javaFormat)        { versionBuilder.append(".").append(patch); }
        if (includePreAndMeta) {
            if (!pre.isEmpty())  { versionBuilder.append("-").append(pre); }
            if (!meta.isEmpty()) { versionBuilder.append("+").append(meta); }
        }
        return versionBuilder.toString();
    }

    @Override public String toString() {
        return toString(false, true);
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
                        ret = equal;
                    }
                }
            }
        }

        if (ret == equal) {
            if (!pre.isEmpty()) {
                if (!otherJavaVersion.getPre().isEmpty()) {
                    ret = Integer.compare(getBuildNumber(), otherJavaVersion.getBuildNumber());
                } else {
                    ret = largerThan;
                }
            } else if (!otherJavaVersion.getPre().isEmpty()) {
                ret = smallerThan;
            }
        }

        if (!meta.isEmpty()) {
            // Check for build number
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
