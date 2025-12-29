package com.luizalebs.comunicacao_api.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luizalebs.comunicacao_api.api.dto.UsuarioDTO;
import com.luizalebs.comunicacao_api.business.exceptions.ConflictException;
import com.luizalebs.comunicacao_api.infraestructure.entities.Usuario;
import com.luizalebs.comunicacao_api.infraestructure.repositories.UsuarioRepository;
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

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    UsuarioRepository usuarioRepository;
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

    @Test
    @DisplayName("salva usuario com sucesso")
    void salvaUsuario() throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .senha("1234")
                .email("test@email.com")
                .nome("test")
                .build();
        String usuarioRequest = mapper.writeValueAsString(usuarioDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioRequest)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(usuarioDTO.getNome()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Erro ConflictException ao salvar usuario, email ja existe")
    void salvaUsuarioErrorConflictException() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .senha("1234")
                .email(usuarioEntity.getEmail())
                .nome("test")
                .build();
        String usuarioRequest = mapper.writeValueAsString(usuarioDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/usuario")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usuarioRequest)
                )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(
                        result -> Assertions.assertTrue(result.getResolvedException() instanceof ConflictException)
                )
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("busca usuarios com sucesso")
    void buscaUsuarios() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/usuario"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("deleta usuario com sucesso")
    void deletaUsuarioPorId() throws Exception {
        Usuario usuario = criaUsuarioTest(null);
        String token = criaTokenTest(usuario);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/usuario")
                        .header("Authorization", token)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("atualiza usuario com sucesso")
    void atualizaDadosUsuario() throws Exception {
        Usuario usuarioEntity = criaUsuarioTest(null);
        String token = criaTokenTest(usuarioEntity);
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .nome("test de atualizacao")
                .build();
        String usuarioRequest = mapper.writeValueAsString(usuarioDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/usuario")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioRequest)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value(usuarioDTO.getNome()))
                .andDo(MockMvcResultHandlers.print());
    }


}