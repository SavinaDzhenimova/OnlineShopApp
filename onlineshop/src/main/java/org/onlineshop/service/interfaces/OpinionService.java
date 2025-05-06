package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.importDTO.AddOpinionDTO;

public interface OpinionService {

    Result addOpinion(AddOpinionDTO addOpinionDTO);
}
