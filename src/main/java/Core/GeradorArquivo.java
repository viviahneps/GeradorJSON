package Core;

import Model.Pessoa;
import Model.TipoDados;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GeradorArquivo {

        private TipoDados tp;


    public static void main(String[] args) throws IOException {
       GeradorDados gDados = new GeradorDados();
       Scanner opcao = new Scanner(System.in);

       gDados.geraPessoaCampo(TipoDados.VAZIO,"teste");
    }

    private static void geraJSon(JSONObject dados, String nome) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nome))) {
            // Escrevendo o conteúdo JSON no arquivo
            writer.write(dados.toString(4)); // O segundo parâmetro especifica a indentação (opcional)
            System.out.println("JSONObject foi escrito para o arquivo: "+nome+" com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro: "+ e.getMessage() +"ao escrever JSONObject no arquivo: "+nome );
        }
    }
}