package com.gymmanagement.gymapp.repository;

import com.gymmanagement.gymapp.model.ClassStatus;
import com.gymmanagement.gymapp.model.GymClass;
import com.gymmanagement.gymapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GymClassRepository extends JpaRepository<GymClass, Long> {

    Page<GymClass> findByStatusOrderByStartTimeAsc(ClassStatus status, Pageable pageable);

    @Query("SELECT gc FROM GymClass gc WHERE gc.startTime BETWEEN :startDate AND :endDate ORDER BY gc.startTime ASC")
    List<GymClass> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    Page<GymClass> findByInstructorOrderByStartTimeAsc(User instructor, Pageable pageable);

    @Query("SELECT gc FROM GymClass gc WHERE :user MEMBER OF gc.participants ORDER BY gc.startTime ASC")
    Page<GymClass> findByParticipant(@Param("user") User user, Pageable pageable);

    @Query("SELECT gc FROM GymClass gc WHERE LOWER(gc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY gc.startTime ASC")
    Page<GymClass> searchClasses(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT gc FROM GymClass gc WHERE gc.startTime > :now AND gc.status = 'SCHEDULED' ORDER BY gc.startTime ASC")
    List<GymClass> findUpcomingClasses(@Param("now") LocalDateTime now);

    @Query("SELECT gc.name, COUNT(gc) FROM GymClass gc WHERE gc.status = 'COMPLETED' GROUP BY gc.name ORDER BY COUNT(gc) DESC")
    List<Object[]> getMostPopularClasses();
}