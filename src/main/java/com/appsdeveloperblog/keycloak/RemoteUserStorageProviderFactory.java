package com.appsdeveloperblog.keycloak;

import com.appsdeveloperblog.keycloak.user.service.UserApiService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

@NoArgsConstructor
public class RemoteUserStorageProviderFactory implements UserStorageProviderFactory<RemoteUserStorageProvider> {

    public static final String PROVIDER_NAME = "USER_STORAGE_PROVIDER";

    @Override
    public RemoteUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new RemoteUserStorageProvider(session, model, buildHttpClient("http://localhost:8081"));
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }

    private UserApiService buildHttpClient(String uri) {
        ResteasyClient client = new ResteasyClientBuilder().build();

        ResteasyWebTarget target = client.target(uri);

        return target.proxyBuilder(UserApiService.class)
                .classloader(UserApiService.class.getClassLoader())
                .build();
    }
}