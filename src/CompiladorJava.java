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
    String charArray = "";
    //teste
 
    System.out.printf("Informe o nome de arquivo texto:\n");
    String nome = ler.nextLine();
    nome = nome.concat(".txt");
 
    System.out.printf("\nConteúdo do arquivo texto:\n");
    try{
        FileReader arq = new FileReader(nome);
        BufferedReader lerArq = new BufferedReader(arq);
 
        String linha = lerArq.readLine(); // lê a primeira linha
// a variável "linha" recebe o valor "null" quando o processo
// de repetição atingir o final do arquivo texto
        while (linha != null) {
            //System.out.printf("%s\n", linha);
    		for (int i=0; i < linha.length(); i++) {
    			System.out.println(linha.charAt(i));
                        charArray = charArray.concat(Character.toString(linha.charAt(i)));
                }
 
            linha = lerArq.readLine(); // lê da segunda até a última linha
        }
 
        arq.close();
    }catch(IOException e){
        System.err.printf("Erro na abertura do arquivo: %s.\n",
            e.getMessage());
    }
    
    System.out.println();
  }    
}
