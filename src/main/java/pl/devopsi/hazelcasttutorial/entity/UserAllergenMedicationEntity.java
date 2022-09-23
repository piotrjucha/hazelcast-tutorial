package pl.devopsi.hazelcasttutorial.entity;

import lombok.*;
import pl.devopsi.hazelcasttutorial.model.AllergenMedication;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_ALLERGEN_MEDICAMENT")
@Builder
@SuppressWarnings("JpaDataSourceORMInspection")
public class UserAllergenMedicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pesel;

    @Enumerated(STRING)
    private AllergenMedication medication;
}
