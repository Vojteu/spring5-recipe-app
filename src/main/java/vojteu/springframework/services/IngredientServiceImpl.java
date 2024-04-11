package vojteu.springframework.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vojteu.springframework.commands.IngredientCommand;
import vojteu.springframework.converters.IngredientCommandToIngredient;
import vojteu.springframework.converters.IngredientToIngredientCommand;
import vojteu.springframework.domain.Ingredient;
import vojteu.springframework.domain.Recipe;
import vojteu.springframework.repositories.RecipeRepository;
import vojteu.springframework.repositories.UnitOfMeasureRepository;

import javax.transaction.Transactional;
import java.util.Optional;

    @Slf4j
    @Service
    public class IngredientServiceImpl implements IngredientService {

        private final IngredientToIngredientCommand ingredientToIngredientCommand;
        private final RecipeRepository recipeRepository;

        private final IngredientCommandToIngredient ingredientCommandToIngredient;
        private final UnitOfMeasureRepository unitOfMeasureRepository;

        public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository, IngredientCommandToIngredient ingredientCommandToIngredient, UnitOfMeasureRepository unitOfMeasureRepository) {
            this.ingredientToIngredientCommand = ingredientToIngredientCommand;
            this.recipeRepository = recipeRepository;
            this.ingredientCommandToIngredient = ingredientCommandToIngredient;
            this.unitOfMeasureRepository = unitOfMeasureRepository;
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

        @Override
        @Transactional
        public IngredientCommand saveIngredientCommand(IngredientCommand command) {
            Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

            if(!recipeOptional.isPresent()){

                log.error("Recipe not found for id: " + command.getRecipeId());
                return new IngredientCommand();
            } else {
                Recipe recipe = recipeOptional.get();

                Optional<Ingredient> ingredientOptional = recipe
                        .getIngredients()
                        .stream()
                        .filter(ingredient -> ingredient.getId().equals(command.getId()))
                        .findFirst();

                if(ingredientOptional.isPresent()){
                    Ingredient ingredientFound = ingredientOptional.get();
                    ingredientFound.setDescription(command.getDescription());
                    ingredientFound.setAmount(command.getAmount());
                    ingredientFound.setUom(unitOfMeasureRepository
                            .findById(command.getUnitOfMeasure().getId())
                            .orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
                } else {
                    Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                    ingredient.setRecipe(recipe);
                    recipe.addIngredient(ingredient);
                }

                Recipe savedRecipe = recipeRepository.save(recipe);

                Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                        .findFirst();

                if(!savedIngredientOptional.isPresent()){
                    savedIngredientOptional = savedRecipe.getIngredients().stream()
                            .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                            .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                            .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUnitOfMeasure().getId()))
                            .findFirst();
                }

                return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            }

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
