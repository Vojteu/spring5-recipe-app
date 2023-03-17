package vojteu.springframework.repositories;

import org.springframework.data.repository.CrudRepository;
import vojteu.springframework.domain.UnitOfMeasure;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, Long> {
    Optional<UnitOfMeasure> findByDescription(String description);
}
