package org.luizlopes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "nome")
public class Carta {

    private String nome;
    private String src;

}
