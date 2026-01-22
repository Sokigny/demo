package com.cinema.demo.repository;

import com.cinema.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    
    List<Reservation> findByClientId(Integer clientId);
    
    List<Reservation> findByStatus(Integer status);
}
