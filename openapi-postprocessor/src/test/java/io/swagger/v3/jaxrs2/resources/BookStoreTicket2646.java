package io.swagger.v3.jaxrs2.resources;

import java.util.Arrays;
import java.util.List;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Controller("/bookstore")
public class BookStoreTicket2646 {
    @Produces({ MediaType.APPLICATION_JSON })
    @Get
    public HttpResponse<List<Book>> getBooks(
            @Parameter(in = ParameterIn.QUERY, name = "page") int page) {
        return HttpResponse.ok(
                Arrays.asList(
                        new Book(),
                        new Book()
                )
        );
    }

    @Produces({ MediaType.APPLICATION_JSON })
    @Get("/{id}")
    public Book getBook(@Parameter(in = ParameterIn.PATH, name = "id") Long id) {
        return new Book();
    }

    @Delete("/{id}")
    public HttpResponse<Void> delete(@Parameter(in = ParameterIn.PATH, name = "id") String id) {
        return HttpResponse.ok();
    }

    public static class Book {
        public String foo;
    }
}