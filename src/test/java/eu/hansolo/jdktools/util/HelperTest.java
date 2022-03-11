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

package eu.hansolo.jdktools.util;

import eu.hansolo.jdktools.TermOfSupport;
import eu.hansolo.jdktools.versioning.VersionNumber;
import org.junit.jupiter.api.Test;


class HelperTest {

    @Test
    void trimPrefixTest() {
        assert Helper.trimPrefix("BlaBlaBlaHanSolo", "BlaBlaBla").equals("HanSolo");
        assert Helper.trimPrefix("BlaBlaBlaHanSolo", "Bla").equals("BlaBlaHanSolo");
    }

    @Test
    void isPositiveIntegerTest() {
        assert Helper.isPositiveInteger("1");
        assert Helper.isPositiveInteger("+10");
        assert !Helper.isPositiveInteger("11.2");
        assert !Helper.isPositiveInteger("-12");
    }


    @Test
    void releaseTermOfSupportTest() {
        assert Helper.isReleaseTermOfSupport(17, TermOfSupport.LTS);
        assert Helper.isReleaseTermOfSupport(15, TermOfSupport.MTS);
        assert Helper.isReleaseTermOfSupport(12, TermOfSupport.STS);
        assert !Helper.isReleaseTermOfSupport(18, TermOfSupport.MTS);
        assert Helper.isReleaseTermOfSupport(21, TermOfSupport.LTS);
        assert Helper.isReleaseTermOfSupport(22, TermOfSupport.STS);
    }

    @Test
    void termOfSupportTest() {
        assert TermOfSupport.LTS == Helper.getTermOfSupport(new VersionNumber(17, 0, 2));
        assert TermOfSupport.MTS == Helper.getTermOfSupport(new VersionNumber(13, 0, 5, 1));
        assert TermOfSupport.STS == Helper.getTermOfSupport(new VersionNumber(16, 0, 2));
        assert TermOfSupport.STS == Helper.getTermOfSupport(new VersionNumber(13, 0, 5, 1), false);
        assert TermOfSupport.MTS == Helper.getTermOfSupport(new VersionNumber(13, 0, 5, 1), true);
        assert TermOfSupport.LTS == Helper.getTermOfSupport(11);
        assert TermOfSupport.MTS == Helper.getTermOfSupport(13);
        assert TermOfSupport.STS == Helper.getTermOfSupport(16);
    }

    @Test
    void isLTSTest() {
        assert Helper.isLTS(7);
        assert Helper.isLTS(8);
        assert !Helper.isLTS(9);
        assert !Helper.isLTS(10);
        assert Helper.isLTS(11);
    }

    @Test
    void isMTSTest() {
        assert Helper.isMTS(13);
        assert Helper.isMTS(15);
        assert Helper.isMTS(23);
        assert !Helper.isMTS(17);
    }

    @Test
    void isSTSTest() {
        assert Helper.isSTS(9);
        assert Helper.isSTS(10);
        assert Helper.isSTS(18);
        assert !Helper.isSTS(17);
    }
}
