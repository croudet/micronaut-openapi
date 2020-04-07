/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.openapi.swagger.v3;

import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * OpenAPI extensions.
 *
 * @author croudet
 */
public final class OpenAPIExtensions {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAPIExtensions.class);

    private static List<OpenAPIExtension> extensions = null;

    private OpenAPIExtensions() {

    }

    /**
     * Returns an iterator.
     * @return An iterator.
     */
    public static Iterator<OpenAPIExtension> chain() {
        return extensions.iterator();
    }

    static {
        extensions = new ArrayList<>();
        ServiceLoader<OpenAPIExtension> loader = ServiceLoader.load(OpenAPIExtension.class);
        for (OpenAPIExtension ext : loader) {
            LOGGER.debug("adding extension {}", ext);
            extensions.add(ext);
        }
        extensions.add(new DefaultParameterExtension());
    }
}
