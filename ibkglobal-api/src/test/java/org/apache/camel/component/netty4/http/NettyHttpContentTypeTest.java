package org.apache.camel.component.netty4.http;

import java.nio.charset.Charset;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

public class NettyHttpContentTypeTest extends BaseNettyTest {

    @Test
    public void testContentType() throws Exception {
        getMockEndpoint("mock:input").expectedBodiesReceived("Hello World");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.CONTENT_TYPE, "text/plain; charset=\"iso-8859-1\"");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.HTTP_CHARACTER_ENCODING, "iso-8859-1");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.HTTP_URL, "http://localhost:" + getPort() + "/foo");
        getMockEndpoint("mock:input").expectedPropertyReceived(Exchange.CHARSET_NAME, "iso-8859-1");

        byte[] data = "Hello World".getBytes(Charset.forName("iso-8859-1"));
        String out = template.requestBodyAndHeader("netty4-http:http://localhost:{{port}}/foo", data,
                "content-type", "text/plain; charset=\"iso-8859-1\"", String.class);
        assertEquals("Bye World", out);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testContentTypeWithAction() throws Exception {
        getMockEndpoint("mock:input").expectedBodiesReceived("Hello World");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.CONTENT_TYPE, "text/plain;charset=\"iso-8859-1\";action=\"http://somewhere.com/foo\"");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.HTTP_CHARACTER_ENCODING, "iso-8859-1");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.HTTP_URL, "http://localhost:" + getPort() + "/foo");
        getMockEndpoint("mock:input").expectedPropertyReceived(Exchange.CHARSET_NAME, "iso-8859-1");

        byte[] data = "Hello World".getBytes(Charset.forName("iso-8859-1"));
        String out = template.requestBodyAndHeader("netty4-http:http://localhost:{{port}}/foo", data,
                "content-type", "text/plain;charset=\"iso-8859-1\";action=\"http://somewhere.com/foo\"", String.class);
        assertEquals("Bye World", out);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testContentTypeWithActionAndPlus() throws Exception {
        getMockEndpoint("mock:input").expectedBodiesReceived("Hello World");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.CONTENT_TYPE, "application/soap+xml;charset=\"utf-8\";action=\"http://somewhere.com/foo\"");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.HTTP_CHARACTER_ENCODING, "utf-8");
        getMockEndpoint("mock:input").expectedHeaderReceived(Exchange.HTTP_URL, "http://localhost:" + getPort() + "/foo");
        getMockEndpoint("mock:input").expectedPropertyReceived(Exchange.CHARSET_NAME, "utf-8");

        byte[] data = "Hello World".getBytes(Charset.forName("utf-8"));
        String out = template.requestBodyAndHeader("netty4-http:http://localhost:{{port}}/foo", data,
                "content-type", "application/soap+xml;charset=\"utf-8\";action=\"http://somewhere.com/foo\"", String.class);
        assertEquals("Bye World", out);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("netty4-http:http://0.0.0.0:{{port}}/foo")
                    .to("mock:input")
                    .transform().constant("Bye World");
            }
        };
    }

}
