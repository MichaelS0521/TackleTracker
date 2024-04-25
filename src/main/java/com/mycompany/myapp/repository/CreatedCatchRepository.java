package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CreatedCatch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CreatedCatch entity.
 *
 * When extending this class, extend CreatedCatchRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CreatedCatchRepository extends CreatedCatchRepositoryWithBagRelationships, JpaRepository<CreatedCatch, Long> {
    default Optional<CreatedCatch> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<CreatedCatch> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<CreatedCatch> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
