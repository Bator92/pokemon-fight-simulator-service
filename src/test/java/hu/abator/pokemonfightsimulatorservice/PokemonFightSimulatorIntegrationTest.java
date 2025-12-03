package hu.abator.pokemonfightsimulatorservice;

import com.triceracode.pokeapi.PokeAPIService;
import com.triceracode.pokeapi.model.NamedAPIResource;
import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import hu.abator.pokemonfightsimulatorservice.dto.BattleRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PokemonFightSimulatorIntegrationTest {

    @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
    PokeAPIService pokeAPIService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Pokemon pikachu;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Pokemon bulbasaur;

    @MockitoBean
    Random random;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testBattle() throws Exception {
        // given
        final var pikachuResource = new NamedAPIResource(1L, "pikachu", "url");
        final var bulbasaurResource = new NamedAPIResource(1L, "bulbasaur", "url");

        when(pikachu.getName()).thenReturn("pikachu");
        when(pikachu.getTypes().get(0).getType().getName()).thenReturn("electric");
        when(pikachu.getSprites().getFrontDefault()).thenReturn("pikachuPicture");
        when(bulbasaur.getName()).thenReturn("bulbasaur");
        when(bulbasaur.getTypes().get(0).getType().getName()).thenReturn("grass");
        when(bulbasaur.getSprites().getFrontDefault()).thenReturn("bulbasaurPicture");
        List<NamedAPIResource> pokemonResources = List.of(pikachuResource, bulbasaurResource);
        when(random.nextInt(pokemonResources.size())).thenReturn(0, 1);
        when(random.nextInt(1, 21)).thenReturn(5, 10);

        when(pokeAPIService.pokemon().list(100L, 0L).execute().body().getResults())
                .thenReturn(pokemonResources);
        when(pokeAPIService.pokemon().byName("pikachu").execute().body())
                .thenReturn(pikachu);
        when(pokeAPIService.pokemon().byName("bulbasaur").execute().body())
                .thenReturn(bulbasaur);

        // when / then
        mockMvc.perform(get("/api/pokemons/random-fighting-pair"))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pokemon1.name").value("pikachu"))
                .andExpect(jsonPath("$.pokemon2.name").value("bulbasaur"));

        mockMvc.perform(post("/api/battles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BattleRequestDto.builder()
                                .pokemon1Name("pikachu")
                                .pokemon2Name("bulbasaur")
                                .build())
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winnerPokemon.name").value("bulbasaur"))
                .andExpect(jsonPath("$.looserPokemon.name").value("pikachu"));

        mockMvc.perform(get("/api/battles")
                        .queryParam("limit", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].winnerPokemon.name").value("bulbasaur"))
                .andExpect(jsonPath("$[0].winnerPokemon.power").value("10"))
                .andExpect(jsonPath("$[0].looserPokemon.name").value("pikachu"))
                .andExpect(jsonPath("$[0].looserPokemon.power").value("5"))
        ;
    }
}
