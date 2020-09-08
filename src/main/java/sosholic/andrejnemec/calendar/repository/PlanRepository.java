package sosholic.andrejnemec.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sosholic.andrejnemec.calendar.entities.Plan;
import sosholic.andrejnemec.calendar.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Override
    Optional<Plan> findById(Long id);

    List<Plan> findAllByUser(User user);

    List<Plan> findAllByUserAndStartBetweenOrEndBetween(User user, LocalDateTime start1, LocalDateTime start2, LocalDateTime end1, LocalDateTime end2);

    List<Plan> findAllByStartBetweenOrEndBetween(LocalDateTime start1, LocalDateTime start2, LocalDateTime end1, LocalDateTime end2);

}
