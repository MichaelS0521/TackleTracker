package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CreatedCatchTestSamples.*;
import static com.mycompany.myapp.domain.WeatherDataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WeatherDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeatherData.class);
        WeatherData weatherData1 = getWeatherDataSample1();
        WeatherData weatherData2 = new WeatherData();
        assertThat(weatherData1).isNotEqualTo(weatherData2);

        weatherData2.setId(weatherData1.getId());
        assertThat(weatherData1).isEqualTo(weatherData2);

        weatherData2 = getWeatherDataSample2();
        assertThat(weatherData1).isNotEqualTo(weatherData2);
    }

    @Test
    void createdCatchTest() throws Exception {
        WeatherData weatherData = getWeatherDataRandomSampleGenerator();
        CreatedCatch createdCatchBack = getCreatedCatchRandomSampleGenerator();

        weatherData.addCreatedCatch(createdCatchBack);
        assertThat(weatherData.getCreatedCatches()).containsOnly(createdCatchBack);
        assertThat(createdCatchBack.getWeather()).isEqualTo(weatherData);

        weatherData.removeCreatedCatch(createdCatchBack);
        assertThat(weatherData.getCreatedCatches()).doesNotContain(createdCatchBack);
        assertThat(createdCatchBack.getWeather()).isNull();

        weatherData.createdCatches(new HashSet<>(Set.of(createdCatchBack)));
        assertThat(weatherData.getCreatedCatches()).containsOnly(createdCatchBack);
        assertThat(createdCatchBack.getWeather()).isEqualTo(weatherData);

        weatherData.setCreatedCatches(new HashSet<>());
        assertThat(weatherData.getCreatedCatches()).doesNotContain(createdCatchBack);
        assertThat(createdCatchBack.getWeather()).isNull();
    }
}
