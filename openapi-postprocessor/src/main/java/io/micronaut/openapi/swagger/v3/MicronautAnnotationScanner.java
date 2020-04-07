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

import io.micronaut.http.annotation.*;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.integration.IgnoredPackages;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiScanner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Micronaut annotation scanner.
 *
 * @author croudet
 *
 * @param <T> Scanner type.
 */
public class MicronautAnnotationScanner<T extends MicronautAnnotationScanner<T>> implements OpenApiScanner {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MicronautAnnotationScanner.class);
    static final Set<String> IGNORED = new HashSet<>(2);

    static {
        IGNORED.addAll(IgnoredPackages.ignored);
    }

    protected OpenAPIConfiguration openApiConfiguration;

    /**
     * Sets the configurations.
     * @param openApiConfiguration the configuration.
     */
    @Override
    public void setConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    /**
     * Returns the classes to read.
     * @return A list of classes.
     */
    @Override
    public Set<Class<?>> classes() {

        if (openApiConfiguration == null) {
            openApiConfiguration = new SwaggerConfiguration();
        }

        ClassGraph graph = new ClassGraph().enableAllInfo();
        Set<String> acceptablePackages = new HashSet<>();
        Set<Class<?>> output = new HashSet<>();

        // if classes are passed, use them
        if (openApiConfiguration.getResourceClasses() != null && !openApiConfiguration.getResourceClasses().isEmpty()) {
            for (String className : openApiConfiguration.getResourceClasses()) {
                if (!isIgnored(className)) {
                    try {
                        output.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        LOGGER.warn("error loading class from resourceClasses: " + e.getMessage(), e);
                    }
                }
            }
            return output;
        }

        boolean allowAllPackages = false;
        if (openApiConfiguration.getResourcePackages() != null && !openApiConfiguration.getResourcePackages().isEmpty()) {
            for (String pkg : openApiConfiguration.getResourcePackages()) {
                if (!isIgnored(pkg)) {
                    acceptablePackages.add(pkg);
                    graph.whitelistPackages(pkg);
                }
            }
        } else {
            allowAllPackages = true;
        }
        final Set<Class<?>> classes;
        try (ScanResult scanResult = graph.scan()) {
            classes = new HashSet<>(scanResult.getClassesWithAnnotation(Controller.class.getName()).loadClasses());
            classes.addAll(new HashSet<>(scanResult.getClassesWithAnnotation(OpenAPIDefinition.class.getName()).loadClasses()));
        }

        for (Class<?> cls : classes) {
            if (allowAllPackages) {
                output.add(cls);
            } else {
                for (String pkg : acceptablePackages) {
                    if (cls.getPackage().getName().startsWith(pkg)) {
                        output.add(cls);
                    }
                }
            }
        }
        LOGGER.trace("classes() - output size {}", output.size());
        return output;
    }

    /**
     * Returns true if class or package should be ignored.
     * @param classOrPackageName A class of package name
     * @return true if class or package should be ignored.
     */
    protected boolean isIgnored(String classOrPackageName) {
        if (StringUtils.isBlank(classOrPackageName)) {
            return true;
        }
        return IGNORED.stream().anyMatch(classOrPackageName::startsWith);
    }

    /**
     * Returns the resources.
     * @return The resources.
     */
    @Override
    public Map<String, Object> resources() {
        return new HashMap<>();
    }
}
