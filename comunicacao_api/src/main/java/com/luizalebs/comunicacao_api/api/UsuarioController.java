package com.luizalebs.comunicacao_api.api;


import com.luizalebs.comunicacao_api.api.dto.UsuarioDTO;
import com.luizalebs.comunicacao_api.business.service.UsuarioService;
import com.luizalebs.comunicacao_api.infraestructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
@SecurityRequirement(name= SecurityConfig.SECURITY_SCHEME)
@Tag(name="usuario", description = "cadastro e login de usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(
                usuarioService.salvaUsuario(usuarioDTO)
        );
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody UsuarioDTO usuarioDTO){

        return  ResponseEntity.status(201).body(usuarioService.autenticarUsuario(usuarioDTO));
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email" ) String email){
        return ResponseEntity.ok(usuarioService.buscaUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(
            @RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token
    ){
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }









}


