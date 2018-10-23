public class Nodulo {
    String valor; //raiz
    Nodulo esq;   //folha esquerda
    Nodulo dir;   //folha direita
    
    Nodulo(String valor){
        esq = null;
        this.valor = valor;
        dir = null;
    }
}
