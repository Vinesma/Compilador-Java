import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Lexico {
        
    private LinkedList<Token> tokenFila = new LinkedList<Token>();
    private Token token;
    
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
                } else if((int) linha.charAt(i) > 31 && cont == 0){ //remove os espaços (ASCII = 32)
                    charArray = charArray.concat(Character.toString(linha.charAt(i))); 
                    //concatena com a String de chars
                }
            }
            charArray = charArray.concat("|");//concatena um espaço vazio na String para marcar o fim da linha
            linhax += 1;
            linha = lerArq.readLine(); //lê da segunda linha em diante
        }
        
        if(cont != 0){ //erro relacionado a comentários
                throw new NovaException("ERRO 1: Identificador ou símbolo invalido, verifique os comentários");
        }
       
        arq.close(); //fim de leitura do arquivo
                
        cont = 0;
        linhax = 1;
        for (int i = 0; i < charArray.length(); i++){
            if (Character.isAlphabetic(charArray.charAt(i)) || Character.isDigit(charArray.charAt(i))){
                compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
                
                for (int j = 0; j < 16; j++){
                    if (compara.equals(RESERVADAS[j])){
                        token = new Token(compara, "", linhax);
                        tokenFila.add(token);
                        compara = "";
                        j = 16;
                    }else if (compara.equals("|")){
                        linhax += 1;
                        compara = "";
                        j = 16;
                    }else if (compara.equals(" ")){
                        j = 16;
                    }
                }
            }else{
                switch (charArray.charAt(i)) {
                    case ' ':
                        if(!tokenFila.peek().getId().equals("ID") || !tokenFila.peek().getLexema().equals("")){
                            if(ehNumerico(compara)){
                                token = new Token("NUMERICO", "", linhax, Integer.parseInt(compara));
                                tokenFila.add(token);
                            }else if(!ehValido(compara)){
                                throw new NovaException("ERRO 1: Identificador ou símbolo invalido: '" + compara + "', linha: " + linhax);
                            }else{
                                token = new Token("ID", compara, linhax);
                                tokenFila.add(token);
                            }
                            compara = "";                            
                        }else{
                            token = new Token(" ","",linhax);
                            tokenFila.add(token);
                            compara = "";
                        }
                        break;
                    case '(':
                        token = new Token(Character.toString(charArray.charAt(i)), "", linhax);
                        tokenFila.add(token);
                        compara = "";
                        break;
                    case ';':
                    case ',':
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case ')':
                        if (charArray.charAt(i - 1) != ')') {
                            if(ehNumerico(compara)){
                                token = new Token("NUMERICO", "", linhax, Integer.parseInt(compara));
                                tokenFila.add(token);
                            }else if(!ehValido(compara)){
                                throw new NovaException("ERRO 1: Identificador ou símbolo invalido: '" + compara + "', linha: " + linhax);
                            }else{
                                token = new Token("ID", compara, linhax);
                                tokenFila.add(token);
                            }
                            compara = "";
                        }
                        token = new Token(Character.toString(charArray.charAt(i)), "", linhax);
                        tokenFila.add(token);
                        compara = "";
                        break;
                    case ':':
                    case '<':
                    case '>':                    
                        if(ehNumerico(compara)){
                            token = new Token("NUMERICO", "", linhax, Integer.parseInt(compara));
                            tokenFila.add(token);
                        }else if(!ehValido(compara)){
                            throw new NovaException("ERRO 1: Identificador ou símbolo invalido: '" + compara + "', linha: " + linhax);
                        }else{
                            token = new Token("ID", compara, linhax);    
                            tokenFila.add(token);
                        }                            
                        compara = "";
                        compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
                        break;
                    case '=':
                        break;
                    case '|':
                        linhax += 1;
                        compara = "";
                        break;
                    default:
                        compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(i))));
                        break;
                }
            }
        }
        //DEBUG
        for (int i = 0; i < tokenFila.size(); i++) {
        System.out.println("Num:" + i);
        tokenFila.get(i).dados();
        }
        
        Sintatico sint;
        sint = new Sintatico();
        
        //sint.PARSER(tokenFila, arquivo);    
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