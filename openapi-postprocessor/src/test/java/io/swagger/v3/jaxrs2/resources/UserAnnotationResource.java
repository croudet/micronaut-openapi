package io.swagger.v3.jaxrs2.resources;

import javax.validation.constraints.NotNull;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;

@Controller("test")
@Produces(MediaType.APPLICATION_JSON)
public class UserAnnotationResource {
    @Get("/status")
    @UserAnnotation(name = "test")
    @NotNull
    public String getStatus() {
        return "{\"status\":\"OK!\"}";
    }
}
