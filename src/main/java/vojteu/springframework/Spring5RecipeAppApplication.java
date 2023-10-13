package vojteu.springframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import vojteu.springframework.domain.Category;
import vojteu.springframework.repositories.CategoryRepository;



@SpringBootApplication
public class Spring5RecipeAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(Spring5RecipeAppApplication.class, args);
    }
}
