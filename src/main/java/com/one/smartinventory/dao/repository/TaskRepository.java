package com.one.smartinventory.dao.repository;

import com.one.smartinventory.dao.entity.TaskEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByOrigin(String origin);

    List<TaskEntity> findByDestination(String destination);

    List<TaskEntity> findByOriginAndDestination(String origin, String destination);

    List<TaskEntity> findAllByProductId(long productId);

    TaskEntity findById(long id);

    TaskEntity findByIdAndPlanDate(long id, LocalDate planDate);

    List<TaskEntity> findAllByPlanDate(LocalDate planDate);

    TaskEntity findByProductIdAndOriginAndDestination(long productId, String origin, String destination);

    @Modifying
    @Transactional
    void deletePlanTasks();

}
