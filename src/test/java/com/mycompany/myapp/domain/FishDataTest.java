package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CreatedCatchTestSamples.*;
import static com.mycompany.myapp.domain.FishDataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FishDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FishData.class);
        FishData fishData1 = getFishDataSample1();
        FishData fishData2 = new FishData();
        assertThat(fishData1).isNotEqualTo(fishData2);

        fishData2.setId(fishData1.getId());
        assertThat(fishData1).isEqualTo(fishData2);

        fishData2 = getFishDataSample2();
        assertThat(fishData1).isNotEqualTo(fishData2);
    }

    @Test
    void createdCatchTest() throws Exception {
        FishData fishData = getFishDataRandomSampleGenerator();
        CreatedCatch createdCatchBack = getCreatedCatchRandomSampleGenerator();

        fishData.setCreatedCatch(createdCatchBack);
        assertThat(fishData.getCreatedCatch()).isEqualTo(createdCatchBack);

        fishData.createdCatch(null);
        assertThat(fishData.getCreatedCatch()).isNull();
    }
}
