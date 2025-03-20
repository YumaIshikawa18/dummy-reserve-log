package com.yuzukiku.dummy_reserve_log.application;

import com.yuzukiku.dummy_reserve_log.domain.entity.Facilities;
import com.yuzukiku.dummy_reserve_log.domain.repository.CampFacilityRepository;
import com.yuzukiku.dummy_reserve_log.domain.repository.CampRepository;
import com.yuzukiku.dummy_reserve_log.domain.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CampService {
    private final CampRepository campRepository;
    private final CampFacilityRepository campFacilityRepository;
    private final FacilityRepository facilityRepository;

    /**
     * 指定された campId の施設ポイント合計を計算して、level更新
     * (合計 >= 23 -> level = 1, それ以外 -> level = 2)
     * 23 は施設ポイントのバランスを見て主観的に指定した値
     */
    public void updateCampLevel(String campId) {
        UUID uuid = UUID.fromString(campId);
        campRepository.findByCampId(uuid).ifPresent(camp -> {
            int totalPoints = campFacilityRepository.findByCampId(uuid).stream()
                    .map(cf -> facilityRepository.findByFacilityId(cf.getFacilityId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .mapToInt(Facilities::getPoints)
                    .sum();
            int newLevel = (totalPoints >= 23) ? 1 : 2;
            camp.setLevel(newLevel);
            campRepository.save(camp);
        });
    }

    public void updateAllCampLevels() {
        campRepository.findAll()
                .forEach(camp -> updateCampLevel(camp.getCampId().toString()));
    }
}
