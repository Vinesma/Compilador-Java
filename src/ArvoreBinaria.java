public class ArvoreBinaria {
    Nodulo raiz;
    
    private Nodulo addRecursive(Nodulo current, String valor) {
        if (current == null) {
            return new Nodulo(valor);
        }
 
        if (current.esq == null) {
            current.esq = addRecursive(current.esq, valor);
        } else if (current.dir == null) {
            current.dir = addRecursive(current.dir, valor);
        } else {
            // valor already exists
            return current;
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
    
    private Nodulo deleteRecursive(Nodulo current, String valor) {
        if (current == null) {
            return null;
        }
 
        if (valor == current.valor) {
            // Nodulo to delete found
            // ... code to delete the node will go here
            if (current.esq == null && current.dir == null) {
                return null;
            }
            
            if (current.dir == null) {
                return current.esq;
            }
 
            if (current.esq == null) {
                return current.dir;
            }
        }
        
        if (current.esq != null) {
            current.esq = deleteRecursive(current.esq, valor);
            return current;
        }
        
        current.dir = deleteRecursive(current.dir, valor);
        return current;
    }
}
