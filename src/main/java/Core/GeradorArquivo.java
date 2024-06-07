package Core;

import com.github.javafaker.Faker;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GeradorArquivo {

     public JSONObject geraPessoaAleatoria(){

         JSONObject json = new JSONObject();

         Faker fake= new Faker(new Locale("pt-BR"));
         TipoPessoa tipo = TipoPessoa.randomTipopessoa();
         Indicador indr = Indicador.randomIndicador();

         switch(tipo){

             case F:
                 json.put("tipo",tipo.toString());
                 json.put("cpfcnpj",generateCPF());
                 json.put("nome",fake.name().firstName()+" "+fake.name().lastName());
                 break;
             case J:
                 json.put("tipoPessoa",tipo.toString());
                 json.put("cpfcnpj",generateCNPJ());
                 json.put("nome",fake.company().name());
                 json.put("IE",generateIE());
                break;
           }

         json.put("nome",fake.name().firstName()+" "+fake.name().lastName());
         json.put("logradouro",fake.address().streetAddressNumber());
         json.put("cidade",fake.address().city());
         json.put("cep",fake.address().zipCode().replaceAll("-",""));
         json.put("uf",fake.address().stateAbbr());
         json.put("indicador",indr);

         System.out.println("JSON GERADO:");
         System.out.println(json.toString(4 ));

         return json;
         }


      public static String generateCPF() {
        Random random = new Random();

        int[] digits = new int[11];

        // Gerando os primeiros nove dígitos aleatoriamente
        for (int i = 0; i < 9; i++) {
            digits[i] = random.nextInt(10);
        }

        // Calculando os dígitos verificadores
        digits[9] = calculateVerifierDigit(digits, 9, 10);
        digits[10] = calculateVerifierDigit(digits, 10, 11);

        // Formatando o CPF no formato XXX.XXX.XXX-XX
        return String.format("%d%d%d%d%d%d%d%d%d%d%d",
                digits[0], digits[1], digits[2],
                digits[3], digits[4], digits[5],
                digits[6], digits[7], digits[8],
                digits[9], digits[10]);
    }

    public static String generateCNPJ() {
        Random random = new Random();

        int[] digits = new int[14];

        // Gerando os primeiros doze dígitos aleatoriamente
        for (int i = 0; i < 12; i++) {
            digits[i] = random.nextInt(10);
        }

        // Calculando os dígitos verificadores
        digits[12] = calculateVerifierDigit(digits, 12, 5);
        digits[13] = calculateVerifierDigit(digits, 13, 6);

        // Formatando o CNPJ no formato XX.XXX.XXX/XXXX-XX
        return String.format("%d%d%d%d%d%d%d%d%d%d%d%d%d%d",
                digits[0], digits[1],
                digits[2], digits[3], digits[4],
                digits[5], digits[6], digits[7],
                digits[8], digits[9], digits[10], digits[11],
                digits[12], digits[13]);
    }

    private static int calculateVerifierDigit(int[] digits, int length, int weight) {
        int sum = 0;
        int weightLocal = weight;

        for (int i = 0; i < length; i++) {
            sum += digits[i] * weightLocal;
            weightLocal--;
            if (weightLocal < 2) {
                weightLocal = (length == 9) ? 10 : 9;  // Reset weight for CPF or CNPJ
            }
        }

        int result = 11 - (sum % 11);
        return (result >= 10) ? 0 : result;
    }



    public static String generateIE() {
        Random random = new Random();

        int[] digits = new int[12];

        // Gerando os primeiros oito dígitos aleatoriamente
        for (int i = 0; i < 8; i++) {
            digits[i] = random.nextInt(10);
        }

        // Calculando o primeiro dígito verificador
        digits[8] = calculateIESPVerifierDigit(digits, 1);

        // Gerando os próximos dois dígitos
        digits[9] = random.nextInt(10);
        digits[10] = random.nextInt(10);

        // Calculando o segundo dígito verificador
        digits[11] = calculateIESPVerifierDigit(digits, 2);

        // Formatando a IE no formato XXX.XXX.XXX.XXX
        return String.format("%d%d%d%d%d%d%d%d%d%d%d%d",

                digits[0], digits[1], digits[2],
                digits[3], digits[4], digits[5],
                digits[6], digits[7], digits[8],
                digits[9], digits[10], digits[11]);
    }

    private static int calculateIESPVerifierDigit(int[] digits, int type) {
        int sum = 0;
        int weight = (type == 1) ? 1 : 3;

        for (int i = 0; i < 8; i++) {
            sum += digits[i] * weight;
            weight++;
            if (weight > 10) weight = 1;
        }

        if (type == 2) {
            sum += digits[9] * 3;
            sum += digits[10] * 2;
        }

        int result = sum % 11;
        if (result == 10) result = 0;
        return result;
    }

    public enum TipoPessoa {
        F,J;

        private static final Random PRNG = new Random();

        public static TipoPessoa randomTipopessoa()  {
            TipoPessoa[] tppessoa = values();
            return tppessoa[PRNG.nextInt(tppessoa.length)];
        }
    }

    public enum Indicador {
        S,N;

        private static final Random PRNG = new Random();

        public static Indicador randomIndicador()  {
            Indicador[] indicadors = values();
            return indicadors[PRNG.nextInt(indicadors.length)];
        }
    }



}
