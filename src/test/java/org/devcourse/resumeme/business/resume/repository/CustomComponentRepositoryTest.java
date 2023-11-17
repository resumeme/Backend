package org.devcourse.resumeme.business.resume.repository;

import jakarta.persistence.EntityManager;
import org.devcourse.resumeme.business.resume.controller.career.dto.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomComponentRepositoryTest {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private EntityManager em;

    @Test
    void 컴포넌트_복제_성공() {
        // given
        CareerCreateRequest careerCreateRequest = new CareerCreateRequest("company name", "BACK", List.of("java", "spring"), List.of(new CareerCreateRequest.DutyRequest("title", "description", LocalDate.now(), LocalDate.now().plusYears(1L))), false, LocalDate.now(), LocalDate.now().plusYears(1L), "content");
        Component component = careerCreateRequest.toEntity().of(1L);
        componentRepository.save(component);

        em.flush();
        em.clear();

        // when
        componentRepository.copy(1L, 2L);

        em.flush();
        em.clear();

        // then
        List<Component> originComponent = componentRepository.findAllByResumeId(1L);
        List<Component> copiedComponent = componentRepository.findAllByResumeId(2L);

        assertThat(originComponent).hasSameClassAs(copiedComponent);
    }

}
