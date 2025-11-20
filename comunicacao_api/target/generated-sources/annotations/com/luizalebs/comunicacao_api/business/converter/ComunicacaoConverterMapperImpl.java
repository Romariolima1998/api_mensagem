package com.luizalebs.comunicacao_api.business.converter;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-19T16:11:53-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.29 (Amazon.com Inc.)"
)
@Component
public class ComunicacaoConverterMapperImpl implements ComunicacaoConverterMapper {

    @Override
    public ComunicacaoEntity paraEntity(ComunicacaoInDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ComunicacaoEntity.ComunicacaoEntityBuilder comunicacaoEntity = ComunicacaoEntity.builder();

        comunicacaoEntity.dataHoraEnvio( dto.getDataHoraEnvio() );
        comunicacaoEntity.nomeDestinatario( dto.getNomeDestinatario() );
        comunicacaoEntity.emailDestinatario( dto.getEmailDestinatario() );
        comunicacaoEntity.telefoneDestinatario( dto.getTelefoneDestinatario() );
        comunicacaoEntity.mensagem( dto.getMensagem() );
        comunicacaoEntity.modoDeEnvio( dto.getModoDeEnvio() );
        comunicacaoEntity.statusEnvio( dto.getStatusEnvio() );

        return comunicacaoEntity.build();
    }

    @Override
    public ComunicacaoOutDTO paraDTO(ComunicacaoEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ComunicacaoOutDTO.ComunicacaoOutDTOBuilder comunicacaoOutDTO = ComunicacaoOutDTO.builder();

        comunicacaoOutDTO.id( entity.getId() );
        comunicacaoOutDTO.dataHoraEnvio( entity.getDataHoraEnvio() );
        comunicacaoOutDTO.nomeDestinatario( entity.getNomeDestinatario() );
        comunicacaoOutDTO.emailDestinatario( entity.getEmailDestinatario() );
        comunicacaoOutDTO.emailOwner( entity.getEmailOwner() );
        comunicacaoOutDTO.telefoneDestinatario( entity.getTelefoneDestinatario() );
        comunicacaoOutDTO.mensagem( entity.getMensagem() );
        comunicacaoOutDTO.modoDeEnvio( entity.getModoDeEnvio() );
        comunicacaoOutDTO.statusEnvio( entity.getStatusEnvio() );

        return comunicacaoOutDTO.build();
    }

    @Override
    public List<ComunicacaoOutDTO> paraListDTO(List<ComunicacaoEntity> entity) {
        if ( entity == null ) {
            return null;
        }

        List<ComunicacaoOutDTO> list = new ArrayList<ComunicacaoOutDTO>( entity.size() );
        for ( ComunicacaoEntity comunicacaoEntity : entity ) {
            list.add( paraDTO( comunicacaoEntity ) );
        }

        return list;
    }
}
