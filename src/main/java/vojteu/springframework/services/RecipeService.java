package vojteu.springframework.services;

import vojteu.springframework.commands.RecipeCommand;
import vojteu.springframework.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(Long l);
    void deleteById(Long i);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    RecipeCommand findCommandById(Long id);
}
