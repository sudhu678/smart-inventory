package com.one.smartinventory.dao.repository;

import com.one.smartinventory.dao.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<VisitEntity, Long> {

    List<VisitEntity> find(LocalDate date, long limit);

    VisitEntity findByAisleAndDate(String locator, LocalDate date);
}
