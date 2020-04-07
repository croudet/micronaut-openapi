package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@Controller
public class DeprecatedFieldsResource {
    @Get("/")
    @Operation(deprecated = true)
    public HttpResponse<String> deprecatedMethod() {
        return HttpResponse.ok("ok");
    }
}