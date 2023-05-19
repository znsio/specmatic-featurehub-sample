package io.featurehub.examples.springboot;

import in.specmatic.stub.ContractStub;
import in.specmatic.test.SpecmaticJUnitSupport;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

import static in.specmatic.stub.API.createStub;

@ActiveProfiles("fh-on")
public class FeatureFlagOnTest extends SpecmaticJUnitSupport {
    private static final String SPECMATIC_STUB_HOST = "localhost";
    private static final int SPECMATIC_STUB_PORT = 9000;
    private static ContractStub stub;
    private static ConfigurableApplicationContext context;
    private static final String featureHubURL = "http://localhost:9000/_specmatic/sse-expectations";
    @BeforeAll
    public static void setUp() {
        System.setProperty("host", "localhost");
        System.setProperty("port", "8080");
        System.setProperty("filterName", "FEATURE_FLAG_ON");
        System.setProperty("spring.profiles.active", "fh-on");
        System.setProperty("endpointsAPI", "http://localhost:8080/actuator/mappings");
        stub = createStub(SPECMATIC_STUB_HOST, SPECMATIC_STUB_PORT);
        context = SpringApplication.run(MyApplication.class);
    }

    @BeforeEach
    public void before() throws Exception {
        setFeatureHubFlag();
    }

    private void setFeatureHubFlag() throws IOException {

        final String featureFlagExpectation = FileUtils.readFileToString(new File("src/test/resources/fh-expectations/feature_flag_on.json"));

        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri(featureHubURL);
        request.body(featureFlagExpectation);
        Response response = request.post();
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(200);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        if (stub != null) {
            stub.close();
        }
    }
}