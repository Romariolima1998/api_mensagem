package com.luizalebs.comunicacao_api.business.service;


import com.luizalebs.comunicacao_api.api.converter.UsuarioConverter;
import com.luizalebs.comunicacao_api.api.dto.UsuarioDTO;
import com.luizalebs.comunicacao_api.business.exceptions.BadRequestException;
import com.luizalebs.comunicacao_api.business.exceptions.ConflictException;
import com.luizalebs.comunicacao_api.business.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.business.exceptions.UnaltorizedException;
import com.luizalebs.comunicacao_api.infraestructure.entities.Usuario;
import com.luizalebs.comunicacao_api.infraestructure.repositories.UsuarioRepository;
import com.luizalebs.comunicacao_api.infraestructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public String autenticarUsuario(UsuarioDTO usuarioDTO){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
            );
            return "Bearer " + jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException | UsernameNotFoundException  e) {
            throw new UnaltorizedException("usuario ou senha invalidos", e.getCause());
        }
    }

    public void emailExiste(String email) {
        if(email != null) {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("email ja cadastrado");
            }
        } else {
            throw new BadRequestException("email obrigatorio");
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }


    public List<UsuarioDTO> buscaUsuarios() {
        List<Usuario> usuario = (List<Usuario>) usuarioRepository.findAll();
        return usuarioConverter.paraListaEnderecoDTO(usuario);
    }

    public void deletaUsuarioPorToken(String token) {
        String email = jwtUtil.extractUsernameToken(token.substring(7));
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("usuario nao existe mais")
        );

        usuarioRepository.delete(usuarioEntity);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        String email = jwtUtil.extractUsernameToken(token.substring(7));

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("usuario nao existe mais"));
        if (dto.getEmail() != null) {
            emailExiste(dto.getEmail());
        }
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        if(dto.getSenha() != null){
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }


}
