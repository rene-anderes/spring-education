package org.anderes.edu.security.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.anderes.edu.security.rest.UserRepository;
import org.apache.commons.lang3.Validate;

public class InMemoryUserRepository implements UserRepository {
    
    private Set<UserData> users;

    @Override
    public Optional<Long> checkLogin(String name, String password) {
        Validate.notNull(name);
        Validate.notNull(password);
        
        final Optional<UserData> user = getUsers().stream()
                        .filter(d -> d.getName().equalsIgnoreCase(name) && d.getPassword().equals(password)).findFirst();
        if (user.isPresent()) {
            return Optional.of(user.get().getUserId());
        }
        return Optional.empty();
    }

    @Override
    public Set<String> getRolesByName(String name) {
        Validate.notNull(name);
        
        final Optional<Set<String>> roles = getUsers().stream()
                        .filter(d -> d.getName().equalsIgnoreCase(name)).map(d -> d.getRoles()).findFirst();
        if (roles.isPresent()) {
            return roles.get();
        }
        return new HashSet<>(0);
    }

    private Set<UserData> getUsers() {
        if (users == null) {
            users = new HashSet<>();
            final UserData user_1 = new UserData(42L, "admin", "password");
            user_1.setRoles("ROLE_ADMIN", "ROLE_USER");
            users.add(user_1);
            final UserData user_2 = new UserData(43L, "user", "password");
            user_2.setRoles("ROLE_USER");
            users.add(user_2);
        }
        return users;
    }
}
