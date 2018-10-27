public class Nodulo {
    Token raiz;  //raiz
    Nodulo esq;   //folha esquerda
    Nodulo dir;   //folha direita
    
    Nodulo(Token raiz){
        esq = null;
        this.raiz = raiz;
        dir = null;
    }
}
