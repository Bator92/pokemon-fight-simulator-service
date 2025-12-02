package hu.abator.pokemonfightsimulatorservice.repository;

import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonRepository extends JpaRepository<PokemonEntity, Long> {

    Optional<PokemonEntity> findByName(String name);
}
