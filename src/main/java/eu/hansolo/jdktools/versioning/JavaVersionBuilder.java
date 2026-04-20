package eu.hansolo.jdktools.versioning;


public class JavaVersionBuilder {
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



    public static JavaVersionBuilder create() {
        return new JavaVersionBuilder();
    }

    public JavaVersionBuilder feature(final int feature) {
        this.feature = feature;
        return this;
    }

    public JavaVersionBuilder interim(final int interim) {
        this.interim = interim;
        return this;
    }

    public JavaVersionBuilder update(final int update) {
        this.update = update;
        return this;
    }

    public JavaVersionBuilder patch(final int patch) {
        this.patch = patch;
        return this;
    }

    public JavaVersionBuilder pre(final String pre) {
        this.pre         = (pre == null || pre.isEmpty()) ? "" : pre.replaceFirst("-", "");
        this.earlyAccess = !this.pre.isEmpty();
        return this;
    }

    public JavaVersionBuilder meta(final String meta) {
        this.meta = (meta == null || meta.isEmpty()) ? "" : meta.replaceFirst("\\+", "");
        return this;
    }

    public JavaVersionBuilder earlyAccess(final boolean earlyAccess) {
        this.earlyAccess = earlyAccess;
        return this;
    }

    public JavaVersionBuilder graal(final boolean graal) {
        this.graal = graal;
        return this;
    }

    public JavaVersionBuilder target(final int target) {
        this.target = target;
        return this;
    }

    public JavaVersionBuilder fx(final boolean fx) {
        this.fx = fx;
        return this;
    }

    public JavaVersionBuilder crac(final boolean crac) {
        this.crac = crac;
        return this;
    }

    public JavaVersionBuilder buildNumber(final int build) {
        this.build = build;
        return this;
    }

    public JavaVersion build() {
        return new JavaVersion(feature, interim, update, patch, pre, meta, earlyAccess, graal, target, fx, crac, build);
    }
}