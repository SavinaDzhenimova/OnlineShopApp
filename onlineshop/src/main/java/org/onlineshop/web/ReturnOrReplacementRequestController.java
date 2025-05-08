package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.OpinionDTO;
import org.onlineshop.model.exportDTO.ReturnOrReplacementRequestDTO;
import org.onlineshop.model.importDTO.AddReturnOrReplacementRequestDTO;
import org.onlineshop.service.interfaces.ReturnOrReplacementRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ReturnOrReplacementRequestController {

    private final ReturnOrReplacementRequestService replacementRequestService;

    public ReturnOrReplacementRequestController(ReturnOrReplacementRequestService replacementRequestService) {
        this.replacementRequestService = replacementRequestService;
    }

    @GetMapping("/return-or-replacement/requests")
    public ModelAndView requests() {

        ModelAndView modelAndView = new ModelAndView("return-or-replacement-requests");

        List<ReturnOrReplacementRequestDTO> requestsDTOList = this.replacementRequestService.getAllRequests();

        modelAndView.addObject("requests",requestsDTOList);
        modelAndView.addObject("title", "Заявки за замяна/връщане");

        if (requestsDTOList.isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма направени заявки за замяна или връщане.");
        }

        return modelAndView;
    }

    @GetMapping("/users/my-requests")
    public ModelAndView showLoggedUserOpinions() {

        ModelAndView modelAndView = new ModelAndView("return-or-replacement-requests");

        List<ReturnOrReplacementRequestDTO> loggedUserRequests = this.replacementRequestService.getLoggedUserRequests();

        modelAndView.addObject("requests", loggedUserRequests);
        modelAndView.addObject("title", "Моите заявки");

        if (loggedUserRequests.isEmpty()) {
            modelAndView.addObject("warningMessage", "Нямате направени заявки за замяна или връщане!");
        }

        return modelAndView;
    }

    @GetMapping("/return-or-replacement")
    public ModelAndView returnOrReplacement(Model model) {

        if (!model.containsAttribute("addReturnOrReplacementRequestDTO")) {
            model.addAttribute("addReturnOrReplacementRequestDTO", new AddReturnOrReplacementRequestDTO());
        }

        return new ModelAndView("return-or-replacement");
    }

    @PostMapping("/return-or-replacement")
    public ModelAndView makeReturnOrReplacement(@Valid @ModelAttribute("addReturnOrReplacementRequestDTO")
                                                AddReturnOrReplacementRequestDTO addReturnOrReplacementRequestDTO,
                                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addReturnOrReplacementRequestDTO", addReturnOrReplacementRequestDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addReturnOrReplacementRequestDTO",
                            bindingResult);

            return new ModelAndView("return-or-replacement");
        }

        Result result = this.replacementRequestService.returnOrReplaceProduct(addReturnOrReplacementRequestDTO);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/return-or-replacement#result");
    }

    @DeleteMapping("/return-or-replacement/delete-request/{id}")
    public ModelAndView deleteRequest(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        Result result = this.replacementRequestService.deleteRequest(id);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/return-or-replacement/requests");
    }
}