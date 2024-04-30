package com.one.smartinventory.dao.repository;

import com.one.smartinventory.dao.entity.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, Long> {

    AreaEntity find(String aisle);

}
