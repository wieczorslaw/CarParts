package pl.ailux.carparts.service.mapper;


import pl.ailux.carparts.domain.*;
import pl.ailux.carparts.service.dto.CarModelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarModel} and its DTO {@link CarModelDTO}.
 */
@Mapper(componentModel = "spring", uses = {CarMakeMapper.class})
public interface CarModelMapper extends EntityMapper<CarModelDTO, CarModel> {

    @Mapping(source = "make.id", target = "makeId")
    CarModelDTO toDto(CarModel carModel);

    @Mapping(target = "carParts", ignore = true)
    @Mapping(target = "removeCarPart", ignore = true)
    @Mapping(source = "makeId", target = "make")
    CarModel toEntity(CarModelDTO carModelDTO);

    default CarModel fromId(Long id) {
        if (id == null) {
            return null;
        }
        CarModel carModel = new CarModel();
        carModel.setId(id);
        return carModel;
    }
}
