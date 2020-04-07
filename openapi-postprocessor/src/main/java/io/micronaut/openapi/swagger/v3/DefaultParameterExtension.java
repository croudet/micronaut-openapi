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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.ResolvedParameter;
import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Micronaut parameter extension.
 *
 * @author croudet
 */
public class DefaultParameterExtension extends AbstractOpenAPIExtension {

    final ObjectMapper mapper = Json.mapper();

    @Override
    public ResolvedParameter extractParameters(List<Annotation> annotations,
                                               Type type,
                                               Set<Type> typesToSkip,
                                               Components components,
                                               javax.ws.rs.Consumes classConsumes,
                                               javax.ws.rs.Consumes methodConsumes,
                                               boolean includeRequestBody,
                                               JsonView jsonViewAnnotation,
                                               Iterator<OpenAPIExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ResolvedParameter();
        }


        Parameter parameter = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryValue) {
                QueryValue param = (QueryValue) annotation;
                QueryParameter qp = new QueryParameter();
                qp.setName(param.value());
                parameter = qp;
            } else if (annotation instanceof PathVariable) {
                PathVariable param = (PathVariable) annotation;
                PathParameter pp = new PathParameter();
                pp.setName(param.name());
                parameter = pp;
            } else if (annotation instanceof CookieValue) {
                CookieValue param = (CookieValue) annotation;
                CookieParameter pp = new CookieParameter();
                pp.setName(param.value());
                parameter = pp;
            } else if (annotation instanceof Header) {
                Header param = (Header) annotation;
                HeaderParameter pp = new HeaderParameter();
                pp.setName(param.name());
                parameter = pp;
            } else if (annotation instanceof io.swagger.v3.oas.annotations.Parameter) {
                if (((io.swagger.v3.oas.annotations.Parameter) annotation).hidden()) {
                    return new ResolvedParameter();
                }
                if (parameter == null) {
                    parameter = new Parameter();
                }
                if (StringUtils.isNotBlank(((io.swagger.v3.oas.annotations.Parameter) annotation).ref())) {
                    parameter.$ref(((io.swagger.v3.oas.annotations.Parameter) annotation).ref());
                }
            }
        }
        List<Parameter> parameters = new ArrayList<>();
        ResolvedParameter extractParametersResult = new ResolvedParameter();

        if (parameter != null && (StringUtils.isNotBlank(parameter.getIn()) || StringUtils.isNotBlank(parameter.get$ref()))) {
            parameters.add(parameter);
        } else if (includeRequestBody) {
            Parameter unknownParameter = ParameterProcessor.applyAnnotations(
                    null,
                    type,
                    annotations,
                    components,
                    classConsumes == null ? new String[0] : classConsumes.value(),
                    methodConsumes == null ? new String[0] : methodConsumes.value(), jsonViewAnnotation);
            if (unknownParameter != null) {
                if (StringUtils.isNotBlank(unknownParameter.getIn())) {
                    extractParametersResult.parameters.add(unknownParameter);
                } else {            // return as request body
                    extractParametersResult.requestBody = unknownParameter;
                }
            }
        }
        for (Parameter p : parameters) {
            Parameter processedParameter = ParameterProcessor.applyAnnotations(
                    p,
                    type,
                    annotations,
                    components,
                    classConsumes == null ? new String[0] : classConsumes.value(),
                    methodConsumes == null ? new String[0] : methodConsumes.value(),
                    jsonViewAnnotation);
            if (processedParameter != null) {
                extractParametersResult.parameters.add(processedParameter);
            }
        }
        return extractParametersResult;
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        return HttpRequest.class.isAssignableFrom(cls) || cls.getName().equals("io.micronaut.http.BasicAuth")
                || HttpResponse.class.isAssignableFrom(cls)
                || Principal.class.isAssignableFrom(cls)
                || cls.getName().startsWith("io.micronaut.security.authentication.")
                || cls.getName().startsWith("javax.ws.rs.");
    }
}
