package hu.abator.pokemonfightsimulatorservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pokemon")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "type"})
public class PokemonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private Integer power;
    private String pictureUrl;
}
