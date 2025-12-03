package hu.abator.pokemonfightsimulatorservice.service;

import com.triceracode.pokeapi.PokeAPIService;
import com.triceracode.pokeapi.model.NamedAPIResource;
import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonApiAdapterServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private PokeAPIService pokeAPIService;

    @Mock
    private Random random;

    @InjectMocks
    private PokemonApiAdapterService service;

    @Test
    @DisplayName("getRandomPokemon returns detailed Pokemon fetched by name from a single-item list")
    void getRandomPokemon_singleItemList_returnsByName() throws Exception {
        // given
        NamedAPIResource resource = org.mockito.Mockito.mock(NamedAPIResource.class);
        when(resource.getName()).thenReturn("pikachu");

        when(pokeAPIService.pokemon().list(anyLong(), anyLong()).execute().body().getResults())
                .thenReturn(List.of(resource));
        when(random.nextInt(1)).thenReturn(0);

        Pokemon expected = org.mockito.Mockito.mock(Pokemon.class);
        when(pokeAPIService.pokemon().byName(eq("pikachu")).execute().body()).thenReturn(expected);

        // when
        Pokemon actual = service.getRandomPokemon();

        // then
        assertSame(expected, actual);
    }
}
