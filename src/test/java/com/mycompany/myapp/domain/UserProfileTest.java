package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CreatedCatchTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void catchesTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        CreatedCatch createdCatchBack = getCreatedCatchRandomSampleGenerator();

        userProfile.addCatches(createdCatchBack);
        assertThat(userProfile.getCatches()).containsOnly(createdCatchBack);
        assertThat(createdCatchBack.getUser()).isEqualTo(userProfile);

        userProfile.removeCatches(createdCatchBack);
        assertThat(userProfile.getCatches()).doesNotContain(createdCatchBack);
        assertThat(createdCatchBack.getUser()).isNull();

        userProfile.catches(new HashSet<>(Set.of(createdCatchBack)));
        assertThat(userProfile.getCatches()).containsOnly(createdCatchBack);
        assertThat(createdCatchBack.getUser()).isEqualTo(userProfile);

        userProfile.setCatches(new HashSet<>());
        assertThat(userProfile.getCatches()).doesNotContain(createdCatchBack);
        assertThat(createdCatchBack.getUser()).isNull();
    }
}
