package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.FishDataAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FishData;
import com.mycompany.myapp.repository.FishDataRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link FishDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FishDataResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WATER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_WATER_TYPE = "BBBBBBBBBB";

    private static final Float DEFAULT_WEIGHT = 1F;
    private static final Float UPDATED_WEIGHT = 2F;

    private static final String ENTITY_API_URL = "/api/fish-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FishDataRepository fishDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFishDataMockMvc;

    private FishData fishData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FishData createEntity(EntityManager em) {
        FishData fishData = new FishData().name(DEFAULT_NAME).waterType(DEFAULT_WATER_TYPE).weight(DEFAULT_WEIGHT);
        return fishData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FishData createUpdatedEntity(EntityManager em) {
        FishData fishData = new FishData().name(UPDATED_NAME).waterType(UPDATED_WATER_TYPE).weight(UPDATED_WEIGHT);
        return fishData;
    }

    @BeforeEach
    public void initTest() {
        fishData = createEntity(em);
    }

    @Test
    @Transactional
    void createFishData() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FishData
        var returnedFishData = om.readValue(
            restFishDataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fishData)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FishData.class
        );

        // Validate the FishData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFishDataUpdatableFieldsEquals(returnedFishData, getPersistedFishData(returnedFishData));
    }

    @Test
    @Transactional
    void createFishDataWithExistingId() throws Exception {
        // Create the FishData with an existing ID
        fishData.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFishDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fishData)))
            .andExpect(status().isBadRequest());

        // Validate the FishData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFishData() throws Exception {
        // Initialize the database
        fishDataRepository.saveAndFlush(fishData);

        // Get all the fishDataList
        restFishDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fishData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].waterType").value(hasItem(DEFAULT_WATER_TYPE)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    void getFishData() throws Exception {
        // Initialize the database
        fishDataRepository.saveAndFlush(fishData);

        // Get the fishData
        restFishDataMockMvc
            .perform(get(ENTITY_API_URL_ID, fishData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fishData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.waterType").value(DEFAULT_WATER_TYPE))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFishData() throws Exception {
        // Get the fishData
        restFishDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFishData() throws Exception {
        // Initialize the database
        fishDataRepository.saveAndFlush(fishData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fishData
        FishData updatedFishData = fishDataRepository.findById(fishData.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFishData are not directly saved in db
        em.detach(updatedFishData);
        updatedFishData.name(UPDATED_NAME).waterType(UPDATED_WATER_TYPE).weight(UPDATED_WEIGHT);

        restFishDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFishData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFishData))
            )
            .andExpect(status().isOk());

        // Validate the FishData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFishDataToMatchAllProperties(updatedFishData);
    }

    @Test
    @Transactional
    void putNonExistingFishData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fishData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFishDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fishData.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fishData))
            )
            .andExpect(status().isBadRequest());

        // Validate the FishData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFishData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fishData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFishDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fishData))
            )
            .andExpect(status().isBadRequest());

        // Validate the FishData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFishData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fishData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFishDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fishData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FishData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFishDataWithPatch() throws Exception {
        // Initialize the database
        fishDataRepository.saveAndFlush(fishData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fishData using partial update
        FishData partialUpdatedFishData = new FishData();
        partialUpdatedFishData.setId(fishData.getId());

        partialUpdatedFishData.name(UPDATED_NAME);

        restFishDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFishData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFishData))
            )
            .andExpect(status().isOk());

        // Validate the FishData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFishDataUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFishData, fishData), getPersistedFishData(fishData));
    }

    @Test
    @Transactional
    void fullUpdateFishDataWithPatch() throws Exception {
        // Initialize the database
        fishDataRepository.saveAndFlush(fishData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fishData using partial update
        FishData partialUpdatedFishData = new FishData();
        partialUpdatedFishData.setId(fishData.getId());

        partialUpdatedFishData.name(UPDATED_NAME).waterType(UPDATED_WATER_TYPE).weight(UPDATED_WEIGHT);

        restFishDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFishData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFishData))
            )
            .andExpect(status().isOk());

        // Validate the FishData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFishDataUpdatableFieldsEquals(partialUpdatedFishData, getPersistedFishData(partialUpdatedFishData));
    }

    @Test
    @Transactional
    void patchNonExistingFishData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fishData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFishDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fishData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fishData))
            )
            .andExpect(status().isBadRequest());

        // Validate the FishData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFishData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fishData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFishDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fishData))
            )
            .andExpect(status().isBadRequest());

        // Validate the FishData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFishData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fishData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFishDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fishData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FishData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFishData() throws Exception {
        // Initialize the database
        fishDataRepository.saveAndFlush(fishData);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fishData
        restFishDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, fishData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fishDataRepository.count();
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

    protected FishData getPersistedFishData(FishData fishData) {
        return fishDataRepository.findById(fishData.getId()).orElseThrow();
    }

    protected void assertPersistedFishDataToMatchAllProperties(FishData expectedFishData) {
        assertFishDataAllPropertiesEquals(expectedFishData, getPersistedFishData(expectedFishData));
    }

    protected void assertPersistedFishDataToMatchUpdatableProperties(FishData expectedFishData) {
        assertFishDataAllUpdatablePropertiesEquals(expectedFishData, getPersistedFishData(expectedFishData));
    }
}
