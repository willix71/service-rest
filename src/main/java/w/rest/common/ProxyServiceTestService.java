package w.rest.common;

import javax.ws.rs.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.events.Characters;
import java.util.List;

/**
 * Created by wkeyser on 10.10.16.
 */

@Path("/test/")
@Consumes("application/json")
@Produces("application/json")
public interface ProxyServiceTestService {

    @XmlRootElement
    class SimpleServiceDTO {
        Long id;
        String text;
        public SimpleServiceDTO() {}
        public SimpleServiceDTO(Long id, String text) {
            this.id = id;
            this.text = text;
        }
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }

    @GET
    @Path("/testGet1/{nss}")
    public String testGet1(@PathParam("nss") String name);

    @GET
    @Path("/testGet2/{nss}/{id}")
    public String testGet2(@PathParam("nss") String name, @PathParam("id") int id);

    @GET
    @Path("/testGetWithQueryParam/{nss}")
    public String testGetWithQueryParam(@PathParam("nss") String name, @QueryParam("id") int id);

    @GET
    @Path("/testGetWithMultipleQueryParam/{nss}")
    public String testGetWithMultipleQueryParam(@PathParam("nss") String name, @QueryParam("p1") String p1, @QueryParam("p2") String p2);

    @GET
    @Path("/testGetWithHeaderParam/{nss}")
    public String testGetWithHeaderParam(@PathParam("nss") String name, @HeaderParam("uid") String uid);

    @GET
    @Path("/testGetArray")
    public SimpleServiceDTO[] testGetArray();

    @GET
    @Path("/testGetList")
    public List<SimpleServiceDTO> testGetList();

    @POST
    @Path("/testSimplePost")
    public String testSimplePost(String name);

    @POST
    @Path("/testPostBody")
    public SimpleServiceDTO testPostBody(SimpleServiceDTO dto);

    @POST
    @Path("/testPostPathAndBody/{nss}")
    public SimpleServiceDTO testPostPathAndBody(@PathParam("nss") String name, SimpleServiceDTO dto);

    @POST
    @Path("/testPutReturnVoid/{nss}")
    public void testPutReturnVoid(@PathParam("nss") String name, SimpleServiceDTO dto) throws Exception;

    @DELETE
    @Path("/testDelete")
    public void testDelete();

    @DELETE
    @Path("/testError")
    public void testError();

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("/testSimpleString/{nss}")
    public String testSimpleString(@PathParam("nss") String name);

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("/testSimpleBoolean/{nss}")
    public boolean testSimpleBoolean(@PathParam("nss") String name);

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("/testSimpleCharacter/{nss}")
    public Character testSimpleCharacter(@PathParam("nss") String name);

    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("/testSimpleFloat/{nss}")
    public Float testSimpleFloat(@PathParam("nss") String name);
}
