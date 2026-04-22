package eu.hansolo.jdktools.versioning;


public class SdkmanVersionBuilder {
    private int     feature     = 1;
    private int     interim     = 0;
    private int     update      = 0;
    private int     patch       = 0;
    private String  pre         = "";
    private String  meta        = "";
    private boolean earlyAccess = false;
    private boolean graal       = false;
    private int     target      = 0;
    private boolean fx          = false;
    private boolean crac        = false;
    private int     build       = 0;


    private SdkmanVersionBuilder(final int feature) {
        this.feature = feature;
    }

    public static SdkmanVersionBuilder feature(final int feature) {
        return new SdkmanVersionBuilder(feature);
    }


    public SdkmanVersionBuilder interim(final int interim) {
        this.interim = interim;
        return this;
    }

    public SdkmanVersionBuilder update(final int update) {
        this.update = update;
        return this;
    }

    public SdkmanVersionBuilder patch(final int patch) {
        this.patch = patch;
        return this;
    }

    public SdkmanVersionBuilder pre(final String pre) {
        this.pre         = (pre == null || pre.isEmpty()) ? "" : pre.replaceFirst("-", "");
        this.earlyAccess = !this.pre.isEmpty();
        return this;
    }

    public SdkmanVersionBuilder meta(final String meta) {
        this.meta = (meta == null || meta.isEmpty()) ? "" : meta.replaceFirst("\\+", "");
        return this;
    }

    public SdkmanVersionBuilder earlyAccess(final boolean earlyAccess) {
        this.earlyAccess = earlyAccess;
        return this;
    }

    public SdkmanVersionBuilder graal(final boolean graal) {
        this.graal = graal;
        return this;
    }

    public SdkmanVersionBuilder target(final int target) {
        this.target = target;
        return this;
    }

    public SdkmanVersionBuilder fx(final boolean fx) {
        this.fx = fx;
        return this;
    }

    public SdkmanVersionBuilder crac(final boolean crac) {
        this.crac = crac;
        return this;
    }

    public SdkmanVersionBuilder buildNumber(final int build) {
        this.build = build;
        return this;
    }

    public SdkmanVersion build() {
        return new SdkmanVersion(feature, interim, update, patch, pre, meta, earlyAccess, graal, target, fx, crac, build);
    }
}