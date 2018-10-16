import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Otavio
 */
public class CompiladorJava {
    
    public static Token[] tokenArray = new Token[400]; //vetor de objetos token
    
    /*Vetores de checagem*/
    public static final String[] RESERVADAS = { "PROGRAM", "BEGIN", "END", "IF",
        "THEN", "ELSE", "WHILE", "DO", "UNTIL", "REPEAT", "INTEGER", "REAL",
        "ALL", "AND", "OR", "STRING" };
    
    public static final String[] OPERADORES = { "<", ">", "=>", "<=", "=", "<>",
        "+", "-", "*", "/", "OR", "AND", ".", ",", ";", ")", "(", ":="};
    
    public static boolean ehValido(String str){ //verifica se existe um digito no primeiro caractere da string        
        if (!str.equals("")) {
            return !Character.isDigit(str.charAt(0));
        }else{
            return true;
        }
    }
    
    public static boolean ehNumerico(String str){ //verifica se toda a string é um numero
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    
    public static void LEXICO(){
        //analisador léxico
    }
    
    public static void SCANNER(String arquivo) throws IOException,NovaException{
        boolean fileRead; //variável para checar se o arquivo foi encontrado
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
            
            if(cont != 0){ //erros relacionados a comentários
                throw new NovaException("ERRO 1: Identificador ou símbolo invalido, verifique os comentários, linha: " + linhax);
            }
        
            charArray = charArray.concat(" ");//concatena um espaço vazio na String para marcar o fim da linha
            linhax += 1;
            linha = lerArq.readLine(); //lê da segunda linha em diante 
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
            throw new NovaException("ERRO 1: Identificador ou símbolo invalido: '" + compara + "', linha: " + linhax);
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
                            if (charArray.charAt(i - 1) != ')') {
                                if(ehNumerico(compara)){
                                    tokenArray[cont] = new Token("ID", compara, linhax, Integer.parseInt(compara));
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
                                    tokenArray[cont] = new Token("ID", compara, linhax, Integer.parseInt(compara));
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
        linhax = 1;
        compara = "";
        charArray = "";
              
        //DEBUG
        for (int i = 0; i < cont; i++) {
            System.out.println("Num:" + i);
            tokenArray[i].dados();
        }        
    }  
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {                    
        String nome;
        
        do{            
            nome = JOptionPane.showInputDialog("Informe o nome do arquivo de texto:");            
            if (nome != null) {
                nome = nome.concat(".txt");
                try{
                    SCANNER(nome);
                }catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Erro na abertura do arquivo, tente novamente!");
                }catch (NovaException e){
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    JOptionPane.showMessageDialog(null, "Compilação encerrada com erros!");
                }
                
            }            
        }while(nome != null);
    }    
}