package org.devcourse.resumeme.business.resume.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.repository.CommentRepository;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.repository.ComponentRepository;
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

    public Long create(Component component, String type) {
        Component createdComponent = createComponent(component, type);
        componentRepository.save(createdComponent);

        return component.getId();
    }

    private Component createComponent(Component newComponent, String type) {
        Long resumeId = newComponent.getResumeId();
        Optional<Component> existBlock1 = componentRepository.findExistBlock(resumeId, type);
        if (existBlock1.isPresent()) {
            Component existComponent = existBlock1.get();
            existComponent.addSubComponent(newComponent);

            return existComponent;
        }

        return new Component(type, null, null, null, resumeId, List.of(newComponent));
    }

    public List<Component> getAll(Long resumeId) {
        return componentRepository.findAllByResumeId(resumeId).stream()
                .filter(component -> component.getComponent() == null)
                .toList();
    }

    public Component getOne(Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new CustomException(COMPONENT_NOT_FOUND));
    }

    public String delete(Long componentId) {
        Component component = getOne(componentId);
        componentRepository.delete(component);

        return component.getProperty();
    }

    public Long update(Long componentId, Component newComponent, String type) {
        Component component = getOne(componentId);
        componentRepository.delete(component);

        newComponent.updateOriginDate(component.getCreatedDate());
        newComponent.updateOriginComponentId(component.getOriginComponentId());

        Long newComponentId = create(newComponent, type);
        commentRepository.updateComment(newComponentId, componentId);

        return newComponentId;
    }

    public void copy(Long originResumeId, Long newResumeId) {
        List<Component> components = getAll(originResumeId);
        List<Component> newComponents = components.stream()
                .map(component -> component.copy(newResumeId))
                .toList();

        componentRepository.saveAll(newComponents);
    }

}
