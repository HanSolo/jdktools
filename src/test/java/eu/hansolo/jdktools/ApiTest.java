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

package eu.hansolo.jdktools;


import org.junit.jupiter.api.Test;


public class ApiTest {

    @Test
    public void architectureFromTextTest() {
        assert Architecture.fromText(null) == Architecture.NOT_FOUND;
        assert Architecture.fromText("") == Architecture.NONE;
        assert Architecture.fromText("arm") == Architecture.ARM;
        assert Architecture.fromText("armv6") == Architecture.ARM;
        assert Architecture.fromText("armv7") == Architecture.ARM;
        assert Architecture.fromText("aarch32") == Architecture.AARCH32;
        assert Architecture.fromText("aarch64") == Architecture.AARCH64;
        assert Architecture.fromText("amd64") == Architecture.AMD64;
        assert Architecture.fromText("arm32") == Architecture.ARM32;
        assert Architecture.fromText("arm64") == Architecture.ARM64;
        assert Architecture.fromText("armv8") == Architecture.ARM64;
        assert Architecture.fromText("armel") == Architecture.ARMEL;
        assert Architecture.fromText("armhf") == Architecture.ARMHF;
        assert Architecture.fromText("i386") == Architecture.I386;
        assert Architecture.fromText("i586") == Architecture.I586;
        assert Architecture.fromText("i686") == Architecture.I686;
        assert Architecture.fromText("ia64") == Architecture.IA64;
        assert Architecture.fromText("mips") == Architecture.MIPS;
        assert Architecture.fromText("mipsel") == Architecture.MIPSEL;
        assert Architecture.fromText("ppc") == Architecture.PPC;
        assert Architecture.fromText("ppc64") == Architecture.PPC64;
        assert Architecture.fromText("ppc64le") == Architecture.PPC64LE;
        assert Architecture.fromText("riscv64") == Architecture.RISCV64;
        assert Architecture.fromText("s390x") == Architecture.S390X;
        assert Architecture.fromText("s390") == Architecture.S390X;
        assert Architecture.fromText("sparc") == Architecture.SPARC;
        assert Architecture.fromText("sparcv9") == Architecture.SPARCV9;
        assert Architecture.fromText("x32") == Architecture.X32;
        assert Architecture.fromText("x64") == Architecture.X64;
        assert Architecture.fromText("x86lx64") == Architecture.X64;
        assert Architecture.fromText("x86-64") == Architecture.X64;
        assert Architecture.fromText("x86") == Architecture.X86;
        assert Architecture.fromText("i486") == Architecture.X86;
        assert Architecture.fromText("286") == Architecture.X86;
        assert Architecture.fromText("386") == Architecture.X86;
        assert Architecture.fromText("x86_64") == Architecture.X86_64;
    }

    @Test
    public void archiveTypeFromTextTest() {
        assert ArchiveType.fromText(null) == ArchiveType.NOT_FOUND;
        assert ArchiveType.fromText("") == ArchiveType.NONE;
        assert ArchiveType.fromText("apk") == ArchiveType.APK;
        assert ArchiveType.fromText("bin") == ArchiveType.BIN;
        assert ArchiveType.fromText("cab") == ArchiveType.CAB;
        assert ArchiveType.fromText("deb") == ArchiveType.DEB;
        assert ArchiveType.fromText("dmg") == ArchiveType.DMG;
        assert ArchiveType.fromText("exe") == ArchiveType.EXE;
        assert ArchiveType.fromText("msi") == ArchiveType.MSI;
        assert ArchiveType.fromText("pkg") == ArchiveType.PKG;
        assert ArchiveType.fromText("rpm") == ArchiveType.RPM;
        assert ArchiveType.fromText("src.tar") == ArchiveType.SRC_TAR;
        assert ArchiveType.fromText("tar") == ArchiveType.TAR;
        assert ArchiveType.fromText("tar.gz") == ArchiveType.TAR_GZ;
        assert ArchiveType.fromText("tgz") == ArchiveType.TGZ;
        assert ArchiveType.fromText("tar.z") == ArchiveType.TAR_Z;
        assert ArchiveType.fromText("zip") == ArchiveType.ZIP;
    }

    @Test
    public void bitnessFromTextTest() {
        assert Bitness.fromText(null) == Bitness.NOT_FOUND;
        assert Bitness.fromText("") == Bitness.NONE;
        assert Bitness.fromText("32") == Bitness.BIT_32;
        assert Bitness.fromText("32bit") == Bitness.BIT_32;
        assert Bitness.fromText("64") == Bitness.BIT_64;
        assert Bitness.fromText("64bit") == Bitness.BIT_64;
    }

    @Test
    public void fpuFromTextTest() {
        assert FPU.fromText(null) == FPU.NOT_FOUND;
        assert FPU.fromText("") == FPU.NONE;
        assert FPU.fromText("hfl") == FPU.HARD_FLOAT;
        assert FPU.fromText("hardfloat") == FPU.HARD_FLOAT;
        assert FPU.fromText("hard_float") == FPU.HARD_FLOAT;
        assert FPU.fromText("sfl") == FPU.SOFT_FLOAT;
        assert FPU.fromText("softfloat") == FPU.SOFT_FLOAT;
        assert FPU.fromText("soft_float") == FPU.SOFT_FLOAT;
    }

    @Test
    public void hashAlgorithmFromTextTest() {
        assert HashAlgorithm.fromText(null) == HashAlgorithm.NOT_FOUND;
        assert HashAlgorithm.fromText("") == HashAlgorithm.NONE;
        assert HashAlgorithm.fromText("md5") == HashAlgorithm.MD5;
        assert HashAlgorithm.fromText("sha1") == HashAlgorithm.SHA1;
        assert HashAlgorithm.fromText("sha3_256") == HashAlgorithm.SHA3_256;
        assert HashAlgorithm.fromText("sha_224") == HashAlgorithm.SHA224;
        assert HashAlgorithm.fromText("sha224") == HashAlgorithm.SHA224;
        assert HashAlgorithm.fromText("sha_256") == HashAlgorithm.SHA256;
        assert HashAlgorithm.fromText("sha256") == HashAlgorithm.SHA256;
        assert HashAlgorithm.fromText("sha_384") == HashAlgorithm.SHA384;
        assert HashAlgorithm.fromText("sha384") == HashAlgorithm.SHA384;
        assert HashAlgorithm.fromText("sha_512") == HashAlgorithm.SHA512;
        assert HashAlgorithm.fromText("sha512") == HashAlgorithm.SHA512;
    }

    @Test
    public void latestFromTextTest() {
        assert Latest.fromText(null) == Latest.NOT_FOUND;
        assert Latest.fromText("") == Latest.NONE;
        assert Latest.fromText("all_of_version") == Latest.ALL_OF_VERSION;
        assert Latest.fromText("available") == Latest.AVAILABLE;
        assert Latest.fromText("overall") == Latest.OVERALL;
        assert Latest.fromText("per_distribution") == Latest.PER_DISTRIBUTION;
        assert Latest.fromText("per_version") == Latest.PER_VERSION;
    }

    @Test
    public void libCTypeFromTextTest() {
        assert LibCType.fromText(null) == LibCType.NOT_FOUND;
        assert LibCType.fromText("") == LibCType.NONE;
        assert LibCType.fromText("libc") == LibCType.LIBC;
        assert LibCType.fromText("glibc") == LibCType.GLIBC;
        assert LibCType.fromText("musl") == LibCType.MUSL;
        assert LibCType.fromText("c_std_lib") == LibCType.C_STD_LIB;
    }

    @Test
    public void matchFromTextTest() {
        assert Match.fromText(null) == Match.NOT_FOUND;
        assert Match.fromText("") == Match.NONE;
        assert Match.fromText("any") == Match.ANY;
        assert Match.fromText("all") == Match.ALL;
    }

    @Test
    public void operatingModeFromTextTest() {
        assert OperatingMode.fromText(null) == OperatingMode.NOT_FOUND;
        assert OperatingMode.fromText("") == OperatingMode.NONE;
        assert OperatingMode.fromText("native") == OperatingMode.NATIVE;
        assert OperatingMode.fromText("emulated") == OperatingMode.EMULATED;
    }

    @Test
    public void operatingSystemFromTextTest() {
        assert OperatingSystem.fromText(null) == OperatingSystem.NOT_FOUND;
        assert OperatingSystem.fromText("") == OperatingSystem.NONE;
        assert OperatingSystem.fromText("aix") == OperatingSystem.AIX;
        assert OperatingSystem.fromText("alpine") == OperatingSystem.ALPINE_LINUX;
        assert OperatingSystem.fromText("osx") == OperatingSystem.MACOS;
        assert OperatingSystem.fromText("macos") == OperatingSystem.MACOS;
        assert OperatingSystem.fromText("linux") == OperatingSystem.LINUX;
        assert OperatingSystem.fromText("musl") == OperatingSystem.LINUX_MUSL;
        assert OperatingSystem.fromText("qnx") == OperatingSystem.QNX;
        assert OperatingSystem.fromText("solaris") == OperatingSystem.SOLARIS;
        assert OperatingSystem.fromText("windows") == OperatingSystem.WINDOWS;
        assert OperatingSystem.fromText("win") == OperatingSystem.WINDOWS;
    }

    @Test
    public void packageTypeFromTextTest() {
        assert PackageType.fromText(null) == PackageType.NOT_FOUND;
        assert PackageType.fromText("") == PackageType.NONE;
        assert PackageType.fromText("jdk") == PackageType.JDK;
        assert PackageType.fromText("jre") == PackageType.JRE;
    }

    @Test
    public void releaseStatusFromTextTest() {
        assert ReleaseStatus.fromText(null) == ReleaseStatus.NOT_FOUND;
        assert ReleaseStatus.fromText("") == ReleaseStatus.NONE;
        assert ReleaseStatus.fromText("ea") == ReleaseStatus.EA;
        assert ReleaseStatus.fromText("ga") == ReleaseStatus.GA;
    }

    @Test
    public void severityFromTextTest() {
        assert Severity.fromText(null) == Severity.NOT_FOUND;
        assert Severity.fromText("") == Severity.NONE;
        assert Severity.fromText("low") == Severity.LOW;
        assert Severity.fromText("medium") == Severity.MEDIUM;
        assert Severity.fromText("high") == Severity.HIGH;
        assert Severity.fromText("critical") == Severity.CRITICAL;
    }

    @Test
    public void signatureTypeFromTextTest() {
        assert SignatureType.fromText(null) == SignatureType.NOT_FOUND;
        assert SignatureType.fromText("") == SignatureType.NONE;
        assert SignatureType.fromText("dsa") == SignatureType.DSA;
        assert SignatureType.fromText("ecdsa") == SignatureType.ECDSA;
        assert SignatureType.fromText("rsa") == SignatureType.RSA;
        assert SignatureType.fromText("eddsa") == SignatureType.EDDSA;
    }

    @Test
    public void termOfSupportFromTextTest() {
        assert TermOfSupport.fromText(null) == TermOfSupport.NOT_FOUND;
        assert TermOfSupport.fromText("") == TermOfSupport.NONE;
        assert TermOfSupport.fromText("lts") == TermOfSupport.LTS;
        assert TermOfSupport.fromText("mts") == TermOfSupport.MTS;
        assert TermOfSupport.fromText("sts") == TermOfSupport.STS;
    }

    @Test
    public void verificationFromTextTest() {
        assert Verification.fromText(null) == Verification.NOT_FOUND;
        assert Verification.fromText("") == Verification.NONE;
        assert Verification.fromText("yes") == Verification.YES;
        assert Verification.fromText("no") == Verification.NO;
        assert Verification.fromText("true") == Verification.YES;
        assert Verification.fromText("false") == Verification.NO;
    }
}
