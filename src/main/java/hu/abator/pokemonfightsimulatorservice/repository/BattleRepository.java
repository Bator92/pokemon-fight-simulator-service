package hu.abator.pokemonfightsimulatorservice.repository;

import hu.abator.pokemonfightsimulatorservice.domain.BattleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleRepository extends JpaRepository<BattleEntity, Long> {

    List<BattleEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
