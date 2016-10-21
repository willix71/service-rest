package w.rest.proxy;

import org.junit.*;
import w.rest.common.ProxyServiceTestService;
import w.rest.server.EmbeddedServer;

import java.io.IOException;
import java.util.List;


/**
 * Created by wkeyser on ...
 */
public class ProxyServiceTest {

    @BeforeClass
    public static void statServer() throws IOException {
        EmbeddedServer.startServer();
    }

    @AfterClass
    public static void stopServer() throws IOException {
        EmbeddedServer.stopServer();
    }

    ProxyServiceTestService svc;

    @Before
    public void initSvc() {
        svc = ProxyServiceFactory.getProxyService(ProxyServiceTestService.class, EmbeddedServer.getHttpServerPath(), "login");
    }

    @Test
    public void testGet1() {
        Assert.assertEquals("hello william", svc.testGet1("william"));
    }

    @Test
    public void testGet2() {
        Assert.assertEquals("{'william':1}", svc.testGet2("william", 1));
    }

    @Test
    public void testGetArray() {
        ProxyServiceTestService.SimpleServiceDTO[] response = svc.testGetArray();
        Assert.assertNotNull(response);
        Assert.assertEquals("size", 3, response.length);
        Assert.assertEquals(1L, response[0].getId().longValue());
    }

    @Test
    public void testGetWithQueryParam() {
        Assert.assertEquals("{'william':111}", svc.testGetWithQueryParam("william", 111));
    }

    @Test
    public void testGetWithMultipleQueryParam() {
        Assert.assertEquals("{'name':'william','p1':'1','p2':'2','p1IsNull':'false', 'p2IsNull':'false'}",
                svc.testGetWithMultipleQueryParam("william", "1", "2"));

        Assert.assertEquals("{'name':'william','p1':'1','p2':'','p1IsNull':'false', 'p2IsNull':'false'}",
                svc.testGetWithMultipleQueryParam("william", "1", ""));

        Assert.assertEquals("{'name':'william','p1':'1','p2':'null','p1IsNull':'false', 'p2IsNull':'true'}",
                svc.testGetWithMultipleQueryParam("william", "1", null));
    }


    @Test
    public void testGetWithHeaderParam() {
        Assert.assertEquals("{'william':123456789}", svc.testGetWithHeaderParam("william", "123456789"));
    }

    @Test
    public void testGetList() {
        List<?> response = svc.testGetList();
        Assert.assertNotNull(response);
        Assert.assertEquals("size", 3, response.size());

        // can't unserialize a generic list, the generic information is lost at runtime
        //Assert.assertEquals(1L, response.get(0).getId().longValue());
    }

    @Test
    public void testSimplePost() {
        Assert.assertEquals("hello william", svc.testSimplePost("william"));
    }

    @Test
    public void testPostDto1() {
        ProxyServiceTestService.SimpleServiceDTO dto = new ProxyServiceTestService.SimpleServiceDTO();
        dto.setText("William");
        dto.setId(4l);

        dto = svc.testPostBody(dto);
        Assert.assertNotNull(dto);
        Assert.assertEquals("William", dto.getText() );
        Assert.assertEquals(5l, dto.getId().longValue() );
    }

    @Test
    public void testPostDto2() {
        ProxyServiceTestService.SimpleServiceDTO dto = new ProxyServiceTestService.SimpleServiceDTO();
        dto.setText("William");
        dto.setId(4l);

        dto = svc.testPostPathAndBody("henri", dto);
        Assert.assertNotNull(dto);
        Assert.assertEquals("henri", dto.getText() );
        Assert.assertEquals(5l, dto.getId().longValue() );
    }

    @Test
    public void testDelete() {
        svc.testDelete();
    }

    @Test
    public void testError() {
        try {
            svc.testError();
            Assert.fail("No Exception");
        } catch(ProxyServiceException e) {
            Assert.assertEquals("HttpStatus", 400, e.getStatusCode());

            System.err.println(e);
        }
    }

    @Test
    public void testPutReturnVoidOK() throws Exception {
        ProxyServiceTestService.SimpleServiceDTO dto = new ProxyServiceTestService.SimpleServiceDTO();
        dto.setText("William");
        dto.setId(4l);

        svc.testPutReturnVoid("William", dto);
    }


    @Test(expected = ProxyServiceException.class)
    public void testPutReturnVoidKO() throws Exception {
        ProxyServiceTestService.SimpleServiceDTO dto = new ProxyServiceTestService.SimpleServiceDTO();
        dto.setText("William");
        dto.setId(4l);

        svc.testPutReturnVoid("William...", dto);
    }

    @Test
    public void testSimpleString() {
        Assert.assertEquals("hello William",  svc.testSimpleString("William"));
    }

    @Test
    public void testSimpleBoolean() {
        Assert.assertTrue( svc.testSimpleBoolean("true") );
        Assert.assertFalse( svc.testSimpleBoolean("false") );
    }

    @Test
    public void testSimpleCharacter() {
        Assert.assertEquals(Character.valueOf('W'),  svc.testSimpleCharacter("William"));
    }

    @Test
    public void testSimpleFloat() {
        Assert.assertEquals(Float.valueOf("7.7"),  svc.testSimpleFloat("William"));
    }
}
