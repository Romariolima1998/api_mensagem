package com.luizalebs.comunicacao_api.api.converter;


import com.luizalebs.comunicacao_api.api.dto.UsuarioDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .build();
    }
    // para DTO

    public UsuarioDTO paraUsuarioDTO(Usuario usuarioDTO){
        return UsuarioDTO.builder()
                .id(usuarioDTO.getId())
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .build();
    }

    public List<UsuarioDTO> paraListaEnderecoDTO(List<Usuario> usuarioList){
        List<UsuarioDTO> listUsuarioTDO = new ArrayList<>();

        for(Usuario usuario: usuarioList){
            UsuarioDTO tdo = new UsuarioDTO();
                    tdo.setId(usuario.getId());
                    tdo.setNome(usuario.getNome());
                    tdo.setEmail(usuario.getEmail());
                    tdo.setSenha(usuario.getSenha());

                    listUsuarioTDO.add(tdo);
        }
        return listUsuarioTDO;
    }

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario entity){
        return Usuario.builder()
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : entity.getNome())
                .id(entity.getId())
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : entity.getSenha())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : entity.getEmail())
                .build();
    }



}
