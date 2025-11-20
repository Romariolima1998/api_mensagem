package com.luizalebs.comunicacao_api.api;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.security.SecurityConfig;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "comunicacao", description = "cadastro e envio de mensagens")
@RestController
@RequestMapping("/comunicacao")
@RequiredArgsConstructor
@SecurityRequirement(name= SecurityConfig.SECURITY_SCHEME)
public class ComunicacaoController {

    private final ComunicacaoService service;


    @Operation(summary = "salva uma mensagem", description = "agenda uma mensagem")
    @ApiResponse(responseCode = "200", description = "dados salvos com sucesso")
    @ApiResponse(responseCode = "400", description = "json invalido ou faltando campos")
    @PostMapping("/agendar")
    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody ComunicacaoInDTO dto,
                                                     @RequestHeader("Authorization") String token)  {
        return ResponseEntity.ok(service.agendarComunicacao(dto, token));
    }

    @Operation(summary = "busca mensagens", description = "busca mensagens do autor")
    @ApiResponse(responseCode = "200", description = "mensagem encontrada com sucesso")
    @GetMapping()
    public ResponseEntity<List<ComunicacaoOutDTO>> buscaMessageAutor(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.buscaMessagesAutor(token));
    }


    @Operation(summary = "busca mensagens", description = "busca mensagem por email")
    @ApiResponse(responseCode = "200", description = "mensagem encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "dados nao encontrados")
    @GetMapping("/status")
    public ResponseEntity<List<ComunicacaoOutDTO>> buscarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @Operation(summary = "altera status", description = "altera status por id")
    @ApiResponse(responseCode = "200", description = "status alterado com sucesso com sucesso")
    @ApiResponse(responseCode = "404", description = "dados nao encontrados")
    @PatchMapping("/cancelar/{id})")
    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@PathVariable Long id,
                                                            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.alterarStatusComunicacao(id, token));
    }

   
}
