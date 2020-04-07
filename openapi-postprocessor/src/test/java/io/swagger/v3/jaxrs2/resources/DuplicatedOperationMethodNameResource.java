package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@Controller
public class DuplicatedOperationMethodNameResource {

    @Get("/1")
    @Operation(operationId = "getSummaryAndDescription2",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> getSummaryAndDescription1() {
        return HttpResponse.ok("ok");
    }

    @Get("/2")
    public HttpResponse<String> getSummaryAndDescription2() {
        return HttpResponse.ok("ok");
    }

    @Post("/2")
    @Operation(operationId = "postSummaryAndDescription3",
            summary = "Operation Summary",
            description = "Operation Description")
    public HttpResponse<String> postSummaryAndDescription2() {
        return HttpResponse.ok("ok");
    }

    @Get("/3")
    public HttpResponse<String> getSummaryAndDescription3() {
        return HttpResponse.ok("ok");
    }

    @Post("/3")
    public HttpResponse<String> postSummaryAndDescription3() {
        return HttpResponse.ok("ok");
    }

    @Get("/4")
    public HttpResponse<String> getSummaryAndDescription3(String foo) {
        return HttpResponse.ok("ok");
    }

}
