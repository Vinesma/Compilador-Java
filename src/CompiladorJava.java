import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

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
    
    public static void SCANNER(String arquivo){
        boolean fileRead; //variável para checar se o arquivo foi encontrado
        String charArray = ""; //String de todos os chars encontrados sem os espaços
        String compara = "";        
        int cont = 0;
        int linhax = 1;
        Token[] tokenArray = new Token[300]; //vetor de objetos token
                    
        try {
            FileReader arq = new FileReader(arquivo);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine(); //lê a primeira linha do arquivo de texto                
            
            while (linha != null) {           //enquanto não for EOF, ler o arquivo
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
            linha = lerArq.readLine(); //lê da segunda linha em diante 
            }
            
        if(cont < 0){ //erros relacionados a comentários
            geraErro(1, linhax);
            throw new IOException();
        }
        
        cont = 0;
        fileRead = true;
        arq.close();
        
        }catch(IOException e){ //catch para erros de abertura de arquivo
            JOptionPane.showMessageDialog(null, "Erro na abertura do arquivo, tente novamente!");
            fileRead = false;
        }            
        
        if(fileRead){
            do {                
                compara = compara.concat(Character.toString(Character.toUpperCase(charArray.charAt(cont))));
                cont++;
            } while (cont < 7 && cont < charArray.length());
            
            cont = 0;
            
            if (!compara.equals("PROGRAM")) {
                geraErro(1, linhax);
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
                                    tokenArray[cont] = new Token("ID", compara, linhax);
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
                                tokenArray[cont] = new Token("ID", compara, linhax);
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
        }
        
        //DEBUG
        for (int i = 0; i < cont; i++) {
            System.out.println("Num:" + i);
            tokenArray[i].dados();
        }
        
    }
    
    public static void geraErro(int erro, int linha){
        switch(erro){
            case 1:
                JOptionPane.showMessageDialog(null, "ERRO 1: Identificador ou símbolo invalido, linha: " + linha );
                JOptionPane.showMessageDialog(null, "Compilação encerrada com erros!");
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
                SCANNER(nome);
            }            
        }while(nome != null);
    }    
}