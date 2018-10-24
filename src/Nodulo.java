public class Nodulo {
    Token valor;  //raiz
    Nodulo esq;   //folha esquerda
    Nodulo dir;   //folha direita
    
    Nodulo(Token valor){
        esq = null;
        this.valor = valor;
        dir = null;
    }
}
