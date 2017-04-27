
package org.anderes.edu.jpa.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.hateoas.ResourceSupport;

public class IngredientResource extends ResourceSupport {

    private Long dbId;
    private String quantity;
    private String description;
    private String annotation;

    IngredientResource(Long id) {
        super();
        this.dbId = id;
    }

    public IngredientResource(final Long id, final String quantity, final String description, final String annotation) {
        this(id);
        this.quantity = quantity;
        this.description = description;
        this.annotation = annotation;
    }

    public Long getDbId() {
        return dbId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(final String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(final String annotation) {
        this.annotation = annotation;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(quantity).append(description).append(annotation).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        IngredientResource rhs = (IngredientResource) obj;
        return new EqualsBuilder().append(quantity, rhs.quantity).append(description, rhs.description).append(annotation, rhs.annotation).isEquals();
    }

    @Override
    public String toString() {
    	return new ToStringBuilder(this).append("quantity", quantity).append("description", description).append("annotation", annotation).build();
    }
}
