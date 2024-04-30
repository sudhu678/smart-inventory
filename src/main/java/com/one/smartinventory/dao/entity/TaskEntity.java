package com.one.smartinventory.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "TASK")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "TaskEntity.findByOrigin", query = "SELECT t FROM TASK t WHERE t.origin = ?1"),
        @NamedQuery(name = "TaskEntity.findByDestination", query = "SELECT t FROM TASK t WHERE t.destination = ?1"),
        @NamedQuery(name = "TaskEntity.findByOriginAndDestination", query = "SELECT t FROM TASK t WHERE t.origin = ?1" +
                " AND t.destination = ?2"),
        @NamedQuery(name = "TaskEntity.deletePlanTasks", query = "DELETE FROM TASK t WHERE t.planDate IS NOT NULL")})

public class TaskEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "UKEY")
    private long id;

    @Column(name = "PLAN_DATE")
    private LocalDate planDate;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "ORIGIN")
    private String origin;

    @Column(name = "DESTINATION")
    private String destination;

    @Column(name = "COUNT")
    private long count;

}

