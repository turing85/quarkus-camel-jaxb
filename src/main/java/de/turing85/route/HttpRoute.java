package de.turing85.route;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import de.turing85.dto.Foo;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.platform.http.spi.Method;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.platformHttp;

public class HttpRoute extends RouteBuilder {

  public static final String HTTP_ENDPOINT = "/foo";
  public static final String ROUTE_ID = "foo-post";

  @Override
  public void configure() {
    // @formatter:off
    from(platformHttp(HTTP_ENDPOINT).httpMethodRestrict(Method.POST.name()))
        .routeId(ROUTE_ID)
        .convertBodyTo(Foo.class)
        .log("Body : ${body.toString()}")
        .log("Body as XML: ${body}")
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.OK.getStatusCode()))
        .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_XML));
    // @formatter:on
  }
}
