package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Controller
public class Ticket2806Resource {

    @Get("/test")
    public Test getTest() {
        return null;
    }

    class Test {
        private String[] stringArray;

        @ArraySchema(
                minItems = 2,
                maxItems = 4,
                uniqueItems = true,
                arraySchema = @Schema(
                        description = "Array desc",
                        example = "[\"aaa\", \"bbb\"]"
                ),
                schema = @Schema(
                        description = "Hello, World!",
                        example = "Lorem ipsum dolor set"
                )
        )
        public void setStringArray(String[] value) { stringArray = value; }
        public String[] getStringArray() { return stringArray; }
    }
}
