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

import org.junit.jupiter.api.Test;


class ComparisonTest {
    @Test
    void comparisonFormTextTest() {
        Comparison comparison = Comparison.fromText("\u003c");
        assert comparison.getOperator().equals(Comparison.LESS_THAN.getOperator());

        comparison = Comparison.fromText("\u003c\u003d");
        assert comparison.getOperator().equals(Comparison.LESS_THAN_OR_EQUAL.getOperator());

        comparison = Comparison.fromText("\u003d");
        assert comparison.getOperator().equals(Comparison.EQUAL.getOperator());

        comparison = Comparison.fromText("\u003e\u003d");
        assert comparison.getOperator().equals(Comparison.GREATER_THAN_OR_EQUAL.getOperator());

        comparison = Comparison.fromText("\u003e");
        assert comparison.getOperator().equals(Comparison.GREATER_THAN.getOperator());

        comparison = Comparison.fromText("...");
        assert comparison.getOperator().equals(Comparison.RANGE_INCLUDING.getOperator());

        comparison = Comparison.fromText("..\u003c");
        assert comparison.getOperator().equals(Comparison.RANGE_EXCLUDING_TO.getOperator());

        comparison = Comparison.fromText("\u003e..");
        assert comparison.getOperator().equals(Comparison.RANGE_EXCLUDING_FROM.getOperator());

        comparison = Comparison.fromText("\u003e.\u003c");
        assert comparison.getOperator().equals(Comparison.RANGE_EXCLUDING.getOperator());
    }
}
