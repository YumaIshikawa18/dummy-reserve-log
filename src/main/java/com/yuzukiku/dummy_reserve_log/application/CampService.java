package com.yuzukiku.dummy_reserve_log.application;

import com.yuzukiku.dummy_reserve_log.domain.entity.CampFacilities;
import com.yuzukiku.dummy_reserve_log.domain.entity.Camps;
import com.yuzukiku.dummy_reserve_log.domain.entity.Facilities;
import com.yuzukiku.dummy_reserve_log.domain.repository.CampFacilityRepository;
import com.yuzukiku.dummy_reserve_log.domain.repository.CampRepository;
import com.yuzukiku.dummy_reserve_log.domain.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * (合計 >= 23 -> level = 1, else = 2)
     * 23の理由は施設ポイントのバランスを見て主観で指定
     */
    public void updateCampLevel(String campId) {
        Camps camps = campRepository.findByCampId(UUID.fromString(campId)).orElse(null);
        if (camps == null) return;

        List<CampFacilities> campFacilities = campFacilityRepository.findByCampId(UUID.fromString(campId));

        int totalPoints = 0;
        for (CampFacilities campFacility : campFacilities) {
            Facilities facilities = facilityRepository.findByFacilityId(campFacility.getFacilityId()).orElse(null);
            if (facilities != null) {
                totalPoints += facilities.getPoints();
            }
        }

        int newLevel = (totalPoints >= 23) ? 1 : 2;
        camps.setLevel(newLevel);
        campRepository.save(camps);
    }

    public void updateAllCampLevels() {
        List<Camps> camps = campRepository.findAll();
        for (Camps camp : camps) {
            updateCampLevel(String.valueOf(camp.getCampId()));
        }
    }
}
