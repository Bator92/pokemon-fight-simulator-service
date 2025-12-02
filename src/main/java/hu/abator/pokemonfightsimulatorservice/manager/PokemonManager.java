package hu.abator.pokemonfightsimulatorservice.manager;

import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.dto.RandomFightingPairDto;
import hu.abator.pokemonfightsimulatorservice.mapper.PokemonMapper;
import hu.abator.pokemonfightsimulatorservice.service.PokemonApiAdapterService;
import hu.abator.pokemonfightsimulatorservice.service.PokemonService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PokemonManager {

    PokemonApiAdapterService pokemonApiAdapterService;
    PokemonMapper pokemonMapper;
    PokemonService pokemonService;

    public RandomFightingPairDto getRandomFightingPair() {
        Pokemon randomPokemon1 = pokemonApiAdapterService.getRandomPokemon();
        Pokemon randomPokemon2 = pokemonApiAdapterService.getRandomPokemon();

        PokemonEntity pokemon1 = pokemonService.getOrSaveByName(pokemonMapper.toEntity(randomPokemon1));
        PokemonEntity pokemon2 = pokemonService.getOrSaveByName(pokemonMapper.toEntity(randomPokemon2));

        return RandomFightingPairDto.builder()
                .pokemon1(pokemonMapper.toDto(pokemon1))
                .pokemon2(pokemonMapper.toDto(pokemon2))
                .build();
    }
}
