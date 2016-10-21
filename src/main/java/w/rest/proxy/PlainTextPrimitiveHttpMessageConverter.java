package w.rest.proxy;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import w.rest.common.PlainTextPrimitiveConverter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class PlainTextPrimitiveHttpMessageConverter extends PlainTextPrimitiveConverter implements HttpMessageConverter<Object> {

    public boolean supports(Class<?> type, MediaType mediaType) {
        return mediaType != null && super.supports(type, mediaType.getType() + "/" + mediaType.getSubtype());
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) { return supports(clazz, mediaType); }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return supports(clazz, mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.TEXT_PLAIN);
    }

    @Override
    public void write(Object obj, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        write(obj,outputMessage.getBody());
    }

    @Override
    public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return read(clazz, inputMessage.getBody());
    }
}