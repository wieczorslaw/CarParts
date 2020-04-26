package pl.ailux.carparts.repository;

import pl.ailux.carparts.domain.CarModel;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CarModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

}
