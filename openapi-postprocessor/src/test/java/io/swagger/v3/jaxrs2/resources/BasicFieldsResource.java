package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;

import io.swagger.v3.jaxrs2.resources.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@Controller
public class BasicFieldsResource {

    @Get("/1")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getSummaryAndDescription(@Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            schema = @Schema(
                    description = "the generated UUID",
                    type = "string",
                    format = "uuid",
                    accessMode = Schema.AccessMode.READ_ONLY)
    ) String subscriptionId) {
        return HttpResponse.ok("ok");
    }

    @Get("/2")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getSummaryAndDescription2(@Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            array = @ArraySchema(maxItems = 10, minItems = 1,
                    schema = @Schema(implementation = Category.class, description = "the generated UUID"),
                    uniqueItems = true)
    ) String subscriptionId) {

        return HttpResponse.ok("ok");
    }

    @Get("/3")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getSummaryAndDescription3(@Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            schema = @Schema(
                    implementation = Category.class,
                    description = "the generated UUID",
                    accessMode = Schema.AccessMode.READ_ONLY)
    ) String subscriptionId) {
        return HttpResponse.ok("ok");
    }

    @Get("/4")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getSummaryAndDescription4(@Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            array = @ArraySchema(maxItems = 10, minItems = 1,
                    schema = @Schema(format = "uuid", description = "the generated UUID"),
                    uniqueItems = true)
    ) String subscriptionId) {
        return HttpResponse.ok("ok");
    }

    @Get("/5")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getSummaryAndDescription5(@Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true, content = @Content(mediaType = "application/json")
    ) String subscriptionId) {
        return HttpResponse.ok("ok");
    }

    @Get("/6")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getSummaryAndDescription6(
            @Parameter(
                    schema = @Schema(description = "test")
            ) String subscriptionId) {
        return HttpResponse.ok("ok");
    }
}
