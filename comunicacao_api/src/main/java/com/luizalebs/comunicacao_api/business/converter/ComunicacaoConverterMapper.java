package com.luizalebs.comunicacao_api.business.converter;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ComunicacaoConverterMapper {

    ComunicacaoEntity paraEntity(ComunicacaoInDTO dto);


    ComunicacaoOutDTO paraDTO(ComunicacaoEntity entity);

    List<ComunicacaoOutDTO> paraListDTO(List<ComunicacaoEntity> entity);
}
