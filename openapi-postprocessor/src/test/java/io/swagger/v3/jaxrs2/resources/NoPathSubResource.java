package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

/**
 * The {@code NoPathSubResource} class defines test sub-resource without
 * {@link javax.ws.rs.Path} annotations.
 */
@Controller
public class NoPathSubResource {

    @Operation(description = "Returns greeting")
    @Get
    public String getGreeting() {
        return "Hello!";
    }
}