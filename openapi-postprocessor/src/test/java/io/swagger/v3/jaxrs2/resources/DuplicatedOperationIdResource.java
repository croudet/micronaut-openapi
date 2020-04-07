package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@Controller
public class DuplicatedOperationIdResource {

    @Get("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getSummaryAndDescription() {
        return HttpResponse.ok("ok");
    }

    @Post("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> postSummaryAndDescription() {
        return HttpResponse.ok("ok");
    }

    @Get("/path")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getDuplicatedOperation() {
        return HttpResponse.ok("ok");
    }

}
