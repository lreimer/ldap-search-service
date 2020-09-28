package cloud.nativ.quarkus;

import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

@ApplicationScoped
public class LdapContextProvider {

    private static final Logger LOGGER = Logger.getLogger(LdapContextProvider.class);

    @Inject
    LdapContextConfiguration configuration;

    private LdapContext ldapContext;

    @PostConstruct
    void initialize() {
        Hashtable<String, String> env = new Hashtable<>(13);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.ldap.derefAliases", "never");

        env.put(Context.PROVIDER_URL, configuration.getLdapUrl());
        env.put(Context.SECURITY_PRINCIPAL, configuration.getLdapUsername());
        env.put(Context.SECURITY_CREDENTIALS, configuration.getLdapPassword());
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        env.put("com.sun.jndi.ldap.connect.pool", "true");
        env.put("com.sun.jndi.ldap.connect.pool.protocol", "plain ssl");
        env.put("com.sun.jndi.ldap.connect.pool.timeout", "60000");
        env.put("com.sun.jndi.ldap.connect.pool.prefsize", "5");
        env.put("com.sun.jndi.ldap.connect.pool.maxsize", "50");
        env.put("com.sun.jndi.ldap.connect.timeout", "5000");
        env.put("com.sun.jndi.ldap.read.timeout", "10000");

        try {
            LOGGER.infov("Initializing LDAP context for environment {0}.", env);
            ldapContext = new InitialLdapContext(env, null);
        } catch (NamingException e) {
            throw new IllegalStateException("Exception occurred during LDAP context instantiation.", e);
        }
    }

    @Produces
    public LdapContext getLdapContext() {
        return ldapContext;
    }

    @PreDestroy
    void destroy() {
        try {
            ldapContext.close();
        } catch (NamingException e) {
            LOGGER.warn("Error closing LDAP context.", e);
        }
    }
}
