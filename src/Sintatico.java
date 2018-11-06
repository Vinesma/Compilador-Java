import java.util.LinkedList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.File;

public class Sintatico {
    private LinkedList<Token>   tokens;
    private LinkedList<String>  stringExpressoesList   = new LinkedList<>();
    private LinkedList<Token>   variaveisStringList    = new LinkedList<>();
    private LinkedList<Token>   variaveisIntegerList   = new LinkedList<>();
    private LinkedList<Token>   variaveisRealList      = new LinkedList<>();
    private LinkedList<Token>   variaveisList          = new LinkedList<>();
    private LinkedList<Boolean> variaveisAuxList       = new LinkedList<>();
    private LinkedList<Integer> linhas_gotoList        = new LinkedList<>();
    private Token tokenAtual;
    private int linha = 1;
    private FileWriter arq;
    private PrintWriter gravarArq;
    
    Scanner ler = new Scanner(System.in);
    
    public void PARSER(LinkedList<Token> tokenFila, String arquivo) throws NovaException, IOException{
        this.tokens = tokenFila;
        tokenAtual = this.tokens.getFirst();
        Semantico sem;
        
        arquivo = arquivo.replaceAll(".txt", "");
        arq = new FileWriter(arquivo + "_compilado.txt");
        gravarArq = new PrintWriter(arq);
        
        program_();     //Programa  <id> ;
        while (!tokenAtual.getId().equals(Token.BEGIN)){
            decl_var(); //[ <decl_var> ]*
        }
        sem = new Semantico(variaveisStringList,
                            variaveisIntegerList,
                            variaveisRealList);
        begin_();       //Begin
        bloco(sem);    //Begin [<comando>  [ <comando>]*]? End ;
        end_();         //End
        ponto();        //.
        
        for (int i = 0; i < variaveisList.size(); i++) {
            sem.SEMANTICS_CHECK_ERRO3_ERRO4(variaveisList.get(i), variaveisAuxList.get(i));
        }
        
        arq.close();
        JOptionPane.showMessageDialog(null, "Arquivo compilado com sucesso! "
                    + "Seu arquivo compilado foi criado: " + arquivo + "_compilado.txt");
        
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                File arquivoCriado = new File(arquivo + "_compilado.txt");
                desktop.open(arquivoCriado);
            }catch (IOException e){
            
            }
        }
    }
    
    private void proxToken(){
        int linha;
        
        linha = tokenAtual.getPos();
        tokens.pop();

        if (tokens.isEmpty())
            tokenAtual = new Token(Token.FINAL, "", linha);
        else
            tokenAtual = tokens.getFirst();
    }
    
    private void valor(boolean ehExpressao) throws NovaException{
        if (tokenAtual.getId().equals("NUMERICO")){
            proxToken();
        }else if (tokenAtual.getId().equals("ID")){
            variaveisList.add(tokenAtual);
            variaveisAuxList.add(ehExpressao);
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'ID ou NUMERICO'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void id_(boolean ehExpressao) throws NovaException{
        if (tokenAtual.getId().equals("ID")){
            variaveisList.add(tokenAtual);
            variaveisAuxList.add(ehExpressao);
            gravaToken();
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'ID'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private boolean e_valor(String compara) throws NovaException{
        if (compara.equals("NUMERICO") || compara.equals("ID")){
            return true;
        }else{
            return false;
        }
    }

    private void pontovirgula() throws NovaException{
        if (!tokenAtual.getId().equals(Token.PONTOVIRGULA)){
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: ';'. "
                    + "Linha: " + tokenAtual.getPos());
        }else{
            
        }
    }
      
    private void virgula() throws NovaException{
        if (tokenAtual.getId().equals(Token.VIRGULA)){
            gravaToken();
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: ','. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void begin_() throws NovaException{
        if (tokenAtual.getId().equals(Token.BEGIN)){
            gravaLinha();
            gravaToken();
            pulaLinha();
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'BEGIN'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void end_() throws NovaException{
        if (tokenAtual.getId().equals(Token.END)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'END'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void ponto() throws NovaException{
        if (tokenAtual.getId().equals(Token.PONTO)){
            proxToken();
            gravaLinha();
            gravarArq.printf(" FIM");
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: '.'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void abre_parenteses() throws NovaException{
        if (tokenAtual.getId().equals(Token.ABRE_PARENTESES)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: '('. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void fecha_parenteses() throws NovaException{
        if (tokenAtual.getId().equals(Token.FECHA_PARENTESES)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: ')'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void doispontos_igual() throws NovaException{
        if (tokenAtual.getId().equals(Token.DOISPONTOS_IGUAL)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: ':='. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void program_() throws NovaException{
        if (tokenAtual.getId().equals(Token.PROGRAM)){
            gravaLinha();
            gravaToken();
            proxToken();
            id_(false);
            variaveisList.pop();
            variaveisAuxList.pop();
            pontovirgula();
            pulaLinha();
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'PROGRAM'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void decl_var() throws NovaException{
        if (tokenAtual.getId().equals(Token.INTEGER)){
            gravaLinha();
            gravaToken();
            proxToken();
            variaveisIntegerList.add(tokenAtual);
            id_(false);
            while(!tokenAtual.getId().equals(";")){
                virgula();
                variaveisIntegerList.add(tokenAtual);
                id_(false);
            }
            pulaLinha();
            proxToken();
        }else if(tokenAtual.getId().equals(Token.REAL)){
            gravaLinha();
            gravaToken();
            proxToken();
            variaveisRealList.add(tokenAtual);
            id_(false);
            while(!tokenAtual.getId().equals(";")){
                virgula();
                variaveisRealList.add(tokenAtual);
                id_(false);
            }
            pulaLinha();
            proxToken();
        }else if(tokenAtual.getId().equals(Token.STRING)){
            gravaLinha();
            gravaToken();
            proxToken();
            variaveisStringList.add(tokenAtual);
            id_(false);
            while(!tokenAtual.getId().equals(";")){
                virgula();
                variaveisStringList.add(tokenAtual);
                id_(false);
            }
            pulaLinha();
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'INTEGER, STRING ou REAL'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void bloco(Semantico sem) throws NovaException{ //Begin [<comando>  [ <comando>]*]? End ;
        begin_();
            comando(sem);
            while (!tokenAtual.getId().equals(Token.END)) {            
                comando(sem);
            }
        end_();
        pontovirgula();
        proxToken();
    }
    
    private void comando(Semantico sem) throws NovaException{ //<comando_basico> | <iteracao> | if 
        if (tokenAtual.getId().equals(Token.IF)){
            if_(sem);
        }else if(tokenAtual.getId().equals(Token.WHILE)){
            while_(sem);
        }else if(tokenAtual.getId().equals(Token.REPEAT)){
            repeat_(sem);
        }else{
            comando_basico(sem);
        }
    }
    
    private void comando_basico(Semantico sem) throws NovaException{ //<atribuicao> | <bloco> | All ( <id>  [, <id>]* );
        Nodulo temp = new Nodulo(null);
        
        if (tokenAtual.getId().equals("ID")){ //<id> := <expr_arit> ;
            variaveisList.add(tokenAtual);
            variaveisAuxList.add(true);
            temp.esq = new Nodulo(tokenAtual);
            proxToken();
            temp.raiz = tokenAtual;
            doispontos_igual();
                temp.dir = expr_arit();
                stringExpressoesList = sem.SEMANTICS(temp);
                while(!stringExpressoesList.isEmpty()){
                    gravaLinha();
                    gravarArq.printf(" " + stringExpressoesList.pop());
                    pulaLinha();
                }
            pontovirgula();
            proxToken();
        }else if(tokenAtual.getId().equals(Token.ALL)){
            all(sem);
        }else{
            bloco(sem);
        }
    }
    
    private void all(Semantico sem) throws NovaException{
        gravaLinha();
        gravaToken();
        proxToken();
        gravaToken();
        abre_parenteses();
            sem.SEMANTICS_CHECK_ALL(tokenAtual);
            gravaToken();
            valor(false);
            while (!tokenAtual.getId().equals(")")) {
                virgula();
                sem.SEMANTICS_CHECK_ALL(tokenAtual);
                gravaToken();
                valor(false);
            }
        gravaToken();
        fecha_parenteses();
        pontovirgula();
        pulaLinha();
        proxToken();
    }
    
    private void if_(Semantico sem) throws NovaException{ //if (<expr_relacional>) then <comando> [else <comando>]?
        Nodulo temp = new Nodulo(null);
        temp.raiz = tokenAtual;
        
        proxToken();
        abre_parenteses();
            temp.dir = expr_relacional();
            stringExpressoesList = sem.SEMANTICS(temp);
            while(!stringExpressoesList.isEmpty()){
                gravaLinha();
                gravarArq.printf(" " + stringExpressoesList.pop());
                pulaLinha();
            }
        fecha_parenteses();
        then_();
        comando(sem);
        if(tokenAtual.getId().equals(Token.ELSE)){
            gravaLinha();
            gravarArq.printf(" ELSE");
            proxToken();
            pulaLinha();
            comando(sem);
        }
    }
    
    private void then_() throws NovaException{
        if (tokenAtual.getId().equals(Token.THEN)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'THEN'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void while_(Semantico sem) throws NovaException{ //while (<expr_relacional>) do <comando>
        Nodulo temp = new Nodulo(null);
        temp.raiz = tokenAtual;
        
        proxToken();
        abre_parenteses();
            temp.dir = expr_relacional();
            stringExpressoesList = sem.SEMANTICS(temp);
            while(!stringExpressoesList.isEmpty()){
                gravaLinha();
                gravarArq.printf(" " + stringExpressoesList.pop());
                pulaLinha();
            }
        fecha_parenteses();
        do_();
        comando(sem);
    }
    
    private void do_() throws NovaException{
        if (tokenAtual.getId().equals(Token.DO)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'DO'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void repeat_(Semantico sem) throws NovaException{ //repeat <comando> until (<expr_relacional>);
        Nodulo temp = new Nodulo(null);
        temp.raiz = tokenAtual;
        
        gravaLinha();
        gravaToken();
        proxToken();
        pulaLinha();
        comando(sem);
        until_();
        abre_parenteses();
            temp.dir = expr_relacional();
            stringExpressoesList = sem.SEMANTICS(temp);
            while(!stringExpressoesList.isEmpty()){
                gravaLinha();
                gravarArq.printf(" " + stringExpressoesList.pop());
                pulaLinha();
            }
        fecha_parenteses();
        pontovirgula();
        proxToken();
    }
    
    private void until_() throws NovaException{
        if (tokenAtual.getId().equals(Token.UNTIL)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'UNTIL'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private Nodulo expr_relacional() throws NovaException{ 
    //<val> <op_relacionais> <val> | (<expr_relacional>) [<op_booleanos> (<expr_relacional>)] ?
        Nodulo arvore = new Nodulo(null);
    
        if(e_valor(tokenAtual.getId())){
            arvore.esq = new Nodulo(tokenAtual);
            valor(true);
            arvore.raiz = tokenAtual;
            op_relacionais();
            arvore.dir = new Nodulo(tokenAtual);
            valor(true);
            
            return arvore;
        }else{
            abre_parenteses();
                arvore.esq = expr_relacional();
            fecha_parenteses();
            if (tokenAtual.getId().equals(Token.AND) || tokenAtual.getId().equals(Token.OR)) {
                arvore.raiz = tokenAtual;               
                proxToken();
                abre_parenteses();
                    arvore.dir = expr_relacional();
                fecha_parenteses();
            }else{
                arvore = arvore.esq;
            }
            return arvore;
        }       
    }
    
    private Nodulo expr_arit() throws NovaException{ 
    //<val> | <val>  <op_aritmetico> <val> | (<expr_arit> ) <op_aritmetico> (<expr_arit>)
        Nodulo arvore = new Nodulo(null);
        Token temp;
    
        if(e_valor(tokenAtual.getId())){
            temp = tokenAtual;
            
            arvore.raiz = temp;
            valor(true);
            
            if(tokenAtual.getId().equals(Token.ADD) 
                || tokenAtual.getId().equals(Token.SUB) 
                || tokenAtual.getId().equals(Token.DIV) 
                || tokenAtual.getId().equals(Token.MULT)){
                
                arvore.raiz = tokenAtual;
                arvore.esq = new Nodulo(temp);
                op_arit();
                arvore.dir = new Nodulo (tokenAtual);
                valor(true);
            }
            
            return arvore;
        }else{
            abre_parenteses();
                arvore.esq = expr_arit();
            fecha_parenteses();
                arvore.raiz = tokenAtual;
                op_arit();
            abre_parenteses();
                arvore.dir = expr_arit();
            fecha_parenteses();
            
            return arvore;
        }
    }
    
    private void op_arit() throws NovaException{ // + | - | * | /
        if (tokenAtual.getId().equals(Token.ADD) 
                || tokenAtual.getId().equals(Token.SUB) 
                || tokenAtual.getId().equals(Token.DIV) 
                || tokenAtual.getId().equals(Token.MULT)){
            proxToken();
        }else{
            throw new NovaException("Erro 7: Operador "
                    + tokenAtual.getId() + " invalido. Esperando: 'Operador aritmetico'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void op_relacionais() throws NovaException{ // < | > | <= | >= | = | <>
        if (tokenAtual.getId().equals(Token.MENORQ) 
                || tokenAtual.getId().equals(Token.MAIORQ) 
                || tokenAtual.getId().equals(Token.MENORQ_IGUAL) 
                || tokenAtual.getId().equals(Token.MAIORQ_IGUAl) 
                || tokenAtual.getId().equals(Token.IGUAL) 
                || tokenAtual.getId().equals(Token.DIFERENTE)){
            proxToken();
        }else{
            throw new NovaException("Erro 7: Operador "
                    + tokenAtual.getId() + " invalido. Esperando: 'Operador relacional'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void pulaLinha(){
        gravarArq.printf("%n");
        linha++;
    }
    
    private void gravaLinha(){
        if(linha < 10){
            gravarArq.printf("000" + Integer.toString(linha) + ":");
        }else if(linha < 100){
            gravarArq.printf("00" + Integer.toString(linha) + ":");
        }else if(linha < 1000){
            gravarArq.printf("0" + Integer.toString(linha) + ":");
        }else{
            gravarArq.printf(Integer.toString(linha) + ":");
        }
    }
    
    private void gravaToken(){
        if(tokenAtual.getLexema().equals("")){
            gravarArq.printf(" " + tokenAtual.getId());
        }else{
            gravarArq.printf(" " + tokenAtual.getLexema());
        }
    }
}