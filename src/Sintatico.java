import java.util.LinkedList;

public class Sintatico {
    LinkedList<Token> tokens;
    Token tokenAtual;
    
    public void PARSER(LinkedList<Token> tokenFila) throws NovaException{
        this.tokens = tokenFila;
        tokenAtual = this.tokens.getFirst();
        
        do
        if (tokenAtual.getId().equals("PROGRAM")) {
            proxToken();
            valor();
            pontovirgula();
            proxToken();
        } else if(tokenAtual.getId().equals("INTEGER") || tokenAtual.getId().equals("REAL") || tokenAtual.getId().equals("STRING")) {
            proxToken();
            valor();
            do
                virgula_ID();
            while(!tokenAtual.getId().equals(";"));
            proxToken();
        } else if(tokenAtual.getId().equals("BEGIN")){
            proxToken();
        } else {
            throw new NovaException("Erro 2: Símbolo " 
                    + tokenAtual.getId() + " inesperado. "
                    + "Linha: " + tokenAtual.getPos());
        }
        while(!tokenAtual.getId().equals(Token.FINAL));
        
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
}
