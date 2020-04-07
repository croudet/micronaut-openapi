package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Class with Links
 */
@Controller
public class RefLinksResource {
    @Get("/links")
    @Operation(operationId = "getUserWithAddress",
            responses = {
                    @ApiResponse(description = "test description",
                             content = @Content(mediaType = "*/*", schema = @Schema(ref = "#/components/schemas/User")),
                            links = {
                                    @Link(
                                            name = "address",
                                            operationId = "getAddress",
                                            ref = "Link",
                                            parameters = @LinkParameter(
                                                    name = "userId",
                                                    expression = "$request.query.userId"))
                            })}
    )
    public String getUser(@Parameter(in = ParameterIn.QUERY, name = "userId") final String userId) {
        return null;
    }

}
