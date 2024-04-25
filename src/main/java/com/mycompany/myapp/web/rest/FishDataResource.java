package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FishData;
import com.mycompany.myapp.repository.FishDataRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.FishData}.
 */
@RestController
@RequestMapping("/api/fish-data")
@Transactional
public class FishDataResource {

    private final Logger log = LoggerFactory.getLogger(FishDataResource.class);

    private static final String ENTITY_NAME = "fishData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FishDataRepository fishDataRepository;

    public FishDataResource(FishDataRepository fishDataRepository) {
        this.fishDataRepository = fishDataRepository;
    }

    /**
     * {@code POST  /fish-data} : Create a new fishData.
     *
     * @param fishData the fishData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fishData, or with status {@code 400 (Bad Request)} if the fishData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FishData> createFishData(@RequestBody FishData fishData) throws URISyntaxException {
        log.debug("REST request to save FishData : {}", fishData);
        if (fishData.getId() != null) {
            throw new BadRequestAlertException("A new fishData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fishData = fishDataRepository.save(fishData);
        return ResponseEntity.created(new URI("/api/fish-data/" + fishData.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, fishData.getId().toString()))
            .body(fishData);
    }

    /**
     * {@code PUT  /fish-data/:id} : Updates an existing fishData.
     *
     * @param id the id of the fishData to save.
     * @param fishData the fishData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fishData,
     * or with status {@code 400 (Bad Request)} if the fishData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fishData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FishData> updateFishData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FishData fishData
    ) throws URISyntaxException {
        log.debug("REST request to update FishData : {}, {}", id, fishData);
        if (fishData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fishData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fishDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fishData = fishDataRepository.save(fishData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fishData.getId().toString()))
            .body(fishData);
    }

    /**
     * {@code PATCH  /fish-data/:id} : Partial updates given fields of an existing fishData, field will ignore if it is null
     *
     * @param id the id of the fishData to save.
     * @param fishData the fishData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fishData,
     * or with status {@code 400 (Bad Request)} if the fishData is not valid,
     * or with status {@code 404 (Not Found)} if the fishData is not found,
     * or with status {@code 500 (Internal Server Error)} if the fishData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FishData> partialUpdateFishData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FishData fishData
    ) throws URISyntaxException {
        log.debug("REST request to partial update FishData partially : {}, {}", id, fishData);
        if (fishData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fishData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fishDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FishData> result = fishDataRepository
            .findById(fishData.getId())
            .map(existingFishData -> {
                if (fishData.getName() != null) {
                    existingFishData.setName(fishData.getName());
                }
                if (fishData.getWaterType() != null) {
                    existingFishData.setWaterType(fishData.getWaterType());
                }
                if (fishData.getWeight() != null) {
                    existingFishData.setWeight(fishData.getWeight());
                }

                return existingFishData;
            })
            .map(fishDataRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fishData.getId().toString())
        );
    }

    /**
     * {@code GET  /fish-data} : get all the fishData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fishData in body.
     */
    @GetMapping("")
    public List<FishData> getAllFishData() {
        log.debug("REST request to get all FishData");
        return fishDataRepository.findAll();
    }

    /**
     * {@code GET  /fish-data/:id} : get the "id" fishData.
     *
     * @param id the id of the fishData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fishData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FishData> getFishData(@PathVariable("id") Long id) {
        log.debug("REST request to get FishData : {}", id);
        Optional<FishData> fishData = fishDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fishData);
    }

    /**
     * {@code DELETE  /fish-data/:id} : delete the "id" fishData.
     *
     * @param id the id of the fishData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFishData(@PathVariable("id") Long id) {
        log.debug("REST request to delete FishData : {}", id);
        fishDataRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
