package org.onlineshop.service;

import org.onlineshop.model.entity.Opinion;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.exportDTO.OpinionDTO;
import org.onlineshop.model.importDTO.AddOpinionDTO;
import org.onlineshop.repository.OpinionRepository;
import org.onlineshop.service.interfaces.OpinionService;
import org.onlineshop.service.interfaces.UserService;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OpinionServiceImpl implements OpinionService {

    private final OpinionRepository opinionRepository;
    private final UserService userService;
    private final CurrentUserProvider currentUserProvider;

    public OpinionServiceImpl(OpinionRepository opinionRepository, UserService userService,
                              CurrentUserProvider currentUserProvider) {
        this.opinionRepository = opinionRepository;
        this.userService = userService;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public List<OpinionDTO> getOpinionsForIndexPage() {

        return this.opinionRepository.findAll().stream()
                .sorted(Comparator.comparing(Opinion::getAddedOn).reversed())
                .map(this::mapOpinionToDto)
                .limit(3)
                .toList();
    }

    @Override
    public List<OpinionDTO> getAllOpinions() {

        return this.opinionRepository.findAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getRating(), o1.getRating()))
                .map(this::mapOpinionToDto)
                .toList();
    }

    @Override
    public List<OpinionDTO> getLoggedUserOpinions() {

        User loggedUser = this.currentUserProvider.getLoggedUser();

        return loggedUser.getOpinions().stream()
                .map(this::mapOpinionToDto)
                .toList();
    }

    private OpinionDTO mapOpinionToDto(Opinion opinion) {
        OpinionDTO opinionDTO = new OpinionDTO();

        opinionDTO.setAuthor(opinion.getAuthor());
        opinionDTO.setOpinion(opinion.getOpinion());
        opinionDTO.setRating(opinion.getRating());
        opinionDTO.setAddedOn(opinion.getAddedOn());

        return opinionDTO;
    }

    @Override
    public Result addOpinion(AddOpinionDTO addOpinionDTO) {

        if (addOpinionDTO == null) {
            return new Result(false, "Мнението не съществува!");
        }

        Opinion opinion = new Opinion();

        opinion.setAuthor(addOpinionDTO.getAuthor());
        opinion.setEmail(addOpinionDTO.getEmail());
        opinion.setPhoneNumber(addOpinionDTO.getPhoneNumber());
        opinion.setOpinion(addOpinionDTO.getOpinion());
        opinion.setRating(addOpinionDTO.getRating());
        opinion.setAddedOn(LocalDate.now());

        Optional<User> optionalUser = this.userService.getUserByEmail(addOpinionDTO.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            opinion.setUser(user);
            user.getOpinions().add(opinion);

            this.userService.saveAndFlushUser(user);
        } else {
            this.opinionRepository.saveAndFlush(opinion);
        }

        return new Result(true, "Успешно добавихте своя коментар!");
    }
}