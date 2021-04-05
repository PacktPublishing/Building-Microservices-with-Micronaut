package com.packtpub.micronaut.domain;

import com.packtpub.micronaut.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Owner.class);
        Owner owner1 = new Owner();
        owner1.setId(1L);
        Owner owner2 = new Owner();
        owner2.setId(owner1.getId());
        assertThat(owner1).isEqualTo(owner2);
        owner2.setId(2L);
        assertThat(owner1).isNotEqualTo(owner2);
        owner1.setId(null);
        assertThat(owner1).isNotEqualTo(owner2);
    }
}
