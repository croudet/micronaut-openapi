package io.swagger.v3.jaxrs2.resources.extensions;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

@Controller
public class OperationExtensionsResource {

    @Get("/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description",
            extensions = {
                    @Extension(name = "x-operation", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "x-operation-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
                    })
    public HttpResponse<String> getSummaryAndDescription() {
        return HttpResponse.ok("ok");
    }


}
