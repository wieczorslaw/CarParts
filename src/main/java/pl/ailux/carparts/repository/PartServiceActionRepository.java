package pl.ailux.carparts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ailux.carparts.domain.PartServiceAction;

import java.time.Instant;

/**
 * Spring Data  repository for the PartServiceAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartServiceActionRepository extends JpaRepository<PartServiceAction, Long> {

    Page<PartServiceAction> findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(Instant startDate, Instant endDate, Pageable pageable);
}
