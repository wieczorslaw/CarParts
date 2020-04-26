package pl.ailux.carparts.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.ailux.carparts.service.PartServiceActionService;
import pl.ailux.carparts.service.dto.PartServiceActionDTO;
import pl.ailux.carparts.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PartServiceActionResource {

    private final Logger log = LoggerFactory.getLogger(PartServiceActionResource.class);

    private static final String ENTITY_NAME = "carPartsPartServiceAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartServiceActionService partServiceActionService;

    public PartServiceActionResource(final PartServiceActionService partServiceActionService) {
        this.partServiceActionService = partServiceActionService;
    }

    @PostMapping("/car-parts/{partId}/part-service-actions")
    public ResponseEntity<PartServiceActionDTO> createPartServiceAction(@PathVariable final Long partId,
                                                                        @Valid @RequestBody final PartServiceActionDTO partServiceActionDTO) throws URISyntaxException {
        log.debug("REST request to save PartServiceAction : {}", partServiceActionDTO);
        if (partServiceActionDTO.getId() != null) {
            throw new BadRequestAlertException("A new partServiceAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        final PartServiceActionDTO result = partServiceActionService.saveForPart(partId, partServiceActionDTO);
        return ResponseEntity.created(new URI("/api/part-service-actions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/car-parts/{partId}/part-service-actions")
    public ResponseEntity<PartServiceActionDTO> updatePartServiceAction(@PathVariable final Long partId,
                                                                        @Valid @RequestBody final PartServiceActionDTO partServiceActionDTO) throws URISyntaxException {
        log.debug("REST request to update PartServiceAction : {}", partServiceActionDTO);
        if (partServiceActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        final PartServiceActionDTO result = partServiceActionService.saveForPart(partId, partServiceActionDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partServiceActionDTO.getId().toString()))
                .body(result);
    }

    @GetMapping("/part-service-actions")
    public ResponseEntity<List<PartServiceActionDTO>> getAllPartServiceActions(final Pageable pageable) {
        log.debug("REST request to get a page of PartServiceActions");
        final Page<PartServiceActionDTO> page = partServiceActionService.findAll(pageable);
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/part-service-actions/{id}")
    public ResponseEntity<PartServiceActionDTO> getPartServiceAction(@PathVariable final Long id) {
        log.debug("REST request to get PartServiceAction : {}", id);
        final Optional<PartServiceActionDTO> partServiceActionDTO = partServiceActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partServiceActionDTO);
    }

    @DeleteMapping("/part-service-actions/{id}")
    public ResponseEntity<Void> deletePartServiceAction(@PathVariable final Long id) {
        log.debug("REST request to delete PartServiceAction : {}", id);
        partServiceActionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/service-actions/date-range")
    public ResponseEntity<List<PartServiceActionDTO>> getAllServiceActionsByDateRange(final Instant startDate, final Instant endDate, final Pageable pageable) {
        log.debug("REST request to get a page of ServiceActions");
        final Page<PartServiceActionDTO> page = partServiceActionService.findAllByDateRange(startDate, endDate, pageable);
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
