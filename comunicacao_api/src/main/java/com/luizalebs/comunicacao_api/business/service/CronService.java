package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.Client;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CronService {

    private final ComunicacaoService comunicacaoService;


    @Scheduled(cron = "${cron.horario}")
    public void buscaMensagemEnviaPorEmail(){
        LocalDateTime dataInicial = LocalDateTime.now();
        LocalDateTime dataFinal = LocalDateTime.now().plusHours(1);

        List<ComunicacaoOutDTO> entity = comunicacaoService.buscaComunicacaoPorPeriodo(
                dataInicial, dataFinal, StatusEnvioEnum.PENDENTE);

        entity.forEach(message -> {
            comunicacaoService.alteraStatusEnviado(message.getId());
            comunicacaoService.enviaEmail(message);
        } );
    }
}
