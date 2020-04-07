package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Controller("/")
public class Ticket2848Resource {
    @Get
    public Town getter() {
        return null;
    }

    public static class Town {
        @ArraySchema( schema = @Schema(required = true), minItems = 1,uniqueItems = true )
        public List<String> streets;
    }



}
