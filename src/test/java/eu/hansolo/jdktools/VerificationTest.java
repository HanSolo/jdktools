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
import static org.junit.jupiter.api.Assertions.*;


public class VerificationTest {

    @Test
    public void testVerificationFromText() {
        String tck_tested0 = null;
        String tck_tested1 = "";
        String tck_tested2 = "yes";
        String tck_tested3 = "no";
        String tck_tested4 = "true";
        String tck_tested5 = "false";

        assert Verification.fromText(tck_tested0) == Verification.NOT_FOUND;
        assert Verification.fromText(tck_tested1) == Verification.NONE;
        assert Verification.fromText(tck_tested2) == Verification.YES;
        assert Verification.fromText(tck_tested3) == Verification.NO;
        assert Verification.fromText(tck_tested4) == Verification.YES;
        assert Verification.fromText(tck_tested5) == Verification.NO;
    }
}
