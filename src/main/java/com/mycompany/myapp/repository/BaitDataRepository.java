package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BaitData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BaitData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaitDataRepository extends JpaRepository<BaitData, Long> {}
