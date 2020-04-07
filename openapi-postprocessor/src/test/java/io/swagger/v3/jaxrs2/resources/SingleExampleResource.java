package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
public class SingleExampleResource {

    @Post("/test1")
    public HttpResponse<String> test1(
            @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"foo\" : \"foo\", \"bar\" : \"bar\"}")
                    )
            ) final User user) {
        return HttpResponse.ok("");
    }

    @Post("/test2")
    @Operation(requestBody = @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class),
                    examples = @ExampleObject(value = "{\"foo\" : \"foo\", \"bar\" : \"bar\"}")
            )
    ))
    public HttpResponse<String> test2(final User user) {
        return HttpResponse.ok("");
    }
}
