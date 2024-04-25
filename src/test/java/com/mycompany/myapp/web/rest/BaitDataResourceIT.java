package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.BaitDataAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BaitData;
import com.mycompany.myapp.repository.BaitDataRepository;
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
 * Integration tests for the {@link BaitDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BaitDataResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HARD = false;
    private static final Boolean UPDATED_HARD = true;

    private static final Boolean DEFAULT_SOFT = false;
    private static final Boolean UPDATED_SOFT = true;

    private static final String ENTITY_API_URL = "/api/bait-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BaitDataRepository baitDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaitDataMockMvc;

    private BaitData baitData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaitData createEntity(EntityManager em) {
        BaitData baitData = new BaitData().type(DEFAULT_TYPE).color(DEFAULT_COLOR).hard(DEFAULT_HARD).soft(DEFAULT_SOFT);
        return baitData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaitData createUpdatedEntity(EntityManager em) {
        BaitData baitData = new BaitData().type(UPDATED_TYPE).color(UPDATED_COLOR).hard(UPDATED_HARD).soft(UPDATED_SOFT);
        return baitData;
    }

    @BeforeEach
    public void initTest() {
        baitData = createEntity(em);
    }

    @Test
    @Transactional
    void createBaitData() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BaitData
        var returnedBaitData = om.readValue(
            restBaitDataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baitData)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BaitData.class
        );

        // Validate the BaitData in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBaitDataUpdatableFieldsEquals(returnedBaitData, getPersistedBaitData(returnedBaitData));
    }

    @Test
    @Transactional
    void createBaitDataWithExistingId() throws Exception {
        // Create the BaitData with an existing ID
        baitData.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaitDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baitData)))
            .andExpect(status().isBadRequest());

        // Validate the BaitData in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBaitData() throws Exception {
        // Initialize the database
        baitDataRepository.saveAndFlush(baitData);

        // Get all the baitDataList
        restBaitDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baitData.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].hard").value(hasItem(DEFAULT_HARD.booleanValue())))
            .andExpect(jsonPath("$.[*].soft").value(hasItem(DEFAULT_SOFT.booleanValue())));
    }

    @Test
    @Transactional
    void getBaitData() throws Exception {
        // Initialize the database
        baitDataRepository.saveAndFlush(baitData);

        // Get the baitData
        restBaitDataMockMvc
            .perform(get(ENTITY_API_URL_ID, baitData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(baitData.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.hard").value(DEFAULT_HARD.booleanValue()))
            .andExpect(jsonPath("$.soft").value(DEFAULT_SOFT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingBaitData() throws Exception {
        // Get the baitData
        restBaitDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBaitData() throws Exception {
        // Initialize the database
        baitDataRepository.saveAndFlush(baitData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baitData
        BaitData updatedBaitData = baitDataRepository.findById(baitData.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBaitData are not directly saved in db
        em.detach(updatedBaitData);
        updatedBaitData.type(UPDATED_TYPE).color(UPDATED_COLOR).hard(UPDATED_HARD).soft(UPDATED_SOFT);

        restBaitDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBaitData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBaitData))
            )
            .andExpect(status().isOk());

        // Validate the BaitData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBaitDataToMatchAllProperties(updatedBaitData);
    }

    @Test
    @Transactional
    void putNonExistingBaitData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baitData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaitDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baitData.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baitData))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaitData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaitData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baitData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaitDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(baitData))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaitData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaitData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baitData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaitDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baitData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaitData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaitDataWithPatch() throws Exception {
        // Initialize the database
        baitDataRepository.saveAndFlush(baitData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baitData using partial update
        BaitData partialUpdatedBaitData = new BaitData();
        partialUpdatedBaitData.setId(baitData.getId());

        partialUpdatedBaitData.type(UPDATED_TYPE).color(UPDATED_COLOR).hard(UPDATED_HARD).soft(UPDATED_SOFT);

        restBaitDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaitData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBaitData))
            )
            .andExpect(status().isOk());

        // Validate the BaitData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBaitDataUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBaitData, baitData), getPersistedBaitData(baitData));
    }

    @Test
    @Transactional
    void fullUpdateBaitDataWithPatch() throws Exception {
        // Initialize the database
        baitDataRepository.saveAndFlush(baitData);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baitData using partial update
        BaitData partialUpdatedBaitData = new BaitData();
        partialUpdatedBaitData.setId(baitData.getId());

        partialUpdatedBaitData.type(UPDATED_TYPE).color(UPDATED_COLOR).hard(UPDATED_HARD).soft(UPDATED_SOFT);

        restBaitDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaitData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBaitData))
            )
            .andExpect(status().isOk());

        // Validate the BaitData in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBaitDataUpdatableFieldsEquals(partialUpdatedBaitData, getPersistedBaitData(partialUpdatedBaitData));
    }

    @Test
    @Transactional
    void patchNonExistingBaitData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baitData.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaitDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baitData.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baitData))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaitData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBaitData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baitData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaitDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baitData))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaitData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBaitData() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baitData.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaitDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(baitData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaitData in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaitData() throws Exception {
        // Initialize the database
        baitDataRepository.saveAndFlush(baitData);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the baitData
        restBaitDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, baitData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return baitDataRepository.count();
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

    protected BaitData getPersistedBaitData(BaitData baitData) {
        return baitDataRepository.findById(baitData.getId()).orElseThrow();
    }

    protected void assertPersistedBaitDataToMatchAllProperties(BaitData expectedBaitData) {
        assertBaitDataAllPropertiesEquals(expectedBaitData, getPersistedBaitData(expectedBaitData));
    }

    protected void assertPersistedBaitDataToMatchUpdatableProperties(BaitData expectedBaitData) {
        assertBaitDataAllUpdatablePropertiesEquals(expectedBaitData, getPersistedBaitData(expectedBaitData));
    }
}
