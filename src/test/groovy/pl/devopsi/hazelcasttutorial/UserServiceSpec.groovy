package pl.devopsi.hazelcasttutorial

import pl.devopsi.hazelcasttutorial.entity.UserEntity
import pl.devopsi.hazelcasttutorial.exception.UserNotFoundException
import pl.devopsi.hazelcasttutorial.model.User
import pl.devopsi.hazelcasttutorial.repository.UserAllergenMedicationRepository
import pl.devopsi.hazelcasttutorial.repository.UserRepository
import pl.devopsi.hazelcasttutorial.service.UserAllergenMedicationService
import pl.devopsi.hazelcasttutorial.service.UserService
import spock.lang.Specification

class UserServiceSpec extends Specification {

    def repository = Mock(UserRepository)
    def allergenRepository = Mock(UserAllergenMedicationRepository)
    def userAllergenMedicationService = Mock(UserAllergenMedicationService)
    def service = new UserService(repository, allergenRepository, userAllergenMedicationService)

    def 'should add user to database'() {
        given:
        def user = [
                name   : name,
                surname: surname,
                pesel  : pesel
        ] as User

        and:
        1 * repository.save(_ as UserEntity) >> {
            [] as UserEntity
        }

        when:
        service.addUser(user)

        then:
        true

        where:
        name        | surname        | pesel
        "TEST_NAME" | "TEST_SURNAME" | "PESEL"
    }

    def 'should return existing user'() {
        given:
        1 * repository.findByPesel(_ as String) >> {
            [
                    name   : "TEST_NAME",
                    surname: "TEST_SURNAME",
                    pesel  : pesel
            ] as UserEntity
        }

        when:
        def user = service.getUser(pesel)

        then:
        user
        user.pesel == pesel
        user.surname == surname
        user.name == name

        where:
        name        | surname        | pesel
        "TEST_NAME" | "TEST_SURNAME" | "PESEL"
    }

    def 'should throw UserNotFoundException if user doesnt exists [get user]'() {
        given:
        1 * repository.findByPesel(_ as String) >> {
            null
        }

        when:
        service.getUser(pesel)

        then:
        thrown(UserNotFoundException)

        where:
        name        | surname        | pesel
        "TEST_NAME" | "TEST_SURNAME" | "PESEL"
    }

    def 'should throw UserNotFoundException if user doesnt exists [update user]'() {
        given:
        def user = [
                name   : name,
                surname: surname,
                pesel  : pesel
        ] as User

        and:
        1 * repository.findByPesel(_ as String) >> {
            null
        }

        when:
        service.updateUser(user)

        then:
        thrown(UserNotFoundException)

        where:
        name        | surname        | pesel
        "TEST_NAME" | "TEST_SURNAME" | "PESEL"
    }

    def 'should update user in database'() {
        given:
        def user = [
                name   : name,
                surname: surname,
                pesel  : pesel
        ] as User

        and:
        1 * repository.findByPesel(_ as String) >> {
            [] as UserEntity
        }
        1 * repository.save(_ as UserEntity) >> {
            [] as UserEntity
        }

        when:
        service.updateUser(user)

        then:
        true

        where:
        name        | surname        | pesel
        "TEST_NAME" | "TEST_SURNAME" | "PESEL"
    }
}