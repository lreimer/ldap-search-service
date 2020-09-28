package cloud.nativ.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class LdapSearchRepository {
    @Inject
    LdapContext ldapContext;

    public List<Map<String, Object>> search(String dn, String filter) throws NamingException {

        List<Map<String, Object>> results = new ArrayList<>();

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> search = ldapContext.search(dn, filter, searchControls);

        while (search.hasMoreElements()) {
            Map<String, Object> result = new HashMap<>();

            SearchResult searchResult = search.nextElement();
            Attributes attributes = searchResult.getAttributes();
            NamingEnumeration<? extends Attribute> enumeration = attributes.getAll();
            while (enumeration.hasMoreElements()) {
                Attribute attribute = enumeration.nextElement();
                result.put(attribute.getID(), attribute.get());
            }

            results.add(result);
        }

        return results;
    }
}
