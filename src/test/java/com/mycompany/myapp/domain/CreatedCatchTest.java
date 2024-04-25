package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BaitDataTestSamples.*;
import static com.mycompany.myapp.domain.CreatedCatchTestSamples.*;
import static com.mycompany.myapp.domain.FishDataTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static com.mycompany.myapp.domain.WeatherDataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CreatedCatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreatedCatch.class);
        CreatedCatch createdCatch1 = getCreatedCatchSample1();
        CreatedCatch createdCatch2 = new CreatedCatch();
        assertThat(createdCatch1).isNotEqualTo(createdCatch2);

        createdCatch2.setId(createdCatch1.getId());
        assertThat(createdCatch1).isEqualTo(createdCatch2);

        createdCatch2 = getCreatedCatchSample2();
        assertThat(createdCatch1).isNotEqualTo(createdCatch2);
    }

    @Test
    void baitdataTest() throws Exception {
        CreatedCatch createdCatch = getCreatedCatchRandomSampleGenerator();
        BaitData baitDataBack = getBaitDataRandomSampleGenerator();

        createdCatch.addBaitdata(baitDataBack);
        assertThat(createdCatch.getBaitdata()).containsOnly(baitDataBack);

        createdCatch.removeBaitdata(baitDataBack);
        assertThat(createdCatch.getBaitdata()).doesNotContain(baitDataBack);

        createdCatch.baitdata(new HashSet<>(Set.of(baitDataBack)));
        assertThat(createdCatch.getBaitdata()).containsOnly(baitDataBack);

        createdCatch.setBaitdata(new HashSet<>());
        assertThat(createdCatch.getBaitdata()).doesNotContain(baitDataBack);
    }

    @Test
    void fishTest() throws Exception {
        CreatedCatch createdCatch = getCreatedCatchRandomSampleGenerator();
        FishData fishDataBack = getFishDataRandomSampleGenerator();

        createdCatch.setFish(fishDataBack);
        assertThat(createdCatch.getFish()).isEqualTo(fishDataBack);
        assertThat(fishDataBack.getCreatedCatch()).isEqualTo(createdCatch);

        createdCatch.fish(null);
        assertThat(createdCatch.getFish()).isNull();
        assertThat(fishDataBack.getCreatedCatch()).isNull();
    }

    @Test
    void weatherTest() throws Exception {
        CreatedCatch createdCatch = getCreatedCatchRandomSampleGenerator();
        WeatherData weatherDataBack = getWeatherDataRandomSampleGenerator();

        createdCatch.setWeather(weatherDataBack);
        assertThat(createdCatch.getWeather()).isEqualTo(weatherDataBack);

        createdCatch.weather(null);
        assertThat(createdCatch.getWeather()).isNull();
    }

    @Test
    void userTest() throws Exception {
        CreatedCatch createdCatch = getCreatedCatchRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        createdCatch.setUser(userProfileBack);
        assertThat(createdCatch.getUser()).isEqualTo(userProfileBack);

        createdCatch.user(null);
        assertThat(createdCatch.getUser()).isNull();
    }
}
