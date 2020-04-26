package pl.ailux.carparts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.ailux.carparts.domain.CarPart;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface CarPartRepository extends JpaRepository<CarPart, Long> {

    @Query(value = "select cpart.* from car_make cmake inner join car_model cmodel on cmake.id = cmodel.make_id " +
            "inner join car_part cpart on cmodel.id = cpart.model_id " +
            "where cmake.name = :make and cmodel.name = :model " +
            "and (:nameContaining is null or cpart.name ilike concat('%', :nameContaining, '%')) " +
            "and (:descriptionContaining is null or cpart.description ilike concat('%', :descriptionContaining, '%'))", nativeQuery = true)
    List<CarPart> findAllPartsByCarMakeAndModel(@Param("make") String make, @Param("model") String model,
                                                @Param("nameContaining") Optional<String> nameContaining,
                                                @Param("descriptionContaining") Optional<String> descriptionContaining);
}
