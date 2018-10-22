public class ArvoreBinaria {
    Nodulo raiz;
    String expressao;
    
    private Nodulo addRecursive(Nodulo current, String valor) {
        if (current == null) {
            return new Nodulo(valor);
        }
 
        if (current.esq == null) {
            current.esq = addRecursive(current.esq, valor);
        } else if (current.dir == null) {
            current.dir = addRecursive(current.dir, valor);
        } else {
            current.dir = addRecursive(current.dir, valor);
        }
 
        return current;
    }
    
    private Nodulo addLeft(Nodulo current, String valor){        
        if (current == null) {
            return new Nodulo(valor);
        }
        
        current.esq = addLeft(current.esq, valor);
        
        return current;
    }
    
    public void addEsq(String valor){
        raiz = addLeft(raiz, valor);
    }
    
    public void add(String valor) {
        raiz = addRecursive(raiz, valor);
    }
    
    private Nodulo deletaAll(Nodulo current){
        if(current != null){
            if(deletaAll(current.esq) == null){
                    if(deletaAll(current.dir) == null){
                        return null;
                    }else{
                        deletaAll(current.dir);
                    }
            }else{
                deletaAll(current.esq);
            }          
        }
        
        return current;
    }
    
    public void deletaArvore(){
        raiz = deletaAll(raiz);
    }
    
    public String getExpressao(){
        expressao = "";
        traverseInOrder(raiz);             
        return expressao; //dando bug n sei pq
    }
    
    private void traverseInOrder(Nodulo node) {
        if (node != null) {
            traverseInOrder(node.esq);
            expressao.concat(node.valor + " ");
            traverseInOrder(node.dir);
        }       
    }
}
