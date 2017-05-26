package org.luizlopes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Anotacoes implements Serializable {

    private ArrayList<Anotacao> suspeitos;
    private ArrayList<Anotacao> armas;
    private ArrayList<Anotacao> locais;

}
