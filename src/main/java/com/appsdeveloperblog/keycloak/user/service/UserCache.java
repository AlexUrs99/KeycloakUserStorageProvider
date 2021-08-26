package com.appsdeveloperblog.keycloak.user.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserCache {
    public static final Map<String, UserPojo> cache = new HashMap<>();

    public static int getCount() {
        return cache.size();
    }

    public static void addUser(UserPojo user) {
        cache.put(user.getUsername(), user);
    }

    public static List<UserPojo> getUsers() {
        return cache.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(UserPojo::getUsername))
                .collect(Collectors.toList());
    }

    public static List<UserPojo> findUsers(String search) {
        return cache.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(u -> (u.getUsername() + ";" + u.getFirstName() + ";" + u.getLastName()).contains(search))
                .sorted(Comparator.comparing(UserPojo::getUsername))
                .collect(Collectors.toList());
    }
}
