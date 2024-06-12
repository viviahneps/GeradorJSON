package Core;

import org.apache.logging.log4j.core.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import Model.Pessoa;
import Model.TipoDados;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import java.util.*;
import org.apache.logging.log4j.LogManager;



public class GeradorDados {

    private static final Logger logger = (Logger) LogManager.getLogger(GeradorDados.class);

        public  JSONObject geraPessoaCampo( TipoDados tp, String campo) {
            Gson gson = new Gson();
            JSONObject json = new JSONObject(gson.toJson(geraPessoaAleatoria()));
            Random random = new Random();

            int i;
            String aux = null;


            for (i = 0; i < json.length(); i++) {
                if (tp.equals(TipoDados.VAZIO)) {
                    try{
                    if (json.get(campo) != null) {
                        json.put(campo, "");
                    } else {
                        System.out.println("Propriedade: "+campo+"não encontrada !");
                           break;
                    }
                   }catch (JSONException e){
                        logger.info("Propriedade incorreta"+campo);

                    }
                }
                if (tp.equals(TipoDados.ZERADO)) {
                    if (json.get(campo) != null) {
                        aux = (String) json.get(campo);
                        json.put(campo, aux.replaceAll(".", "0"));

                    } else {
                        System.out.println("Propriedade: "+campo+"não encontrada !");
                        break;
                    }
                }
                if (tp.equals(TipoDados.SEMCAMPO)) {
                    if (json.get(campo) != null) {
                        json.remove(campo);
                        break;
                    } else {
                        System.out.println("Propriedade: "+campo+"não encontrada !");
                        break;
                    }
                }
                if (tp.equals(TipoDados.INVALIDO)) {
                        if (json.get(campo) != null) {
                            aux = (String) json.get(campo);
                            json.put(campo, aux.replaceAll(".", String.valueOf((char) (random.nextInt()))));

                        } else {
                            System.out.println("Propriedade: "+campo+"não encontrada !");
                            break;
                        }

                    }
               else {
                 System.out.println("Tipo de Dados inválido !");
                 }

            }
            System.out.println("Pessoa gerada com sucesso !\n"+json.toString(4));

            return  json;
        }

        public  Pessoa geraPessoaAleatoria(){

            JSONObject json = new JSONObject();
            Faker fake= new Faker(new Locale("pt-BR"));
            Pessoa pessoa = new Pessoa();
            TipoPessoa tipo = TipoPessoa.randomTipopessoa();
            Indicador indr = Indicador.randomIndicador();

            switch(tipo){

                case F:
                    pessoa.setTipo(tipo.toString());
                    pessoa.setCpf(generateCPF());
                    pessoa.setNome(fake.name().firstName()+" "+fake.name().lastName());
                    break;
                case J:
                    pessoa.setTipo(tipo.toString());
                    pessoa.setCnpj(generateCNPJ());
                    pessoa.setNome(fake.company().name());
                    pessoa.setInscr_estadual(generateIE());
                    break;
            }

            pessoa.setLogradouro(fake.address().streetAddressNumber());
            pessoa.setCidade(fake.address().city());
            pessoa.setCep(fake.address().zipCode().replaceAll("-","-"));
            pessoa.setUf(fake.address().stateAbbr());
            pessoa.setIndicador(indr.toString());

            return pessoa;
        }


        public  String generateCPF() {
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

        public  String generateCNPJ() {
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

        private  int calculateVerifierDigit(int[] digits, int length, int weight) {
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



        public  String generateIE() {
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


        private  int calculateIESPVerifierDigit(int[] digits, int type) {
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



