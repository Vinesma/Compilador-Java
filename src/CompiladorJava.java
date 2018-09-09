/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
        "+", "-", "*", "/", "or", "and",".", ",",";", ")", "(", ":="};
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    Scanner ler = new Scanner(System.in); //usado para ler o arquivo de texto            
    
    boolean fileRead = false; //variável para checar se o arquivo foi encontrado
    String charArray = ""; //String de todos os chars encontrados sem os espaços
 
        do {            
            System.out.printf("Informe o nome de arquivo texto:\n");
            String nome = ler.nextLine(); 
            nome = nome.concat(".txt");
            
            //System.out.printf("\nConteúdo do arquivo texto:\n");
            try {
                FileReader arq = new FileReader(nome);
                BufferedReader lerArq = new BufferedReader(arq);
                
                String linha = lerArq.readLine(); //lê a primeira linha do arquivo de texto                
                while (linha != null) { //enquanto não for EOF, ler o arquivo
                    for (int i = 0; i < linha.length(); i++) { //usa o charAt para pegar cada caractere
                        System.out.println(linha.charAt(i));
                        if ((int) linha.charAt(i) != 32) { //remove os espaços (ASCII = 32)
                            charArray = charArray.concat(Character.toString(linha.charAt(i))); 
                            //concatena com a String de chars
                        }                        
                    }
                    linha = lerArq.readLine(); //lê da segunda linha em diante 
                }
                
                fileRead = true;
                arq.close();
            } catch (IOException e) { //catch para erros de abertura de arquivo
                System.out.printf("Erro na abertura do arquivo, tente novamente!");
                System.out.println();
                fileRead = false;
            }
        } while (fileRead != true);
    
    System.out.println();
  }    
}
