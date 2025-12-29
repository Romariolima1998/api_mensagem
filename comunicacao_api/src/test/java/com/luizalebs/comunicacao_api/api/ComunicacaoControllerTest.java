package com.luizalebs.comunicacao_api.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.UsuarioDTO;
import com.luizalebs.comunicacao_api.business.exceptions.BadRequestException;
import com.luizalebs.comunicacao_api.business.exceptions.ForbiddenException;
import com.luizalebs.comunicacao_api.business.exceptions.NotFaundException;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.entities.Usuario;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import com.luizalebs.comunicacao_api.infraestructure.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ComunicacaoControllerTest{

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ComunicacaoRepository comunicacaoRepository;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    @Autowired
    PasswordEncoder passwordEncoder;

    Usuario criaUsuarioTest(String email){
        String senha = "test";
        Usuario usuario = Usuario.builder()
                .nome("test")
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .build();

        if(email == null){
            usuario.setEmail("test@email.com");
        }
        usuarioRepository.save(usuario);
        usuario.setSenha(senha);
        return usuario;
    }

    String criaTokenTest(Usuario usuario) throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .build();
        String usuarioDTORequest = mapper.writeValueAsString(usuarioDTO);
        String token = mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioDTORequest)
        )
                .andReturn()
                .getResponse()
                .getContentAsString();
        return token;
    }

    ComunicacaoEntity criaMensagemTest(Usuario usuario){
        ComunicacaoEntity mensagem = ComunicacaoEntity.builder()
                .mensagem("mensagem de test")
                .dataHoraEnvio(LocalDateTime.now())
                .emailDestinatario("test@email.com")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .nomeDestinatario("test")
                .statusEnvio(StatusEnvioEnum.PENDENTE)
                .telefoneDestinatario("11999999999")
                .emailOwner(usuario.getEmail())
                .build();

        return comunicacaoRepository.save(mensagem);
    }

    @AfterEach
    void down(){
        comunicacaoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }


    @Test
    @DisplayName("agenda mensagem com sucesso")
    void agendar() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        String token = criaTokenTest(usuarioEntity);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ComunicacaoInDTO comunicacaoDTO = ComunicacaoInDTO.builder()
                .nomeDestinatario("test")
                .dataHoraEnvio(LocalDateTime.now())
                .emailDestinatario("test@email.com")
                .mensagem("mensagem de test")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .telefoneDestinatario("11999999999")
                .build();
        String comunicacaoDTORequest = mapper.writeValueAsString(comunicacaoDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/comunicacao/agendar")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(comunicacaoDTORequest)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem").value(comunicacaoDTO.getMensagem()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Erro BadRequest ao agendar mensagem, faltando campos obrigatorio")
    void agendarErroCamposObrigatoris() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        String token = criaTokenTest(usuarioEntity);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ComunicacaoInDTO comunicacaoDTO = ComunicacaoInDTO.builder()
                .nomeDestinatario(null)
                .dataHoraEnvio(null)
                .emailDestinatario(null)
                .mensagem(null)
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .telefoneDestinatario("11999999999")
                .build();
        String comunicacaoDTORequest = mapper.writeValueAsString(comunicacaoDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/comunicacao/agendar")
                                .header("Authorization", token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(comunicacaoDTORequest)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        result -> Assertions.assertTrue(result.getResolvedException() instanceof BadRequestException)
                )
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Busca menssagens com sucesso")
    void buscaMessageAutor() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        String token = criaTokenTest(usuarioEntity);
        ComunicacaoEntity comunicacaoEntity = criaMensagemTest(usuarioEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/comunicacao")
                        .header("Authorization", token)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("Busca menssagens por email com sucesso")
    void buscarStatus() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        ComunicacaoEntity comunicacaoEntity = criaMensagemTest(usuarioEntity);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/comunicacao/status")
                                .param("emailDestinatario", usuarioEntity.getEmail())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Erro NotFound ao Busca menssagens por email, nao existe mensagens para este email")
    void buscarStatusErroNaoExisteMensagensParaEsteeEmail() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/comunicacao/status")
                                .param("emailDestinatario", "test@test.com")
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(
                        result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFaundException)
                )
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("altera status para cancelado com sucesso")
    void cancelarStatus() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        String token = criaTokenTest(usuarioEntity);
        ComunicacaoEntity comunicacaoEntity = criaMensagemTest(usuarioEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/comunicacao/cancelar/{id}", comunicacaoEntity.getId())
                        .header("Authorization", token)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusEnvio").value(StatusEnvioEnum.CANCELADO.toString()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Erro ao alterar status para cancelado, id Mensagem nao existe")
    void cancelarStatusErrorNotFound() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        String token = criaTokenTest(usuarioEntity);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/comunicacao/cancelar/{id}", 10)
                                .header("Authorization", token)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(
                        result -> Assertions.assertTrue(result.getResolvedException() instanceof NotFaundException)
                )
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Erro ao alterar status para cancelado, Mensagem nao pertence a este usuario")
    void cancelarStatusErrorForbidden() throws Exception {
        Usuario usuarioEntity1 = criaUsuarioTest(null);
        String token = criaTokenTest(usuarioEntity1);
        Usuario usuarioEntity2 = criaUsuarioTest("test2@email.com");
        ComunicacaoEntity mensagemEntity = criaMensagemTest(usuarioEntity2);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/comunicacao/cancelar/{id}", mensagemEntity.getId())
                                .header("Authorization", token)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(
                        result -> Assertions.assertTrue(result.getResolvedException() instanceof ForbiddenException)
                )
                .andDo(MockMvcResultHandlers.print());
    }
}