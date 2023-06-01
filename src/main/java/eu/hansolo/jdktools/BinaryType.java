package eu.hansolo.jdktools;

import eu.hansolo.jdktools.util.OutputFormat;

import java.util.Arrays;
import java.util.List;

import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.COMMA_NEW_LINE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.INDENTED_QUOTES;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;


public enum BinaryType implements Api {
    PACKAGE("Package", "package", ArchiveType.APK, ArchiveType.CAB, ArchiveType.SRC_TAR, ArchiveType.TAR, ArchiveType.TAR_GZ, ArchiveType.TAR_Z, ArchiveType.TAR_XZ, ArchiveType.TGZ, ArchiveType.ZIP),
    INSTALLER("Installer", "installer", ArchiveType.BIN, ArchiveType.PKG, ArchiveType.DMG, ArchiveType.EXE, ArchiveType.MSI, ArchiveType.DEB, ArchiveType.RPM),
    NONE("-", "", ArchiveType.NONE),
    NOT_FOUND("", "", ArchiveType.NOT_FOUND);

    private final String            uiString;
    private final String            apiString;
    private final List<ArchiveType> archiveTypes;


    BinaryType(final String uiString, final String apiString, final ArchiveType... archiveTypes) {
        this.uiString     = uiString;
        this.apiString    = apiString;
        this.archiveTypes = List.of(archiveTypes);
    }


    @Override public String getUiString() { return uiString; }

    @Override public String getApiString() { return apiString; }

    @Override public BinaryType getDefault() { return BinaryType.NONE; }

    @Override public BinaryType getNotFound() { return BinaryType.NOT_FOUND; }

    @Override public BinaryType[] getAll() { return values(); }

    @Override public String toString(final OutputFormat outputFormat) {
        StringBuilder msgBuilder = new StringBuilder();
        switch(outputFormat) {
            case FULL, REDUCED, REDUCED_ENRICHED ->
                msgBuilder.append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                          .append(INDENTED_QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENTED_QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES).append(NEW_LINE)
                          .append(CURLY_BRACKET_CLOSE);
            default ->
                msgBuilder.append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append("name").append(QUOTES).append(COLON).append(QUOTES).append(name()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("ui_string").append(QUOTES).append(COLON).append(QUOTES).append(uiString).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("api_string").append(QUOTES).append(COLON).append(QUOTES).append(apiString).append(QUOTES)
                          .append(CURLY_BRACKET_CLOSE);
        }
        return msgBuilder.toString();
    }

    @Override public String toString() { return toString(OutputFormat.FULL_COMPRESSED); }

    /**
     * Returns BinaryType parsed from a given text
     * @param text Name of the archive type to parse usually the api_string of an binary type e.g. 'package'
     * @return BinaryType parsed from a given text
     */
    public static BinaryType fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch (text) {
            case "package", "Package", "PACKAGE"       -> { return PACKAGE; }
            case "installer", "Installer", "INSTALLER" -> { return INSTALLER; }
            default                                    -> { return NOT_FOUND; }
        }
    }

    public List<ArchiveType> getArchiveTypes() { return archiveTypes; }

    /**
     * Returns BinaryType parsed from a given filename
     * @param filename Filename from which the binary type should be parsed e.g. 'zulu-18.jdk.tar.gz' -> BinaryType.PACKAGE
     * @return BinaryType determined by a given filename
     */
    public static BinaryType getFromFileName(final String filename) {
        if (null == filename || filename.isEmpty()) { return BinaryType.NONE; }
        for (BinaryType binaryType : values()) {
            for (ArchiveType archiveType : binaryType.getArchiveTypes()) {
                for (String ending : archiveType.getFileEndings()) {
                    if (filename.toLowerCase().endsWith(ending)) { return binaryType; }
                }
            }
        }
        return BinaryType.NONE;
    }

    public static BinaryType getFromArchiveType(final ArchiveType archiveType) {
        for (BinaryType binaryType : values()) {
            if (binaryType.getArchiveTypes().contains(archiveType)) { return binaryType; }
        }
        return BinaryType.NONE;
    }

    /**
     * Returns the values of the enum as list
     * @return the values of the enum as list
     */
    public static List<BinaryType> getAsList() { return Arrays.asList(values()); }
}
