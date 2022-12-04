package com.pequla.data.repository;

import com.pequla.data.entity.WebAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebAccessRepository extends JpaRepository<WebAccess, Integer> {
}
