package com.packtpub.micronaut.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;

@Singleton
public class LocalAuthProvider implements AuthenticationProvider {

    @Inject
    IdentityStore store;

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest httpRequest, AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getIdentity().toString();
        String password = authenticationRequest.getSecret().toString();
        if (password.equals(store.getUserPassword(username))) {
            UserDetails details = new UserDetails(username, Collections.singletonList(store.getUserRole(username)));
            return Flowable.just(details);
        } else {
            return Flowable.just(new AuthenticationFailed());
        }
    }
}
