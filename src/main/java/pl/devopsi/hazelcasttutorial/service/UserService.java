package pl.devopsi.hazelcasttutorial.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.devopsi.hazelcasttutorial.entity.UserAllergenMedicationEntity;
import pl.devopsi.hazelcasttutorial.entity.UserEntity;
import pl.devopsi.hazelcasttutorial.exception.UserNotFoundException;
import pl.devopsi.hazelcasttutorial.model.AllergenMedication;
import pl.devopsi.hazelcasttutorial.model.User;
import pl.devopsi.hazelcasttutorial.repository.UserAllergenMedicationRepository;
import pl.devopsi.hazelcasttutorial.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserAllergenMedicationRepository allergenRepository;

    private final UserAllergenMedicationService userAllergenMedicationService;

    public void addUser(@NonNull User user) {
        log.info("Adding user: {}", user);
        repository.save(getUserEntity(user));
        allergenRepository.saveAll(getAllergenEntities(user));
    }

    public User getUser(@NonNull String pesel) throws UserNotFoundException {
        log.info("Finding user with pesel: {}", pesel);
        UserEntity userEntity = repository.findByPesel(pesel);

        if (isNull(userEntity))
            throw new UserNotFoundException();

        return getUser(userEntity, userAllergenMedicationService.getUserMedicationList(pesel));
    }

    private List<UserAllergenMedicationEntity> getAllergenEntities(User user) {
        List<AllergenMedication> allergens = ofNullable(user.getAllergens()).orElse(Collections.emptyList());
        List<UserAllergenMedicationEntity> resultList = new ArrayList<>();

        allergens.forEach(
                allergen ->
                        resultList.add(UserAllergenMedicationEntity.builder()
                                .medication(allergen)
                                .pesel(user.getPesel())
                                .build())
        );

        return resultList;
    }

    public void updateUser(@NonNull User user) throws UserNotFoundException {
        log.info("Updating user data: {}", user);

        repository.save(getUpdatedUser(user));
    }

    private UserEntity getUpdatedUser(@NonNull User user) throws UserNotFoundException {
        UserEntity userEntity = repository.findByPesel(user.getPesel());
        if (isNull(userEntity))
            throw new UserNotFoundException();

        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());

        return userEntity;
    }

    private UserEntity getUserEntity(@NonNull User user) {
        return UserEntity.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .pesel(user.getPesel())
                .build();
    }

    private User getUser(@NonNull UserEntity user, List<AllergenMedication> allergens) {
        return User.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .pesel(user.getPesel())
                .allergens(allergens)
                .build();
    }
}
