
package org.anderes.cookbook.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull @Size(min = 36, max = 36)
    @Column(nullable = false, updatable = false)
    private String uuid;
    @Size(min = 0, max = 20)
    @Column(nullable = true, length = 20)
    private String quantity;
    @NotNull @Size(min = 1, max = 80)
    @Column(nullable = false, length = 80)
    private String description;
    @Size(min = 0, max = 80)
    @Column(nullable = true, length = 80)
    private String annotation;

    public Ingredient() {
        super();
        uuid = UUID.randomUUID().toString();
    }

    public Ingredient(final String uuid, final String quantity, final String description, final String annotation) {
        this.uuid = uuid;
        this.quantity = quantity;
        this.description = description;
        this.annotation = annotation;
    }
    
    public Ingredient(final String quantity, final String description, final String annotation) {
        this();
        this.quantity = quantity;
        this.description = description;
        this.annotation = annotation;
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
    
    public String getUuid() {
        return uuid;
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
        Ingredient rhs = (Ingredient) obj;
        return new EqualsBuilder().append(quantity, rhs.quantity).append(description, rhs.description).append(annotation, rhs.annotation).isEquals();
    }

    @Override
    public String toString() {
    	return new ToStringBuilder(this).append("uuid", uuid).append("quantity", quantity).append("description", description).append("annotation", annotation).build();
    }
}
