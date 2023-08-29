## JDKTools

JDKTools is a collection of classes that can be helpful when working with OpenJDK related things.

It contains classes like:
- Architecture (e.g. AARCH64, X64 etc.)
- Bitness (BIT_32, BIT64, etc.)
- OperatingSystem (MACOS, WINDOWS, LINUX etc.)
- ArchiveType (ZIP, TAR_GZ, DMG, PKG, etc.)
- FPU (HARD_FLOAT, SOFT_FLOAT, etc.)
- HashAlgorithm (MD5, SHA256, etc.)
- LibCType (LIBC, GLIBC, etc.)
- ReleaseStatus (EA, GA)
- PackageType (JDK, JRE)
- SignatureType (RSA, DSA, etc.)
- TermOfSupport (LTS, MTS, STS)

and others.

In addition it contains different kind of scopes that could be used for filtering like:
- BuildScope (BUILD_OF_OPEN_JDK, BUILD_OF_GRAALVM)
- DownloadScope (DIRECTLY, NOT_DIRECTLY)
- QualityScope (TCK_TESTED, AQAVIT_CERTIFIED)
- SignatureScope (SIGNATURE_AVAILABLE, SIGNATURE_NOT_AVAILABLE)
- UsageScope (FREE_TO_USE_IN_PRODUCTION, LICENSE_NEEDED_FOR_PRODUCTION)


Furthermore there are classes to represent version numbers:
- Semver 
- VersionNumber


The idea is to have a common set of classes that can be used in different libraries.