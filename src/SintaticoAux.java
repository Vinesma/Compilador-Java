import java.util.LinkedList;

/**
 *
 * @author Otavio
 */

public class SintaticoAux {
    LinkedList<Token> tokens;
    Token lookahead;
    
    public void PARSER(LinkedList<Token> tokenFila) throws NovaException{
        this.tokens = (LinkedList<Token>) tokens.clone();
        lookahead = this.tokens.getFirst();

        expression();

        if (!lookahead.getId().equals(Token.EPSILON))
            throw new NovaException("Unexpected symbol %s found");
    }
    
    private void nextToken(){
        tokens.pop();
        // at the end of input we return an epsilon token
        if (tokens.isEmpty())
            lookahead = new Token(Token.EPSILON, "", 0);
        else
            lookahead = tokens.getFirst();
    }
    
    private void expression() throws NovaException{
        // expression -> signed_term sum_op
        signedTerm();
        sumOp();
    }
    
    private void sumOp() throws NovaException{
        if (lookahead.getId().equals("+") || lookahead.getId().equals("-")){
            // sum_op -> PLUSMINUS term sum_op
            nextToken();
            term();
            sumOp();
        }else{
            // sum_op -> EPSILON
        }
    }
    
    private void signedTerm() throws NovaException{
        if (lookahead.getId().equals("+") || lookahead.getId().equals("-")){
            // signed_term -> PLUSMINUS term
            nextToken();
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
        if (lookahead.getId().equals("*") || lookahead.getId().equals("/")){
            // term_op -> MULTDIV factor term_op
            nextToken();
            signedFactor();
            termOp();
        }else{
            // term_op -> EPSILON
        }
    }
    
    private void factor() throws NovaException{
        // factor -> argument factor_op
        argument();
        factorOp();
    }

    private void factorOp() throws NovaException{
        if (lookahead.getId().equals("^")){
            // factor_op -> RAISED expression
            nextToken();
            signedFactor();
        }else{
            // factor_op -> EPSILON
        }
    }
    
    private void signedFactor() throws NovaException{
        if (lookahead.getId().equals("+") || lookahead.getId().equals("-")){
            // signed_factor -> PLUSMINUS factor
            nextToken();
            factor();
        }else{
            // signed_factor -> factor
            factor();
        }
    }
    
    private void argument() throws NovaException{
        if (lookahead.getId().equals(Token.FUNCTION)){
            // argument -> FUNCTION argument
            nextToken();
            argument();
        }else if (lookahead.equals("(")){
            // argument -> OPEN_BRACKET sum CLOSE_BRACKET
            nextToken();
            expression();

            if (lookahead.equals(")"))
                throw new NovaException("Closing brackets expected and " + lookahead.getId() + " found instead");

            nextToken();
        }else{
            // argument -> value
            value();
        }
    }
    
    private void value() throws NovaException{
        if (lookahead.getValor() != 0){
            // argument -> NUMBER
            nextToken();
        }else if (lookahead.getId().equals("ID")){
            // argument -> VARIABLE
            nextToken();
        }else{
            throw new NovaException("Unexpected symbol "+ lookahead.getId() +" found");
        }
    }
}
