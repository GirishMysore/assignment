package com.johnlewis;

import org.junit.Test;

public class ProductApplicationTest {

    @Test
    public void contextLoads() {
        ProductApplication.main(new String[]{"--server.port=9090", "--products.url=http://localhost:3020"});
    }

}
