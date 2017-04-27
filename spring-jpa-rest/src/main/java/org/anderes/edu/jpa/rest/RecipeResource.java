package org.anderes.edu.jpa.rest;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.hateoas.ResourceSupport;

public class RecipeResource extends ResourceSupport {
	
	private String uuid;
	private Integer version;
	private Date lastUpdate = new Date();
    private Date addingDate = new Date();
	private String title;
	private String preparation;
	private String preamble;
	private String noOfPerson;
	private Set<String> tags = new HashSet<String>();
	private Integer rating = Integer.valueOf(0);

	public RecipeResource() {
	    super();
    }
	
	public RecipeResource(final String uuid) {
        this();
        this.uuid = uuid;
    }

	public Integer getVersion() {
		return version;
	}

	public String getUuid() {
        return uuid;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public RecipeResource setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }
    
    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the Date object.
     * @return time
     */
    public Long getLastUpdateTime() {
        return this.lastUpdate.getTime();
    }

    public Date getAddingDate() {
        return addingDate;
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the Date object.
     * @return time
     */
    public Long getAddingDateTime() {
        return this.addingDate.getTime();
    }
    
    public RecipeResource setAddingDate(Date addingDate) {
        this.addingDate = addingDate;
        return this;
    }

    public String getTitle() {
		return title;
	}

	public RecipeResource setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getPreparation() {
		return preparation;
	}

	public RecipeResource setPreparation(final String preparation) {
		this.preparation = preparation;
		return this;
	}

	public String getPreamble() {
		return preamble;
	}

	public RecipeResource setPreamble(final String preamble) {
		this.preamble = preamble;
		return this;
	}

	public String getNoOfPerson() {
		return noOfPerson;
	}

	public RecipeResource setNoOfPerson(final String noOfPerson) {
		this.noOfPerson = noOfPerson;
		return this;
	}

	public Set<String> getTags() {
		return Collections.unmodifiableSet(tags);
	}

	public RecipeResource addTag(final String tag) {
		this.tags.add(tag);
		return this;
	}
	
	public RecipeResource removeTag(final String tag) {
		this.tags.remove(tag);
		return this;
	}

    public Integer getRating() {
        return rating;
    }

    public RecipeResource setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("uuid", uuid).append("title", title).append("preamble", preamble)
            .append("noOfPerson", noOfPerson).append("preparation", preparation)
            .append("tags", tags).append("lastUpdate", lastUpdate)
            .append("addingUpdate", addingDate).append("rating", rating).toString();
    }
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(uuid).append(title)
				.append(preamble).append(preparation).append(noOfPerson)
				.append(lastUpdate).append(addingDate).append(rating)
				.append(tags).toHashCode();
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
		RecipeResource rhs = (RecipeResource) obj;
		return new EqualsBuilder().append(uuid, rhs.uuid).append(title, rhs.title)
				.append(preamble, rhs.preamble).append(lastUpdate, rhs.lastUpdate)
				.append(addingDate, rhs.addingDate).append(rating, rhs.rating)
				.append(noOfPerson, rhs.noOfPerson).append(tags, rhs.tags)
				.append(preparation, rhs.preparation).isEquals();
	}

}
