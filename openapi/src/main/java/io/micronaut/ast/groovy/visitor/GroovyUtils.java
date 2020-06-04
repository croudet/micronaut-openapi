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
package io.micronaut.ast.groovy.visitor;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import io.micronaut.core.annotation.Internal;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.MethodElement;
import io.micronaut.ast.groovy.annotation.GroovyAnnotationMetadataBuilder;

/**
 * Utilities for groovy.
 *
 * @author croudet
 * @since 1.5
 */
@Internal
public final class GroovyUtils {

    private GroovyUtils() {

    }

    /**
     * Returns the methods of the specified class.
     *
     * @param ce a GroovyClassElement.
     * @return A list of methods.
     */
    public static List<MethodElement> getCandidateMethods(ClassElement ce) {
        GroovyClassElement classElement = (GroovyClassElement) ce;
        ClassNode classNode = (ClassNode) classElement.getNativeType();

        List<MethodElement> methods = new ArrayList<>();
        for (MethodNode method: classNode.getAllDeclaredMethods()) {
            if (method.getName().contains("$") || "java.lang.Object".equals(method.getDeclaringClass().getName())) {
                continue;
            }
            final GroovyAnnotationMetadataBuilder groovyAnnotationMetadataBuilder = new GroovyAnnotationMetadataBuilder(
                    classElement.sourceUnit, classElement.compilationUnit);
            methods.add(new GroovyMethodElement(classElement, classElement.sourceUnit, classElement.compilationUnit, method,
                    groovyAnnotationMetadataBuilder.buildForMethod(method)));
        }
        return methods;
    }
}
