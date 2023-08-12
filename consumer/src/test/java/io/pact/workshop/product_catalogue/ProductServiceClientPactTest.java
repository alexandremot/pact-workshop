package io.pact.workshop.product_catalogue;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.pact.workshop.product_catalogue.clients.ProductServiceClient;
import io.pact.workshop.product_catalogue.models.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
public class ProductServiceClientPactTest {

    @Autowired
    private ProductServiceClient productServiceClient;

    @Pact(consumer = "ProductCatalogue")
    public RequestResponsePact singleProduct(PactDslWithProvider builder){
        return builder
                .given("product with id 10 exists")
                .uponReceiving("get product with id 10")
                .path("/products/10")
                .willRespondWith()
                .status(200)
                .body(
                        new PactDslJsonBody()
                                .integerType("id", 10L)
                                .stringType("name", "28 Degrees")
                                .stringType("type", "CREDIT_CARD")
                                .stringType("code", "CC_001")
                                .stringType("version", "v1")
                ).toPact();
    }

    @Test
    @PactTestFor(pactMethod = "singleProduct", port = "8080", pactVersion = PactSpecVersion.V3)
    void testSingleProduct(MockServer mockServer){
        productServiceClient.setBaseUrl(mockServer.getUrl());
        Product product = productServiceClient.getProductById(10);
        Product objProduct = new Product(10l, "28 Degrees", "CREDIT_CARD", "v1");
        assertThat(product).isEqualTo(objProduct);
    }


}
