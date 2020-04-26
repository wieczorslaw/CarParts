package pl.ailux.carparts.service;

import pl.ailux.carparts.domain.CarMake;
import pl.ailux.carparts.repository.CarMakeRepository;
import pl.ailux.carparts.service.dto.CarMakeDTO;
import pl.ailux.carparts.service.mapper.CarMakeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CarMakeService {

    private final Logger log = LoggerFactory.getLogger(CarMakeService.class);

    private final CarMakeRepository carMakeRepository;

    private final CarMakeMapper carMakeMapper;

    public CarMakeService(CarMakeRepository carMakeRepository, CarMakeMapper carMakeMapper) {
        this.carMakeRepository = carMakeRepository;
        this.carMakeMapper = carMakeMapper;
    }

    public CarMakeDTO save(CarMakeDTO carMakeDTO) {
        log.debug("Request to save CarMake : {}", carMakeDTO);
        CarMake carMake = carMakeMapper.toEntity(carMakeDTO);
        carMake = carMakeRepository.save(carMake);
        return carMakeMapper.toDto(carMake);
    }

    @Transactional(readOnly = true)
    public Page<CarMakeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarMakes");
        return carMakeRepository.findAll(pageable)
            .map(carMakeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<CarMakeDTO> findOne(Long id) {
        log.debug("Request to get CarMake : {}", id);
        return carMakeRepository.findById(id)
            .map(carMakeMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete CarMake : {}", id);
        carMakeRepository.deleteById(id);
    }
}
