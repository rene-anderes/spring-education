package org.anderes.edu.jpa.rest;

import org.springframework.hateoas.ResourceSupport;

public class RecipeShortResource extends ResourceSupport {

    private String uuid;
    private String title;
    
    public RecipeShortResource(String uuid, String title) {
        super();
        this.uuid = uuid;
        this.title = title;
    }
    public String getUuid() {
        return uuid;
    }
    public String getTitle() {
        return title;
    }
}
