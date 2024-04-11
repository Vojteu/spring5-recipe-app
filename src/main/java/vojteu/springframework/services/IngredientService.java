package vojteu.springframework.services;

import vojteu.springframework.commands.IngredientCommand;


public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
    void deleteById(Long recipeId, Long idToDelete);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
}
