package hu.abator.pokemonfightsimulatorservice.web;

import hu.abator.pokemonfightsimulatorservice.dto.PokemonDto;
import hu.abator.pokemonfightsimulatorservice.dto.RandomFightingPairDto;
import hu.abator.pokemonfightsimulatorservice.manager.PokemonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PokemonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PokemonManager pokemonManager;

    @BeforeEach
    void setUp() {
        PokemonController controller = new PokemonController(pokemonManager);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/pokemons/random-fighting-pair returns pair JSON")
    void getRandomFightingPair_returnsOkJson() throws Exception {
        // given
        PokemonDto p1 = PokemonDto.builder().name("pikachu").type("electric").power(55).pictureUrl("u1").build();
        PokemonDto p2 = PokemonDto.builder().name("bulbasaur").type("grass").power(49).pictureUrl("u2").build();
        RandomFightingPairDto pair = RandomFightingPairDto.builder().pokemon1(p1).pokemon2(p2).build();
        when(pokemonManager.getRandomFightingPair()).thenReturn(pair);

        // when / then
        mockMvc.perform(get("/api/pokemons/random-fighting-pair").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pokemon1.name").value("pikachu"))
                .andExpect(jsonPath("$.pokemon2.name").value("bulbasaur"));
    }

    @Test
    @DisplayName("GET /api/pokemons/random-fighting-pair maps IllegalStateException to 409 with error body")
    void getRandomFightingPair_conflictOnIllegalState() throws Exception {
        // given
        when(pokemonManager.getRandomFightingPair()).thenThrow(new IllegalStateException("no pokemons available"));

        // when / then
        mockMvc.perform(get("/api/pokemons/random-fighting-pair").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("no pokemons available"))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }
}
