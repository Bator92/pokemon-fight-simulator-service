package hu.abator.pokemonfightsimulatorservice.service;

import hu.abator.pokemonfightsimulatorservice.domain.BattleEntity;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.exception.CannotDetermineWinnerException;
import hu.abator.pokemonfightsimulatorservice.repository.BattleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @Mock
    private BattleRepository battleRepository;

    @InjectMocks
    private BattleService battleService;

    @Test
    @DisplayName("save delegates to repository")
    void save_delegates() {
        BattleEntity input = BattleEntity.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .createdAt(ZonedDateTime.now())
                .build();
        when(battleRepository.save(input)).thenReturn(input);

        BattleEntity result = battleService.save(input);

        assertSame(input, result);
        verify(battleRepository).save(input);
    }

    @Test
    @DisplayName("selectWinner returns pokemon1 when power greater")
    void selectWinner_pokemon1() throws CannotDetermineWinnerException {
        PokemonEntity p1 = PokemonEntity.builder().name("p1").power(60).build();
        PokemonEntity p2 = PokemonEntity.builder().name("p2").power(50).build();

        PokemonEntity winner = battleService.selectWinner(p1, p2);

        assertSame(p1, winner);
    }

    @Test
    @DisplayName("selectWinner returns pokemon2 when power greater")
    void selectWinner_pokemon2() throws CannotDetermineWinnerException {
        PokemonEntity p1 = PokemonEntity.builder().name("p1").power(40).build();
        PokemonEntity p2 = PokemonEntity.builder().name("p2").power(50).build();

        PokemonEntity winner = battleService.selectWinner(p1, p2);

        assertSame(p2, winner);
    }

    @Test
    @DisplayName("selectWinner throws when powers equal")
    void selectWinner_tie_throws() {
        PokemonEntity p1 = PokemonEntity.builder().name("p1").power(50).build();
        PokemonEntity p2 = PokemonEntity.builder().name("p2").power(50).build();

        assertThrows(CannotDetermineWinnerException.class, () -> battleService.selectWinner(p1, p2));
    }

    @Test
    @DisplayName("findBattles returns set built from repository list and uses given limit for page size")
    void findBattles_usesLimitAndReturnsSet() {
        // given
        BattleEntity b1 = BattleEntity.builder().id(1L).uuid(UUID.randomUUID()).createdAt(ZonedDateTime.now()).build();
        BattleEntity b2 = BattleEntity.builder().id(2L).uuid(UUID.randomUUID()).createdAt(ZonedDateTime.now().minusMinutes(1)).build();
        when(battleRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(List.of(b1, b2));

        // when
        int limit = 5;
        Set<BattleEntity> result = battleService.findBattles(limit);

        // then
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(b1, b2)));

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(battleRepository).findAllByOrderByCreatedAtDesc(captor.capture());
        assertEquals(limit, captor.getValue().getPageSize());
    }
}
