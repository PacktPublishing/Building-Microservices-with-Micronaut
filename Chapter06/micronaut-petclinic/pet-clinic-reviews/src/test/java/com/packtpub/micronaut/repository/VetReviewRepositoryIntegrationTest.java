package com.packtpub.micronaut.repository;

import com.packtpub.micronaut.domain.VetReview;
import com.packtpub.micronaut.utils.AbstractContainerBaseTest;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VetReviewRepositoryIntegrationTest extends AbstractContainerBaseTest {

    private VetReviewRepository vetReviewRepository;

    private VetReview vetReview;

    @BeforeAll
    void init() {
        ApplicationContext context = ApplicationContext.run(
            PropertySource.of("test", Map.of("mongodb.uri", MONGO_DB_CONTAINER.getReplicaSetUrl()))
        );
        vetReviewRepository = context.getBean(VetReviewRepository.class);
        vetReview = this.createEntity();
    }

    @BeforeEach
    public void initTest() {
        if (!MONGO_DB_CONTAINER.isRunning()) {
            MONGO_DB_CONTAINER.start();
        }
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public VetReview createEntity() {
        VetReview vetReview = new VetReview();
        vetReview.setReviewId(UUID.randomUUID().toString());
        vetReview.setDateAdded(LocalDate.now());
        vetReview.setComment("Decent service");
        vetReview.setRating(3D);
        return vetReview;
    }

    @Test
    @Order(1)
    public void saveVetReview() {
        String reviewId = vetReview.getReviewId();

        // Save vet review
        vetReviewRepository.save(vetReview);

        // Validate the vet review in the database
        VetReview savedVetReview = vetReviewRepository.findByReviewId(vetReview.getReviewId());

        assertThat(savedVetReview).isNotNull();
        assertThat(savedVetReview.getReviewId()).isEqualTo(reviewId);
    }

    @Test
    @Order(2)
    public void findVetReviews() {
        List<VetReview> vetReviews = vetReviewRepository.findAll();

        // Validate vet reviews
        assertThat(vetReviews).isNotNull();
        assertThat(vetReviews).isNotEmpty();
    }

    @Test
    @Order(3)
    public void findVetReviewByReviewId() {
        VetReview fetchedVetReview = vetReviewRepository.findByReviewId(vetReview.getReviewId());

        // Validate vet review
        assertThat(fetchedVetReview).isNotNull();
        assertThat(fetchedVetReview.getReviewId()).isEqualTo(vetReview.getReviewId());
    }

    @Test
    @Order(4)
    public void deleteVetReview() {
        int databaseSizeBeforeDelete = vetReviewRepository.findAll().size();

        // Delete vet review
        vetReviewRepository.delete(vetReview.getReviewId());

        // Validate that vet review is deleted
        int databaseSizeAfterDelete = vetReviewRepository.findAll().size();
        assertThat(databaseSizeAfterDelete).isEqualTo(databaseSizeBeforeDelete - 1);
    }
}
