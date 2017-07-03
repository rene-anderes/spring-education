package org.anderes.edu.security.rest;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    Optional<Long> checkLogin(String name, String password);

    Set<String> getRolesByName(String name);

}
