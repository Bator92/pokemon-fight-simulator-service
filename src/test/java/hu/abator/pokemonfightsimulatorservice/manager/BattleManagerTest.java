package hu.abator.pokemonfightsimulatorservice.manager;

import hu.abator.pokemonfightsimulatorservice.domain.BattleEntity;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.dto.BattleDto;
import hu.abator.pokemonfightsimulatorservice.dto.BattleRequestDto;
import hu.abator.pokemonfightsimulatorservice.exception.CannotDetermineWinnerException;
import hu.abator.pokemonfightsimulatorservice.mapper.BattleMapper;
import hu.abator.pokemonfightsimulatorservice.service.BattleService;
import hu.abator.pokemonfightsimulatorservice.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleManagerTest {

    @Mock
    BattleService battleService;
    @Mock
    PokemonService pokemonService;
    @Mock
    BattleMapper battleMapper;

    @Captor
    ArgumentCaptor<BattleEntity> battleEntityCaptor;

    @InjectMocks
    BattleManager battleManager;

    @Test
    void start_shouldCreateBattlePersistAndReturnDto() throws CannotDetermineWinnerException {
        // given
        BattleRequestDto req = BattleRequestDto.builder()
                .pokemon1Name("pikachu")
                .pokemon2Name("bulbasaur")
                .build();

        PokemonEntity winner = PokemonEntity.builder().name("pikachu").build();
        PokemonEntity p2 = PokemonEntity.builder().name("bulbasaur").build();

        UUID battleId = UUID.randomUUID();

        BattleEntity saved = BattleEntity.builder()
                .uuid(battleId)
                .winner(winner)
                .looser(p2)
                .createdAt(ZonedDateTime.now())
                .build();

        BattleDto expected = BattleDto.builder().id(battleId).build();

        when(pokemonService.getByName("pikachu")).thenReturn(winner);
        when(pokemonService.getByName("bulbasaur")).thenReturn(p2);
        when(battleService.selectWinner(winner, p2)).thenReturn(winner);
        when(battleService.save(any())).thenReturn(saved);
        when(battleMapper.toDto(saved)).thenReturn(expected);

        // when
        BattleDto result = battleManager.start(req);

        // then
        assertThat(result).isEqualTo(expected);

        verify(battleService).save(battleEntityCaptor.capture());
        BattleEntity captured = battleEntityCaptor.getValue();

        assertThat(captured.getWinner()).isEqualTo(winner);
        assertThat(captured.getLooser()).isEqualTo(p2);
        assertThat(captured.getUuid()).isNotNull();
        assertThat(captured.getCreatedAt()).isNotNull();
    }

    @Test
    void getBattles_shouldReturnMappedDtos() {
        Set<BattleEntity> entities = Set.of(
                BattleEntity.builder().uuid(UUID.randomUUID()).build()
        );
        Set<BattleDto> dtos = Set.of(
                BattleDto.builder().id(UUID.randomUUID()).build()
        );

        when(battleService.findBattles(5)).thenReturn(entities);
        when(battleMapper.toDtos(entities)).thenReturn(dtos);

        Set<BattleDto> result = battleManager.getBattles(5);

        assertThat(result).isEqualTo(dtos);
    }
}