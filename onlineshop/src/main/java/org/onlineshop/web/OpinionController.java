package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.OpinionDTO;
import org.onlineshop.model.importDTO.AddOpinionDTO;
import org.onlineshop.model.user.UserRegisterDTO;
import org.onlineshop.service.interfaces.OpinionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OpinionController {

    private final OpinionService opinionService;

    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("/opinions")
    public ModelAndView opinions() {

        ModelAndView modelAndView = new ModelAndView("opinions");

        List<OpinionDTO> opinionDTOList = this.opinionService.getAllOpinions();

        modelAndView.addObject("opinions", opinionDTOList);
        modelAndView.addObject("title", "Мнения от клиентите ни");

        if (opinionDTOList.isEmpty()) {
            modelAndView.addObject("warningMessage", "Все още няма добавени коментари за разглеждане!");
        }

        return modelAndView;
    }

    @GetMapping("/users/my-opinions")
    public ModelAndView showLoggedUserOpinions() {

        ModelAndView modelAndView = new ModelAndView("opinions");

        List<OpinionDTO> loggedUserOpinions = this.opinionService.getLoggedUserOpinions();

        modelAndView.addObject("opinions", loggedUserOpinions);
        modelAndView.addObject("title", "Моите мнения");

        if (loggedUserOpinions.isEmpty()) {
            modelAndView.addObject("warningMessage", "Все още нямате добавени коментари за разглеждане!");
        }

        return modelAndView;
    }

    @GetMapping("/opinions/add-opinion")
    public ModelAndView showAddOpinion(Model model) {

        if (!model.containsAttribute("addOpinionDTO")) {
            model.addAttribute("addOpinionDTO", new AddOpinionDTO());
        }

        return new ModelAndView("add-opinion");
    }

    @PostMapping("/opinions/add-opinion")
    public ModelAndView addOpinion(@Valid @ModelAttribute("addOpinionDTO") AddOpinionDTO addOpinionDTO,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addOpinionDTO", addOpinionDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addOpinionDTO",
                            bindingResult);

            return new ModelAndView("add-opinion");
        }

        Result result = this.opinionService.addOpinion(addOpinionDTO);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/opinions");
    }

    @DeleteMapping("/opinions/delete-opinion/{id}")
    public ModelAndView deleteOpinion(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        Result result = this.opinionService.deleteOpinion(id);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/opinions");
    }
}