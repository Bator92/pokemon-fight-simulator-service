package hu.abator.pokemonfightsimulatorservice.manager;

import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.dto.PokemonDto;
import hu.abator.pokemonfightsimulatorservice.dto.RandomFightingPairDto;
import hu.abator.pokemonfightsimulatorservice.mapper.PokemonMapper;
import hu.abator.pokemonfightsimulatorservice.service.PokemonApiAdapterService;
import hu.abator.pokemonfightsimulatorservice.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonManagerTest {

    @Mock
    PokemonApiAdapterService api;
    @Mock
    PokemonMapper mapper;
    @Mock
    PokemonService service;

    @InjectMocks
    PokemonManager manager;

    @Test
    void getRandomFightingPair_shouldReturnTwoDtos() {
        // given
        Pokemon remote1 = mock(Pokemon.class);
        Pokemon remote2 = mock(Pokemon.class);

        PokemonEntity e1 = PokemonEntity.builder().name("pikachu").build();
        PokemonEntity e2 = PokemonEntity.builder().name("charmander").build();

        PokemonDto d1 = PokemonDto.builder().name("pikachu").build();
        PokemonDto d2 = PokemonDto.builder().name("charmander").build();

        when(api.getRandomPokemon()).thenReturn(remote1, remote2);
        when(mapper.toEntity(remote1)).thenReturn(e1);
        when(mapper.toEntity(remote2)).thenReturn(e2);
        when(service.getOrSaveByName(e1)).thenReturn(e1);
        when(service.getOrSaveByName(e2)).thenReturn(e2);
        when(mapper.toDto(e1)).thenReturn(d1);
        when(mapper.toDto(e2)).thenReturn(d2);

        // when
        RandomFightingPairDto result = manager.getRandomFightingPair();

        // then
        assertThat(result.getPokemon1()).isEqualTo(d1);
        assertThat(result.getPokemon2()).isEqualTo(d2);

        verify(api, times(2)).getRandomPokemon();
    }
}