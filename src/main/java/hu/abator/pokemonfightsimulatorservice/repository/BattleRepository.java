package hu.abator.pokemonfightsimulatorservice.repository;

import hu.abator.pokemonfightsimulatorservice.domain.BattleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface BattleRepository extends JpaRepository<BattleEntity, Long> {

    Set<BattleEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
