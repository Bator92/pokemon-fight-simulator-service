package hu.abator.pokemonfightsimulatorservice.mapper;

import com.triceracode.pokeapi.model.NamedAPIResource;
import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import com.triceracode.pokeapi.model.resource.pokemon.PokemonSprites;
import com.triceracode.pokeapi.model.resource.pokemon.PokemonType;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.dto.PokemonDto;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PokemonMapperTest {

    private final PokemonMapper mapper = new PokemonMapper();

    @Test
    void toDto_shouldMapAllFields() {
        // given
        PokemonEntity entity = PokemonEntity.builder()
                .name("pikachu")
                .power(15)
                .pictureUrl("https://img/pikachu.png")
                .type("electric")
                .build();

        // when
        PokemonDto dto = mapper.toDto(entity);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("pikachu");
        assertThat(dto.getPower()).isEqualTo(15);
        assertThat(dto.getPictureUrl()).isEqualTo("https://img/pikachu.png");
        assertThat(dto.getType()).isEqualTo("electric");
    }

    @Test
    void toEntity_shouldMapFieldsFromPokemonResource() {
        // given
        Pokemon pokemon = mock(Pokemon.class);
        PokemonSprites sprites = mock(PokemonSprites.class);
        PokemonType typeSlot = mock(PokemonType.class);
        NamedAPIResource typeResource = mock(NamedAPIResource.class);

        when(pokemon.getName()).thenReturn("charmander");
        when(pokemon.getSprites()).thenReturn(sprites);
        when(sprites.getFrontDefault()).thenReturn("https://img/charmander.png");
        when(pokemon.getTypes()).thenReturn(Collections.singletonList(typeSlot));
        when(typeSlot.getType()).thenReturn(typeResource);
        when(typeResource.getName()).thenReturn("fire");

        // when
        PokemonEntity entity = mapper.toEntity(pokemon);

        // then
        assertThat(entity.getName()).isEqualTo("charmander");
        assertThat(entity.getType()).isEqualTo("fire");
        assertThat(entity.getPictureUrl()).isEqualTo("https://img/charmander.png");
        assertThat(entity.getPower()).isBetween(1, 19); // random
    }
}