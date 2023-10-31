package org.devcourse.resumeme.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.resume.ForeignLanguage;
import org.devcourse.resumeme.repository.ForeignLanguageRepository;
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

