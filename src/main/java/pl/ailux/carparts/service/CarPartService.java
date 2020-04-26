package pl.ailux.carparts.service;

import pl.ailux.carparts.domain.CarPart;
import pl.ailux.carparts.repository.CarPartRepository;
import pl.ailux.carparts.service.dto.CarPartDTO;
import pl.ailux.carparts.service.mapper.CarPartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarPartService {

    private final Logger log = LoggerFactory.getLogger(CarPartService.class);

    private final CarPartRepository carPartRepository;

    private final CarPartMapper carPartMapper;

    public CarPartService(CarPartRepository carPartRepository, CarPartMapper carPartMapper) {
        this.carPartRepository = carPartRepository;
        this.carPartMapper = carPartMapper;
    }

    public CarPartDTO save(CarPartDTO carPartDTO) {
        log.debug("Request to save CarPart : {}", carPartDTO);
        CarPart carPart = carPartMapper.toEntity(carPartDTO);
        carPart = carPartRepository.save(carPart);
        return carPartMapper.toDto(carPart);
    }

    @Transactional(readOnly = true)
    public Page<CarPartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarParts");
        return carPartRepository.findAll(pageable)
            .map(carPartMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<CarPartDTO> findOne(Long id) {
        log.debug("Request to get CarPart : {}", id);
        return carPartRepository.findById(id)
            .map(carPartMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete CarPart : {}", id);
        carPartRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CarPartDTO> findAllPartsByMakeAndModel(String make, String model, Optional<String> nameContaining, Optional<String> descriptionContaining) {
        log.debug("Request to get all CarParts by car make {} and model {}", make, model);
        return carPartRepository.findAllPartsByCarMakeAndModel(make, model, nameContaining, descriptionContaining).stream()
                .map(carPartMapper::toDto).collect(Collectors.toList());
    }
}
