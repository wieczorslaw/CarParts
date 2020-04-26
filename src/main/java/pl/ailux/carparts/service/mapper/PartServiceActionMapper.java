package pl.ailux.carparts.service.mapper;


import pl.ailux.carparts.domain.*;
import pl.ailux.carparts.service.dto.PartServiceActionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartServiceAction} and its DTO {@link PartServiceActionDTO}.
 */
@Mapper(componentModel = "spring", uses = {CarPartMapper.class})
public interface PartServiceActionMapper extends EntityMapper<PartServiceActionDTO, PartServiceAction> {

    @Mapping(source = "part.id", target = "partId")
    PartServiceActionDTO toDto(PartServiceAction partServiceAction);

    @Mapping(source = "partId", target = "part")
    PartServiceAction toEntity(PartServiceActionDTO partServiceActionDTO);

    default PartServiceAction fromId(Long id) {
        if (id == null) {
            return null;
        }
        PartServiceAction partServiceAction = new PartServiceAction();
        partServiceAction.setId(id);
        return partServiceAction;
    }
}
