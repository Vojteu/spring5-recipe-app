package vojteu.springframework.repositories;

import org.springframework.data.repository.CrudRepository;
import vojteu.springframework.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
