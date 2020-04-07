package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@Controller
public class ResponseContentWithArrayResource {

    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    })
            }
    )
    @SecurityRequirement(name = "JWT")
    @Get("/user")
    public List<User> getUsers() {
        return null;
    }

    class User {
        public String foo;

        @ArraySchema(arraySchema = @Schema(required = true), schema = @Schema(type = "string"))
        public List<String> issue3438;
    }
}
