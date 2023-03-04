package com.example.project.pharmacy.service

import com.example.project.AbstractIntegrationContainerBaseTest
import com.example.project.pharmacy.entity.Pharmacy
import com.example.project.pharmacy.repository.PharmacyRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class PharmacyRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PharmacyRepositoryService pharmacyRepositoryService

    @Autowired
    private PharmacyRepository pharmacyRepository

    def setup(){
        pharmacyRepository.deleteAll();
    }

    def "PharmacyRepository update - dirty checking success"(){
        given:
        String inputAddress = "서울 특별시 성북구 종암동"   //https://isntyet.github.io/java/TestContainers%EB%A1%9C-test-%EB%A9%B1%EB%93%B1%EC%84%B1-%EB%86%92%EC%9D%B4%EA%B8%B0/
        String modifiedAddress = "서울 광진구 구의동"                   // characterset에 문제있어서 에러
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(inputAddress)
                .pharmacyName(name)
                .build()

        when:
        def entity = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getPharmacyAddress() == modifiedAddress
    }

    def "PharmacyRepository update - dirty checking fail"(){
        given:
        String inputAddress = "서울 특별시 성북구 종암동"   //https://isntyet.github.io/java/TestContainers%EB%A1%9C-test-%EB%A9%B1%EB%93%B1%EC%84%B1-%EB%86%92%EC%9D%B4%EA%B8%B0/
        String modifiedAddress = "서울 광진구 구의동"                   // characterset에 문제있어서 에러
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(inputAddress)
                .pharmacyName(name)
                .build()

        when:
        def entity = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddressWithoutTransaction(entity.getId(), modifiedAddress)

        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getPharmacyAddress() == inputAddress
    }
}
