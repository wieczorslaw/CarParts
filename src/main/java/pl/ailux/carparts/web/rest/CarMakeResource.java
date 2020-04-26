package pl.ailux.carparts.web.rest;

import pl.ailux.carparts.service.CarMakeService;
import pl.ailux.carparts.web.rest.errors.BadRequestAlertException;
import pl.ailux.carparts.service.dto.CarMakeDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CarMakeResource {

    private final Logger log = LoggerFactory.getLogger(CarMakeResource.class);

    private static final String ENTITY_NAME = "carPartsCarMake";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarMakeService carMakeService;

    public CarMakeResource(CarMakeService carMakeService) {
        this.carMakeService = carMakeService;
    }

    @PostMapping("/car-makes")
    public ResponseEntity<CarMakeDTO> createCarMake(@Valid @RequestBody CarMakeDTO carMakeDTO) throws URISyntaxException {
        log.debug("REST request to save CarMake : {}", carMakeDTO);
        if (carMakeDTO.getId() != null) {
            throw new BadRequestAlertException("A new carMake cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarMakeDTO result = carMakeService.save(carMakeDTO);
        return ResponseEntity.created(new URI("/api/car-makes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/car-makes")
    public ResponseEntity<CarMakeDTO> updateCarMake(@Valid @RequestBody CarMakeDTO carMakeDTO) throws URISyntaxException {
        log.debug("REST request to update CarMake : {}", carMakeDTO);
        if (carMakeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CarMakeDTO result = carMakeService.save(carMakeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carMakeDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/car-makes")
    public ResponseEntity<List<CarMakeDTO>> getAllCarMakes(Pageable pageable) {
        log.debug("REST request to get a page of CarMakes");
        Page<CarMakeDTO> page = carMakeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/car-makes/{id}")
    public ResponseEntity<CarMakeDTO> getCarMake(@PathVariable Long id) {
        log.debug("REST request to get CarMake : {}", id);
        Optional<CarMakeDTO> carMakeDTO = carMakeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carMakeDTO);
    }

    @DeleteMapping("/car-makes/{id}")
    public ResponseEntity<Void> deleteCarMake(@PathVariable Long id) {
        log.debug("REST request to delete CarMake : {}", id);
        carMakeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

}
