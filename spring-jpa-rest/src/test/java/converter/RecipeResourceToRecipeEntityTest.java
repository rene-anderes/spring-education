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
public class RecipeResourceToRecipeEntityTest {

    @Inject
    private ConversionService conversionService;
    
    @Test
    public void shouldBeConvertRecipe() {
        // given
        RecipeResource resource = new RecipeResource(UUID.randomUUID().toString());
        resource.setTitle("Arabische Spaghetti").setPreamble("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird...")
            .setNoOfPerson("2").setPreparation("Pouletfleisch in schmale Streifen schneiden und kurz anbraten")
            .setRating(4).addTag("test").addTag("vegetarisch").setAddingDate(decemberAsLong(29, 2000)).setEditingDate(decemberAsLong(29, 2001));
       
        // when
        final Recipe recipe = conversionService.convert(resource, Recipe.class);
        
        // then
        assertThat(recipe.getUuid(), is(resource.getUuid()));
        assertThat(recipe.getTitle(), is("Arabische Spaghetti"));
        assertThat(recipe.getTags().size(), is(2));
        assertThat(recipe.getTags(), hasItems("test", "vegetarisch"));
        assertThat(recipe.getPreamble(), is("Da bei diesem Rezept das Scharfe (Curry) mit dem Süssen (Sultaninen) gemischt wird..."));
        assertThat(recipe.getNoOfPerson(), is("2"));
        assertThat(recipe.getVersion(), is(nullValue()));
        assertThat(recipe.getRating(), is(4));
        assertThat(recipe.getPreparation(), is("Pouletfleisch in schmale Streifen schneiden und kurz anbraten"));
        assertThat(recipe.getAddingDate(), is(december(29, 2000)));
        assertThat(recipe.getLastUpdate(), is(december(29, 2001)));
    }
    
    @Test
    public void shouldBeConvert() {
        // given
        final RecipeResource resource = new RecipeResource("0b5a738f-1d68-45ae-976c-8d60876bc929");
        resource.setTitle("junit-Title").setNoOfPerson("2").setPreparation("no way");
        
        // when
        final Recipe recipe = conversionService.convert(resource, Recipe.class);
        
        // then
        assertThat(resource.getUuid(), is("0b5a738f-1d68-45ae-976c-8d60876bc929"));
        assertThat(recipe, is(not(nullValue())));
        assertThat(recipe.getUuid(), is("0b5a738f-1d68-45ae-976c-8d60876bc929"));
        
    }
    
    @Test
    public void shouldBeConvertWithoutUUID() {
     // given
        final RecipeResource resource = new RecipeResource();
        resource.setTitle("junit-Title").setNoOfPerson("2").setPreparation("no way");
        
        // when
        final Recipe recipe = conversionService.convert(resource, Recipe.class);
        
        // then
        assertThat(resource.getUuid(), is(nullValue()));
        assertThat(recipe, is(not(nullValue())));
        assertThat(recipe.getUuid(), is(not(nullValue())));
        
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
