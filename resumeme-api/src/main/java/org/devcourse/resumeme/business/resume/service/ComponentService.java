package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.repository.CommentRepository;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.repository.ComponentRepository;
import org.devcourse.resumeme.business.resume.service.v2.ResumeTemplate;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.devcourse.resumeme.global.exception.ExceptionCode.COMPONENT_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentService {

    private final ComponentRepository componentRepository;

    private final CommentRepository commentRepository;

    public Long create(Component component, Property type) {
        Component createdComponent = createComponent(component, type);
        componentRepository.save(createdComponent);

        return component.getId();
    }

    private Component createComponent(Component newComponent, Property type) {
        Long resumeId = newComponent.getResumeId();
        Optional<Component> existComponent = componentRepository.findExistComponent(resumeId, type);
        if (existComponent.isPresent()) {
            Component component = existComponent.get();
            component.addSubComponent(newComponent);

            return component;
        }

        return new Component(type, null, null, null, resumeId, List.of(newComponent));
    }

    public ResumeTemplate getAll(Long resumeId, Property type) {
        List<Component> components = componentRepository.findAllByResumeId(resumeId).stream()
                .filter(component -> component.getComponent() == null)
                .filter(component -> component.getProperty().isType(type))
                .toList();

        return ResumeTemplate.from(components);
    }

    public Component getOne(Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new CustomException(COMPONENT_NOT_FOUND));
    }

    public void delete(Long componentId) {
        Component component = getOne(componentId);
        componentRepository.delete(component);
    }

    public Long update(Long componentId, Component newComponent, Property type) {
        Component component = getOne(componentId);
        componentRepository.delete(component);

        newComponent.updateOriginDate(component.getCreatedDate());
        newComponent.updateOriginComponentId(component.getOriginComponentId());

        Long newComponentId = create(newComponent, type);
        commentRepository.updateComment(newComponentId, componentId);

        return newComponentId;
    }

    public void copy(Long originResumeId, Long newResumeId) {
        List<Component> components = componentRepository.findAllByResumeId(originResumeId).stream()
                .filter(component -> component.getComponent() == null)
                .toList();

        List<Component> newComponents = components.stream()
                .map(component -> component.copy(newResumeId))
                .toList();

        componentRepository.saveAll(newComponents);
    }

}
