package hu.abator.pokemonfightsimulatorservice.mapper;

import hu.abator.pokemonfightsimulatorservice.domain.BattleEntity;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.dto.BattleDto;
import hu.abator.pokemonfightsimulatorservice.dto.PokemonDto;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BattleMapperTest {

    @Test
    void toDtos_shouldMapAllBattles() {
        // given
        PokemonMapper pokemonMapper = mock(PokemonMapper.class);
        BattleMapper battleMapper = new BattleMapper(pokemonMapper);

        PokemonEntity winner = PokemonEntity.builder().name("pikachu").build();
        PokemonEntity loser = PokemonEntity.builder().name("bulbasaur").build();

        BattleEntity battle1 = BattleEntity.builder()
                .uuid(UUID.randomUUID())
                .winner(winner)
                .looser(loser)
                .createdAt(ZonedDateTime.now())
                .build();

        BattleEntity battle2 = BattleEntity.builder()
                .uuid(UUID.randomUUID())
                .winner(loser)
                .looser(winner)
                .createdAt(ZonedDateTime.now())
                .build();

        when(pokemonMapper.toDto(winner)).thenReturn(PokemonDto.builder().name("pikachu").build());
        when(pokemonMapper.toDto(loser)).thenReturn(PokemonDto.builder().name("bulbasaur").build());

        // when
        Set<BattleDto> dtos = battleMapper.toDtos(Set.of(battle1, battle2));

        // then
        assertThat(dtos).hasSize(2);
        assertThat(dtos.stream().map(BattleDto::getId).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(battle1.getUuid(), battle2.getUuid());
    }

    @Test
    void toDto_shouldMapSingleBattle() {
        // given
        PokemonMapper pokemonMapper = mock(PokemonMapper.class);
        BattleMapper battleMapper = new BattleMapper(pokemonMapper);

        PokemonEntity winner = PokemonEntity.builder().name("pikachu").build();
        PokemonEntity loser = PokemonEntity.builder().name("bulbasaur").build();

        ZonedDateTime createdAt = ZonedDateTime.now();
        UUID id = UUID.randomUUID();

        BattleEntity battle = BattleEntity.builder()
                .uuid(id)
                .winner(winner)
                .looser(loser)
                .createdAt(createdAt)
                .build();

        PokemonDto winnerDto = PokemonDto.builder().name("pikachu").build();
        PokemonDto loserDto = PokemonDto.builder().name("bulbasaur").build();

        when(pokemonMapper.toDto(winner)).thenReturn(winnerDto);
        when(pokemonMapper.toDto(loser)).thenReturn(loserDto);

        // when
        BattleDto dto = battleMapper.toDto(battle);

        // then
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getWinnerPokemon()).isEqualTo(winnerDto);
        assertThat(dto.getLooserPokemon()).isEqualTo(loserDto);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt.toOffsetDateTime());
    }
}