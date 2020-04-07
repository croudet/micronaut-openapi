package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Controller("/notnullparameter")
public class Ticket2794Resource
{
    @Produces({ MediaType.APPLICATION_JSON })
    @Get
    public HttpResponse<List<Book>> getBooks(
            @QueryValue("page") @NotNull int page) {
        return HttpResponse.ok(
                Arrays.asList(
                        new Book(),
                        new Book()
                )
        );
    }

    @Post("/new_reqBody_required")
    public HttpResponse<String> insert(@RequestBody(required = true) Book book) {
        return HttpResponse.ok();
    }

    @Post("/newnotnull")
    public HttpResponse<String> insertnotnull(@NotNull Book book) {
        return HttpResponse.ok();
    }

    public static class Book {
        public String foo;
    }
}