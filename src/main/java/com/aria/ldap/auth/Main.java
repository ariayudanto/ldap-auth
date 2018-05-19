package com.aria.ldap.auth;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

public class Main {

    public static void main(String[] args) throws Exception {

        // ldap config
        String url = "ldap://ldap.forumsys.com:389";

        // service/manager credential --> use for binding LDAP
        String base = "dc=example,dc=com";
        String managerUserDn = "cn=read-only-admin,dc=example,dc=com";
        String managerPassword = "password";

        // user credential
        String userSearchFilter = "uid";
        String userSearchBase = "";

        // username and password --> get from login form
        String username = "einstein";
        String password = "password";

        boolean isUserValid = false;

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, managerUserDn);
        env.put(Context.SECURITY_CREDENTIALS, managerPassword);

        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            Attributes attributes = new BasicAttributes(true); // ignore case
            attributes.put(userSearchFilter, username);

            NamingEnumeration results = ctx.search(base, attributes);
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();

                System.out.println(searchResult.toString());

                // attempt another authentication, now with the user
                // populate user search
                StringBuilder userSearch = new StringBuilder();
                userSearch.append(userSearchFilter + "=" + username);
                userSearch.append(",");
                if (userSearchBase.length() != 0) {
                    userSearch.append(userSearchBase);
                } else {
                    userSearch.append(base);
                }

                Hashtable authEnv = new Hashtable();
                authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                authEnv.put(Context.PROVIDER_URL, url);
                authEnv.put(Context.SECURITY_PRINCIPAL, userSearch.toString());
                authEnv.put(Context.SECURITY_CREDENTIALS, password);
                new InitialDirContext(authEnv);

                isUserValid = true;

            }

        } catch (Exception e) {
            isUserValid = false;
            throw e;
        } finally {
            if (ctx != null)
                ctx.close();
        }

        System.out.println("User is authenticated : " + isUserValid);
    }

}
