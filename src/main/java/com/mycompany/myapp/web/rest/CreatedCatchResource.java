package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CreatedCatch;
import com.mycompany.myapp.repository.CreatedCatchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CreatedCatch}.
 */
@RestController
@RequestMapping("/api/created-catches")
@Transactional
public class CreatedCatchResource {

    private final Logger log = LoggerFactory.getLogger(CreatedCatchResource.class);

    private static final String ENTITY_NAME = "createdCatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreatedCatchRepository createdCatchRepository;

    public CreatedCatchResource(CreatedCatchRepository createdCatchRepository) {
        this.createdCatchRepository = createdCatchRepository;
    }

    /**
     * {@code POST  /created-catches} : Create a new createdCatch.
     *
     * @param createdCatch the createdCatch to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new createdCatch, or with status {@code 400 (Bad Request)} if the createdCatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CreatedCatch> createCreatedCatch(@RequestBody CreatedCatch createdCatch) throws URISyntaxException {
        log.debug("REST request to save CreatedCatch : {}", createdCatch);
        if (createdCatch.getId() != null) {
            throw new BadRequestAlertException("A new createdCatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        createdCatch = createdCatchRepository.save(createdCatch);
        return ResponseEntity.created(new URI("/api/created-catches/" + createdCatch.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, createdCatch.getId().toString()))
            .body(createdCatch);
    }

    /**
     * {@code PUT  /created-catches/:id} : Updates an existing createdCatch.
     *
     * @param id the id of the createdCatch to save.
     * @param createdCatch the createdCatch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated createdCatch,
     * or with status {@code 400 (Bad Request)} if the createdCatch is not valid,
     * or with status {@code 500 (Internal Server Error)} if the createdCatch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CreatedCatch> updateCreatedCatch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CreatedCatch createdCatch
    ) throws URISyntaxException {
        log.debug("REST request to update CreatedCatch : {}, {}", id, createdCatch);
        if (createdCatch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, createdCatch.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!createdCatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        createdCatch = createdCatchRepository.save(createdCatch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, createdCatch.getId().toString()))
            .body(createdCatch);
    }

    /**
     * {@code PATCH  /created-catches/:id} : Partial updates given fields of an existing createdCatch, field will ignore if it is null
     *
     * @param id the id of the createdCatch to save.
     * @param createdCatch the createdCatch to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated createdCatch,
     * or with status {@code 400 (Bad Request)} if the createdCatch is not valid,
     * or with status {@code 404 (Not Found)} if the createdCatch is not found,
     * or with status {@code 500 (Internal Server Error)} if the createdCatch couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CreatedCatch> partialUpdateCreatedCatch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CreatedCatch createdCatch
    ) throws URISyntaxException {
        log.debug("REST request to partial update CreatedCatch partially : {}, {}", id, createdCatch);
        if (createdCatch.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, createdCatch.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!createdCatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CreatedCatch> result = createdCatchRepository
            .findById(createdCatch.getId())
            .map(existingCreatedCatch -> {
                if (createdCatch.getDatestamp() != null) {
                    existingCreatedCatch.setDatestamp(createdCatch.getDatestamp());
                }
                if (createdCatch.getLocation() != null) {
                    existingCreatedCatch.setLocation(createdCatch.getLocation());
                }

                return existingCreatedCatch;
            })
            .map(createdCatchRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, createdCatch.getId().toString())
        );
    }

    /**
     * {@code GET  /created-catches} : get all the createdCatches.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of createdCatches in body.
     */
    @GetMapping("")
    public List<CreatedCatch> getAllCreatedCatches(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("fish-is-null".equals(filter)) {
            log.debug("REST request to get all CreatedCatchs where fish is null");
            return StreamSupport.stream(createdCatchRepository.findAll().spliterator(), false)
                .filter(createdCatch -> createdCatch.getFish() == null)
                .toList();
        }
        log.debug("REST request to get all CreatedCatches");
        if (eagerload) {
            return createdCatchRepository.findAllWithEagerRelationships();
        } else {
            return createdCatchRepository.findAll();
        }
    }

    /**
     * {@code GET  /created-catches/:id} : get the "id" createdCatch.
     *
     * @param id the id of the createdCatch to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the createdCatch, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreatedCatch> getCreatedCatch(@PathVariable("id") Long id) {
        log.debug("REST request to get CreatedCatch : {}", id);
        Optional<CreatedCatch> createdCatch = createdCatchRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(createdCatch);
    }

    /**
     * {@code DELETE  /created-catches/:id} : delete the "id" createdCatch.
     *
     * @param id the id of the createdCatch to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreatedCatch(@PathVariable("id") Long id) {
        log.debug("REST request to delete CreatedCatch : {}", id);
        createdCatchRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
