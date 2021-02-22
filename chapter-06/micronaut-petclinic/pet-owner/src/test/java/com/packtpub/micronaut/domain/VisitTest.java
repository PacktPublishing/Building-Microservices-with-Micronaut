package com.packtpub.micronaut.domain;

import com.packtpub.micronaut.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VisitTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visit.class);
        Visit visit1 = new Visit();
        visit1.setId(1L);
        Visit visit2 = new Visit();
        visit2.setId(visit1.getId());
        assertThat(visit1).isEqualTo(visit2);
        visit2.setId(2L);
        assertThat(visit1).isNotEqualTo(visit2);
        visit1.setId(null);
        assertThat(visit1).isNotEqualTo(visit2);
    }
}
