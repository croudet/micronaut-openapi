package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("resources")
@Tag(name = "resource")
public interface Ticket2644AnnotatedInterface {
    @Get
    HttpResponse<String> getResource();
}

