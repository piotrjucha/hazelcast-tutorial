package pl.devopsi.hazelcasttutorial

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pl.devopsi.hazelcasttutorial.entity.UserEntity
import pl.devopsi.hazelcasttutorial.repository.UserRepository
import spock.lang.Specification

import static java.util.Objects.nonNull

@DataJpaTest
class UserRepositorySpec extends Specification {

    @Autowired
    private UserRepository repository

    def 'should save and load user from database'() {

        given:
        def userEntity = [
                name   : name,
                surname: surname,
                pesel  : pesel
        ] as UserEntity

        when:
        repository.save(userEntity)
        def user = repository.findByPesel(pesel)

        then:
        user
        nonNull(user.id)
        user.name == name
        user.surname == surname
        user.pesel == pesel

        where:
        name        | surname        | pesel
        "TEST_NAME" | "TEST_SURNAME" | "PESEL"
    }
}
