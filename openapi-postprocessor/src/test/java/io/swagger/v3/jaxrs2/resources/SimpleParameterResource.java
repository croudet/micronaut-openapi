package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Controller
public class SimpleParameterResource {

    @Get("/")
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with a payload complex input object",
            operationId = "sendPayload",
            deprecated = true
    )
    public void sendPayload(@Parameter(in = ParameterIn.QUERY, name = "id", description = "Id Description",
            required = true,
            example = "1") @QueryValue("id") final int id) {
    }

}
