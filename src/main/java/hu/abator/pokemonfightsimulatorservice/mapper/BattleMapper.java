package hu.abator.pokemonfightsimulatorservice.mapper;

import hu.abator.pokemonfightsimulatorservice.domain.BattleEntity;
import hu.abator.pokemonfightsimulatorservice.dto.BattleDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BattleMapper {

    PokemonMapper pokemonMapper;

    public Set<BattleDto> toDtos(Set<BattleEntity> battles) {
        return battles.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public BattleDto toDto(BattleEntity battleEntity) {
        return BattleDto.builder()
                .id(battleEntity.getUuid())
                .winnerPokemon(pokemonMapper.toDto(battleEntity.getWinner()))
                .looserPokemon(pokemonMapper.toDto(battleEntity.getLooser()))
                .createdAt(battleEntity.getCreatedAt().toOffsetDateTime())
                .build();
    }
}
