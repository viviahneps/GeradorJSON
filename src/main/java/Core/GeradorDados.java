package Core;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GeradorDados {

    public static void main(String[] args) throws IOException {

        GeradorArquivo gArq = new GeradorArquivo();
        Scanner ler = new Scanner(System.in);
        int i = 0;
        System.out.println("Quantos arquivos deseja gerar ? ");
        int n = ler.nextInt();

        for (i=0;i<n;i++) {
            geraJSon(gArq.geraPessoaAleatoria(), "PESSOA_CT"+i+".json");
        }
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
