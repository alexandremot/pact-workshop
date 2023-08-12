package io.pact.workshop.product_catalogue;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.pact.workshop.product_catalogue.clients.ProductServiceClient;
import io.pact.workshop.product_catalogue.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ProductServiceClientTest {

    @Autowired
    ProductServiceClient productServiceClient;

    @Test
    void getProductById(){

        WireMockServer server = new WireMockServer();
        server.start();
                stubFor(
                get(urlPathEqualTo("/products/10"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody(
                                                "{\n" +
                                                        "            \"id\": 10,\n" +
                                                        "            \"type\": \"CREDIT_CARD\",\n" +
                                                        "            \"name\": \"28 Degrees\",\n" +
                                                        "            \"version\": \"v1\"\n" +
                                                        "        }\n"
                                        )
                                        .withHeader("Content-Type", "application/json")
                        )

        );

        productServiceClient.setBaseUrl(server.baseUrl());

        Product product = productServiceClient.getProductById(10);

        Product objProduct = new Product(10l, "28 Degrees", "CREDIT_CARD", "v1");

        assertThat(product).isEqualTo(objProduct);

    }

}
