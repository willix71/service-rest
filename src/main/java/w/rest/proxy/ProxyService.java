package w.rest.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import javax.ws.rs.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wkeyser on 10.10.16.
 */
public class ProxyService implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProxyService.class);

    private final String baseUrl;

    private final String login;

    private MediaType defaultContentTypeHeader;

    private List<MediaType> defaultAcceptHeader;

    private RestTemplate restTemplate = new RestTemplate();

    public ProxyService(Class<?> serviceClass, String serverUrl, String login) {
        // support for plain text numbers
        restTemplate.getMessageConverters().add(new PlainTextPrimitiveHttpMessageConverter());

        this.defaultContentTypeHeader = getHeaderValue(serviceClass.getAnnotation(Consumes.class), null);
        this.defaultAcceptHeader = getHeaderValue(serviceClass.getAnnotation(Produces.class), null);
        this.baseUrl = appendUriPath(serverUrl, serviceClass.getAnnotation(Path.class));
        this.login = login;
    }

    @Override
    public Object invoke(Object proxy, Method javaMethod, Object[] args) throws Throwable {
        HttpMethod httpMethod = getHttpMethod(javaMethod);

        CallBuilder builder = new CallBuilder(initializeHttpHeaders(javaMethod));
        builder.processArguments(javaMethod.getParameterAnnotations(), args);
        URI uri = builder.getUri(appendUriPath(baseUrl, javaMethod.getAnnotation(Path.class)));

        logger.debug("Calling {} {}", httpMethod, uri);
        try {

            HttpEntity entity = builder.getHttpEntity();

            Class<?> returnType = javaMethod.getReturnType();
            if (returnType == Void.TYPE) {
                return call(httpMethod, uri, entity, null);
            } else {
                checkXmlRootElementAnnotated (returnType);
                return call(httpMethod, uri, entity, returnType);
            }

        } catch(HttpClientErrorException e) {
            throw new ProxyServiceException(httpMethod + " " + uri, e.getStatusCode(), e.getStatusText(), e.getResponseBodyAsString());
        }
    }

    protected <T> T call(HttpMethod httpMethod, URI uri, HttpEntity<?> entity, Class<T> returnType) {
        final ResponseEntity<T> exchange = restTemplate.exchange(uri, httpMethod, entity, returnType);
        return exchange.getBody();
    }

    protected String appendUriPath(String basePath, Path annotatedPath) {
        if (annotatedPath == null) return basePath;
        String subPath = annotatedPath.value();
        if (subPath == null || subPath.trim().length() == 0) return basePath;
        if (basePath == null || basePath.trim().length() == 0) return subPath;
        if (basePath.charAt(basePath.length() - 1) != '/' && subPath.charAt(0) != '/')
            return basePath + "/" + subPath;
        else
            return basePath + subPath;
    }

    protected MediaType getHeaderValue(Consumes annotation, MediaType defaultValue) {
        if (annotation != null) return MediaType.parseMediaType(annotation.value()[0]);
        else return defaultValue;
    }

    protected List<MediaType> getHeaderValue(Produces annotation, List<MediaType> defaultValue) {
        if (annotation != null) return MediaType.parseMediaTypes(String.join(",",annotation.value()));
        else return defaultValue;
    }

    protected HttpHeaders initializeHttpHeaders(Method javaMethod) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( getHeaderValue(javaMethod.getAnnotation(Consumes.class), defaultContentTypeHeader) );
        headers.setAccept( getHeaderValue(javaMethod.getAnnotation(Produces.class), defaultAcceptHeader) ) ;
        headers.set("iam-userid", this.login);
        return headers;
    }

    protected HttpMethod getHttpMethod(Method javaMethod) {
        for(Annotation annotation: javaMethod.getAnnotations()) {
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            if (POST.class.equals(annotationClass)) return HttpMethod.POST;
            if (PUT.class.equals(annotationClass)) return HttpMethod.PUT;
            if (GET.class.equals(annotationClass)) return HttpMethod.GET;
            if (DELETE.class.equals(annotationClass)) return HttpMethod.DELETE;
        }
        throw new IllegalArgumentException("No Http Method defined on method " + javaMethod.getName());
    }

    protected void checkXmlRootElementAnnotated(Class<?> clazz) {
        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        if (!clazz.isPrimitive() && !clazz.getName().startsWith("java") && !clazz.isAnnotationPresent(XmlRootElement.class))
            throw new IllegalArgumentException("Transfer object class " + clazz.getName() + " is not @XmlRootElement annotated!");
    }

    private class CallBuilder {
        HttpHeaders headers;
        Map<String, Object> queryValues = new HashMap<>();
        List<String> queryParams = new ArrayList<>();
        Object body = null;

        CallBuilder(HttpHeaders headers) {
            this.headers = headers;
        }

        public HttpEntity getHttpEntity() {
            return new HttpEntity<Object>(body, headers);
        }

        public URI getUri(String baseUri) {
            String rawUri = baseUri;
            if (!queryParams.isEmpty())
                rawUri += "?" + String.join("&", queryParams);
            return new UriTemplate(rawUri).expand(queryValues);
        }

        public void processArguments(Annotation[][] annotationParams, Object[] args) {
            for(int i=0;i<annotationParams.length;i++) {
                if (annotationParams[i].length == 0) {
                    body = args[i];
                } else {
                    processArgument(annotationParams[i], args[i]);
                }
            }
        }

        private void processArgument(Annotation[] annotationParams, Object arg) {
            for(Annotation annotation: annotationParams) {
                if ( annotation instanceof PathParam ) {
                    String name = ((PathParam) annotation).value();
                    queryValues.put(name, arg);
                } else if (annotation instanceof QueryParam ) {
                    if (arg!=null) {
                        String name = ((QueryParam) annotation).value();
                        queryValues.put(name, arg);
                        queryParams.add(name + "={" + name + "}");
                    }
                } else if (annotation instanceof HeaderParam) {
                    if (arg!=null) {
                        String name = ((HeaderParam) annotation).value();
                        headers.add(name, arg.toString());
                    }
                }
            }
        }

    }
}




