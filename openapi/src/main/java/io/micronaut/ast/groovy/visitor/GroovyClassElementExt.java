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
package io.micronaut.ast.groovy.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import io.micronaut.core.annotation.Internal;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.MethodElement;
import io.micronaut.ast.groovy.annotation.GroovyAnnotationMetadataBuilder;
import io.micronaut.ast.groovy.utils.PublicMethodVisitor;

/**
 * A class element returning data from a {@link ClassNode}.
 *
 * @author James Kleeh
 * @since 1.0
 */
@Internal
public class GroovyClassElementExt {
    private final GroovyClassElement classElement;
    private final ClassNode classNode;

    /**
     * Creates a GroovyClassElement.
     * @param classElement A GroovyClassElement
     */
    public GroovyClassElementExt(ClassElement classElement) {
        this((GroovyClassElement) classElement);
    }

    /**
     * Creates a GroovyClassElement.
     * @param classElement A GroovyClassElement
     */
    GroovyClassElementExt(GroovyClassElement classElement) {
        this.classElement = classElement;
        this.classNode = (ClassNode) classElement.getNativeType();
    }

    /**
     * Returns the methods of this class.
     *
     * @return A list of methods.
     */
    public List<MethodElement> getMethods() {
        List<MethodElement> fields = new ArrayList<>();
        ClassNode localClassNode = this.classNode;
        List<MethodNode> methods = new ArrayList<>();
        while (localClassNode != null && !localClassNode.equals(ClassHelper.OBJECT_TYPE)
                && !localClassNode.equals(ClassHelper.Enum_Type)) {

            localClassNode.visitContents(new PublicMethodVisitor(null) {

                @Override
                protected boolean isAcceptable(MethodNode node) {
                    boolean validModifiers = node.isPublic() && !node.isStatic() && !node.isSynthetic()
                            && !node.isAbstract();
                    if (validModifiers) {
                        String methodName = node.getName();
                        if (methodName.contains("$")) {
                            return false;
                        }
                        methods.add(node);
                    }
                    return validModifiers;
                }

                @Override
                public void accept(ClassNode classNode, MethodNode node) {
                     for (MethodNode method: methods) {
                         if (method.equals(node)) {
                             continue;
                         }
                         // try to detect overrides
                         if (method.getTypeDescriptor().equals(node.getTypeDescriptor())) {
                             return;
                         }
                     }
                    final GroovyAnnotationMetadataBuilder groovyAnnotationMetadataBuilder = new GroovyAnnotationMetadataBuilder(
                            classElement.sourceUnit, classElement.compilationUnit);
                    fields.add(new GroovyMethodElement(classElement, classElement.sourceUnit, classElement.compilationUnit, node,
                            groovyAnnotationMetadataBuilder.buildForMethod(node)));
                }
            });
            localClassNode = localClassNode.getSuperClass();
        }

        return Collections.unmodifiableList(fields);
    }
}
