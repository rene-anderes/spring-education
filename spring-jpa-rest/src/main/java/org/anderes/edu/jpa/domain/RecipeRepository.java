package org.anderes.edu.jpa.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, String> {

    Collection<Recipe> findByTitleLike(String string);
    
    List<String> findAllTag();

}
