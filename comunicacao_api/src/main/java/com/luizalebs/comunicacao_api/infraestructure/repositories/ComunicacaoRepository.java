package com.luizalebs.comunicacao_api.infraestructure.repositories;

import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ComunicacaoRepository extends CrudRepository<ComunicacaoEntity, Long> {



    Optional<ComunicacaoEntity>findByEmailDestinatario(String emailDestinatario);

    List<ComunicacaoEntity> findAllByEmailDestinatario(String emailDestinatario);

    Optional<ComunicacaoEntity> findById(Long id);

    List<ComunicacaoEntity> findByDataHoraEnvioBetweenAndStatusEnvio(
            LocalDateTime dataInicial, LocalDateTime dataFinal, StatusEnvioEnum statusEnvio
    );
}

