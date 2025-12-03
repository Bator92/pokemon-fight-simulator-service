package hu.abator.pokemonfightsimulatorservice.mapper;

import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.dto.PokemonDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PokemonMapper {

    Random random;

    public PokemonDto toDto(PokemonEntity pokemon) {
        return PokemonDto.builder()
                .name(pokemon.getName())
                .power(pokemon.getPower())
                .pictureUrl(pokemon.getPictureUrl())
                .type(pokemon.getType())
                .build();
    }

    public PokemonEntity toEntity(Pokemon pokemon){
        return PokemonEntity.builder()
                .power(getRandomInt(1,20))
                .type(pokemon.getTypes().get(0).getType().getName())
                .name(pokemon.getName())
                .pictureUrl(pokemon.getSprites().getFrontDefault())
                .build();
    }

    private int getRandomInt(int min, int max) {
        return random.nextInt(min, max + 1);
    }

}
