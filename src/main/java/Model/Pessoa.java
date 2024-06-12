package Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Pessoa {
    private String tipo;
    private String cpf;
    private String cnpj;
    private String nome;
    private String logradouro;
    private String inscr_estadual;
    private String cidade;
    private String cep;
    private String uf;
    private String indicador;


}
