package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.ReturnOrReplacementRequestDTO;
import org.onlineshop.model.importDTO.AddReturnOrReplacementRequestDTO;

import java.util.List;

public interface ReturnOrReplacementRequestService {

    Result returnOrReplaceProduct(AddReturnOrReplacementRequestDTO addReturnOrReplacementRequestDTO);

    List<ReturnOrReplacementRequestDTO> getAllRequests();

    Result deleteRequest(Long id);

    List<ReturnOrReplacementRequestDTO> getLoggedUserRequests();

    Result completeRequest(Long id);

}