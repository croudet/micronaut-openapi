/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.openapi.annotation;

import io.swagger.v3.oas.annotations.media.ExampleObject;

/**
 * Changes the Retention Policy of the annotation to SOURCE.
 *
 * @since 2.0
 * @author croudet
 */
public class ExampleObjectMappingRetentionPolicyAnnotationTransformer extends AbstractRetentionPolicyAnnotationTransformer<ExampleObject> {

    /**
     * Changes the Retention Policy of the annotation to SOURCE.
     */
    public ExampleObjectMappingRetentionPolicyAnnotationTransformer() {
        super(ExampleObject.class);
    }
}
