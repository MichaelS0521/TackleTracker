package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BaitDataTestSamples.*;
import static com.mycompany.myapp.domain.CreatedCatchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BaitDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaitData.class);
        BaitData baitData1 = getBaitDataSample1();
        BaitData baitData2 = new BaitData();
        assertThat(baitData1).isNotEqualTo(baitData2);

        baitData2.setId(baitData1.getId());
        assertThat(baitData1).isEqualTo(baitData2);

        baitData2 = getBaitDataSample2();
        assertThat(baitData1).isNotEqualTo(baitData2);
    }

    @Test
    void catchesTest() throws Exception {
        BaitData baitData = getBaitDataRandomSampleGenerator();
        CreatedCatch createdCatchBack = getCreatedCatchRandomSampleGenerator();

        baitData.addCatches(createdCatchBack);
        assertThat(baitData.getCatches()).containsOnly(createdCatchBack);
        assertThat(createdCatchBack.getBaitdata()).containsOnly(baitData);

        baitData.removeCatches(createdCatchBack);
        assertThat(baitData.getCatches()).doesNotContain(createdCatchBack);
        assertThat(createdCatchBack.getBaitdata()).doesNotContain(baitData);

        baitData.catches(new HashSet<>(Set.of(createdCatchBack)));
        assertThat(baitData.getCatches()).containsOnly(createdCatchBack);
        assertThat(createdCatchBack.getBaitdata()).containsOnly(baitData);

        baitData.setCatches(new HashSet<>());
        assertThat(baitData.getCatches()).doesNotContain(createdCatchBack);
        assertThat(createdCatchBack.getBaitdata()).doesNotContain(baitData);
    }
}
