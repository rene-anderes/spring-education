package org.anderes.edu.security.rest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    @Override
    public Optional<Long> checkLogin(String name, String password) {
        return Optional.of(42L);
    }

    @Override
    public Collection<String> getRolesByName(String name) {
        HashSet<String> roles = new HashSet<String>(2);
        roles.add("user");
        roles.add("asmin");
        return roles;
    }

}
