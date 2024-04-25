package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CreatedCatchAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CreatedCatch;
import com.mycompany.myapp.repository.CreatedCatchRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CreatedCatchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CreatedCatchResourceIT {

    private static final LocalDate DEFAULT_DATESTAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATESTAMP = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/created-catches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CreatedCatchRepository createdCatchRepository;

    @Mock
    private CreatedCatchRepository createdCatchRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreatedCatchMockMvc;

    private CreatedCatch createdCatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreatedCatch createEntity(EntityManager em) {
        CreatedCatch createdCatch = new CreatedCatch().datestamp(DEFAULT_DATESTAMP).location(DEFAULT_LOCATION);
        return createdCatch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreatedCatch createUpdatedEntity(EntityManager em) {
        CreatedCatch createdCatch = new CreatedCatch().datestamp(UPDATED_DATESTAMP).location(UPDATED_LOCATION);
        return createdCatch;
    }

    @BeforeEach
    public void initTest() {
        createdCatch = createEntity(em);
    }

    @Test
    @Transactional
    void createCreatedCatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CreatedCatch
        var returnedCreatedCatch = om.readValue(
            restCreatedCatchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(createdCatch)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CreatedCatch.class
        );

        // Validate the CreatedCatch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCreatedCatchUpdatableFieldsEquals(returnedCreatedCatch, getPersistedCreatedCatch(returnedCreatedCatch));
    }

    @Test
    @Transactional
    void createCreatedCatchWithExistingId() throws Exception {
        // Create the CreatedCatch with an existing ID
        createdCatch.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreatedCatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(createdCatch)))
            .andExpect(status().isBadRequest());

        // Validate the CreatedCatch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCreatedCatches() throws Exception {
        // Initialize the database
        createdCatchRepository.saveAndFlush(createdCatch);

        // Get all the createdCatchList
        restCreatedCatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(createdCatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].datestamp").value(hasItem(DEFAULT_DATESTAMP.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCreatedCatchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(createdCatchRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCreatedCatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(createdCatchRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCreatedCatchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(createdCatchRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCreatedCatchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(createdCatchRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCreatedCatch() throws Exception {
        // Initialize the database
        createdCatchRepository.saveAndFlush(createdCatch);

        // Get the createdCatch
        restCreatedCatchMockMvc
            .perform(get(ENTITY_API_URL_ID, createdCatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(createdCatch.getId().intValue()))
            .andExpect(jsonPath("$.datestamp").value(DEFAULT_DATESTAMP.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getNonExistingCreatedCatch() throws Exception {
        // Get the createdCatch
        restCreatedCatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCreatedCatch() throws Exception {
        // Initialize the database
        createdCatchRepository.saveAndFlush(createdCatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the createdCatch
        CreatedCatch updatedCreatedCatch = createdCatchRepository.findById(createdCatch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCreatedCatch are not directly saved in db
        em.detach(updatedCreatedCatch);
        updatedCreatedCatch.datestamp(UPDATED_DATESTAMP).location(UPDATED_LOCATION);

        restCreatedCatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCreatedCatch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCreatedCatch))
            )
            .andExpect(status().isOk());

        // Validate the CreatedCatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCreatedCatchToMatchAllProperties(updatedCreatedCatch);
    }

    @Test
    @Transactional
    void putNonExistingCreatedCatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        createdCatch.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreatedCatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, createdCatch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(createdCatch))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatedCatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreatedCatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        createdCatch.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreatedCatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(createdCatch))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatedCatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreatedCatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        createdCatch.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreatedCatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(createdCatch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreatedCatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCreatedCatchWithPatch() throws Exception {
        // Initialize the database
        createdCatchRepository.saveAndFlush(createdCatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the createdCatch using partial update
        CreatedCatch partialUpdatedCreatedCatch = new CreatedCatch();
        partialUpdatedCreatedCatch.setId(createdCatch.getId());

        partialUpdatedCreatedCatch.datestamp(UPDATED_DATESTAMP);

        restCreatedCatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreatedCatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCreatedCatch))
            )
            .andExpect(status().isOk());

        // Validate the CreatedCatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCreatedCatchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCreatedCatch, createdCatch),
            getPersistedCreatedCatch(createdCatch)
        );
    }

    @Test
    @Transactional
    void fullUpdateCreatedCatchWithPatch() throws Exception {
        // Initialize the database
        createdCatchRepository.saveAndFlush(createdCatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the createdCatch using partial update
        CreatedCatch partialUpdatedCreatedCatch = new CreatedCatch();
        partialUpdatedCreatedCatch.setId(createdCatch.getId());

        partialUpdatedCreatedCatch.datestamp(UPDATED_DATESTAMP).location(UPDATED_LOCATION);

        restCreatedCatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreatedCatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCreatedCatch))
            )
            .andExpect(status().isOk());

        // Validate the CreatedCatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCreatedCatchUpdatableFieldsEquals(partialUpdatedCreatedCatch, getPersistedCreatedCatch(partialUpdatedCreatedCatch));
    }

    @Test
    @Transactional
    void patchNonExistingCreatedCatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        createdCatch.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreatedCatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, createdCatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(createdCatch))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatedCatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreatedCatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        createdCatch.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreatedCatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(createdCatch))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatedCatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreatedCatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        createdCatch.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreatedCatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(createdCatch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreatedCatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCreatedCatch() throws Exception {
        // Initialize the database
        createdCatchRepository.saveAndFlush(createdCatch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the createdCatch
        restCreatedCatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, createdCatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return createdCatchRepository.count();
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

    protected CreatedCatch getPersistedCreatedCatch(CreatedCatch createdCatch) {
        return createdCatchRepository.findById(createdCatch.getId()).orElseThrow();
    }

    protected void assertPersistedCreatedCatchToMatchAllProperties(CreatedCatch expectedCreatedCatch) {
        assertCreatedCatchAllPropertiesEquals(expectedCreatedCatch, getPersistedCreatedCatch(expectedCreatedCatch));
    }

    protected void assertPersistedCreatedCatchToMatchUpdatableProperties(CreatedCatch expectedCreatedCatch) {
        assertCreatedCatchAllUpdatablePropertiesEquals(expectedCreatedCatch, getPersistedCreatedCatch(expectedCreatedCatch));
    }
}
