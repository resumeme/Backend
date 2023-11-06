package org.devcourse.resumeme.business.resume.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;
import org.devcourse.resumeme.business.resume.repository.ForeignLanguageRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ForeignLanguageService {

    private final ForeignLanguageRepository foreignLanguageRepository;

    public Long create(ForeignLanguage foreignLanguage) {
        return foreignLanguageRepository.save(foreignLanguage).getId();
    }

}
