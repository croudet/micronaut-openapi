package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Controller
public class Ticket2763Resource {

    @Get("/array")
    @ApiResponse(content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            array = @ArraySchema(schema = @Schema(
                    ref="https://openebench.bsc.es/monitor/tool/tool.json"))))
    public void getArrayResponses() {
    }

    @Get("/schema")
    @ApiResponse(content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(ref="https://openebench.bsc.es/monitor/tool/tool.json")))
    public void getSchemaResponses() {
    }
}
