package hu.abator.pokemonfightsimulatorservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.abator.pokemonfightsimulatorservice.dto.BattleDto;
import hu.abator.pokemonfightsimulatorservice.dto.BattleRequestDto;
import hu.abator.pokemonfightsimulatorservice.dto.PokemonDto;
import hu.abator.pokemonfightsimulatorservice.manager.BattleManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BattleControllerIntegrationTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BattleManager battleManager;

    @BeforeEach
    void setUp() {
        BattleController controller = new BattleController(battleManager);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/battles returns BattleDto JSON on success")
    void start_returnsOkJson() throws Exception {
        // given
        BattleRequestDto req = new BattleRequestDto();
        req.setPokemon1Name("pikachu");
        req.setPokemon2Name("bulbasaur");

        PokemonDto winner = PokemonDto.builder().name("pikachu").type("electric").power(55).pictureUrl("u1").build();
        PokemonDto looser = PokemonDto.builder().name("bulbasaur").type("grass").power(49).pictureUrl("u2").build();
        BattleDto response = BattleDto.builder()
                .id(UUID.randomUUID())
                .winnerPokemon(winner)
                .looserPokemon(looser)
                .createdAt(OffsetDateTime.now())
                .build();

        when(battleManager.start(any(BattleRequestDto.class))).thenReturn(response);

        // when / then
        mockMvc.perform(post("/api/battles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winnerPokemon.name").value("pikachu"))
                .andExpect(jsonPath("$.looserPokemon.name").value("bulbasaur"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("POST /api/battles maps IllegalArgumentException to 400 with standard error body")
    void start_badRequestOnIllegalArgument() throws Exception {
        // given
        BattleRequestDto req = new BattleRequestDto();
        req.setPokemon1Name("invalid");
        req.setPokemon2Name("invalid");

        when(battleManager.start(any(BattleRequestDto.class))).thenThrow(new IllegalArgumentException("invalid pokemons"));

        // when / then
        mockMvc.perform(post("/api/battles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("invalid pokemons"));
    }

    @Test
    @DisplayName("GET /api/battles?limit=5 returns set of BattleDto as array JSON")
    void getBattles_returnsOkJson() throws Exception {
        // given
        BattleDto b1 = BattleDto.builder()
                .id(UUID.randomUUID())
                .winnerPokemon(PokemonDto.builder().name("pikachu").build())
                .looserPokemon(PokemonDto.builder().name("bulbasaur").build())
                .createdAt(OffsetDateTime.now())
                .build();
        BattleDto b2 = BattleDto.builder()
                .id(UUID.randomUUID())
                .winnerPokemon(PokemonDto.builder().name("charizard").build())
                .looserPokemon(PokemonDto.builder().name("blastoise").build())
                .createdAt(OffsetDateTime.now())
                .build();

        Set<BattleDto> set = new LinkedHashSet<>();
        set.add(b1);
        set.add(b2);
        when(battleManager.getBattles(5)).thenReturn(set);

        // when / then
        mockMvc.perform(get("/api/battles").param("limit", "5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].winnerPokemon.name").value("pikachu"))
                .andExpect(jsonPath("$[1].winnerPokemon.name").value("charizard"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[1].id").exists());
    }

    @Test
    @DisplayName("GET /api/battles maps generic Exception to 500")
    void getBattles_genericError() throws Exception {
        when(battleManager.getBattles(2)).thenThrow(new RuntimeException("oops"));

        mockMvc.perform(get("/api/battles").param("limit", "2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}
