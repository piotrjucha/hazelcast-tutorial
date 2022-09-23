package pl.devopsi.hazelcasttutorial.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pl.devopsi.hazelcasttutorial.entity.UserEntity;

import javax.annotation.CheckForNull;

@Repository
public interface UserRepository extends QueryByExampleExecutor<UserEntity>,
        JpaRepository<UserEntity, Long> {

    @CheckForNull
    UserEntity findByPesel(@NonNull String pesel);
}
