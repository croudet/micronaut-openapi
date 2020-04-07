package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
public class RefRequestBodyResource {

    @Get("/")
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with a payload complex input object",
            operationId = "sendPayload",
            deprecated = true,
            requestBody = @RequestBody(ref = "User")
    )
    public void sendPayload(final User user) {
    }

}
