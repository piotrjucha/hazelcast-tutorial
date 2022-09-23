package pl.devopsi.hazelcasttutorial.service;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import pl.devopsi.hazelcasttutorial.entity.UserAllergenMedicationEntity;
import pl.devopsi.hazelcasttutorial.exception.UserNotFoundException;
import pl.devopsi.hazelcasttutorial.model.AllergenMedication;
import pl.devopsi.hazelcasttutorial.repository.UserAllergenMedicationRepository;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAllergenMedicationService {

    private final UserAllergenMedicationRepository repository;

    private IMap<String, List<AllergenMedication>> medications;

    private HazelcastInstance hazelcastInstance;

    private static final String CACHE_NAME = "medications";

    private static final String HAZELCAST_INSTANCE_NAME = "allergen-medication-instance";

    @Bean
    void initializeHazelcast() {
        log.info("INITIALIZING HAZELCAST .....");
        createHazelcast();
        medications = hazelcastInstance.getMap(CACHE_NAME);
    }

    public List<AllergenMedication> getUserMedicationList(@NonNull String pesel) throws UserNotFoundException {
        log.info("Searching for medications in cache for user with pesel: {}", pesel);

        if (medications.containsKey(pesel)) {
            log.info("Found medications in cache: {}", medications.get(pesel));
            return medications.get(pesel);
        }

        log.info("Medications for user with pesel: {} not found in cache. Searching in database.", pesel);
        medications.put(pesel, getUserMedications(pesel));
        log.info("Medications for user with pesel: {} found in database. Medications: {}", pesel, medications.get(pesel));

        return medications.get(pesel);
    }

    private List<AllergenMedication> getUserMedications(@NonNull String pesel) throws UserNotFoundException {
        List<UserAllergenMedicationEntity> entities = repository.findAllByPesel(pesel);

        if (isNull(entities))
            throw new UserNotFoundException();

        return entities.stream()
                .map(UserAllergenMedicationEntity::getMedication)
                .collect(toList());
    }

    private void createHazelcast() {
        log.debug("Creating hazelcast instance ...");
        Config hazelcastConfig = new Config();
        NetworkConfig hazelcastNetworkConfig = new NetworkConfig();
        hazelcastNetworkConfig.getJoin().getMulticastConfig().setEnabled(false);
        hazelcastNetworkConfig.getJoin().getKubernetesConfig().setEnabled(false);
        hazelcastConfig.setNetworkConfig(hazelcastNetworkConfig);
        hazelcastConfig.setInstanceName(HAZELCAST_INSTANCE_NAME);

//      hazelcastNetworkConfig.getJoin().getKubernetesConfig().setEnabled(isKubernetesConfigEnabled());
//      hazelcastNetworkConfig.getJoin().getKubernetesConfig().setProperty("service-name", getKubernetesServiceName());

        hazelcastInstance = Hazelcast.newHazelcastInstance(hazelcastConfig);
    }
}
