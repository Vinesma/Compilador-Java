import java.util.LinkedList;

public class Sintatico {
    LinkedList<Token> tokens;
    String[] expressoesArray = new String[300];
    String expressao = "";
    int cont = 0;
    Token tokenAtual;
    
    public void PARSER(LinkedList<Token> tokenFila) throws NovaException{
        this.tokens = tokenFila;
        tokenAtual = this.tokens.getFirst();
        
        program_();     //Programa  <id> ; 
        while (!tokenAtual.getId().equals(Token.BEGIN)) {            
            decl_var(); //[ <decl_var> ]*
        }
        begin_();       //Begin 
            bloco();    //Begin [<comando>  [ <comando>]*]? End ;
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
        if (tokenAtual.getId().equals(Token.INTEGER) 
                || tokenAtual.getId().equals(Token.REAL) 
                || tokenAtual.getId().equals(Token.STRING)){
            proxToken();
            valor();
            do
                virgula_ID();
            while(!tokenAtual.getId().equals(";"));
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
            Nodulo expressaoAtual = new Nodulo(null);
            
            expressaoAtual.valor = ":=";
            expressaoAtual.esq = new Nodulo(tokenAtual.getLexema());
            proxToken();
            doispontos_igual();               
                expressaoAtual.dir = expr_arit();
                traversePostOrder(expressaoAtual.dir);
                expressoesArray[cont] = expressao;
                cont++;
                expressao = "";
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
            expr_relacional();
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
            expr_relacional();
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
            expr_relacional();
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
    
    private void expr_relacional() throws NovaException{ 
    //<val> <op_relacionais> <val> | (<expr_relacional>) [<op_booleanos> (<expr_relacional>)] ? 
        if(e_valor(tokenAtual.getId())) {
            valor();
            op_relacionais();
            valor();
        } else {
            abre_parenteses();
                expr_relacional();
            fecha_parenteses();
            if (tokenAtual.getId().equals(Token.AND) || tokenAtual.getId().equals(Token.OR)) {
                proxToken();
                abre_parenteses();
                    expr_relacional();
                fecha_parenteses();
            }
        }
    }
    
    private Nodulo expr_arit() throws NovaException{ 
    //<val> | <val>  <op_aritmetico> <val> | (<expr_arit> ) <op_aritmetico> (<expr_arit>)
        Nodulo current = new Nodulo(null);
        String temp;
    
        if(e_valor(tokenAtual.getId())){

            if(tokenAtual.getId().equals("NUMERICO")){
                temp = String.valueOf(tokenAtual.getValor());
            }else if(tokenAtual.getLexema().equals("")){
                temp = tokenAtual.getId();                   
            }else{
                temp = tokenAtual.getLexema();
            }
            
            current.valor = temp;
            valor();
            
            if(tokenAtual.getId().equals(Token.ADD) 
                || tokenAtual.getId().equals(Token.SUB) 
                || tokenAtual.getId().equals(Token.DIV) 
                || tokenAtual.getId().equals(Token.MULT)){
                
                current.valor = tokenAtual.getId();
                current.esq = new Nodulo(temp);
                op_arit();
                
                if(tokenAtual.getId().equals("NUMERICO")){
                    current.dir = new Nodulo (String.valueOf(tokenAtual.getValor()));
                }else if(tokenAtual.getLexema().equals("")){
                    current.dir = new Nodulo (tokenAtual.getId());                   
                }else{
                    current.dir = new Nodulo (tokenAtual.getLexema());
                }
                valor();
            }
            
            return current;
        }else{
            abre_parenteses();
                current.esq = expr_arit();
            fecha_parenteses();
                current.valor = tokenAtual.getId();
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
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'Operador aritmetico'. "
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
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'Operador relacional'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void traversePostOrder(Nodulo node) {       
        if (node != null) {           
            traversePostOrder(node.esq);
            traversePostOrder(node.dir);
            expressao = expressao.concat(node.valor + " ");
        }
    }
}
