package com.packtpub.micronaut.domain;

import com.packtpub.micronaut.util.TestUtil;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class PetTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PetType.class);
        PetType petType1 = new PetType();
        petType1.setId(1L);
        PetType petType2 = new PetType();
        petType2.setId(petType1.getId());
        assertThat(petType1).isEqualTo(petType2);
        petType2.setId(2L);
        assertThat(petType1).isNotEqualTo(petType2);
        petType1.setId(null);
        assertThat(petType1).isNotEqualTo(petType2);
    }
}
