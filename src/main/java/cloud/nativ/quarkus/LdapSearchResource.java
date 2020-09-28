package cloud.nativ.quarkus;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/search")
@ApplicationScoped
public class LdapSearchResource {

    private static final Logger LOGGER = Logger.getLogger(LdapSearchResource.class);

    @Inject
    LdapSearchRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Search LDAP entries.",
            description = "Returns LDAP entries and attributes.")
    @APIResponse(
            responseCode = "200",
            description = "current LDAP entry for ",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            type = SchemaType.OBJECT)))
    @Timed(name = "search", absolute = true, unit = MetricUnits.MILLISECONDS)
    public Response search(
            @Parameter(
                    description = "The DN to search",
                    required = true,
                    example = "ou=people,o=sevenSeas",
                    schema = @Schema(type = SchemaType.STRING))
            @QueryParam("dn")
            @NotBlank String dn,
            @Parameter(
                    description = "The search filter",
                    example = "(&(objectClass=person))",
                    schema = @Schema(type = SchemaType.STRING))
            @QueryParam("filter") String filter) {
        try {
            List<Map<String, Object>> results = repository.search(dn, filter);
            if (results.isEmpty()) {
                return Response.noContent().build();
            } else if (results.size() == 1) {
                return Response.ok(results.get(0)).build();
            } else {
                return Response.ok(results).build();
            }
        } catch (NamingException e) {
            LOGGER.warn("Error searching LDAP.", e);
            throw new NotFoundException(e);
        }
    }
}
