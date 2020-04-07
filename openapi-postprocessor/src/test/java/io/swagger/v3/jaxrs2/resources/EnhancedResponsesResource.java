package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Controller
public class EnhancedResponsesResource implements ResponsesInterface{

    @Get("/")
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithPayloadResponse",
            deprecated = true,
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "not found!"
                    )}
    )
    @ApiResponse(
            responseCode = "400",
            description = "boo",
            content = @Content(
                    mediaType = "*/*",
                    schema = @Schema(implementation = GenericError.class)
            )
    )
    public void getResponses() {
    }

    static class SampleResponseSchema {
        @Schema(description = "the user id")
        private String id;
    }

    @SuppressWarnings("unused")
    static class GenericError {
        private int code;
        private String message;
    }

}
