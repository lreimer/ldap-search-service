package cloud.nativ.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class LdapSearchResourceTest {

    @Test
    public void testWeatherEndpoint() {
        given().queryParam("dn", "ou=people,o=sevenSeas")
                .queryParam("filter", "(&(objectClass=person))")
                .when().get("/api/search")
                .then()
                .statusCode(200);
    }

}