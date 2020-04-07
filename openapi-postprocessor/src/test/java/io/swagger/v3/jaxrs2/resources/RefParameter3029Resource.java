package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Controller
public class RefParameter3029Resource {

    @Get("/1")
    @Operation(
            summary = "Simple get operation",
            operationId = "sendPayload1",
            parameters = @Parameter(ref = "id"))
    public void sendPayload1() {
    }

    @Get("/2")
    @Operation(
            summary = "Simple get operation",
            operationId = "sendPayload2")
    @Parameter(ref = "id")
    public void sendPayload2() {
    }

}
