import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Otavio
 */
public class CompiladorJava {
    
    /*Vetores de checagem*/
    public static final String[] RESERVADAS = { "PROGRAM", "BEGIN", "END", "IF",
        "THEN", "ELSE", "WHILE", "DO", "UNTIL", "REPEAT", "INTEGER", "REAL",
        "ALL", "AND", "OR", "STRING" };
    
    public static final String[] OPERADORES = { "<", ">", "=>", "<=", "=", "<>",
        "+", "-", "*", "/", "OR", "AND", ".", ",", ";", ")", "(", ":="};
    
    public static void geraErro(int erro, int linha){

        switch(erro){
            case 1:
                System.out.printf("ERRO 1: Identificador ou símbolo invalido, linha: " + linha );
                System.out.printf("Compilação encerrada com erros!");
        }
    }      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    Scanner ler = new Scanner(System.in); //usado para ler o arquivo de texto            
    
    boolean fileRead; //variável para checar se o arquivo foi encontrado
    int erro = 0;
    String charArray = ""; //String de todos os chars encontrados sem os espaços
    String compara = "";
    Token[] tokenArray = new Token[100]; //vetor de objetos token
    int cont = 0;
    int estado = 0;
    int linhax = 1;
 
        do {            
            System.out.printf("Informe o nome de arquivo texto:\n");
            String nome = ler.nextLine(); 
            nome = nome.concat(".txt");
            
            try {
                FileReader arq = new FileReader(nome);
                BufferedReader lerArq = new BufferedReader(arq);
                
                String linha = lerArq.readLine(); //lê a primeira linha do arquivo de texto                
                while (linha != null) {           //enquanto não for EOF, ler o arquivo
                    for (int i = 0; i < linha.length(); i++) { //usa o charAt para pegar cada caractere
                        if ((int) linha.charAt(i) > 32) {      //remove os espaços (ASCII = 32)
                            charArray = charArray.concat(Character.toString(linha.charAt(i))); 
                            //concatena com a String de chars
                        }                        
                    }
                    charArray = charArray.concat(" ");
                    linha = lerArq.readLine(); //lê da segunda linha em diante 
                }
                
                fileRead = true;
                arq.close();
            } catch (IOException e) { //catch para erros de abertura de arquivo
                System.out.printf("Erro na abertura do arquivo, tente novamente!\n");
                System.out.println();
                fileRead = false;
            }
        } while (fileRead != true);
        
        do {            
            compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(cont))));
            cont++;
        } while (cont < 7 && cont < charArray.length());
        
        cont = 0;
        
        if(!compara.equals("PROGRAM")){
            geraErro(1,linhax);
        }else{
            tokenArray[cont] = new Token(compara,"",linhax);
            cont = 1;
            compara = "";
            
            for (int i = 7; i < charArray.length(); i++){
            
                if(Character.isAlphabetic(charArray.charAt(i)) || Character.isDigit(charArray.charAt(i))){
                    compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
                    
                    for (int j = 0; j < 16; j++) {
                        if(compara.equals(RESERVADAS[j])){
                            tokenArray[cont] = new Token(compara,"",linhax);
                            cont += 1;
                            compara = "";
                            j = 16;
                        }else if(compara.equals(" ")){
                            linhax += 1;
                            compara = "";
                            j = 16;
                        }else if(compara.equals("")){
                            j = 16;
                        }
                    }
                }else{
                    
                    if(        charArray.charAt(i) == ';' 
                            || charArray.charAt(i) == ','
                            || charArray.charAt(i) == '+'
                            || charArray.charAt(i) == '-'
                            || charArray.charAt(i) == '*'
                            || charArray.charAt(i) == '/'
                            || charArray.charAt(i) == '>'
                            || charArray.charAt(i) == '<'
                            || charArray.charAt(i) == ')'
                            || charArray.charAt(i) == '('){
                        
                        tokenArray[cont] = new Token("ID",compara,linhax);
                        cont += 1;
                        compara = "";
                        
                        tokenArray[cont] = new Token(Character.toString(charArray.charAt(i)),"",linhax);
                        cont += 1;
                        compara = "";
                    }else if(charArray.charAt(i) == ':'){
                        tokenArray[cont] = new Token("ID",compara,linhax);
                        cont += 1;
                        compara = "";
                        compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
                    }else{
                        compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
                    }
                    
                    for (int j = 0; j < 18; j++) {
                        if(compara.equals(OPERADORES[j])){
                            tokenArray[cont] = new Token(compara,"",linhax);
                            cont += 1;
                            compara = "";
                            j = 18;
                        }else if(compara.equals(" ")){
                            linhax += 1;
                            compara = "";
                            j = 18;
                        }else if(compara.equals("")){
                            j = 18;
                        }
                    }                    
                }                
            }
        }
    
    //DEBUG
    for (int i = 0; i < cont; i++) {
        tokenArray[i].dados();
    }
    
    System.out.println();
  }    
}