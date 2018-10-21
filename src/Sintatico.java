import java.util.LinkedList;

public class Sintatico {
    LinkedList<Token> tokens;
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
        //expressao();
    }
    
    private void proxToken(){
        int linha;
        
        linha = tokenAtual.getPos();
        tokens.pop();
        // at the end of input we return an epsilon token
        if (tokens.isEmpty())
            tokenAtual = new Token(Token.FINAL, "", linha);
        else
            tokenAtual = tokens.getFirst();
    }
    
    private void expressao() throws NovaException{
        // expressao -> signed_term sum_op
        signedTerm();
        sumOp();
    }
    
    private void sumOp() throws NovaException{
        if (tokenAtual.getId().equals(Token.ADD) || tokenAtual.getId().equals(Token.SUB)){
            // sum_op -> PLUSMINUS term sum_op
            proxToken();
            term();
            sumOp();
        }else{
            // sum_op -> FINAL
        }
    }
    
    private void signedTerm() throws NovaException{
        if (tokenAtual.getId().equals(Token.ADD) || tokenAtual.getId().equals(Token.SUB)){
            // signed_term -> PLUSMINUS term
            proxToken();
            term();
        }else{
            // signed_term -> term
            term();
        }
    }
    
    private void term() throws NovaException{
        // term -> factor term_op
        factor();
        termOp();
    }

    private void termOp() throws NovaException{
        if (tokenAtual.getId().equals(Token.MULT) || tokenAtual.getId().equals(Token.DIV)){
            // term_op -> MULTDIV factor term_op
            proxToken();
            signedFactor();
            termOp();
        }else{
            // term_op -> FINAL
        }
    }
    
    private void factor() throws NovaException{
        // factor -> argument factor_op
        argument();
        factorOp();
    }

    private void factorOp() throws NovaException{
        if (tokenAtual.getId().equals("^")){
            // factor_op -> RAISED expressao
            proxToken();
            signedFactor();
        }else{
            // factor_op -> FINAL
        }
    }
    
    private void signedFactor() throws NovaException{
        if (tokenAtual.getId().equals(Token.ADD) || tokenAtual.getId().equals(Token.SUB)){
            // signed_factor -> PLUSMINUS factor
            proxToken();
            factor();
        }else{
            // signed_factor -> factor
            factor();
        }
    }
    
    private void argument() throws NovaException{
        if (tokenAtual.getId().equals(Token.FUNCTION)){
            // argument -> FUNCTION argument
            proxToken();
            argument();
        }else if (tokenAtual.getId().equals(Token.ABRE_PARENTESES)){
            // argument -> OPEN_BRACKET sum CLOSE_BRACKET
            proxToken();
            expressao();

            if (tokenAtual.getId().equals(Token.FECHA_PARENTESES))
                throw new NovaException("Closing brackets expected and " + tokenAtual.getId() + " found instead");

            proxToken();
        }else{
            // argument -> valor
            valor();
        }
    }
    
    private void valor() throws NovaException{
        if (tokenAtual.getId().equals("NUMERICO")){
            // argument -> NUMBER
            proxToken();
        }else if (tokenAtual.getId().equals("ID")){
            // argument -> VARIABLE
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: 'ID ou NUMERICO'. "
                    + "Linha: " + tokenAtual.getPos());
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
            valor();
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
                expr_arit();
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
            do
                virgula_ID();
            while(!tokenAtual.getId().equals(")"));
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
    
    private void expr_relacional() throws NovaException{ //<val> <op_relacionais> <val> | (<expr_relacional>) [<op_booleanos> (<expr_relacional>)] ? 
        if (tokenAtual.getId().equals(Token.UNTIL)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: ';'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
    
    private void expr_arit() throws NovaException{ //<val> | <val>  <op_aritmetico> <val> | (<expr_arit> ) <op_aritmetico> (<expr_arit>)
        if (tokenAtual.getId().equals(Token.UNTIL)){
            proxToken();
        }else{
            throw new NovaException("Erro 2: Símbolo "
                    + tokenAtual.getId() + " inesperado. Esperando: ';'. "
                    + "Linha: " + tokenAtual.getPos());
        }
    }
}
