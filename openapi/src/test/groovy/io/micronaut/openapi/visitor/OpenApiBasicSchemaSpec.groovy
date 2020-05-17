/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.openapi.visitor

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec
import io.swagger.v3.oas.models.OpenAPI

class OpenApiBasicSchemaSpec extends AbstractTypeElementSpec {

    def setup() {
        System.setProperty(AbstractOpenApiVisitor.ATTR_TEST_MODE, "true")
    }

    void "test @PositiveOrZero and @NegativeOrZero correctly results in minimum 0 and maximum 0"() {

        when:
        buildBeanDefinition("test.MyBean", '''
package test;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.*;

@Controller
class PersonController {

    @Operation(
            summary = "Fetch the person information",
            description = "Fetch the person name, debt and goals information",
            parameters = { @Parameter(name = "name", required = true, description = "The person name", in = ParameterIn.PATH) },
            responses = {
                    @ApiResponse(description = "The person information",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema( implementation = Person.class )
                    ))
            }
    )
    @Get("/person/{name}")
    HttpResponse<Person> get(@NotBlank String name) {
        return HttpResponse.ok();
    }
}

/**
 * The person information.
 */
@Introspected
class Person {

    @NotBlank
    private String name;

    @NegativeOrZero
    private Integer debtValue;

    @PositiveOrZero
    private Integer totalGoals;

    public Person(@NotBlank String name,
                  @NegativeOrZero Integer debtValue,
                  @PositiveOrZero Integer totalGoals) {

        this.name = name;
        this.debtValue = debtValue;
        this.totalGoals = totalGoals;
    }

     /**
     * The person full name.
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

     /**
     * The total debt amount.
     *
     * @return The debtValue
     */
    public Integer getDebtValue() {
        return debtValue;
    }

     /**
     * The total number of person's goals.
     *
     * @return The totalGoals
     */
    public Integer getTotalGoals() {
        return totalGoals;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDebtValue(Integer debtValue) {
        this.debtValue = debtValue;
    }

    public void setTotalGoals(Integer totalGoals) {
        this.totalGoals = totalGoals;
    }
}

@javax.inject.Singleton
public class MyBean {}

''')

        then:
        OpenAPI openAPI = AbstractOpenApiVisitor.testReference
        openAPI?.paths?.get("/person/{name}")?.get
        openAPI.components.schemas["Person"]
        openAPI.components.schemas["Person"].type == "object"

        openAPI.components.schemas["Person"].properties
        openAPI.components.schemas["Person"].properties.size() == 3

        openAPI.components.schemas["Person"].properties["name"]
        openAPI.components.schemas["Person"].properties["debtValue"]
        openAPI.components.schemas["Person"].properties["totalGoals"]

        openAPI.components.schemas["Person"].properties["name"].type == "string"
        openAPI.components.schemas["Person"].properties["name"].description == "The person full name."

        openAPI.components.schemas["Person"].properties["debtValue"].type == "integer"
        openAPI.components.schemas["Person"].properties["debtValue"].maximum == 0
        !openAPI.components.schemas["Person"].properties["debtValue"].exclusiveMaximum
        openAPI.components.schemas["Person"].properties["debtValue"].description == "The total debt amount."

        openAPI.components.schemas["Person"].properties["totalGoals"].type == "integer"
        !openAPI.components.schemas["Person"].properties["totalGoals"].exclusiveMinimum
        openAPI.components.schemas["Person"].properties["totalGoals"].description == "The total number of person's goals."
    }

    void "test basic schema"() {

        when:
        buildBeanDefinition("test.MyBean", '''
package test;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.*;
import java.util.*;

@Controller
class PersonController {

    @Operation(
            summary = "Fetch the person information",
            description = "Fetch the person name, debt and goals information",
            parameters = { @Parameter(name = "name", required = true, description = "The person name", in = ParameterIn.PATH) },
            responses = {
                    @ApiResponse(description = "The person information",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema( implementation = Person.class )
                    ))
            }
    )
    @Get("/person/{name}")
    HttpResponse<Person> get(@NotBlank String name) {
        return HttpResponse.ok();
    }
}

@Introspected
@Schema(name = "Pet", description = "The pet description")
class Pet {

    private String name;

    public Pet(String name) {
        this.name = name;
    }

    @Schema(description = "The pet name.", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@Introspected
@Schema(name = "Dog", description = "The dog description")
class Dog {

    private String name;

    public Dog(String name) {
        this.name = name;
    }

    @Schema(description = "The dog name.", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@Introspected
@Schema(name = "Person", description = "The person description")
class Person {

    private String name;

    private Integer debtValue;

    private Integer totalGoals;

    private Pet pet;

    private List<Pet> pets;

    public Person(String name,
                  Integer debtValue,
                  Integer totalGoals,
                  Pet pet) {

        this.name = name;
        this.debtValue = debtValue;
        this.totalGoals = totalGoals;
        this.pet = pet;
    }

    @ArraySchema(minItems = 2, arraySchema = @Schema(description = "The person pets.", nullable = true), schema = @Schema(type = "number"))
    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    @Schema(description = "The person pet.", nullable = true, implementation = Dog.class)
    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Schema(description = "The person full name.", nullable = false)
    public String getName() {
        return name;
    }

    @Schema(description = "The total debt amount.", nullable = true, minimum = "0", maximum = "100")
    public Integer getDebtValue() {
        return debtValue;
    }

    @Schema(description = "The total number of person's goals.", nullable = true, minimum = "100", maximum = "1000")
    public Integer getTotalGoals() {
        return totalGoals;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDebtValue(Integer debtValue) {
        this.debtValue = debtValue;
    }

    public void setTotalGoals(Integer totalGoals) {
        this.totalGoals = totalGoals;
    }
}

@javax.inject.Singleton
public class MyBean {}

''')

        then:
        OpenAPI openAPI = AbstractOpenApiVisitor.testReference
        openAPI?.paths?.get("/person/{name}")?.get
        openAPI.components.schemas["Person"]
        openAPI.components.schemas["Person"].type == "object"

        openAPI.components.schemas["Person"].properties
        openAPI.components.schemas["Person"].properties.size() == 5

        openAPI.components.schemas["Person"].properties["name"]
        openAPI.components.schemas["Person"].properties["debtValue"]
        openAPI.components.schemas["Person"].properties["totalGoals"]
        openAPI.components.schemas["Person"].properties["pet"]

        openAPI.components.schemas["Person"].properties["name"].type == "string"
        openAPI.components.schemas["Person"].properties["name"].description == "The person full name."
        openAPI.components.schemas["Person"].properties["name"].nullable == false

        openAPI.components.schemas["Person"].properties["debtValue"].type == "integer"
        openAPI.components.schemas["Person"].properties["debtValue"].minimum == 0
        openAPI.components.schemas["Person"].properties["debtValue"].maximum == 100
        !openAPI.components.schemas["Person"].properties["debtValue"].exclusiveMaximum
        openAPI.components.schemas["Person"].properties["debtValue"].description == "The total debt amount."
        openAPI.components.schemas["Person"].properties["debtValue"].nullable == true

        openAPI.components.schemas["Person"].properties["totalGoals"].type == "integer"
        openAPI.components.schemas["Person"].properties["totalGoals"].minimum == 100
        openAPI.components.schemas["Person"].properties["totalGoals"].maximum == 1000
        !openAPI.components.schemas["Person"].properties["totalGoals"].exclusiveMinimum
        openAPI.components.schemas["Person"].properties["totalGoals"].description == "The total number of person's goals."
        openAPI.components.schemas["Person"].properties["totalGoals"].nullable == true

        openAPI.components.schemas["Person"].properties["pet"].type == null
        openAPI.components.schemas["Person"].properties["pet"].$ref == "#/components/schemas/Dog"
        openAPI.components.schemas["Person"].properties["pet"].description == "The person pet."
        openAPI.components.schemas["Person"].properties["pet"].nullable == true

        openAPI.components.schemas["Person"].properties["pets"].type == "array"
        openAPI.components.schemas["Person"].properties["pets"].description == "The person pets."
        openAPI.components.schemas["Person"].properties["pets"].nullable == true
        openAPI.components.schemas["Person"].properties["pets"].minItems == 2
        openAPI.components.schemas["Person"].properties["pets"].$ref == null
        openAPI.components.schemas["Person"].properties["pets"].items.type == "number"
        openAPI.components.schemas["Person"].properties["pets"].items.$ref == null
    }

    void "test schema with fluent accessors"() {

        when:
        buildBeanDefinition("test.MyBean", '''
package test;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.*;

@Controller
class PersonController {

    @Operation(
            summary = "Fetch the person information",
            description = "Fetch the person name, debt and goals information",
            parameters = { @Parameter(name = "name", required = true, description = "The person name", in = ParameterIn.PATH) },
            responses = {
                    @ApiResponse(description = "The person information",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema( implementation = Person.class )
                    ))
            }
    )
    @Get("/person/{name}")
    HttpResponse<Person> get(@NotBlank String name) {
        return HttpResponse.ok();
    }
}

/**
 * The person information.
 */
@Introspected
class Person {

    @NotBlank
    private String name;

    @NegativeOrZero
    private Integer debtValue;

    @PositiveOrZero
    private Integer totalGoals;

    public Person(@NotBlank String name,
                  @NegativeOrZero Integer debtValue,
                  @PositiveOrZero Integer totalGoals) {

        this.name = name;
        this.debtValue = debtValue;
        this.totalGoals = totalGoals;
    }

     /**
     * The person full name.
     *
     * @return The name
     */
    public String name() {
        return name;
    }

     /**
     * The total debt amount.
     *
     * @return The debtValue
     */
    public Integer debtValue() {
        return debtValue;
    }

     /**
     * The total number of person's goals.
     *
     * @return The totalGoals
     */
    public Integer totalGoals() {
        return totalGoals;
    }

    public void name(String name) {
        this.name = name;
    }

    public void debtValue(Integer debtValue) {
        this.debtValue = debtValue;
    }

    public void totalGoals(Integer totalGoals) {
        this.totalGoals = totalGoals;
    }
}

@javax.inject.Singleton
public class MyBean {}

''')

        then:
        OpenAPI openAPI = AbstractOpenApiVisitor.testReference
        openAPI?.paths?.get("/person/{name}")?.get
        openAPI.components.schemas["Person"]
        openAPI.components.schemas["Person"].type == "object"

        openAPI.components.schemas["Person"].properties
        openAPI.components.schemas["Person"].properties.size() == 3

        openAPI.components.schemas["Person"].properties["name"]
        openAPI.components.schemas["Person"].properties["debtValue"]
        openAPI.components.schemas["Person"].properties["totalGoals"]

        openAPI.components.schemas["Person"].properties["name"].type == "string"
        openAPI.components.schemas["Person"].properties["name"].description == "The person full name."

        openAPI.components.schemas["Person"].properties["debtValue"].type == "integer"
        openAPI.components.schemas["Person"].properties["debtValue"].maximum == 0
        !openAPI.components.schemas["Person"].properties["debtValue"].exclusiveMaximum
        openAPI.components.schemas["Person"].properties["debtValue"].description == "The total debt amount."

        openAPI.components.schemas["Person"].properties["totalGoals"].type == "integer"
        !openAPI.components.schemas["Person"].properties["totalGoals"].exclusiveMinimum
        openAPI.components.schemas["Person"].properties["totalGoals"].description == "The total number of person's goals."
    }

    void "test @Pattern in Schema"() {

        when:
        buildBeanDefinition("test.MyBean", '''
package test;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.constraints.*;
@Controller
class PersonController {
    @Operation(
            summary = "Fetch the person information",
            description = "Fetch the person name, debt and goals information",
            parameters = { @Parameter(name = "name", required = true, description = "The person name", in = ParameterIn.PATH) },
            responses = {
                    @ApiResponse(description = "The person information",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema( implementation = Person.class )
                    ))
            }
    )
    @Get("/person/{name}")
    HttpResponse<Person> get(@NotBlank String name) {
        return HttpResponse.ok();
    }
}
/**
 * The person information.
 */
@Introspected
class Person {
    @NotBlank
    @Pattern(regexp = "xxxx")
    private String name;
    @NegativeOrZero
    private Integer debtValue;
    @PositiveOrZero
    private Integer totalGoals;
    public Person(@NotBlank String name,
                  @NegativeOrZero Integer debtValue,
                  @PositiveOrZero Integer totalGoals) {
        this.name = name;
        this.debtValue = debtValue;
        this.totalGoals = totalGoals;
    }
     /**
     * The person full name.
     *
     * @return The name
     */
    public String getName() {
        return name;
    }
     /**
     * The total debt amount.
     *
     * @return The debtValue
     */
    public Integer getDebtValue() {
        return debtValue;
    }
     /**
     * The total number of person's goals.
     *
     * @return The totalGoals
     */
    public Integer getTotalGoals() {
        return totalGoals;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDebtValue(Integer debtValue) {
        this.debtValue = debtValue;
    }
    public void setTotalGoals(Integer totalGoals) {
        this.totalGoals = totalGoals;
    }
}
@javax.inject.Singleton
public class MyBean {}
''')

        then:
        OpenAPI openAPI = AbstractOpenApiVisitor.testReference
        System.out.println(openAPI)
        openAPI?.paths?.get("/person/{name}")?.get
        openAPI.components.schemas["Person"]
        openAPI.components.schemas["Person"].type == "object"

        openAPI.components.schemas["Person"].properties
        openAPI.components.schemas["Person"].properties.size() == 3

        openAPI.components.schemas["Person"].properties["name"]
        openAPI.components.schemas["Person"].properties["debtValue"]
        openAPI.components.schemas["Person"].properties["totalGoals"]

        openAPI.components.schemas["Person"].properties["name"].type == "string"
        openAPI.components.schemas["Person"].properties["name"].description == "The person full name."
        openAPI.components.schemas["Person"].properties["name"].pattern == "xxxx"


        openAPI.components.schemas["Person"].properties["debtValue"].type == "integer"
        openAPI.components.schemas["Person"].properties["debtValue"].maximum == 0
        !openAPI.components.schemas["Person"].properties["debtValue"].exclusiveMaximum
        openAPI.components.schemas["Person"].properties["debtValue"].description == "The total debt amount."

        openAPI.components.schemas["Person"].properties["totalGoals"].type == "integer"
        !openAPI.components.schemas["Person"].properties["totalGoals"].exclusiveMinimum
        openAPI.components.schemas["Person"].properties["totalGoals"].description == "The total number of person's goals."
    }

}
