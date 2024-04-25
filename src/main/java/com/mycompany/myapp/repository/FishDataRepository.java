package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FishData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FishData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FishDataRepository extends JpaRepository<FishData, Long> {}
