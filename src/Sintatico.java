import java.util.LinkedList;

public class Sintatico {
    LinkedList<Token> tokens;
    LinkedList<Nodulo> exp_AritmeticasList  = new LinkedList<>();
    LinkedList<Nodulo> exp_RelacionaisList  = new LinkedList<>();
    LinkedList<String> VariaveisStringList  = new LinkedList<>();
    LinkedList<String> VariaveisIntegerList = new LinkedList<>();
    LinkedList<String> VariaveisRealList    = new LinkedList<>();
    Token tokenAtual;
    
    public void PARSER(LinkedList<Token> tokenFila) throws NovaException{
        this.tokens = tokenFila;
        tokenAtual = this.tokens.getFirst();
        
        program_();     //Programa  <id> ;
        while (!tokenAtual.getId().equals(Token.BEGIN)){
            decl_var(); //[ <decl_var> ]*
        }
        begin_();       //Begin
            bloco();    //Begin [<comando>  [ <comando>]*]? End ;
        end_();         //End
        ponto();        //.
        
        //SEMANTICO; chamar a expressao do semantico aqui
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
    
    private void valor() throws NovaException{
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
    
    private void virgula_ID() throws NovaException{
        if (tokenAtual.getId().equals(Token.VIRGULA)){
            proxToken();
            valor();
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
            VariaveisIntegerList.add(tokenAtual.getLexema());
            id_();            
            while(!tokenAtual.getId().equals(";")){
                virgula();
                VariaveisIntegerList.add(tokenAtual.getLexema());
                id_();
            }
            proxToken();
        }else if(tokenAtual.getId().equals(Token.REAL)){
            proxToken();
            VariaveisRealList.add(tokenAtual.getLexema());
            id_();
            while(!tokenAtual.getId().equals(";")){
                virgula();
                VariaveisRealList.add(tokenAtual.getLexema());
                id_();
            }
            proxToken();
        }else if(tokenAtual.getId().equals(Token.STRING)){
            proxToken();
            VariaveisStringList.add(tokenAtual.getLexema());
            id_();
            while(!tokenAtual.getId().equals(";")){
                virgula();
                VariaveisStringList.add(tokenAtual.getLexema());
                id_();
            }
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'ID'. "
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
        if (tokenAtual.getId().equals("ID")){ //<id> := <expr_arit> ;           
            proxToken();
            doispontos_igual();
                exp_AritmeticasList.add(expr_arit());
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
            valor();
            while (!tokenAtual.getId().equals(")")) {                    
                virgula_ID();
            }
        fecha_parenteses();
        pontovirgula();
        proxToken();
    }
    
    private void if_() throws NovaException{ //if (<expr_relacional>) then <comando> [else <comando>]?       
        proxToken();
        abre_parenteses();
            exp_RelacionaisList.add(expr_relacional());
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
        proxToken();
        abre_parenteses();
            exp_RelacionaisList.add(expr_relacional());
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
        proxToken();
        comando();
        until_();
        abre_parenteses();
            exp_RelacionaisList.add(expr_relacional());
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
        Nodulo current = new Nodulo(null);
    
        if(e_valor(tokenAtual.getId())){
            current.esq = new Nodulo(tokenAtual);
            valor();
            current.valor = tokenAtual;
            op_relacionais();
            current.dir = new Nodulo(tokenAtual);
            valor();
            
            return current;
        }else{
            abre_parenteses();
                current.esq = expr_relacional();
            fecha_parenteses();
            if (tokenAtual.getId().equals(Token.AND) || tokenAtual.getId().equals(Token.OR)) {
                current.valor = tokenAtual;               
                proxToken();
                abre_parenteses();
                    current.dir = expr_relacional();
                fecha_parenteses();
            }else{
                current = current.esq;
            }
            return current;
        }       
    }
    
    private Nodulo expr_arit() throws NovaException{ 
    //<val> | <val>  <op_aritmetico> <val> | (<expr_arit> ) <op_aritmetico> (<expr_arit>)
        Nodulo current = new Nodulo(null);
        Token temp;
    
        if(e_valor(tokenAtual.getId())){
            temp = tokenAtual;
            
            current.valor = temp;
            valor();
            
            if(tokenAtual.getId().equals(Token.ADD) 
                || tokenAtual.getId().equals(Token.SUB) 
                || tokenAtual.getId().equals(Token.DIV) 
                || tokenAtual.getId().equals(Token.MULT)){
                
                current.valor = tokenAtual;
                current.esq = new Nodulo(temp);
                op_arit();
                current.dir = new Nodulo (tokenAtual);
                valor();
            }
            
            return current;
        }else{
            abre_parenteses();
                current.esq = expr_arit();
            fecha_parenteses();
                current.valor = tokenAtual;
                op_arit();
            abre_parenteses();
                current.dir = expr_arit();
            fecha_parenteses();
            
            return current;
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
    
    private void PercorreArvoreE_D_R(Nodulo node) {
        if (node != null) {
            PercorreArvoreE_D_R(node.esq);
            PercorreArvoreE_D_R(node.dir);
            //expressao = expressao.concat(node.valor + " ");
        }
    }
    
    private void PercorreArvoreE_R_D(Nodulo node) {
        if (node != null) {
            PercorreArvoreE_R_D(node.esq);
            //expressao = expressao.concat(node.valor + " ");
            PercorreArvoreE_R_D(node.dir);
        }
    }
}