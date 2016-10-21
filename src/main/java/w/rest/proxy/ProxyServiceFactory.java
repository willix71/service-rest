package w.rest.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by wkeyser on 11.10.16.
 */
public class ProxyServiceFactory {

    public static <T> T getProxyService(Class<T> serviceClass, String serverUrl, String login) {
        return (T) Proxy
                .newProxyInstance(
                        ProxyServiceFactory.class.getClassLoader(),
                        new Class[]{serviceClass} ,
                        new ProxyService(serviceClass, serverUrl, login));
    }
}
