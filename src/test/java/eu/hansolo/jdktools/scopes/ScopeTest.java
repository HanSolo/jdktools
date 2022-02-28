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

package eu.hansolo.jdktools.scopes;

import org.junit.jupiter.api.Test;


class ScopeTest {
    @Test
    void scopeFromTextTest() {
        Scope scope = BasicScope.fromText("public");
        assert scope instanceof BasicScope;

        scope = BuildScope.fromText("build_of_openjdk");
        assert scope instanceof BuildScope;
        assert scope.getApiString().equals(BuildScope.BUILD_OF_OPEN_JDK.getApiString());

        scope = BuildScope.fromText("BuildOfOpenJDK");
        assert scope instanceof BuildScope;
        assert scope.getApiString().equals(BuildScope.BUILD_OF_OPEN_JDK.getApiString());

        scope = BuildScope.fromText("buildofgraalvm");
        assert scope instanceof BuildScope;
        assert scope.getApiString().equals(BuildScope.BUILD_OF_GRAALVM.getApiString());

        scope = DownloadScope.fromText("not_directly_downloadable");
        assert scope instanceof DownloadScope;
        assert scope.getApiString().equals(DownloadScope.NOT_DIRECTLY.getApiString());

        scope = QualityScope.fromText("tck_tested");
        assert scope instanceof QualityScope;
        assert scope.getApiString().equals(QualityScope.TCK_TESTED.getApiString());

        scope = SignatureScope.fromText("signatureAvailable");
        assert scope instanceof SignatureScope;
        assert scope.getApiString().equals(SignatureScope.SIGNATURE_AVAILABLE.getApiString());

        scope = UsageScope.fromText("free_to_use");
        assert scope instanceof UsageScope;
        assert scope.getApiString().equals(UsageScope.FREE_TO_USE_IN_PRODUCTION.getApiString());
    }
}
