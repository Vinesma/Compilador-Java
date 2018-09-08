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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    Scanner ler = new Scanner(System.in);
    
    String[] reservadas = { "PROGRAM", "BEGIN", "END", "IF", "THEN", "ELSE", "WHILE", "DO", 
        "UNTIL", "REPEAT", "INTEGER", "REAL", "ALL", "AND", "OR", "STRING" };
    
    String[] operadores = { "<", ">", "=>", "<=", "=", "<>", "+", "-", "*", "/", "or", "and", ".", ",",
        ";", ")", "(", ":="};
    
    boolean fileRead = false;
    String charArray = "";
 
        do {            
            System.out.printf("Informe o nome de arquivo texto:\n");
            String nome = ler.nextLine();
            nome = nome.concat(".txt");
            
            //System.out.printf("\nConteúdo do arquivo texto:\n");
            try {
                FileReader arq = new FileReader(nome);
                BufferedReader lerArq = new BufferedReader(arq);
                
                String linha = lerArq.readLine();                
                while (linha != null) {
                    for (int i = 0; i < linha.length(); i++) {
                        System.out.println(linha.charAt(i));
                        if ((int) linha.charAt(i) != 32) {
                            charArray = charArray.concat(Character.toString(linha.charAt(i)));
                        }                        
                    }
                    linha = lerArq.readLine();
                }
                
                fileRead = true;
                arq.close();
            } catch (IOException e) {
                System.out.printf("Erro na abertura do arquivo, tente novamente!");
                System.out.println();
                fileRead = false;
            }
        } while (fileRead != true);
    
    System.out.println();
  }    
}
