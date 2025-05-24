package com.pake.pake.Repository;

import com.pake.pake.Entities.FraudActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface FraudActivityRepository extends JpaRepository<FraudActivity, Long> {
    long countByUserAndTimestampAfter(String user, LocalDateTime timestamp);
}