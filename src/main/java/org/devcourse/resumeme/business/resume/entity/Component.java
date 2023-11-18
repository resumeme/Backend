package org.devcourse.resumeme.business.resume.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Component {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "block_id")
    private Long id;

    private String property;

    private String content;

    private LocalDate startDate;

    private LocalDate endDate;

    @Getter
    private Long resumeId;

    @Getter
    private Long originComponentId;

    @Getter
    private boolean isReflectFeedBack;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Component component;

    @Getter
    @OneToMany(mappedBy = "component", cascade = ALL, orphanRemoval = true)
    private List<Component> components = new ArrayList<>();

    @Getter
    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;


    public Component(String property, String content, LocalDate startDate, LocalDate endDate, Long resumeId, List<Component> components) {
        this.property = property;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resumeId = resumeId;
        this.components = components;
        if (components != null) {
            for (Component component : components) {
                component.component = this;
            }
        }
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void addSubComponent(Component component) {
        component.component = this;
        this.components.add(component);
    }

    public boolean isType(String type) {
        return type == null || property.equals(type);
    }

    public void updateOriginDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        this.lastModifiedDate = LocalDateTime.now();

        if (components != null) {
            components.forEach(subComponent -> subComponent.updateOriginDate(createdDate));
        }
    }

}
