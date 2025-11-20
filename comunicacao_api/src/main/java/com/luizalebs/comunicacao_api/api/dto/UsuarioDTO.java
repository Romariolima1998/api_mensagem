package com.luizalebs.comunicacao_api.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ NoArgsConstructor
@Builder
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
}
