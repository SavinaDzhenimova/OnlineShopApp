package org.onlineshop.repository;

import org.onlineshop.model.entity.ReturnOrReplacementRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnOrReplacementRequestRepository extends JpaRepository<ReturnOrReplacementRequest, Long> {

    Optional<ReturnOrReplacementRequest> findByEmail(String email);

    Optional<ReturnOrReplacementRequest> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("DELETE FROM ReturnOrReplacementRequest r WHERE r.id = :id")
    void deleteById(@Param("id") Long id);
}