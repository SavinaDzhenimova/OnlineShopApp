package org.onlineshop.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.onlineshop.model.entity.DiscountCard;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.exportDTO.DiscountCardDTO;
import org.onlineshop.model.exportDTO.OrderDTO;
import org.onlineshop.model.exportDTO.OrderRequestDTO;
import org.onlineshop.model.importDTO.AddOrderDTO;
import org.onlineshop.service.interfaces.DiscountCardService;
import org.onlineshop.service.interfaces.OrderService;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final DiscountCardService discountCardService;
    private final CurrentUserProvider currentUserProvider;

    public OrderController(OrderService orderService, DiscountCardService discountCardService,
                           CurrentUserProvider currentUserProvider) {
        this.orderService = orderService;
        this.discountCardService = discountCardService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping(value = "/create-order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO request, HttpSession session) {

        OrderRequestDTO createdOrder = orderService.createOrder(
                request.getPromoCode(),
                request.getOrderItems()
        );

        session.setAttribute("createdOrder", createdOrder);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirmation")
    public ModelAndView showConfirmation(Model model, HttpSession session) {
        Optional<AddOrderDTO> optionalAddOrderDTO = this.orderService.prepareAddOrderDTOFromSession(session);

        if (optionalAddOrderDTO.isEmpty()) {
            return new ModelAndView("redirect:/shopping-cart");
        }

        AddOrderDTO addOrderDTO = optionalAddOrderDTO.get();
        OrderRequestDTO createdOrder = (OrderRequestDTO) session.getAttribute("createdOrder");

        ModelAndView modelAndView = new ModelAndView("order-confirmation");

        if (!model.containsAttribute("addOrderDTO")) {
            model.addAttribute("addOrderDTO", addOrderDTO);
        }

        User loggedUser = this.currentUserProvider.getLoggedUser();
        DiscountCardDTO discountCardDTO = this.discountCardService.getDiscountCard(loggedUser, createdOrder.getTotalPrice());

        if (loggedUser != null && discountCardDTO != null) {
            modelAndView.addObject("discountCard", discountCardDTO);
        }

        modelAndView.addObject("orderItems", createdOrder.getOrderItems());
        modelAndView.addObject("promoCode", createdOrder.getPromoCode());
        modelAndView.addObject("totalPrice", addOrderDTO.getTotalPrice());
        modelAndView.addObject("discount", addOrderDTO.getDiscount());
        modelAndView.addObject("discountPercent", addOrderDTO.getDiscountPercent());
        modelAndView.addObject("finalPrice", addOrderDTO.getFinalPrice());

        return modelAndView;
    }

    @PostMapping("/make-order")
    public ModelAndView makeOrder(@Valid @ModelAttribute("addOrderDTO") AddOrderDTO addOrderDTO,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                  HttpSession session) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addOrderDTO", addOrderDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addOrderDTO",
                            bindingResult);

            return new ModelAndView("redirect:/orders/confirmation");
        }

        Result result = this.orderService.makeOrder(addOrderDTO, session);

        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            String discountCard = this.discountCardService.checkForDiscountCard(loggedUser, loggedUser.getTotalOutcome());
            redirectAttributes.addFlashAttribute("newDiscountCardMessage", discountCard);
        }

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());

            return new ModelAndView("redirect:/orders/confirmation");
        }

        return new ModelAndView("redirect:/orders/success");
    }

    @GetMapping("/success")
    public ModelAndView showSuccess() {

        return new ModelAndView("order-success");
    }

    @GetMapping("/track/{id}/{trackingCode}")
    public ModelAndView trackOrder(@PathVariable("id") Long id, @PathVariable("trackingCode") String trackingCode) {

        ModelAndView modelAndView = new ModelAndView("order-tracking");

        OrderDTO orderDTO = this.orderService.getOrderInfo(id, trackingCode);

        modelAndView.addObject("order", orderDTO);
        modelAndView.addObject("orderItems", orderDTO.getOrderItems());

        return modelAndView;
    }

    @GetMapping("/all")
    public ModelAndView showAllOrders() {

        ModelAndView modelAndView = new ModelAndView("orders");

        List<OrderDTO> orders = this.orderService.getAllOrders();

        if (orders.isEmpty()) {
            modelAndView.addObject("warningMessage", "Няма поръчки за преглеждане.");
        } else {
            modelAndView.addObject("orders", orders);
            modelAndView.addObject("allOrders", true);
        }

        return modelAndView;
    }

    @PutMapping("/update-status/{id}")
    public ModelAndView updateOrderStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        Result result = this.orderService.updateOrderStatus(id);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/orders/all");
    }
}