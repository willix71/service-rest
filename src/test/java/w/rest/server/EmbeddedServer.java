package w.rest.server;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.LoggerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import org.slf4j.Logger;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

/**
 * Created by wkeyser on 21.10.16.
 */
public class EmbeddedServer {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);

    private static HttpServer httpServer;

    private static URI httpServerURI;

    public static void startServer(int port) throws IOException {
        httpServerURI = UriBuilder.fromUri("http://localhost/").port(port).build();

        logger.info("Starting http server at {}", getHttpServerPath());
        ResourceConfig rc = new ClassNamesResourceConfig(
                ProxyServiceTestServiceImpl.class, GenericExceptionMapper.class, PlainTextPrimitiveProvider.class, JacksonJsonProvider.class);
        httpServer = GrizzlyServerFactory.createHttpServer(httpServerURI, rc);
    }

    public static void startServer() throws IOException {
        int port = new Random().nextInt(5000) + 5000;
        startServer(port);
    }

    public static void stopServer() throws IOException {
        logger.info("Stoping http server at {}", getHttpServerPath());
        httpServer.stop();
    }

    public static URI getHttpServerURI() {
        return httpServerURI;
    }

    public static String getHttpServerPath() {
        return httpServerURI.toString();
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) startServer(Integer.parseInt(args[0]));
        else startServer();
        System.in.read();
        stopServer();
    }
}
