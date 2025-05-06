package org.onlineshop.service;

import org.onlineshop.model.entity.Opinion;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.importDTO.AddOpinionDTO;
import org.onlineshop.repository.OpinionRepository;
import org.onlineshop.service.interfaces.OpinionService;
import org.springframework.stereotype.Service;

@Service
public class OpinionServiceImpl implements OpinionService {

    private final OpinionRepository opinionRepository;

    public OpinionServiceImpl(OpinionRepository opinionRepository) {
        this.opinionRepository = opinionRepository;
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

        this.opinionRepository.saveAndFlush(opinion);
        return new Result(true, "Успешно добавихте своя коментар!");
    }
}