package pl.devopsi.hazelcasttutorial

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pl.devopsi.hazelcasttutorial.entity.UserAllergenMedicationEntity
import pl.devopsi.hazelcasttutorial.model.AllergenMedication
import pl.devopsi.hazelcasttutorial.repository.UserAllergenMedicationRepository
import spock.lang.Specification

@DataJpaTest
class UserAllergenMedicationRepositorySpec extends Specification{

    @Autowired
    private UserAllergenMedicationRepository repository

    def 'should save and load user allergen medications from database'() {

        given:
        def entity = [
                pesel     : pesel,
                medication: medication
        ] as UserAllergenMedicationEntity

        when:
        repository.save(entity)
        def result = repository.findAllByPesel(pesel)

        then:
        result
        result.size() == 1
        result[0].medication == medication

        where:
        pesel       | medication
        "123456789" | AllergenMedication.ACARIZAX
    }
}
