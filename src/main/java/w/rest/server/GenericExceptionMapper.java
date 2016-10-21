package w.rest.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by wkeyser on 10.10.16.
 */
@Provider
@Component
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionMapper.class);

    @Override
    public Response toResponse(Throwable ex) {
        logger.warn("Error in REST service",ex);

        StringWriter errorStackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(errorStackTrace));

        String msg = errorStackTrace.toString();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(msg)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
