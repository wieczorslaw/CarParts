package pl.ailux.carparts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ailux.carparts.domain.CarPart;
import pl.ailux.carparts.domain.PartServiceAction;
import pl.ailux.carparts.repository.CarPartRepository;
import pl.ailux.carparts.repository.PartServiceActionRepository;
import pl.ailux.carparts.service.dto.PartServiceActionDTO;
import pl.ailux.carparts.service.mapper.PartServiceActionMapper;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class PartServiceActionService {

    private final Logger log = LoggerFactory.getLogger(PartServiceActionService.class);

    private final PartServiceActionRepository partServiceActionRepository;

    private final PartServiceActionMapper partServiceActionMapper;

    private final CarPartRepository carPartRepository;


    public PartServiceActionService(final PartServiceActionRepository partServiceActionRepository,
                                    final PartServiceActionMapper partServiceActionMapper,
                                    final CarPartRepository carPartRepository) {
        this.partServiceActionRepository = partServiceActionRepository;
        this.partServiceActionMapper = partServiceActionMapper;
        this.carPartRepository = carPartRepository;
    }

    public PartServiceActionDTO saveForPart(final Long partId, final PartServiceActionDTO partServiceActionDTO) {
        log.debug("Request to save PartServiceAction : {}", partServiceActionDTO);
        PartServiceAction partServiceAction = partServiceActionMapper.toEntity(partServiceActionDTO);
        final CarPart part = carPartRepository.getOne(partId);
        partServiceAction.setPart(part);
        partServiceAction = partServiceActionRepository.save(partServiceAction);
        return partServiceActionMapper.toDto(partServiceAction);
    }

    @Transactional(readOnly = true)
    public Page<PartServiceActionDTO> findAll(final Pageable pageable) {
        log.debug("Request to get all PartServiceActions");
        return partServiceActionRepository.findAll(pageable)
                .map(partServiceActionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PartServiceActionDTO> findOne(final Long id) {
        log.debug("Request to get PartServiceAction : {}", id);
        return partServiceActionRepository.findById(id)
                .map(partServiceActionMapper::toDto);
    }

    public void delete(final Long id) {
        log.debug("Request to delete PartServiceAction : {}", id);
        partServiceActionRepository.deleteById(id);
    }

    public Page<PartServiceActionDTO> findAllByDateRange(final Instant startDate, final Instant endDate, final Pageable pageable) {
        log.debug("Request to get all ServiceActions by date range");
        return partServiceActionRepository.findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate, pageable)
                .map(partServiceActionMapper::toDto);
    }
}
