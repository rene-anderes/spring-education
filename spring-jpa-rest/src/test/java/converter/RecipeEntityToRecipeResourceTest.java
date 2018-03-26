package converter;

import static java.time.Month.DECEMBER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

import javax.inject.Inject;

import org.anderes.edu.jpa.domain.Recipe;
import org.anderes.edu.jpa.rest.dto.RecipeResource;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:converter-context.xml" })
public class RecipeEntityToRecipeResourceTest {

    @Inject
    private ConversionService conversionService;

    @Test
    public void shouldBeConvert() {
     // given
        final Recipe recipe = new Recipe(UUID.randomUUID().toString());
        recipe.setTitle("Arabische Spaghetti").setPreamble("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird...")
            .setNoOfPerson("2").setPreparation("Pouletfleisch in schmale Streifen schneiden und kurz anbraten")
            .setRating(4).addTag("test").addTag("vegetarisch")
            .setAddingDate(december(29, 2000))
            .setLastUpdate(december(29, 2000));
        
        // when
        final RecipeResource resource = conversionService.convert(recipe, RecipeResource.class);
        
        // then
        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getUuid(), is(recipe.getUuid()));
        assertThat(resource.getTitle(), is("Arabische Spaghetti"));
        assertThat(resource.getTags().size(), is(2));
        assertThat(resource.getTags(), hasItems("test", "vegetarisch"));
        assertThat(resource.getPreamble(), is("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird..."));
        assertThat(resource.getNoOfPerson(), is("2"));
        assertThat(resource.getRating(), is(4));
        assertThat(resource.getPreparation(), is("Pouletfleisch in schmale Streifen schneiden und kurz anbraten"));
        assertThat(resource.getAddingDate(), is(decemberAsLong(29, 2000)));
        assertThat(resource.getEditingDate(), is(decemberAsLong(29, 2000)));
    }
    
    private LocalDateTime december(int day, int year) {
        return LocalDateTime.of(year, DECEMBER, day, 12, 10);
    }
    
    private Long decemberAsLong(int day, int year) {
        final Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.DECEMBER, day, 12, 10);
        final Calendar calendar = DateUtils.truncate(cal, Calendar.MINUTE);
        return calendar.getTime().getTime();
    }
}
