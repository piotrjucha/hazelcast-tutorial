package pl.devopsi.hazelcasttutorial.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pl.devopsi.hazelcasttutorial.entity.UserAllergenMedicationEntity;

import javax.annotation.CheckForNull;
import java.util.List;

@Repository
public interface UserAllergenMedicationRepository extends QueryByExampleExecutor<UserAllergenMedicationEntity>,
        JpaRepository<UserAllergenMedicationEntity, Long> {

    @CheckForNull
    List<UserAllergenMedicationEntity> findAllByPesel(@NonNull String pesel);
}
