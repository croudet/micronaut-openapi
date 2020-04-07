package io.swagger.v3.jaxrs2.resources.rs;

import io.micronaut.http.annotation.*;

@Controller
public interface EntityRestService<DTO> {

    @Post("/")
    public DTO create(DTO object) throws Exception;


}
