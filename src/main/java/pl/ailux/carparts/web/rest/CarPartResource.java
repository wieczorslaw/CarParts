package pl.ailux.carparts.web.rest;

import pl.ailux.carparts.service.CarPartService;
import pl.ailux.carparts.web.rest.errors.BadRequestAlertException;
import pl.ailux.carparts.service.dto.CarPartDTO;

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
public class CarPartResource {

    private final Logger log = LoggerFactory.getLogger(CarPartResource.class);

    private static final String ENTITY_NAME = "carPartsCarPart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarPartService carPartService;

    public CarPartResource(CarPartService carPartService) {
        this.carPartService = carPartService;
    }

    @PostMapping("/car-parts")
    public ResponseEntity<CarPartDTO> createCarPart(@Valid @RequestBody CarPartDTO carPartDTO) throws URISyntaxException {
        log.debug("REST request to save CarPart : {}", carPartDTO);
        if (carPartDTO.getId() != null) {
            throw new BadRequestAlertException("A new carPart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarPartDTO result = carPartService.save(carPartDTO);
        return ResponseEntity.created(new URI("/api/car-parts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/car-parts")
    public ResponseEntity<CarPartDTO> updateCarPart(@Valid @RequestBody CarPartDTO carPartDTO) throws URISyntaxException {
        log.debug("REST request to update CarPart : {}", carPartDTO);
        if (carPartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CarPartDTO result = carPartService.save(carPartDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carPartDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/car-parts")
    public ResponseEntity<List<CarPartDTO>> getAllCarParts(Pageable pageable) {
        log.debug("REST request to get a page of CarParts");
        Page<CarPartDTO> page = carPartService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/car-parts/{id}")
    public ResponseEntity<CarPartDTO> getCarPart(@PathVariable Long id) {
        log.debug("REST request to get CarPart : {}", id);
        Optional<CarPartDTO> carPartDTO = carPartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carPartDTO);
    }

    @DeleteMapping("/car-parts/{id}")
    public ResponseEntity<Void> deleteCarPart(@PathVariable Long id) {
        log.debug("REST request to delete CarPart : {}", id);
        carPartService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/car-makes/{make}/car-models/{model}/parts")
    public ResponseEntity<List<CarPartDTO>> getAllPartsByMakeAndModel(@PathVariable final String make, @PathVariable final String model,
                                                                @RequestParam final Optional<String> nameContaining,
                                                                @RequestParam final Optional<String> descriptionContaining) {
        log.debug("REST request to get a result of Parts");
        final List<CarPartDTO> result = carPartService.findAllPartsByMakeAndModel(make, model, nameContaining, descriptionContaining);
        return ResponseEntity.ok().body(result);
    }
}
