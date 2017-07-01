package org.anderes.edu.security.rest;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<Long> checkLogin(String name, String password);

    Collection<String> getRolesByName(String name);

}
