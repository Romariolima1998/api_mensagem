package com.luizalebs.comunicacao_api.infraestructure;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email", url = "${email.url}")
public interface Client {

    @PostMapping
    void enviarEmail(@RequestBody ComunicacaoOutDTO dto);
}
