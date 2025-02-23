package com.yuzukiku.dummy_reserve_log.infrastructure;

import com.yuzukiku.dummy_reserve_log.domain.entity.CampFacilities;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class JpaCampFacilityRepositoryTest {

    @Autowired
    private JpaCampFacilityRepository repository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @ServiceConnection
        PostgreSQLContainer<?> postgresContainer() {
            return new PostgreSQLContainer<>("postgres:latest");
        }
    }

    @Test
    public void givenValidCampFacility_whenSaved_thenCanBeFound() {
        UUID campId = UUID.randomUUID();
        UUID facilityId = UUID.randomUUID();
        // CampFacilitiesの@Builderを活用
        CampFacilities campFacilities = CampFacilities.builder()
                .campId(campId)
                .facilityId(facilityId)
                .build();

        // JpaRepositoryとしてキャストすることで、saveメソッドの曖昧性を解消
        ((JpaRepository<CampFacilities, ?>) repository).save(campFacilities);

        List<CampFacilities> campFacilitiesList = repository.findAll();
        Assertions.assertThat(campFacilitiesList)
                .anyMatch(cf ->
                        cf.getCampId().equals(campId)
                                && cf.getFacilityId().equals(facilityId));
    }

    @Test
    public void givenMultipleCampFacilities_whenFindAll_thenReturnsAll() {
        UUID campId = UUID.randomUUID();
        UUID facilityId1 = UUID.randomUUID();
        UUID facilityId2 = UUID.randomUUID();

        // CampFacilitiesの@Builderを活用
        CampFacilities campFacility1 = CampFacilities.builder()
                .campId(campId)
                .facilityId(facilityId1)
                .build();
        CampFacilities campFacility2 = CampFacilities.builder()
                .campId(campId)
                .facilityId(facilityId2)
                .build();

        // JpaRepositoryとしてキャストすることで、saveメソッドの曖昧性を解消
        ((JpaRepository<CampFacilities, ?>) repository).save(campFacility1);
        ((JpaRepository<CampFacilities, ?>) repository).save(campFacility2);

        List<CampFacilities> campFacilitiesList = repository.findAll();

        Assertions.assertThat(campFacilitiesList)
                .anyMatch(cf ->
                        cf.getCampId().equals(campId)
                        && cf.getFacilityId().equals(facilityId1));
        Assertions.assertThat(campFacilitiesList)
                .anyMatch(cf ->
                        cf.getCampId().equals(campId)
                        && cf.getFacilityId().equals(facilityId2));
    }

    @Test
    public void givenCampId_whenFindByCampId_thenReturnMatchingFacilities() {
        UUID campId = UUID.randomUUID();
        UUID facilityId1 = UUID.randomUUID();
        UUID facilityId2 = UUID.randomUUID();

        // CampFacilitiesの@Builderを活用
        CampFacilities campFacility1 = CampFacilities.builder()
                .campId(campId)
                .facilityId(facilityId1)
                .build();
        CampFacilities campFacility2 = CampFacilities.builder()
                .campId(campId)
                .facilityId(facilityId2)
                .build();

        // JpaRepositoryとしてキャストすることで、saveメソッドの曖昧性を解消
        ((JpaRepository<CampFacilities, ?>) repository).save(campFacility1);
        ((JpaRepository<CampFacilities, ?>) repository).save(campFacility2);

        List<CampFacilities> campFacilitiesList = repository.findByCampId(campId);

        Assertions.assertThat(campFacilitiesList).hasSize(2);
        Assertions.assertThat(campFacilitiesList)
                .allMatch(cf -> cf.getCampId().equals(campId));
        Assertions.assertThat(campFacilitiesList)
                .extracting(CampFacilities::getFacilityId)
                .containsExactlyInAnyOrder(facilityId1, facilityId2);
    }
}
