package hu.abator.pokemonfightsimulatorservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;


@Entity
@Table(name = "battle")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"id", "uuid"})
public class BattleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @ManyToOne(optional = false)
    private PokemonEntity winner;

    @ManyToOne
    private PokemonEntity looser;


    ZonedDateTime createdAt;
}
