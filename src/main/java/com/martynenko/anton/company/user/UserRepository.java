package com.martynenko.anton.company.user;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  Collection<User> findAllByProjectPositionPositionStartDateGreaterThan(LocalDate date);
  Collection<User>
  findAllByProjectPositionIsNullOrProjectPositionPositionStartDateGreaterThan(LocalDate date);

  Collection<User>
  findAllByProjectPositionIsNullOrProjectPositionPositionStartDateGreaterThanOrProjectPositionPositionEndDateLessThan(LocalDate startPeriod, LocalDate endPeriod);

  /*
  * Another solution is declaring findAllByProjectPositionIsNullOrProjectPositionPositionStartDateGreaterThanOrProjectPositionPositionEndDateLessThan
  * But this variant is much more readable.
  * If user currently available,  periodEndDate may be any
  * */
  @Query("""
      SELECT u 
      FROM User u 
      LEFT JOIN u.projectPosition p 
      WHERE p IS NULL 
      OR p.positionStartDate > :periodStartDate 
      OR p.positionEndDate < :periodEndDate 
      """)
  Collection<User>
  findAvailable(LocalDate periodStartDate, LocalDate periodEndDate);

}
