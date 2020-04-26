package pl.ailux.carparts.repository;

import pl.ailux.carparts.domain.PartSellingArgument;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PartSellingArgument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartSellingArgumentRepository extends JpaRepository<PartSellingArgument, Long> {

}
