package com.appsdeveloperblog.keycloak;

import com.appsdeveloperblog.keycloak.user.controller.dto.VerifyPasswordResponse;
import com.appsdeveloperblog.keycloak.user.service.UserApiService;
import com.appsdeveloperblog.keycloak.user.service.UserCache;
import com.appsdeveloperblog.keycloak.user.service.UserPojo;
import lombok.AllArgsConstructor;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.UserCredentialStore;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RemoteUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator,
        UserQueryProvider {

    private KeycloakSession session;
    private ComponentModel model;
    private UserApiService userApiService;

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();

        return getUserByUsername(username, realm);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        UserPojo user = userApiService.getUser(username);

        if (user != null) {
            return toUserModel(username, realm);
        }

        return null;
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realmModel) {
        return null;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {

        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType)) {
            return false;
        }
        List<CredentialModel> credentials = getCredentialStore()
                .getStoredCredentialsByTypeStream(realm, user, credentialType)
                .collect(Collectors.toList());
        return !credentials.isEmpty();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realmModel) {
        List<UserPojo> userPojos = userApiService.getAllUsers();
        List<UserModel> models = new ArrayList<>();

        for (UserPojo inputElement : userPojos) {
            UserModel correspondingUserModel = toUserModel(inputElement.getUsername(), realmModel);
            models.add(correspondingUserModel);
        }

        return models;
    }

    @Override
    public List<UserModel> getUsers(RealmModel realmModel, int firstResult, int maxResults) {
        return getUsers(realmModel);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realmModel) {
        return UserCache.findUsers(search).stream().map(user -> toUserModel(search, realmModel))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realmModel, int firstResult, int maxResults) {
        return searchForUser(search, realmModel);
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        return Collections.emptyList();
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult,
                                         int maxResults) {
        return Collections.emptyList();
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        return Collections.emptyList();
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        return Collections.emptyList();
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        return Collections.emptyList();
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        String inputPassword = credentialInput.getChallengeResponse();

        VerifyPasswordResponse verifyPasswordResponse = userApiService.verifyUserPassword(user.getUsername(),
                inputPassword);

        if (verifyPasswordResponse == null)
            return false;

        return verifyPasswordResponse.getResult().equals("It's a match!");
    }

    private UserModel toUserModel(String username, RealmModel realm) {
        return new AbstractUserAdapter(session, realm, model) {
            @Override
            public String getUsername() {
                return username;
            }
        };
    }

    private UserCredentialStore getCredentialStore() {
        return session.userCredentialManager();
    }
}