package com.cinema.demo.repository;

import com.cinema.demo.model.PaiementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementDetailRepository extends JpaRepository<PaiementDetail, Integer> {
    
    List<PaiementDetail> findByPaiementId(Integer paiementId);
    
    List<PaiementDetail> findBySeanceId(Integer seanceId);
    
    List<PaiementDetail> findByPaiementSocieteIdAndSeanceId(Integer societeId, Integer seanceId);
}
