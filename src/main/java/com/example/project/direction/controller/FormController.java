package com.example.project.direction.controller;

import com.example.project.direction.dto.InputDto;
import com.example.project.pharmacy.service.PharmacyRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class FormController {
    private final PharmacyRecommendationService pharmacyRecommendationService;

    @GetMapping("/")
    public String main(){

        return "main";
    }

    @PostMapping("/search")     // @ModelAttribute는 parameter를 object형태로 매핑. 해당 객체에 setter필요
    public ModelAndView postDirection(@ModelAttribute InputDto inputDto){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("output"); //보통 return을 응답할 jsp나, thymeleaf, hbs를 사용하는데, 이렇게 세팅하면 template에서 ouput을 찾는다.
                                            //https://velog.io/@modsiw/Spring-ModelAndView
        modelAndView.addObject("outputFormList",
                pharmacyRecommendationService.recommendPharmacyList(inputDto.getAddress()));


        return modelAndView;
    }
}
