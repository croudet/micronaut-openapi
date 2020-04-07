package io.swagger.v3.jaxrs2.resources.extensions;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

@Controller
public class ParameterExtensionsResource {

    @Get("/")
    @Operation(operationId = "Id")
    public void getParameters(@Parameter(in = ParameterIn.QUERY, name = "subscriptionId", extensions = @Extension(name = "x-parameter", properties = {
            @ExtensionProperty(name = "parameter", value = "value")}))
                              @QueryValue("subscriptionId") String subscriptionId) {
    }

}
