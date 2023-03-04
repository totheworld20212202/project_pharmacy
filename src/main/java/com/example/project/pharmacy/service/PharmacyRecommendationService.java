package com.example.project.pharmacy.service;

import com.example.project.api.dto.DocumentDto;
import com.example.project.api.dto.KakaoApiResponseDto;
import com.example.project.api.service.KakaoAddressSearchService;
import com.example.project.direction.dto.OutputDto;
import com.example.project.direction.entity.Direction;
import com.example.project.direction.service.Base62Service;
import com.example.project.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
//    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;
    public List<OutputDto> recommendPharmacyList(String address){

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if(Objects.isNull(kakaoAddressSearchService) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())){
            log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address: {}", address);
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        List<Direction> directionList = directionService.buildDirectionList(documentDto);
//        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);


        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction){
//        String params = String.join(",", direction.getTargetPharmacyName(),
//               String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));
//        // 은혜약국, 38.11, 128.11
//
//        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
//                .toUriString(); // 한글에 관하여 자동인코딩해줌. 이전에는 restTemplate.exchange(uri,~) 에서
//                                // uri부분이 toUriString으로 한글 인코딩되어 전달되면 .exchange내에서 한번더 인코딩하는 문제가 생긴다고 함.
//                                // 그래서, uri형태로 보냈던 것이고, 지금은 restTemplate와 상관없어서 string형태로 encoding하여 바로 보냄
//        log.info("direction params: {}, url: {}", params, result);
        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
//                .directionUrl(result)   // 여기는 한글이 끼여들어가서 한글 인코딩된 uri를 전달할필요있음. 밑은 숫자여서 문제없음.
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
