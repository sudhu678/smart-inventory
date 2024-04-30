package com.one.smartinventory.dao.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "PLAN")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "PlanEntity.find", query = "SELECT p FROM PLAN p WHERE p.planDate = ?1")})
public class PlanEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "UKEY")
    private long id;

    @Column(name = "TASKS")
    @OneToMany(mappedBy = "planDate", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<TaskEntity> tasks;

    @Column(name = "PLAN_DATE")
    private LocalDate planDate;

}
