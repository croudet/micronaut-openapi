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

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Reader Utilities.
 *
 * @author croudet
 */
public final class ReaderUtils {

    private static final String PATH_DELIMITER = "/";

    private ReaderUtils() {

    }

    /**
     * Collects constructor-level parameters from class.
     *
     * @param cls        is a class for collecting
     * @param components The component list.
     * @param classConsumes The media type.
     * @param jsonViewAnnotation The json view.
     * @return the collection of supported parameters
     */
    public static List<Parameter> collectConstructorParameters(Class<?> cls, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation) {
        if (cls.isLocalClass() || (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers()))) {
            return Collections.emptyList();
        }

        List<Parameter> selected = Collections.emptyList();
        int maxParamsCount = 0;

        for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
            if (!ReflectionUtils.isConstructorCompatible(constructor)
                    && !ReflectionUtils.isInject(Arrays.asList(constructor.getDeclaredAnnotations()))) {
                continue;
            }

            final Type[] genericParameterTypes = constructor.getGenericParameterTypes();
            final Annotation[][] annotations = constructor.getParameterAnnotations();

            int paramsCount = 0;
            final List<Parameter> parameters = new ArrayList<Parameter>();
            for (int i = 0; i < genericParameterTypes.length; i++) {
                final List<Annotation> tmpAnnotations = Arrays.asList(annotations[i]);
                if (isContext(tmpAnnotations)) {
                    paramsCount++;
                } else {
                    final Type genericParameterType = genericParameterTypes[i];
                    final List<Parameter> tmpParameters = collectParameters(genericParameterType, tmpAnnotations, components, classConsumes, jsonViewAnnotation);
                    if (!tmpParameters.isEmpty()) {
                        for (Parameter tmpParameter : tmpParameters) {
                            Parameter processedParameter = ParameterProcessor.applyAnnotations(
                                    tmpParameter,
                                    genericParameterType,
                                    tmpAnnotations,
                                    components,
                                    classConsumes == null ? new String[0] : classConsumes.value(),
                                    null,
                                    jsonViewAnnotation);
                            if (processedParameter != null) {
                                parameters.add(processedParameter);
                            }
                        }
                        paramsCount++;
                    }
                }
            }

            if (paramsCount >= maxParamsCount) {
                maxParamsCount = paramsCount;
                selected = parameters;
            }
        }

        return selected;
    }

    /**
     * Collects field-level parameters from class.
     *
     * @param cls        is a class for collecting
     * @param components The components list.
     * @param classConsumes The media type.
     * @param jsonViewAnnotation The json view.
     * @return the collection of supported parameters
     */
    public static List<Parameter> collectFieldParameters(Class<?> cls, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation) {
        final List<Parameter> parameters = new ArrayList<>();
        for (Field field : ReflectionUtils.getDeclaredFields(cls)) {
            final List<Annotation> annotations = Arrays.asList(field.getAnnotations());
            final Type genericType = field.getGenericType();
            parameters.addAll(collectParameters(genericType, annotations, components, classConsumes, jsonViewAnnotation));
        }
        return parameters;
    }

    private static List<Parameter> collectParameters(Type type, List<Annotation> annotations, Components components, javax.ws.rs.Consumes classConsumes, JsonView jsonViewAnnotation) {
        final Iterator<OpenAPIExtension> chain = OpenAPIExtensions.chain();
        return chain.hasNext() ? chain.next().extractParameters(annotations, type, new HashSet<>(), components, classConsumes, null, false, jsonViewAnnotation, chain).parameters :
                Collections.emptyList();
    }

    private static boolean isContext(List<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Context) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts an array to a list.
     * @param array The array to convert.
     * @return A list.
     */
    public static Optional<List<String>> getStringListFromStringArray(String[] array) {
        if (array == null) {
            return Optional.empty();
        }
        List<String> list = new ArrayList<>();
        boolean isEmpty = true;
        for (String value : array) {
            if (StringUtils.isNotBlank(value)) {
                isEmpty = false;
            }
            list.add(value);
        }
        if (isEmpty) {
            return Optional.empty();
        }
        return Optional.of(list);
    }

    /**
     * Returns true if the path should be ignored.
     * @param path A path.
     * @param config A config.
     * @return true if path must be ignored.
     */
    public static boolean isIgnored(String path, OpenAPIConfiguration config) {
        if (config.getIgnoredRoutes() == null) {
            return false;
        }
        for (String item : config.getIgnoredRoutes()) {
            final int length = item.length();
            if (path.startsWith(item) && (path.length() == length || path.startsWith(PATH_DELIMITER, length))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a paths.
     * @param classLevelPath The class level path (Controller.)
     * @param methodLevelPath The method level path (Get, Post,...)
     * @return An uri path or null.
     */
    public static String getPath(javax.ws.rs.Path classLevelPath, javax.ws.rs.Path methodLevelPath) {
        if (classLevelPath == null && methodLevelPath == null) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        if (classLevelPath != null) {
            appendPathComponent(classLevelPath.value(), b);
        }
        if (methodLevelPath != null) {
            appendPathComponent(methodLevelPath.value(), b);
        }
        return b.length() == 0 ? "/" : b.toString();
    }

    /**
     * appends a path component string to a StringBuilder.
     * guarantees:
     * <ul>
     *     <li>nulls, empty strings and "/" are nops</li>
     *     <li>output will always start with "/" and never end with "/"</li>
     * </ul>
     * @param component component to be added
     * @param to output
     */
    private static void appendPathComponent(String component, StringBuilder to) {
        if (component == null || component.isEmpty() || "/".equals(component)) {
            return;
        }
        if (!component.startsWith("/") && (to.length() == 0 || '/' != to.charAt(to.length() - 1))) {
            to.append("/");
        }
        if (component.endsWith("/")) {
            to.append(component, 0, component.length() - 1);
        } else {
            to.append(component);
        }
    }
}
