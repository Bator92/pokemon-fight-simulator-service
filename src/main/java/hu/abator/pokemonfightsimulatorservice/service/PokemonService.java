package hu.abator.pokemonfightsimulatorservice.service;

import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.repository.PokemonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults( makeFinal = true, level = AccessLevel.PRIVATE)
public class PokemonService {

    PokemonRepository pokemonRepository;

    public PokemonEntity getOrSaveByName(PokemonEntity entity) {
        return pokemonRepository.findByName(entity.getName())
                .orElseGet(() -> pokemonRepository.save(entity));
    }

    public PokemonEntity getByName(String name){
        return pokemonRepository.findByName(name)
                .orElseThrow(() -> new IllegalStateException("Pokemon not found: " + name));
    }
}
