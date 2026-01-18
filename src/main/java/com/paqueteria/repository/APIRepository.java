package com.paqueteria.repository;

import com.paqueteria.model.API;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APIRepository extends JpaRepository<API, Integer> {
    Optional<API> findByKey(String key);
}
