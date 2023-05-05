package eu.hansolo.jdktools;

public enum CVSS {
    CVSSV2, CVSSV3, NOT_FOUND;

    public static CVSS fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch (text) {
            case "CVSSV2", "cvssV2", "cvssv2" -> { return CVSSV2; }
            case "CVSSV3", "cvssV3", "cvssv3" -> { return CVSSV3; }
            default                           -> { return NOT_FOUND; }
        }
    }
}
