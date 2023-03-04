package com.example.project.pharmacy.repository

import com.example.project.AbstractIntegrationContainerBaseTest
import com.example.project.pharmacy.entity.Pharmacy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDateTime

//@SpringBootTest
//class PharmacyRepositoryTest extends Specification {

class PharmacyRepositoryTest extends AbstractIntegrationContainerBaseTest { // 이미 상속받는 abstract class에 specification상속과 @springbootTest가 있음.
                                                                            // @SpringBootTest에서 pharmacyRepository를 주입받음 그런데,
                                                                            // database는 이제 testcontainer가 된다.
                                                                            // AbstractIntegrationContainerBaseTest 로부터 redis도 받는다.
    @Autowired
    private PharmacyRepository pharmacyRepository

    def setup(){    // test전에 시작한 method
        pharmacyRepository.deleteAll()  // 새 테스트 전에 기존에 있던 데이터를 정리를 한번 해주어야함.
    }
    def "PharmacyRepository save"(){
        given:
        String address = "서울 특별시 성북구 종암동"   //https://isntyet.github.io/java/TestContainers%EB%A1%9C-test-%EB%A9%B1%EB%93%B1%EC%84%B1-%EB%86%92%EC%9D%B4%EA%B8%B0/
        String name = "은혜 약국"                   // characterset에 문제있어서 에러
        double latitude = 36.11
        double longitude = 128.11

        def pharmacy = Pharmacy.builder()
            .pharmacyAddress(address)
            .pharmacyName(name)
            .latitude(latitude)
            .longitude(longitude)
            .build()
        when:
        def result= pharmacyRepository.save(pharmacy)

        then:
        result.getPharmacyAddress() == address
        result.getPharmacyName() == name
        result.getLatitude() == latitude
        result.getLongitude() == longitude
    }

    def "PharmacyRepository saveAll"(){
        given:
        String address = "서울 특별시 성북구 종암동"   //https://isntyet.github.io/java/TestContainers%EB%A1%9C-test-%EB%A9%B1%EB%93%B1%EC%84%B1-%EB%86%92%EC%9D%B4%EA%B8%B0/
        String name = "은혜 약국"                   // characterset에 문제있어서 에러
        double latitude = 36.11
        double longitude = 128.11

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(address)
                .pharmacyName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()
        when:
        pharmacyRepository.saveAll(Arrays.asList(pharmacy))
        def result = pharmacyRepository.findAll()

        then:
        result.size() == 1
    }

    def "BaseTimeEntity 등록"(){
        given:
        LocalDateTime now = LocalDateTime.now()
        String address = "서울 특별시 성북구 종암동"
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
            .pharmacyAddress(address)
            .pharmacyName(name)
            .build()
        when:
        pharmacyRepository.save(pharmacy)
        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getCreatedDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)
    }
}
