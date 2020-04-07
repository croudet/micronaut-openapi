package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Examples Resource Scenario
 */
@Controller
public class RefExamplesResource {
    @Post("/example")
    @Operation(
            operationId = "subscribe",
            description = "subscribes a client to updates relevant to the requestor's account",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true,
                            schema = @Schema(name = "Schema", description = "Schema", example = "Subscription example"),
                            style = ParameterStyle.SIMPLE, example = "example",
                            examples = {
                                    @ExampleObject(name = "subscriptionId_1", value = "12345",
                                            summary = "Subscription number 12345", externalValue = "Subscription external value 1"
                                    , ref = "Id")
                            })
            })
    public SubscriptionResponse subscribe(final int subscriptionId) {
        return null;
    }
}