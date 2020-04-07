package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

@Controller("/bookstore")
public class Ticket2818Resource {

    @Produces({ MediaType.APPLICATION_JSON })
    @Get("/{id}")
    public Book getBook(
            @Parameter(
                    in = ParameterIn.PATH,
                    schema = @Schema (
                            type = "integer",
                            format = "int32"
                    )
            )
            @PathVariable("id") int id) {
        return new Book();
    }


    public static class Book {
        public String foo;
    }
}