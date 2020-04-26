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
import pl.ailux.carparts.service.PartSellingArgumentService;
import pl.ailux.carparts.service.dto.PartSellingArgumentDTO;
import pl.ailux.carparts.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PartSellingArgumentResource {

    private final Logger log = LoggerFactory.getLogger(PartSellingArgumentResource.class);

    private static final String ENTITY_NAME = "carPartsPartSellingArgument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartSellingArgumentService partSellingArgumentService;

    public PartSellingArgumentResource(final PartSellingArgumentService partSellingArgumentService) {
        this.partSellingArgumentService = partSellingArgumentService;
    }

    @PostMapping("/car-parts/{partId}/part-selling-arguments")
    public ResponseEntity<PartSellingArgumentDTO> createPartSellingArgument(@PathVariable final Long partId,
                                                                            @Valid @RequestBody final PartSellingArgumentDTO partSellingArgumentDTO) throws URISyntaxException {
        log.debug("REST request to save PartSellingArgument : {}", partSellingArgumentDTO);
        if (partSellingArgumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new partSellingArgument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        final PartSellingArgumentDTO result = partSellingArgumentService.saveForPart(partId, partSellingArgumentDTO);
        return ResponseEntity.created(new URI("/api/part-selling-arguments/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/car-parts/{partId}/part-selling-arguments")
    public ResponseEntity<PartSellingArgumentDTO> updatePartSellingArgument(@PathVariable final Long partId,
                                                                            @Valid @RequestBody final PartSellingArgumentDTO partSellingArgumentDTO) throws URISyntaxException {
        log.debug("REST request to update PartSellingArgument : {}", partSellingArgumentDTO);
        if (partSellingArgumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        final PartSellingArgumentDTO result = partSellingArgumentService.saveForPart(partId, partSellingArgumentDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partSellingArgumentDTO.getId().toString()))
                .body(result);
    }

    @GetMapping("/part-selling-arguments")
    public ResponseEntity<List<PartSellingArgumentDTO>> getAllPartSellingArguments(final Pageable pageable) {
        log.debug("REST request to get a page of PartSellingArguments");
        final Page<PartSellingArgumentDTO> page = partSellingArgumentService.findAll(pageable);
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/part-selling-arguments/{id}")
    public ResponseEntity<PartSellingArgumentDTO> getPartSellingArgument(@PathVariable final Long id) {
        log.debug("REST request to get PartSellingArgument : {}", id);
        final Optional<PartSellingArgumentDTO> partSellingArgumentDTO = partSellingArgumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partSellingArgumentDTO);
    }

    @DeleteMapping("/part-selling-arguments/{id}")
    public ResponseEntity<Void> deletePartSellingArgument(@PathVariable final Long id) {
        log.debug("REST request to delete PartSellingArgument : {}", id);
        partSellingArgumentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
