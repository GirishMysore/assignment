package com.johnlewis;


import com.johnlewis.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "products.url=http://localhost:${wiremock.server.port}"
})
public class ApplicationWireMockTest {

    @LocalServerPort
    private int appPort;

    @Test
    public void productsSortedWithHighestPriceReduction() throws Exception {

        stubFor(get(urlPathMatching("/v1/categories/600001506/products")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json").withBodyFile("json/inputProducts_wiremock.json")));

        String baseUrl = "http://localhost:" + appPort;

        Product[] actualProducts = given().when().get(baseUrl + "/products/600001506")
                .then().statusCode(200)
                .log().body()
                .extract()
                .body().as(Product[].class);

        assertThat(actualProducts).isEqualTo(getExpectedProducts("expectedProducts.json"));
    }

    private Product[] getExpectedProducts(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("__files/json/" + fileName).getFile());
        String contents = new String(Files.readAllBytes(file.toPath()));
        return new ObjectMapper().readValue(contents, Product[].class);
    }

}
