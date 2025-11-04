package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoConverterMapper;
import com.luizalebs.comunicacao_api.business.exceptions.BadRequestException;
import com.luizalebs.comunicacao_api.business.exceptions.NotFaundException;
import com.luizalebs.comunicacao_api.infraestructure.Client;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComunicacaoService {

    private final ComunicacaoRepository repository;
    private final ComunicacaoConverterMapper converter;
    private final Client client;


    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO dto) {
        if (dto.getDataHoraEnvio() == null || dto.getNomeDestinatario() == null ||
                dto.getEmailDestinatario() == null || dto.getMensagem() == null) {
            throw new BadRequestException("json invalido ou faltando campos ");
        }
        dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        ComunicacaoEntity entity = converter.paraEntity(dto);
        repository.save(entity);
        ComunicacaoOutDTO outDTO = converter.paraDTO(entity);
        return outDTO;
    }

    public List<ComunicacaoOutDTO> buscarStatusComunicacao(String emailDestinatario) {
        List<ComunicacaoEntity> entity = repository.findAllByEmailDestinatario(emailDestinatario);

        if(entity.isEmpty()){
            throw new NotFaundException("nenhum dados encontrado para este email : " + emailDestinatario);
        }

        return converter.paraListDTO(entity);
    }

    public ComunicacaoOutDTO alterarStatusComunicacao(Long id) {
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(
                () ->   new NotFaundException("nenhum dados encontrado para este id : " + id)
        );

        entity.setStatusEnvio(StatusEnvioEnum.CANCELADO);
        repository.save(entity);
        return (converter.paraDTO(entity));
    }

    public List<ComunicacaoOutDTO> buscaComunicacaoPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal, StatusEnvioEnum status){
        List<ComunicacaoEntity> entityList = repository.findByDataHoraEnvioBetweenAndStatusEnvio(
                dataInicial, dataFinal, status
        );
        return converter.paraListDTO(entityList);
    }

    public void enviaEmail(ComunicacaoOutDTO dto){
        client.enviarEmail(dto);
    }

    public void alteraStatusEnviado(Long id){
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(() -> new BadRequestException("id nao encontrado"));

        entity.setStatusEnvio(StatusEnvioEnum.ENVIADO);
        repository.save(entity);
    }

}
