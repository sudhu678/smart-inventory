package com.one.smartinventory.dao.repository;

import com.one.smartinventory.dao.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Long> {

    PlanEntity find(LocalDate date);
}
