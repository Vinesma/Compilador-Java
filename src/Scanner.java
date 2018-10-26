import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Scanner {
    
    public static Token[] tokenArray = new Token[400]; //vetor de objetos token
    
    public static LinkedList<Token> tokenFila = new LinkedList<Token>();
    
    /*Vetores de checagem*/
    public static final String[] RESERVADAS = { "PROGRAM", "BEGIN", "END", "IF",
        "THEN", "ELSE", "WHILE", "DO", "UNTIL", "REPEAT", "INTEGER", "REAL",
        "ALL", "AND", "OR", "STRING" };
    
    public static final String[] OPERADORES = { "<", ">", "=>", "<=", "=", "<>",
        "+", "-", "*", "/", "OR", "AND", ".", ",", ";", ")", "(", ":="};
    
    public void SCANNER(String arquivo) throws IOException,NovaException{
        String charArray = ""; //String de todos os chars encontrados sem os espaços
        String compara = ""; //String utilizada para pegar tokens individualmente        
        int cont = 0; //contador utilizado para diversas coisas
        int linhax = 1; //demarca em que linha o código se encontra
                    
        FileReader arq = new FileReader(arquivo);
        BufferedReader lerArq = new BufferedReader(arq);
        String linha = lerArq.readLine(); //lê a primeira linha do arquivo de texto               
   
        while (linha != null) { //enquanto não for EOF, ler o arquivo
            
            for (int i = 0; i < linha.length(); i++) { //usa o charAt para pegar cada caractere
                if (linha.charAt(i) == '{') { //tratamento de comentários: Ignorar
                    cont += 1;
                } else if (linha.charAt(i) == '}'){
                    cont -= 1;
                } else if((int) linha.charAt(i) > 32 && cont == 0){ //remove os espaços (ASCII = 32)
                    charArray = charArray.concat(Character.toString(linha.charAt(i))); 
                    //concatena com a String de chars
                }                        
            }                                
            charArray = charArray.concat(" ");//concatena um espaço vazio na String para marcar o fim da linha
            linhax += 1;
            linha = lerArq.readLine(); //lê da segunda linha em diante 
        }
        
        if(cont != 0){ //erros relacionados a comentários
                throw new NovaException("ERRO 1: Identificador ou símbolo invalido, verifique os comentários");
        }
       
        arq.close(); //fim de leitura do arquivo
                
        cont = 0;
        linhax = 1;          
        
        do {                
            compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(cont))));
            cont++;
        } while (cont < 7 && cont < charArray.length());
            
        cont = 0;
            
        if (!compara.equals("PROGRAM")) {
            throw new NovaException("Erro 2: Símbolo "
                    + compara + " inesperado. Esperando: 'PROGRAM'. "
                    + "Linha: " + linhax);
        } else {
            tokenArray[cont] = new Token(compara, "", linhax);
            cont = 1;
            compara = "";
                
            for (int i = 7; i < charArray.length(); i++) {
                    
                if (Character.isAlphabetic(charArray.charAt(i)) || Character.isDigit(charArray.charAt(i))) {
                    compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
   
                    for (int j = 0; j < 16; j++) {
                        if (compara.equals(RESERVADAS[j])) {
                            tokenArray[cont] = new Token(compara, "", linhax);
                            cont += 1;
                            compara = "";
                            j = 16;
                        } else if (compara.equals(" ")) {
                            linhax += 1;
                            compara = "";
                            j = 16;
                        } else if (compara.equals("")) {
                            j = 16;
                        }
                    }
                }else{
                        
                    switch (charArray.charAt(i)) {
                        case '(':
                            tokenArray[cont] = new Token(Character.toString(charArray.charAt(i)), "", linhax);
                            cont += 1;
                            compara = "";
                            break;
                        case ';':
                        case ',':
                        case '+':
                        case '-':
                        case '*':
                        case '/':
                        case ')':                                
                            if (charArray.charAt(i - 1) != ')' && !compara.equals("")) {
                                if(ehNumerico(compara)){
                                    tokenArray[cont] = new Token("NUMERICO", "", linhax, Integer.parseInt(compara));
                                }else if(!ehValido(compara)){
                                    throw new NovaException("ERRO 1: Identificador ou símbolo invalido: '" + compara + "', linha: " + linhax);
                                }else{
                                        tokenArray[cont] = new Token("ID", compara, linhax);    
                                }
                                cont += 1;
                                compara = "";
                            }
    
                            tokenArray[cont] = new Token(Character.toString(charArray.charAt(i)), "", linhax);
                            cont += 1;
                            compara = "";
                            break;
                        case ':':
                        case '<':
                        case '>':
                            if(ehNumerico(compara)){
                                    tokenArray[cont] = new Token("NUMERICO", "", linhax, Integer.parseInt(compara));
                                }else if(!ehValido(compara)){
                                    throw new NovaException("ERRO 1: Identificador ou símbolo invalido: '" + compara + "', linha: " + linhax);
                                }else{
                                    tokenArray[cont] = new Token("ID", compara, linhax);     
                                }
                            cont += 1;
                            compara = "";
                            compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
                            break;
                        default:
                            compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
                            break;
                        }
                        
                    for (int j = 0; j < 18; j++) {
                        if (compara.equals(OPERADORES[j])) {
                            tokenArray[cont] = new Token(compara, "", linhax);
                            cont += 1;
                            compara = "";
                            j = 18;
                        } else if (compara.equals(" ")) {
                            linhax += 1;
                            compara = "";
                            j = 18;
                        } else if (compara.equals("")) {
                            j = 18;
                        }
                    }                        
                }                    
            }
        }     
        //DEBUG
        for (int i = 0; i < cont; i++) {
            System.out.println("Num:" + i);
            tokenArray[i].dados();
        }
        
        Sintatico sint;
        sint = new Sintatico();
        
        for (int i = 0; i < cont; i++) {
        tokenFila.add(tokenArray[i]);
        }       
        sint.PARSER(tokenFila);        
    }
    
    private boolean ehValido(String str){ //verifica se existe um digito no primeiro caractere da string        
        if (!str.equals("")) {                  //ou se há um espaço vazio, caso true aos dois, retorna um erro
            return !Character.isDigit(str.charAt(0)) && str.matches("\\S+");
        }else{
            return true;
        }
    }
    
    private boolean ehNumerico(String str){ //verifica se toda a string é um numero
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}