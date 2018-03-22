package org.anderes.edu.jpa.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

public class ImageResource extends ResourceSupport {

    @Size(min = 36, max = 36)
    private Long dbId;
    @NotNull @Size(min = 2, max = 255)
    private String url;
    @Size(min = 0, max = 50)
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
