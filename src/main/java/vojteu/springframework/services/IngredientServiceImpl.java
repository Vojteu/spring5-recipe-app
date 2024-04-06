package vojteu.springframework.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vojteu.springframework.commands.IngredientCommand;
import vojteu.springframework.converters.IngredientToIngredientCommand;
import vojteu.springframework.domain.Ingredient;
import vojteu.springframework.domain.Recipe;
import vojteu.springframework.repositories.RecipeRepository;

import java.util.Optional;

    @Slf4j
    @Service
    public class IngredientServiceImpl implements IngredientService {

        private final IngredientToIngredientCommand ingredientToIngredientCommand;
        private final RecipeRepository recipeRepository;

        public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository) {
            this.ingredientToIngredientCommand = ingredientToIngredientCommand;
            this.recipeRepository = recipeRepository;
        }

        @Override
        public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
            Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

            if(!recipeOptional.isPresent()){
                log.error("recipe id not found. Id: " + recipeId);
            }

            Recipe recipe = recipeOptional.get();

            Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

            if(!ingredientCommandOptional.isPresent()){
                log.error("Ingredient id not found: " + ingredientId);
            }

            return ingredientCommandOptional.get();
        }

            public void deleteById(Long recipeId, Long idToDelete){
                log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

                Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

                if(recipeOptional.isPresent()){
                    Recipe recipe = recipeOptional.get();
                    log.debug("found recipe");

                    Optional<Ingredient> ingredientOptional = recipe
                            .getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equals(idToDelete))
                            .findFirst();

                    if(ingredientOptional.isPresent()){
                        log.debug("found Ingredient");
                        Ingredient ingredientToDelete = ingredientOptional.get();
                        ingredientToDelete.setRecipe(null);
                        recipe.getIngredients().remove(ingredientOptional.get());
                        recipeRepository.save(recipe);
                    }
                }
                else{
                    log.debug("Recipe Id not found. Id:" + recipeId);
                }

            }

    }
