package com.aria.ldap.auth;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class Main {

    public static void main(String[] args) throws Exception {

        // ldap config
        String url = "ldap://ldap.forumsys.com:389";
        String base = "dc=example,dc=com";
        String userSearchFilter = "uid";
        String userSearchBase = "";

        // username and password get from login
        String username = "einstein";
        String password = "password";

        boolean isUserValid = false;

        // populate user search
        StringBuilder userSearch = new StringBuilder();
        userSearch.append(userSearchFilter + "=" + username);
        userSearch.append(",");
        if (userSearchBase.length() != 0) {
            userSearch.append(userSearchBase);
        } else {
            userSearch.append(base);
        }

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_PRINCIPAL, userSearch.toString());
        env.put(Context.SECURITY_CREDENTIALS, password);

        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            if (ctx != null)
                isUserValid = true;

        } catch (Exception e) {
            isUserValid = false;
            e.printStackTrace();
        } finally {
            if (ctx != null)
                ctx.close();
        }

        System.out.println("User is authenticated : " + isUserValid);
    }

}
