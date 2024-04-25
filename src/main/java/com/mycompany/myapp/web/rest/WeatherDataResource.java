package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.WeatherData;
import com.mycompany.myapp.repository.WeatherDataRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WeatherData}.
 */
@RestController
@RequestMapping("/api/weather-data")
@Transactional
public class WeatherDataResource {

    private final Logger log = LoggerFactory.getLogger(WeatherDataResource.class);

    private static final String ENTITY_NAME = "weatherData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeatherDataRepository weatherDataRepository;

    public WeatherDataResource(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    /**
     * {@code POST  /weather-data} : Create a new weatherData.
     *
     * @param weatherData the weatherData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weatherData, or with status {@code 400 (Bad Request)} if the weatherData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WeatherData> createWeatherData(@RequestBody WeatherData weatherData) throws URISyntaxException {
        log.debug("REST request to save WeatherData : {}", weatherData);
        if (weatherData.getId() != null) {
            throw new BadRequestAlertException("A new weatherData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        weatherData = weatherDataRepository.save(weatherData);
        return ResponseEntity.created(new URI("/api/weather-data/" + weatherData.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, weatherData.getId().toString()))
            .body(weatherData);
    }

    /**
     * {@code PUT  /weather-data/:id} : Updates an existing weatherData.
     *
     * @param id the id of the weatherData to save.
     * @param weatherData the weatherData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weatherData,
     * or with status {@code 400 (Bad Request)} if the weatherData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weatherData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WeatherData> updateWeatherData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeatherData weatherData
    ) throws URISyntaxException {
        log.debug("REST request to update WeatherData : {}, {}", id, weatherData);
        if (weatherData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weatherData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weatherDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        weatherData = weatherDataRepository.save(weatherData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weatherData.getId().toString()))
            .body(weatherData);
    }

    /**
     * {@code PATCH  /weather-data/:id} : Partial updates given fields of an existing weatherData, field will ignore if it is null
     *
     * @param id the id of the weatherData to save.
     * @param weatherData the weatherData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weatherData,
     * or with status {@code 400 (Bad Request)} if the weatherData is not valid,
     * or with status {@code 404 (Not Found)} if the weatherData is not found,
     * or with status {@code 500 (Internal Server Error)} if the weatherData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeatherData> partialUpdateWeatherData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeatherData weatherData
    ) throws URISyntaxException {
        log.debug("REST request to partial update WeatherData partially : {}, {}", id, weatherData);
        if (weatherData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weatherData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weatherDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeatherData> result = weatherDataRepository
            .findById(weatherData.getId())
            .map(existingWeatherData -> {
                if (weatherData.getCondition() != null) {
                    existingWeatherData.setCondition(weatherData.getCondition());
                }
                if (weatherData.getTemperature() != null) {
                    existingWeatherData.setTemperature(weatherData.getTemperature());
                }
                if (weatherData.getDateStamp() != null) {
                    existingWeatherData.setDateStamp(weatherData.getDateStamp());
                }

                return existingWeatherData;
            })
            .map(weatherDataRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, weatherData.getId().toString())
        );
    }

    /**
     * {@code GET  /weather-data} : get all the weatherData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weatherData in body.
     */
    @GetMapping("")
    public List<WeatherData> getAllWeatherData() {
        log.debug("REST request to get all WeatherData");
        return weatherDataRepository.findAll();
    }

    /**
     * {@code GET  /weather-data/:id} : get the "id" weatherData.
     *
     * @param id the id of the weatherData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weatherData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WeatherData> getWeatherData(@PathVariable("id") Long id) {
        log.debug("REST request to get WeatherData : {}", id);
        Optional<WeatherData> weatherData = weatherDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(weatherData);
    }

    /**
     * {@code DELETE  /weather-data/:id} : delete the "id" weatherData.
     *
     * @param id the id of the weatherData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeatherData(@PathVariable("id") Long id) {
        log.debug("REST request to delete WeatherData : {}", id);
        weatherDataRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
