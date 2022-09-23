package pl.devopsi.hazelcasttutorial.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@ToString
@Builder
public class User {

    private String name;

    private String surname;

    @NotBlank
    private String pesel;

    private List<AllergenMedication> allergens;
}
