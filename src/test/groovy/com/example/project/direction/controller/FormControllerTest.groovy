package com.example.project.direction.controller

import com.example.project.direction.dto.OutputDto
import com.example.project.pharmacy.service.PharmacyRecommendationService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;           // mockmvc를 간결하게 사용하기위해서 미리받아온것이라고함.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FormControllerTest extends Specification {

    private MockMvc mockMvc
    private PharmacyRecommendationService pharmacyRecommendationService = Mock()
    private List<OutputDto> outputDtoList

    def setup(){
        // FormController MockMvc 객체로 만든다.
        mockMvc = MockMvcBuilders.standaloneSetup(new FormController(pharmacyRecommendationService))
                    .build()
        // mockMvc만드는 법이 두방법이 있다. 위에 standaloneSetup은 단위테스트같은것, 나머지 webAppli~ 는 통합테스트 비슷
        outputDtoList = new ArrayList<>()
        outputDtoList.addAll(
                OutputDto.builder()
                            .pharmacyName("pharmacy1")
                            .build(),
                OutputDto.builder()
                            .pharmacyName("pharmacy2")
                            .build()
        )
    }

    def "GET /"(){
        expect:
        // FormController 의 "/" URI를 get방식으로 호출
        mockMvc.perform(get("/"))
            .andExpect(handler().handlerType(FormController.class))
            .andExpect(handler().methodName("main"))
            .andExpect(status().isOk())
            .andExpect(view().name("main"))
            .andDo(log())
    }

    def "POST /search"(){

        given:
        String inputAddress = "서울 성북구 종암동"

        when:
        def resultActions = mockMvc.perform(post("/search")
                .param("address", inputAddress))

        then:
        1 * pharmacyRecommendationService.recommendPharmacyList(argument -> {
            assert argument == inputAddress // mock 객체의 argument 검증
        }) >> outputDtoList

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("output"))
                .andExpect(model().attributeExists("outputFormList")) // model에 outputFormList라는 key가 존재하는지 확인
                .andExpect(model().attribute("outputFormList", outputDtoList))
                .andDo(print())
    }










}
