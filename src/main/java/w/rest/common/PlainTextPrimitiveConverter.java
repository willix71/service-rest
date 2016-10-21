package w.rest.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by wkeyser on 13.10.16.
 */
public class PlainTextPrimitiveConverter {

    private final List<Class<?>> supportedClasses = Arrays.asList(
            String.class,BigInteger.class,BigDecimal.class,Boolean.class,Character.class,Byte.class,Short.class,Integer.class,Long.class,Float.class,Double.class,Void.class
    );

    public boolean supports(Class<?> type, String mediaType) {
        return "text/plain".equalsIgnoreCase(mediaType) && (type.isPrimitive() || supportedClasses.contains(type));
    }

    protected String readString(InputStream entityStream) {
        return new Scanner(entityStream).next();
    }

    public Object read(Class<?> type, InputStream entityStream) throws IOException {
        if (type == Void.TYPE || type == Void.class) return null;

        String s = readString(entityStream);

        if (s == null || s.trim().isEmpty()) return null;

        if (type == String.class) {
            return s;
        }
        if (type == Boolean.TYPE || type == Boolean.class) {
            return Boolean.valueOf(s);
        }
        if (type == Character.TYPE || type == Character.class) {
            return s.charAt(0);
        }
        if (type == Long.TYPE || type == Long.class) {
            return Long.valueOf(s);
        }
        if (type == Integer.TYPE || type == Long.class) {
            return Integer.valueOf(s);
        }
        if (type == Short.TYPE || type == Short.class) {
            return Short.valueOf(s);
        }
        if (type == Byte.TYPE || type == Byte.class) {
            return Byte.valueOf(s);
        }
        if (type == Float.TYPE || type == Float.class) {
            return Float.valueOf(s);
        }
        if (type == Double.TYPE || type == Double.class) {
            return Double.valueOf(s);
        }
        if (type == BigInteger.class) {
            return new BigInteger(s);
        }
        if (type == BigDecimal.class) {
            return new BigDecimal(s);
        }

        throw new IOException(String.format("Can't convert <%s> to %s", s, type));
    }

    protected String writeString(Object o) {
        return o==null?null:o.toString();
    }

    public void write(Object o, OutputStream entityStream) throws IOException {
        String s = writeString(o);
        if (s!=null) entityStream.write(s.getBytes());
    }
}
