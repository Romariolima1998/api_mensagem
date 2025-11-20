package com.luizalebs.comunicacao_api.business.service;


import com.luizalebs.comunicacao_api.api.converter.UsuarioConverter;
import com.luizalebs.comunicacao_api.api.dto.UsuarioDTO;
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
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("email ja cadastrado");
            }
        } catch (ConflictException e) {
            throw new ConflictException("email ja cadastrado ", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscaUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("email nao encontrado " + email));

        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        String email = jwtUtil.extractUsernameToken(token.substring(7));

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("email nao localizado") );
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);
        if(dto.getSenha() != null){
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }


}
