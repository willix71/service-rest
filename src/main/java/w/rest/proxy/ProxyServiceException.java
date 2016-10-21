package w.rest.proxy;

import org.springframework.http.HttpStatus;

/**
 * Created by wkeyser on 11.10.16.
 */
public class ProxyServiceException extends RuntimeException {

    final String call;
    final int statusCode;
    final String serverError;

    public ProxyServiceException(String call, HttpStatus httpStatus, String statusText, String error) {
        super(String.format("%s (%d) - %s", httpStatus.name(), httpStatus.value(), statusText));
        this.call = call;
        this.statusCode = httpStatus.value();
        this.serverError = error;
    }

    public String getCall() { return call; }

    public int getStatusCode() {
        return statusCode;
    }

    public String getServerError() {
        return serverError;
    }

    @Override
    public String toString() {
        return String.join("\n", super.toString(), call, serverError);
    }
}
