package org.onlineshop.service;

import jakarta.transaction.Transactional;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.ReturnOrReplacementRequest;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.enums.RequestType;
import org.onlineshop.model.exportDTO.ReturnOrReplacementRequestDTO;
import org.onlineshop.model.importDTO.AddReturnOrReplacementRequestDTO;
import org.onlineshop.repository.ReturnOrReplacementRequestRepository;
import org.onlineshop.service.events.MakeRequestEvent;
import org.onlineshop.service.interfaces.ReturnOrReplacementRequestService;
import org.onlineshop.service.interfaces.UserService;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReturnOrReplacementRequestServiceImpl implements ReturnOrReplacementRequestService {

    private final ReturnOrReplacementRequestRepository replacementRequestRepository;
    private final UserService userService;
    private final CurrentUserProvider currentUserProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ReturnOrReplacementRequestServiceImpl(ReturnOrReplacementRequestRepository replacementRequestRepository,
                                                 UserService userService, CurrentUserProvider currentUserProvider,
                                                 ApplicationEventPublisher applicationEventPublisher) {
        this.replacementRequestRepository = replacementRequestRepository;
        this.userService = userService;
        this.currentUserProvider = currentUserProvider;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public List<ReturnOrReplacementRequestDTO> getAllRequests() {
        return this.replacementRequestRepository.findAll().stream()
                .map(this::mapRequestToDto).toList();
    }

    @Override
    public List<ReturnOrReplacementRequestDTO> getLoggedUserRequests() {

        User loggedUser = this.currentUserProvider.getLoggedUser();

        return loggedUser.getReturnOrReplacementRequests().stream()
                .map(this::mapRequestToDto)
                .toList();
    }

    private ReturnOrReplacementRequestDTO mapRequestToDto(ReturnOrReplacementRequest request) {
        ReturnOrReplacementRequestDTO requestDTO = new ReturnOrReplacementRequestDTO();

        requestDTO.setId(request.getId());
        requestDTO.setFullName(request.getFullName());
        requestDTO.setEmail(request.getEmail());
        requestDTO.setPhoneNumber(request.getPhoneNumber());
        requestDTO.setRegion(request.getRegion().getDisplayName());
        requestDTO.setTown(request.getTown());
        requestDTO.setPostalCode(request.getPostalCode());
        requestDTO.setStreet(request.getStreet());
        requestDTO.setAddressType(request.getAddressType().getDisplayName());
        requestDTO.setRequestType(request.getRequestType().getDisplayName());
        requestDTO.setCreatedOn(request.getCreatedOn());
        requestDTO.setCompleted(request.isCompleted());
        requestDTO.setCompletedOn(request.getCompletedOn());

        String requestClass = (request.getRequestType().equals(RequestType.RETURN)) ? "return" : "replace";
        requestDTO.setRequestClass(requestClass);

        return requestDTO;
    }

    @Override
    public Result returnOrReplaceProduct(AddReturnOrReplacementRequestDTO addReturnOrReplacementRequestDTO) {

        Optional<ReturnOrReplacementRequest> optionalRequestByEmail = this.replacementRequestRepository
                .findByEmail(addReturnOrReplacementRequestDTO.getEmail());
        Optional<ReturnOrReplacementRequest> optionalRequestByPhoneNumber = this.replacementRequestRepository
                .findByPhoneNumber(addReturnOrReplacementRequestDTO.getPhoneNumber());

        if (optionalRequestByEmail.isPresent() || optionalRequestByPhoneNumber.isPresent()) {
            return new Result(false, "Вече е направена заявка за замяна/връщане от този имейл/мобилен телефон!");
        }

        ReturnOrReplacementRequest request = new ReturnOrReplacementRequest();

        request.setFullName(addReturnOrReplacementRequestDTO.getFullName());
        request.setEmail(addReturnOrReplacementRequestDTO.getEmail());
        request.setPhoneNumber(addReturnOrReplacementRequestDTO.getPhoneNumber());
        request.setCreatedOn(LocalDateTime.now());
        request.setRegion(addReturnOrReplacementRequestDTO.getRegion());
        request.setTown(addReturnOrReplacementRequestDTO.getTown());
        request.setStreet(addReturnOrReplacementRequestDTO.getStreet());
        request.setPostalCode(addReturnOrReplacementRequestDTO.getPostalCode());
        request.setAddressType(addReturnOrReplacementRequestDTO.getAddressType());
        request.setRequestType(addReturnOrReplacementRequestDTO.getRequestType());
        request.setCompleted(false);

        Optional<User> optionalUserByEmail = this.userService.getUserByEmail(addReturnOrReplacementRequestDTO.getEmail());

        if (optionalUserByEmail.isPresent()) {
            User user = optionalUserByEmail.get();

            request.setUser(user);
            user.getReturnOrReplacementRequests().add(request);
            this.userService.saveAndFlushUser(user);
        } else {
            this.replacementRequestRepository.saveAndFlush(request);
        }

        String address = request.getRegion().getDisplayName() + ", " + request.getTown() + " " + request.getPostalCode() +
                ", " + request.getStreet() + " (" + request.getAddressType().getDisplayName() + ")";

        this.applicationEventPublisher.publishEvent(new MakeRequestEvent(this, request.getFullName(),
                request.getEmail(), request.getPhoneNumber(), address, request.getRequestType().getDisplayName()));

        return new Result(true, "Успешно създадохте заявка за замяна/връщане. " +
                "Наш служител ще се свърже с Вас за потвърждение.");
    }

    @Override
    public Result completeRequest(Long id) {

        Optional<ReturnOrReplacementRequest> optionalRequest = this.replacementRequestRepository.findById(id);

        if (optionalRequest.isEmpty()) {
            return new Result(false, "Заявката, която се опитвате да завършите, не съществува!");
        }

        ReturnOrReplacementRequest request = optionalRequest.get();

        if (!request.isCompleted()) {
            request.setCompleted(true);
            request.setCompletedOn(LocalDateTime.now());
        } else {
            return new Result(false, "Тази заявка вече е завършена!");
        }

        this.replacementRequestRepository.saveAndFlush(request);
        return new Result(true, "Тази заявка беше успешно завършена!");
    }

    @Transactional
    @Override
    public Result deleteRequest(Long id) {

        Optional<ReturnOrReplacementRequest> optionalRequestBeforeDeletion = this.replacementRequestRepository.findById(id);

        if (optionalRequestBeforeDeletion.isEmpty()) {
            return new Result(false, "Заявката, която се опитвате да изтриете, не съществува!");
        }

        if (!optionalRequestBeforeDeletion.get().isCompleted()) {
            return new Result(false, "Не можете да изтриете заявка, която още не е завършена!");
        }

        this.replacementRequestRepository.deleteById(id);
        return new Result(true, "Успешно изтрихте тази заявка.");
    }
}