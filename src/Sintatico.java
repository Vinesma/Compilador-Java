import java.util.LinkedList;

public class Sintatico {
    private LinkedList<Token> tokens;
    private LinkedList<Nodulo> expressoesList  = new LinkedList<>();
    private LinkedList<String> variaveisStringList  = new LinkedList<>();
    private LinkedList<String> variaveisIntegerList = new LinkedList<>();
    private LinkedList<String> variaveisRealList    = new LinkedList<>();
    private Token tokenAtual;
    
    public void PARSER(LinkedList<Token> tokenFila) throws NovaException{
        this.tokens = tokenFila;
        tokenAtual = this.tokens.getFirst();
        Semantico sem;
                
        program_();     //Programa  <id> ;
        while (!tokenAtual.getId().equals(Token.BEGIN)){
            decl_var(); //[ <decl_var> ]*
        }
        begin_();       //Begin
        bloco();    //Begin [<comando>  [ <comando>]*]? End ;
        sem = new Semantico(expressoesList,
                            variaveisStringList,
                            variaveisIntegerList,
                            variaveisRealList);
        end_();         //End
        ponto();        //.        
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
    
    private void raiz() throws NovaException{
        if (tokenAtual.getId().equals("NUMERICO")){
            proxToken();
        }else if (tokenAtual.getId().equals("ID")){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'ID ou NUMERICO'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void id_() throws NovaException{
        if (tokenAtual.getId().equals("ID")){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'ID'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private boolean e_raiz(String compara) throws NovaException{
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
    
    private void virgula_ID() throws NovaException{
        if (tokenAtual.getId().equals(Token.VIRGULA)){
            proxToken();
            raiz();
        }else{
            pontovirgula();
        }
    }
    
    private void virgula() throws NovaException{
        if (tokenAtual.getId().equals(Token.VIRGULA)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: ','. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void begin_() throws NovaException{
        if (tokenAtual.getId().equals(Token.BEGIN)){
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
            proxToken();
            id_();
            pontovirgula();
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'PROGRAM'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void decl_var() throws NovaException{
        if (tokenAtual.getId().equals(Token.INTEGER)){
            proxToken();
            variaveisIntegerList.add(tokenAtual.getLexema());
            id_();            
            while(!tokenAtual.getId().equals(";")){
                virgula();
                variaveisIntegerList.add(tokenAtual.getLexema());
                id_();
            }
            proxToken();
        }else if(tokenAtual.getId().equals(Token.REAL)){
            proxToken();
            variaveisRealList.add(tokenAtual.getLexema());
            id_();
            while(!tokenAtual.getId().equals(";")){
                virgula();
                variaveisRealList.add(tokenAtual.getLexema());
                id_();
            }
            proxToken();
        }else if(tokenAtual.getId().equals(Token.STRING)){
            proxToken();
            variaveisStringList.add(tokenAtual.getLexema());
            id_();
            while(!tokenAtual.getId().equals(";")){
                virgula();
                variaveisStringList.add(tokenAtual.getLexema());
                id_();
            }
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'INTEGER, STRING ou REAL'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void bloco() throws NovaException{ //Begin [<comando>  [ <comando>]*]? End ;
        begin_();
            comando();
            while (!tokenAtual.getId().equals(Token.END)) {            
                comando();
            }
        end_();
        pontovirgula();
        proxToken();
    }
    
    private void comando() throws NovaException{ //<comando_basico> | <iteracao> | if 
        if (tokenAtual.getId().equals(Token.IF)){
            if_();
        }else if(tokenAtual.getId().equals(Token.WHILE)){
            while_();  
        }else if(tokenAtual.getId().equals(Token.REPEAT)){
            repeat_();
        }else{
            comando_basico();  
        }
    }
    
    private void comando_basico() throws NovaException{ //<atribuicao> | <bloco> | All ( <id>  [, <id>]* );
        Nodulo temp = new Nodulo(null);
        
        if (tokenAtual.getId().equals("ID")){ //<id> := <expr_arit> ;
            temp.esq = new Nodulo(tokenAtual);
            proxToken();
            temp.raiz = tokenAtual;
            doispontos_igual();
                temp.dir = expr_arit();
                expressoesList.add(temp);
            pontovirgula();
            proxToken();
        }else if(tokenAtual.getId().equals(Token.ALL)){
            all();
        }else{
            bloco();
        }
    }
    
    private void all() throws NovaException{
        proxToken();
        abre_parenteses();
            raiz();
            while (!tokenAtual.getId().equals(")")) {                    
                virgula_ID();
            }
        fecha_parenteses();
        pontovirgula();
        proxToken();
    }
    
    private void if_() throws NovaException{ //if (<expr_relacional>) then <comando> [else <comando>]?
        Nodulo temp = new Nodulo(null);
        temp.raiz = tokenAtual;
        
        proxToken();
        abre_parenteses();
            temp.dir = expr_relacional();
            expressoesList.add(temp);
        fecha_parenteses();
        then_();
        comando();
        if(tokenAtual.getId().equals(Token.ELSE)){
            comando();
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
    
    private void while_() throws NovaException{ //while (<expr_relacional>) do <comando>
        Nodulo temp = new Nodulo(null);
        temp.raiz = tokenAtual;
        
        proxToken();
        abre_parenteses();
            temp.dir = expr_relacional();
            expressoesList.add(temp);
        fecha_parenteses();
        do_();
        comando();
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
    
    private void repeat_() throws NovaException{ //repeat <comando> until (<expr_relacional>);
        Nodulo temp = new Nodulo(null);
        temp.raiz = tokenAtual;
        
        proxToken();
        comando();
        until_();
        abre_parenteses();
            temp.dir = expr_relacional();
            expressoesList.add(temp);
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
    
        if(e_raiz(tokenAtual.getId())){
            arvore.esq = new Nodulo(tokenAtual);
            raiz();
            arvore.raiz = tokenAtual;
            op_relacionais();
            arvore.dir = new Nodulo(tokenAtual);
            raiz();
            
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
    
        if(e_raiz(tokenAtual.getId())){
            temp = tokenAtual;
            
            arvore.raiz = temp;
            raiz();
            
            if(tokenAtual.getId().equals(Token.ADD) 
                || tokenAtual.getId().equals(Token.SUB) 
                || tokenAtual.getId().equals(Token.DIV) 
                || tokenAtual.getId().equals(Token.MULT)){
                
                arvore.raiz = tokenAtual;
                arvore.esq = new Nodulo(temp);
                op_arit();
                arvore.dir = new Nodulo (tokenAtual);
                raiz();
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
}