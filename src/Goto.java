import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Goto {
    String charArray = "";
    private LinkedList<String> stringList  = new LinkedList<>();
    private FileWriter arq_escr;
    private PrintWriter gravarArq;
    int linha_goto = 1;
    
    public Goto(LinkedList<Integer> linhas_IF_List,
                LinkedList<Integer> linhas_WHILE_List,
                LinkedList<Integer> linhas_REPEAT_List,
                String arquivo) throws IOException{
    
        FileReader arq = new FileReader(arquivo);
        BufferedReader lerArq = new BufferedReader(arq);
        String linha = lerArq.readLine();              
   
        while (linha != null) {            
            for (int i = 0; i < linha.length(); i++) {
                    charArray = charArray.concat(Character.toString(linha.charAt(i)));
            }
            
            goto_check(linhas_IF_List);
            goto_check(linhas_WHILE_List);
            goto_check(linhas_REPEAT_List);
            
            stringList.add(charArray);
            charArray = "";
            linha_goto++;
            linha = lerArq.readLine();
        }               
        arq.close();
        
        arq_escr = new FileWriter(arquivo);
        gravarArq = new PrintWriter(arq_escr);
        
        while(!stringList.isEmpty()){
            gravarArq.println(stringList.pop());
        }
        
        arq_escr.close();
    }
    
    private void goto_check(LinkedList<Integer> l1){
        if (!l1.isEmpty()) {
            if (linha_goto == l1.peek()) {
                l1.pop();
                charArray = charArray.concat(" " + Integer.toString(l1.pop()));
            }
        }
    }
}
