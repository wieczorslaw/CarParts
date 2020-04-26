package pl.ailux.carparts.service.mapper;


import pl.ailux.carparts.domain.*;
import pl.ailux.carparts.service.dto.CarMakeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarMake} and its DTO {@link CarMakeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CarMakeMapper extends EntityMapper<CarMakeDTO, CarMake> {


    @Mapping(target = "carModels", ignore = true)
    @Mapping(target = "removeCarModel", ignore = true)
    CarMake toEntity(CarMakeDTO carMakeDTO);

    default CarMake fromId(Long id) {
        if (id == null) {
            return null;
        }
        CarMake carMake = new CarMake();
        carMake.setId(id);
        return carMake;
    }
}
