package hu.abator.pokemonfightsimulatorservice.manager;

import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import hu.abator.pokemonfightsimulatorservice.domain.BattleEntity;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.dto.BattleDto;
import hu.abator.pokemonfightsimulatorservice.dto.BattleRequestDto;
import hu.abator.pokemonfightsimulatorservice.mapper.BattleMapper;
import hu.abator.pokemonfightsimulatorservice.mapper.PokemonMapper;
import hu.abator.pokemonfightsimulatorservice.service.BattleService;
import hu.abator.pokemonfightsimulatorservice.service.PokemonApiAdapterService;
import hu.abator.pokemonfightsimulatorservice.service.PokemonService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Component
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BattleManager {

    BattleService battleService;
    PokemonService pokemonService;
    BattleMapper battleMapper;

    @SneakyThrows
    public @Nullable BattleDto start(BattleRequestDto req) {
        PokemonEntity pokemon1 = pokemonService.getByName(req.getPokemon1Name());
        PokemonEntity pokemon2 = pokemonService.getByName(req.getPokemon2Name());

        PokemonEntity winner = battleService.selectWinner(pokemon1, pokemon2);
        final var battleEntity = BattleEntity.builder()
                .uuid(UUID.randomUUID())
                .winner(winner)
                .looser(pokemon1.getName().equals(winner.getName()) ? pokemon2 : pokemon1)
                .createdAt(ZonedDateTime.now())
                .build();
        return battleMapper.toDto(battleService.save(battleEntity));
    }

    public @Nullable Set<BattleDto> getBattles(int limit) {
        return battleMapper.toDtos(battleService.findBattles(limit));
    }
}
