package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.importDTO.AddInquiryDTO;
import org.onlineshop.service.events.SendInquiryEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactsController {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ContactsController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/contacts")
    public ModelAndView contacts(Model model) {

        if (!model.containsAttribute("addInquiryDTO")) {
            model.addAttribute("addInquiryDTO", new AddInquiryDTO());
        }

        return new ModelAndView("contacts");
    }

    @PostMapping("/contacts/send-inquiry")
    public ModelAndView sendInquiry(@Valid @ModelAttribute AddInquiryDTO addInquiryDTO,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addInquiryDTO", addInquiryDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addInquiryDTO",
                            bindingResult);

            return new ModelAndView("redirect:/contacts");
        }

        this.applicationEventPublisher.publishEvent(
                new SendInquiryEvent(this, addInquiryDTO.getFullName(), addInquiryDTO.getEmail(),
                        addInquiryDTO.getPhoneNumber(), addInquiryDTO.getMessage()));

        redirectAttributes.addFlashAttribute("successMessage",
                "Вашето запитване беше изпратено успешно!");

        return new ModelAndView("redirect:/contacts");
    }
}