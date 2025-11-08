package br.edu.fatec.model;

import br.edu.fatec.enums.MeetStatus;
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
public class Meet {
    private Long id;
    private LocalDateTime date;
    private String address;
    private MeetStatus status;
}
