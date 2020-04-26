package pl.ailux.carparts.service.mapper;


import pl.ailux.carparts.domain.*;
import pl.ailux.carparts.service.dto.PartSellingArgumentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartSellingArgument} and its DTO {@link PartSellingArgumentDTO}.
 */
@Mapper(componentModel = "spring", uses = {CarPartMapper.class})
public interface PartSellingArgumentMapper extends EntityMapper<PartSellingArgumentDTO, PartSellingArgument> {

    @Mapping(source = "part.id", target = "partId")
    PartSellingArgumentDTO toDto(PartSellingArgument partSellingArgument);

    @Mapping(source = "partId", target = "part")
    PartSellingArgument toEntity(PartSellingArgumentDTO partSellingArgumentDTO);

    default PartSellingArgument fromId(Long id) {
        if (id == null) {
            return null;
        }
        PartSellingArgument partSellingArgument = new PartSellingArgument();
        partSellingArgument.setId(id);
        return partSellingArgument;
    }
}
