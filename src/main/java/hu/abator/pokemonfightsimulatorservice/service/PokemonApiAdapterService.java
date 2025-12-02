package hu.abator.pokemonfightsimulatorservice.service;

import com.triceracode.pokeapi.PokeAPIService;
import com.triceracode.pokeapi.model.NamedAPIResource;
import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults( makeFinal = true, level = AccessLevel.PRIVATE)
public class PokemonApiAdapterService {

    PokeAPIService pokeApiService;

    @SneakyThrows
    public Pokemon getRandomPokemon(){
        List<NamedAPIResource> pokemonResults = Objects.requireNonNull(pokeApiService.pokemon().list(100L, 0L).execute().body()).getResults();
        Random rand = new Random();
        NamedAPIResource pokemon = pokemonResults.get(rand.nextInt(pokemonResults.size()));

        return pokeApiService.pokemon().byName(pokemon.getName()).execute().body();
    }

}
