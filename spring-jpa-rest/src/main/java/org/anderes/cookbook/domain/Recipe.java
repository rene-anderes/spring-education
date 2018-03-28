package org.anderes.cookbook.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@NamedQuery(
        name = "Recipe.findAllTag",
        query = "select r.tags from Recipe r"
    )
@NamedEntityGraph(
        name = "Recipe.short.list",
        attributeNodes = {
            @NamedAttributeNode("uuid"),
            @NamedAttributeNode("title")
        }
    )
public class Recipe implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable = false)
	@NotNull @Size(min = 36, max = 36)
	private String uuid;

	@Version
	private Integer version;
	
	@NotNull
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime lastUpdate = LocalDateTime.now();
	
	@NotNull
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime addingDate = LocalDateTime.now();

	@NotNull
    @Size(min = 1, max = 80)
	@Column(nullable = false, length = 80)
	private String title;

	@Valid
	@Embedded
	@Column(nullable = true)
	private Image image;

	@NotNull @Valid
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "RECIPE_ID")
	private List<Ingredient> ingredients = new ArrayList<Ingredient>();

	@NotNull
    @Size(min = 8, max = 8000)
	@Column(nullable = false, length = 8000)
	private String preparation;

    @Size(min = 0, max = 8000)
	@Column(nullable = true, length = 8000)
	private String preamble;

	@NotNull @Size(min = 1, max = 10)
    @Column(nullable = false, length = 10)
	private String noOfPerson;
	
	@NotNull @Size(min = 0, max = 100)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="TAGS", joinColumns=@JoinColumn(name="RECIPE_ID"))
	private Set<String> tags = new HashSet<String>();
	
	@NotNull @Min(0) @Max(5)
	@Column(nullable = false)
	private Integer rating = Integer.valueOf(0);

	public Recipe() {
	    super();
	    this.uuid = UUID.randomUUID().toString();
    }
	
	public Recipe(final String uuid) {
	    super();
        this.uuid = uuid;
    }

    public Image getImage() {
		return image;
	}

	public Recipe setImage(Image image) {
		this.image = image;
		return this;
	}

	public Integer getVersion() {
		return version;
	}

	public String getUuid() {
        return uuid;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Recipe setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }
    
    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the Date object.
     * @return time
     */
    public Long getLastUpdateTime() {
        return this.lastUpdate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public LocalDateTime getAddingDate() {
        return addingDate;
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the Date object.
     * @return time
     */
    public Long getAddingDateTime() {
        return this.addingDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    public Recipe setAddingDate(LocalDateTime addingDate) {
        this.addingDate = addingDate;
        return this;
    }

    public String getTitle() {
		return title;
	}

	public Recipe setTitle(String title) {
		this.title = title;
        return this;
	}

	public Recipe addIngredient(final Ingredient ingredient) {
		ingredients.add(ingredient);
        return this;
	}
	
	public Recipe removeIngredient(final Ingredient ingredient) {
		ingredients.remove(ingredient);
        return this;
	}

	public List<Ingredient> getIngredients() {
		return Collections.unmodifiableList(ingredients);
	}

	public String getPreparation() {
		return preparation;
	}

	public Recipe setPreparation(final String preparation) {
		this.preparation = preparation;
        return this;
	}

	public String getPreamble() {
		return preamble;
	}

	public Recipe setPreamble(final String preamble) {
		this.preamble = preamble;
        return this;
	}

	public String getNoOfPerson() {
		return noOfPerson;
	}

	public Recipe setNoOfPerson(final String noOfPerson) {
		this.noOfPerson = noOfPerson;
        return this;
	}

	public Set<String> getTags() {
		return Collections.unmodifiableSet(tags);
	}

	public Recipe addTag(final String tag) {
		this.tags.add(tag);
		return this;
	}
	
	public Recipe removeTag(final String tag) {
		this.tags.remove(tag);
        return this;
	}

    public Integer getRating() {
        return rating;
    }

    public Recipe setRating(Integer rating) {
        this.rating = rating;
        return this;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("uuid", uuid).append("title", title).append("preamble", preamble)
            .append("image", image).append("noOfPerson", noOfPerson).append("ingredients", ingredients)
            .append("preparation", preparation).append("tags", tags).append("lastUpdate", lastUpdate)
            .append("addingUpdate", addingDate).append("rating", rating).toString();
    }
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(uuid).append(title).append(image)
				.append(preamble).append(preparation).append(noOfPerson)
				.append(lastUpdate).append(addingDate).append(rating)
				.append(ingredients).append(tags).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Recipe rhs = (Recipe) obj;
		return new EqualsBuilder().append(uuid, rhs.uuid).append(title, rhs.title)
				.append(preamble, rhs.preamble).append(lastUpdate, rhs.lastUpdate)
				.append(addingDate, rhs.addingDate).append(rating, rhs.rating)
				.append(noOfPerson, rhs.noOfPerson).append(image, rhs.image)
				.append(ingredients, rhs.ingredients)
				.append(tags, rhs.tags).append(preparation, rhs.preparation).isEquals();
	}
}
