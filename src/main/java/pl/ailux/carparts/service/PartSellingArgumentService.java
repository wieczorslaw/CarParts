package pl.ailux.carparts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ailux.carparts.domain.CarPart;
import pl.ailux.carparts.domain.PartSellingArgument;
import pl.ailux.carparts.repository.CarPartRepository;
import pl.ailux.carparts.repository.PartSellingArgumentRepository;
import pl.ailux.carparts.service.dto.PartSellingArgumentDTO;
import pl.ailux.carparts.service.mapper.PartSellingArgumentMapper;

import java.util.Optional;

@Service
@Transactional
public class PartSellingArgumentService {

    private final Logger log = LoggerFactory.getLogger(PartSellingArgumentService.class);

    private final PartSellingArgumentRepository partSellingArgumentRepository;

    private final PartSellingArgumentMapper partSellingArgumentMapper;

    private final CarPartRepository carPartRepository;

    public PartSellingArgumentService(final PartSellingArgumentRepository partSellingArgumentRepository,
                                      final PartSellingArgumentMapper partSellingArgumentMapper,
                                      final CarPartRepository carPartRepository) {
        this.partSellingArgumentRepository = partSellingArgumentRepository;
        this.partSellingArgumentMapper = partSellingArgumentMapper;
        this.carPartRepository = carPartRepository;
    }

    public PartSellingArgumentDTO saveForPart(final Long partId, final PartSellingArgumentDTO partSellingArgumentDTO) {
        log.debug("Request to save PartSellingArgument : {} for partId {}", partSellingArgumentDTO, partId);
        PartSellingArgument partSellingArgument = partSellingArgumentMapper.toEntity(partSellingArgumentDTO);
        final CarPart part = carPartRepository.getOne(partId);
        partSellingArgument.setPart(part);
        partSellingArgument = partSellingArgumentRepository.save(partSellingArgument);
        return partSellingArgumentMapper.toDto(partSellingArgument);
    }

    @Transactional(readOnly = true)
    public Page<PartSellingArgumentDTO> findAll(final Pageable pageable) {
        log.debug("Request to get all PartSellingArguments");
        return partSellingArgumentRepository.findAll(pageable)
                .map(partSellingArgumentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<PartSellingArgumentDTO> findOne(final Long id) {
        log.debug("Request to get PartSellingArgument : {}", id);
        return partSellingArgumentRepository.findById(id)
                .map(partSellingArgumentMapper::toDto);
    }

    public void delete(final Long id) {
        log.debug("Request to delete PartSellingArgument : {}", id);
        partSellingArgumentRepository.deleteById(id);
    }
}
