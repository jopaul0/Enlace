package br.edu.fatec.model;

import br.edu.fatec.model.Service;
import br.edu.fatec.model.Mother;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Enlace {
    private Service service;
    private Mother mother;
}
