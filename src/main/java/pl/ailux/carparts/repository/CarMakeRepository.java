package pl.ailux.carparts.repository;

import pl.ailux.carparts.domain.CarMake;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CarMake entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarMakeRepository extends JpaRepository<CarMake, Long> {

}
