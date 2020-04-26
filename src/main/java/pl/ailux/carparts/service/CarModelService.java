package pl.ailux.carparts.service;

import pl.ailux.carparts.domain.CarModel;
import pl.ailux.carparts.repository.CarModelRepository;
import pl.ailux.carparts.service.dto.CarModelDTO;
import pl.ailux.carparts.service.mapper.CarModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CarModelService {

    private final Logger log = LoggerFactory.getLogger(CarModelService.class);

    private final CarModelRepository carModelRepository;

    private final CarModelMapper carModelMapper;

    public CarModelService(CarModelRepository carModelRepository, CarModelMapper carModelMapper) {
        this.carModelRepository = carModelRepository;
        this.carModelMapper = carModelMapper;
    }

    public CarModelDTO save(CarModelDTO carModelDTO) {
        log.debug("Request to save CarModel : {}", carModelDTO);
        CarModel carModel = carModelMapper.toEntity(carModelDTO);
        carModel = carModelRepository.save(carModel);
        return carModelMapper.toDto(carModel);
    }

    @Transactional(readOnly = true)
    public Page<CarModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarModels");
        return carModelRepository.findAll(pageable)
            .map(carModelMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<CarModelDTO> findOne(Long id) {
        log.debug("Request to get CarModel : {}", id);
        return carModelRepository.findById(id)
            .map(carModelMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete CarModel : {}", id);
        carModelRepository.deleteById(id);
    }
}
