package br.edu.fatec.model;

import br.edu.fatec.enums.DefaultStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mother {
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthday;
    private DefaultStatus status;
}
