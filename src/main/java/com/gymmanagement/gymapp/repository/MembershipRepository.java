package com.gymmanagement.gymapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gymmanagement.gymapp.model.Membership;
import com.gymmanagement.gymapp.model.MembershipStatus;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByUserId(Long userId);
    List<Membership> findByStatus(MembershipStatus status);
    List<Membership> findByEndDateBeforeAndStatus(LocalDate date, MembershipStatus status);
    List<Membership> findByEndDateAfterAndStatus(LocalDate date, MembershipStatus status);
}