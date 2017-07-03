package org.anderes.edu.security.rest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StubUserRepository implements UserRepository {

    @Override
    public Optional<Long> checkLogin(String username, String password) {
        if (username.equalsIgnoreCase("Anderes") && password.equals("123456")) {
            return Optional.of(42L);
        }
        return Optional.empty();
    }

    @Override
    public Set<String> getRolesByName(String name) {
        HashSet<String> roles = new HashSet<String>(2);
        roles.add("user");
        roles.add("admin");
        return roles;
    }

}