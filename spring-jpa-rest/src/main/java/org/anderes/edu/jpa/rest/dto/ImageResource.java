package org.anderes.edu.jpa.rest.dto;

import org.springframework.hateoas.ResourceSupport;

public class ImageResource extends ResourceSupport {

    private Long dbId;
    private String url;
    private String description;
    
    public ImageResource(Long id, String url, String description) {
        super();
        this.dbId = id;
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Long getDbId() {
        return dbId;
    }
    
}
