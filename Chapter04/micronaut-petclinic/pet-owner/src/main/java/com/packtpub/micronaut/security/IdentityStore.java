package com.packtpub.micronaut.security;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.convert.format.MapFormat;

import java.util.Map;

@ConfigurationProperties("identity-store")
public class IdentityStore {

    @MapFormat
    Map<String, String> users;
    @MapFormat
    Map<String, String> roles;

    public String getUserPassword(String username) {
        return users.get(username);
    }

    public String getUserRole(String username) {
        return roles.get(username);
    }
}
