package vojteu.springframework.spring5recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import vojteu.springframework.spring5recipeapp.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
