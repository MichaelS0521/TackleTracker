package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BaitData;
import com.mycompany.myapp.repository.BaitDataRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BaitData}.
 */
@RestController
@RequestMapping("/api/bait-data")
@Transactional
public class BaitDataResource {

    private final Logger log = LoggerFactory.getLogger(BaitDataResource.class);

    private static final String ENTITY_NAME = "baitData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BaitDataRepository baitDataRepository;

    public BaitDataResource(BaitDataRepository baitDataRepository) {
        this.baitDataRepository = baitDataRepository;
    }

    /**
     * {@code POST  /bait-data} : Create a new baitData.
     *
     * @param baitData the baitData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baitData, or with status {@code 400 (Bad Request)} if the baitData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BaitData> createBaitData(@RequestBody BaitData baitData) throws URISyntaxException {
        log.debug("REST request to save BaitData : {}", baitData);
        if (baitData.getId() != null) {
            throw new BadRequestAlertException("A new baitData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        baitData = baitDataRepository.save(baitData);
        return ResponseEntity.created(new URI("/api/bait-data/" + baitData.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, baitData.getId().toString()))
            .body(baitData);
    }

    /**
     * {@code PUT  /bait-data/:id} : Updates an existing baitData.
     *
     * @param id the id of the baitData to save.
     * @param baitData the baitData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baitData,
     * or with status {@code 400 (Bad Request)} if the baitData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the baitData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaitData> updateBaitData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BaitData baitData
    ) throws URISyntaxException {
        log.debug("REST request to update BaitData : {}, {}", id, baitData);
        if (baitData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baitData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baitDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        baitData = baitDataRepository.save(baitData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baitData.getId().toString()))
            .body(baitData);
    }

    /**
     * {@code PATCH  /bait-data/:id} : Partial updates given fields of an existing baitData, field will ignore if it is null
     *
     * @param id the id of the baitData to save.
     * @param baitData the baitData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baitData,
     * or with status {@code 400 (Bad Request)} if the baitData is not valid,
     * or with status {@code 404 (Not Found)} if the baitData is not found,
     * or with status {@code 500 (Internal Server Error)} if the baitData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BaitData> partialUpdateBaitData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BaitData baitData
    ) throws URISyntaxException {
        log.debug("REST request to partial update BaitData partially : {}, {}", id, baitData);
        if (baitData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baitData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baitDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BaitData> result = baitDataRepository
            .findById(baitData.getId())
            .map(existingBaitData -> {
                if (baitData.getType() != null) {
                    existingBaitData.setType(baitData.getType());
                }
                if (baitData.getColor() != null) {
                    existingBaitData.setColor(baitData.getColor());
                }
                if (baitData.getHard() != null) {
                    existingBaitData.setHard(baitData.getHard());
                }
                if (baitData.getSoft() != null) {
                    existingBaitData.setSoft(baitData.getSoft());
                }

                return existingBaitData;
            })
            .map(baitDataRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, baitData.getId().toString())
        );
    }

    /**
     * {@code GET  /bait-data} : get all the baitData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baitData in body.
     */
    @GetMapping("")
    public List<BaitData> getAllBaitData() {
        log.debug("REST request to get all BaitData");
        return baitDataRepository.findAll();
    }

    /**
     * {@code GET  /bait-data/:id} : get the "id" baitData.
     *
     * @param id the id of the baitData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baitData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaitData> getBaitData(@PathVariable("id") Long id) {
        log.debug("REST request to get BaitData : {}", id);
        Optional<BaitData> baitData = baitDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(baitData);
    }

    /**
     * {@code DELETE  /bait-data/:id} : delete the "id" baitData.
     *
     * @param id the id of the baitData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBaitData(@PathVariable("id") Long id) {
        log.debug("REST request to delete BaitData : {}", id);
        baitDataRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
