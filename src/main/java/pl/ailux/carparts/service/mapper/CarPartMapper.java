package pl.ailux.carparts.service.mapper;


import pl.ailux.carparts.domain.*;
import pl.ailux.carparts.service.dto.CarPartDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarPart} and its DTO {@link CarPartDTO}.
 */
@Mapper(componentModel = "spring", uses = {CarModelMapper.class})
public interface CarPartMapper extends EntityMapper<CarPartDTO, CarPart> {

    @Mapping(source = "model.id", target = "modelId")
    CarPartDTO toDto(CarPart carPart);

    @Mapping(target = "partSellingArguments", ignore = true)
    @Mapping(target = "removePartSellingArgument", ignore = true)
    @Mapping(target = "partServiceActions", ignore = true)
    @Mapping(target = "removePartServiceAction", ignore = true)
    @Mapping(source = "modelId", target = "model")
    CarPart toEntity(CarPartDTO carPartDTO);

    default CarPart fromId(Long id) {
        if (id == null) {
            return null;
        }
        CarPart carPart = new CarPart();
        carPart.setId(id);
        return carPart;
    }
}
