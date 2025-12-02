package hu.abator.pokemonfightsimulatorservice.service;

import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.repository.PokemonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonService pokemonService;

    @Test
    @DisplayName("getOrSaveByName returns existing when found and does not save")
    void getOrSaveByName_returnsExisting_whenFound() {
        // given
        PokemonEntity existing = PokemonEntity.builder()
                .id(1L)
                .name("pikachu")
                .type("electric")
                .power(55)
                .pictureUrl("url")
                .build();

        when(pokemonRepository.findByName("pikachu")).thenReturn(Optional.of(existing));

        // when
        PokemonEntity result = pokemonService.getOrSaveByName(existing);

        // then
        assertSame(existing, result);
        verify(pokemonRepository, times(1)).findByName("pikachu");
        verify(pokemonRepository, never()).save(any());
    }

    @Test
    @DisplayName("getOrSaveByName saves when not found and returns saved")
    void getOrSaveByName_saves_whenNotFound() {
        // given
        PokemonEntity input = PokemonEntity.builder()
                .name("bulbasaur")
                .type("grass")
                .power(49)
                .build();

        PokemonEntity saved = PokemonEntity.builder()
                .id(10L)
                .name("bulbasaur")
                .type("grass")
                .power(49)
                .build();

        when(pokemonRepository.findByName("bulbasaur")).thenReturn(Optional.empty());
        when(pokemonRepository.save(input)).thenReturn(saved);

        // when
        PokemonEntity result = pokemonService.getOrSaveByName(input);

        // then
        assertEquals(saved, result);
        verify(pokemonRepository).findByName("bulbasaur");
        verify(pokemonRepository).save(input);
    }

    @Test
    @DisplayName("getByName returns entity when found")
    void getByName_returnsEntity_whenFound() {
        // given
        PokemonEntity squirtle = PokemonEntity.builder().id(2L).name("squirtle").type("water").power(48).build();
        when(pokemonRepository.findByName("squirtle")).thenReturn(Optional.of(squirtle));

        // when
        PokemonEntity result = pokemonService.getByName("squirtle");

        // then
        assertSame(squirtle, result);
        verify(pokemonRepository).findByName("squirtle");
    }

    @Test
    @DisplayName("getByName throws when not found")
    void getByName_throws_whenNotFound() {
        // given
        when(pokemonRepository.findByName("mewtwo")).thenReturn(Optional.empty());

        // when / then
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> pokemonService.getByName("mewtwo"));
        assertTrue(ex.getMessage().contains("Pokemon not found: mewtwo"));
        verify(pokemonRepository).findByName("mewtwo");
    }
}
