package w.rest.server;

import org.springframework.stereotype.Component;
import w.rest.common.ProxyServiceTestService;

import javax.ws.rs.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wkeyser on 10.10.16.
 */
@Component
public class ProxyServiceTestServiceImpl implements ProxyServiceTestService {

    @Override
    public String testGet1(String name) {
        return "hello " + name;
    }

    @Override
    public String testGet2(String name, int id) {
        return String.format("{'%s':%d}", name, id);
    }

    @Override
    public String testGetWithQueryParam(String name, int id) {
        return String.format("{'%s':%d}", name, id);
    }

    @Override
    public String testGetWithMultipleQueryParam(String name, String p1, String p2) {
        return String.format("{'name':'%s','p1':'%s','p2':'%s','p1IsNull':'%s', 'p2IsNull':'%s'}", name, p1, p2, p1==null, p2==null);
    }

    @Override
    public String testGetWithHeaderParam(String name, String uid) {
        return String.format("{'%s':%s}", name, uid);
    }

    @Override
    public String testSimplePost(String name) {
        return "hello " + name;
    }

    @Override
    public SimpleServiceDTO testPostBody(SimpleServiceDTO dto) {
        dto.setId(dto.getId() + 1);
        return dto;
    }

    @Override
    public SimpleServiceDTO testPostPathAndBody(String name, SimpleServiceDTO dto) {
        dto.setText(name);
        dto.setId(dto.getId() + 1);
        return dto;
    }

    @Override
    public void testDelete() {
        System.out.println("testDelete called");
    }

    @Override
    public void testError() {
        throw new IllegalArgumentException("Invalid name");
    }

    @Override
    public void testPutReturnVoid(String name, SimpleServiceDTO dto) throws Exception {
        if (!name.equals(dto.getText())) throw new Exception("Bad name");
    }

    @Override
    public SimpleServiceDTO[] testGetArray() {
        return new SimpleServiceDTO[]{new SimpleServiceDTO(1l, "one"),new SimpleServiceDTO(1l, "two"), new SimpleServiceDTO(1l, "three")};
    }

    @Override
    public List<SimpleServiceDTO> testGetList() {
        return Arrays.asList(new SimpleServiceDTO(1l, "one"),new SimpleServiceDTO(1l, "two"), new SimpleServiceDTO(1l, "three"));
    }

    @Override
    public String testSimpleString(String name) {
        return "hello " + name;
    }

    @Override
    public boolean testSimpleBoolean(String name) {
        return Boolean.parseBoolean(name);
    }

    @Override
    public Character testSimpleCharacter(String name) {
        return name.charAt(0);
    }

    @Override
    public Float testSimpleFloat( String name){
        return Float.parseFloat(name.length()+ "." + name.length());
    }
}