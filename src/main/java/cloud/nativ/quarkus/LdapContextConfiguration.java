package cloud.nativ.quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LdapContextConfiguration {

    @Inject
    @ConfigProperty(name = "ldap.url", defaultValue = "ldap://localhost:10389")
    String ldapUrl;

    @Inject
    @ConfigProperty(name = "ldap.username")
    String ldapUsername;

    @Inject
    @ConfigProperty(name = "ldap.password")
    String ldapPassword;

    public String getLdapUsername() {
        return ldapUsername;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public String getLdapUrl() {
        return ldapUrl;
    }
}
