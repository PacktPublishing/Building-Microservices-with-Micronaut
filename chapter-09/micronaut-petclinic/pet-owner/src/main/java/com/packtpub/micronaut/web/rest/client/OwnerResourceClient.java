package com.packtpub.micronaut.web.rest.client;

import com.packtpub.micronaut.service.dto.OwnerDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Singleton
public class OwnerResourceClient {

    private final Logger log = LoggerFactory.getLogger(OwnerResourceClient.class);

    @Inject
    private EmbeddedServer server;

    /**
     *
     * @return a paged list of owners
     * @throws MalformedURLException
     */
    public List<OwnerDTO> getAllOwnersClient() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("https://" + server.getHost() + ":" + server.getPort()));
        OwnerDTO[] owners = client.toBlocking().retrieve(HttpRequest.GET("/api/owners"), OwnerDTO[].class);
        return List.of(owners);
    }

    /**
     * Creates an owner
     * @return created owner
     * @throws MalformedURLException
     */
    public OwnerDTO createOwnerClient() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("https://" + server.getHost() + ":" + server.getPort()));

        OwnerDTO newOwner = new OwnerDTO();
        newOwner.setFirstName("Lisa");
        newOwner.setLastName("Ray");
        newOwner.setAddress("100 Queen St.");
        newOwner.setCity("Toronto");
        newOwner.setTelephone("1234567890");

        return client.toBlocking().retrieve(HttpRequest.POST("/api/owners", newOwner), OwnerDTO.class);
    }

    /**
     * Updates an owner
     * @return updated owner
     * @throws MalformedURLException
     */
    public OwnerDTO updateOwnerClient() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("https://" + server.getHost() + ":" + server.getPort()));

        OwnerDTO owner = new OwnerDTO();
        owner.setId(1L);
        owner.setAddress("120 Queen St.");

        return client.toBlocking().retrieve(HttpRequest.PUT("/api/owners", owner), OwnerDTO.class);
    }

    /**
     * Deletes an owner
     * @return true or false based on if owner is deleted
     * @throws MalformedURLException
     */
    public Boolean deleteOwnerClient() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("https://" + server.getHost() + ":" + server.getPort()));
        long ownerId = 1L;
        HttpResponse httpResponse = client.toBlocking().retrieve(HttpRequest.DELETE("/api/owners" + ownerId), HttpResponse.class);
        return httpResponse.getStatus().equals(HttpStatus.NO_CONTENT);
    }
}
