package com.example.project.pharmacy.service;

import com.example.project.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.project.pharmacy.dto.PharmacyDto;
import com.example.project.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {
    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    public List<PharmacyDto> searchPharmacyDtoList(){

        // redis
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
        System.out.println(pharmacyDtoList);
        if(!pharmacyDtoList.isEmpty()) {
            log.info("redis findAll success!");

            return pharmacyDtoList;
        }

        // db
        return pharmacyRepositoryService.findAll()  // java8이상 stream method : https://futurecreator.github.io/2018/08/26/java-8-streams/
                .stream()// list를 stream 타입으로 변경.
                .map(this::convertToPharmacyDto)// alt+enter 해서  .map(entity -> convertToPharmacyDto(entity)), 변경된 converToPharcyDto(entity)부분들을 따로 모아줌
                .collect(Collectors.toList());// stream타입을 list로 다시 바꾸어줌
    }

    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy  ){
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }
}
