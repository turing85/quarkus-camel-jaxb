package de.turing85.route;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;

import static org.hamcrest.Matchers.is;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

@QuarkusTest
@DisplayName("HTTP Route")
class HttpRouteTest {
  @Test
  @DisplayName("POST")
  void testPost() {
    final String expected = """
        <Foo>
            <Bars>
                <Bar>
                  <Name>bar1</Name>
                  <Number>42</Number>
                </Bar>
                <Bar>
                  <Name>Bar2</Name>
                  <Number>1337</Number>
                </Bar>
            </Bars>
        </Foo>""";
    // @formatter:off
    RestAssured
        .given()
            .contentType(MediaType.APPLICATION_XML)
            .body(expected)
        .when()
            .post(HttpRoute.HTTP_ENDPOINT)
        .then()
            .statusCode(is(Response.Status.OK.getStatusCode()))
            .contentType(is(MediaType.APPLICATION_XML))
            .body(isSimilarTo(expected)
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .ignoreElementContentWhitespace());
    // @formatter:on
  }
}
