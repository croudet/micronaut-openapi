package io.swagger.v3.jaxrs2.resources.rs;

import io.micronaut.http.*;
import io.micronaut.http.annotation.*;

@Controller("token")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProcessTokenRestService extends AbstractEntityRestService<ProcessTokenDTO> {


}
