package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WeatherData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WeatherData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {}
