package io.swagger.v3.jaxrs2.resources;

import io.micronaut.http.annotation.*;

@Controller
public class SimpleMethods {

    @Get("/object")
    public TestBean getTestBean() {
        return new TestBean();
    }

    @Get("/int")
    public int getInt() {
        return 0;
    }

    @Get("/intArray")
    public int[] getIntArray() {
        return new int[]{0};
    }

    @Get("/string")
    public String[] getStringArray() {
        return new String[]{};
    }

    @Get("/stringArray")
    public void getWithIntArrayInput(@QueryValue("ids") int[] inputs) {
    }

    static class TestBean {
        public String foo;
        public TestChild testChild;
    }

    static class TestChild {
        public String foo;
    }
}