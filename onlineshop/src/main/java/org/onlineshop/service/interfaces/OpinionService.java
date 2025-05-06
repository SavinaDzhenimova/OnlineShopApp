package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.OpinionDTO;
import org.onlineshop.model.importDTO.AddOpinionDTO;

import java.util.List;

public interface OpinionService {

    Result addOpinion(AddOpinionDTO addOpinionDTO);

    List<OpinionDTO> getOpinionsForIndexPage();

    List<OpinionDTO> getAllOpinions();

    List<OpinionDTO> getLoggedUserOpinions();

}
