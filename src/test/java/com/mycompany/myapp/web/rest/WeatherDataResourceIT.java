package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.WeatherDataAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WeatherData;
import com.mycompany.myapp.repository.WeatherDataRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WeatherDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeatherDataResourceIT {

    private static final String DEFAULT_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION = "BBBBBBBBBB";

    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    private static final LocalDate DEFAULT_DATE_STAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_STAMP = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/weather-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeatherDataMockMvc;

    private WeatherData weatherData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeatherData createEntity(EntityManager em) {
        WeatherData weatherData = new WeatherData()
            .condition(DEFAULT_CONDITION)
            .temperature(DEFAULT_TEMPERATURE)
            .dateStamp(DEFAULT_DATE_STAMP);
        return weatherData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeatherData createUpdatedEntity(EntityManager em) {
        WeatherData weatherData = new WeatherData()
            .condition(UPDATED_CONDITION)
            .temperature(UPDATED_TEMPERATURE)
            .dateStamp(UPDATED_DATE_STAMP);
        return weatherData;
    }

    @BeforeEach
    public void initTest() {
        weatherData = createEntity(em);
    }

    @Test
    @Transactional
    void createWeatherData() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WeatherData
        var returnedWeatherData = om.readValue(
            restWeatherDataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weatherData)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WeatherData.class
        );

        // Validate the WeatherData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWeatherDataUpdatableFieldsEquals(returnedWeatherData, getPersistedWeatherData(returnedWeatherData));
    }

    @Test
    @Transactional
    void createWeatherDataWithExistingId() throws Exception {
        // Create the WeatherData with an existing ID
        weatherData.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeatherDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weatherData)))
            .andExpect(status().isBadRequest());

        // Validate the WeatherData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWeatherData() throws Exception {
        // Initialize the database
        weatherDataRepository.saveAndFlush(weatherData);

        // Get all the weatherDataList
        restWeatherDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weatherData.getId().intValue())))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION)))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateStamp").value(hasItem(DEFAULT_DATE_STAMP.toString())));
    }

    @Test
    @Transactional
    void getWeatherData() throws Exception {
        // Initialize the database
        weatherDataRepository.saveAndFlush(weatherData);

        // Get the weatherData
        restWeatherDataMockMvc
            .perform(get(ENTITY_API_URL_ID, weatherData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weatherData.getId().intValue()))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.dateStamp").value(DEFAULT_DATE_STAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWeatherData() throws Exception {
        // Get the weatherData
        restWeatherDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWeatherData() throws Exception {
        // Initialize the database
        weatherDataRepository.saveAndFlush(weatherData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weatherData
        WeatherData updatedWeatherData = weatherDataRepository.findById(weatherData.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWeatherData are not directly saved in db
        em.detach(updatedWeatherData);
        updatedWeatherData.condition(UPDATED_CONDITION).temperature(UPDATED_TEMPERATURE).dateStamp(UPDATED_DATE_STAMP);

        restWeatherDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWeatherData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWeatherData))
            )
            .andExpect(status().isOk());

        // Validate the WeatherData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWeatherDataToMatchAllProperties(updatedWeatherData);
    }

    @Test
    @Transactional
    void putNonExistingWeatherData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weatherData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeatherDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weatherData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(weatherData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeatherData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weatherData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(weatherData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeatherData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weatherData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weatherData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeatherData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeatherDataWithPatch() throws Exception {
        // Initialize the database
        weatherDataRepository.saveAndFlush(weatherData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weatherData using partial update
        WeatherData partialUpdatedWeatherData = new WeatherData();
        partialUpdatedWeatherData.setId(weatherData.getId());

        partialUpdatedWeatherData.condition(UPDATED_CONDITION).dateStamp(UPDATED_DATE_STAMP);

        restWeatherDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeatherData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWeatherData))
            )
            .andExpect(status().isOk());

        // Validate the WeatherData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWeatherDataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWeatherData, weatherData),
            getPersistedWeatherData(weatherData)
        );
    }

    @Test
    @Transactional
    void fullUpdateWeatherDataWithPatch() throws Exception {
        // Initialize the database
        weatherDataRepository.saveAndFlush(weatherData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weatherData using partial update
        WeatherData partialUpdatedWeatherData = new WeatherData();
        partialUpdatedWeatherData.setId(weatherData.getId());

        partialUpdatedWeatherData.condition(UPDATED_CONDITION).temperature(UPDATED_TEMPERATURE).dateStamp(UPDATED_DATE_STAMP);

        restWeatherDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeatherData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWeatherData))
            )
            .andExpect(status().isOk());

        // Validate the WeatherData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWeatherDataUpdatableFieldsEquals(partialUpdatedWeatherData, getPersistedWeatherData(partialUpdatedWeatherData));
    }

    @Test
    @Transactional
    void patchNonExistingWeatherData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weatherData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeatherDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weatherData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(weatherData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeatherData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weatherData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(weatherData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeatherData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weatherData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(weatherData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeatherData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeatherData() throws Exception {
        // Initialize the database
        weatherDataRepository.saveAndFlush(weatherData);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the weatherData
        restWeatherDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, weatherData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return weatherDataRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected WeatherData getPersistedWeatherData(WeatherData weatherData) {
        return weatherDataRepository.findById(weatherData.getId()).orElseThrow();
    }

    protected void assertPersistedWeatherDataToMatchAllProperties(WeatherData expectedWeatherData) {
        assertWeatherDataAllPropertiesEquals(expectedWeatherData, getPersistedWeatherData(expectedWeatherData));
    }

    protected void assertPersistedWeatherDataToMatchUpdatableProperties(WeatherData expectedWeatherData) {
        assertWeatherDataAllUpdatablePropertiesEquals(expectedWeatherData, getPersistedWeatherData(expectedWeatherData));
    }
}
