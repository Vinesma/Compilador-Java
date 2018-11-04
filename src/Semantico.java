import java.util.LinkedList;

public class Semantico {
    //private LinkedList<Token> tokens;
    private LinkedList<Nodulo> expressoesList  = new LinkedList<>();
    private LinkedList<String> variaveisStringList  = new LinkedList<>();
    private LinkedList<String> variaveisIntegerList = new LinkedList<>();
    private LinkedList<String> variaveisRealList    = new LinkedList<>();
    private Token tokenAtual;
    
    public Semantico(LinkedList<Nodulo> out_expList,
                     LinkedList<String> out_VariaveisSList,
                     LinkedList<String> out_VariaveisIList,
                     LinkedList<String> out_VariaveisRList){
        
        this.expressoesList = out_expList;
        this.variaveisStringList = out_VariaveisSList;
        this.variaveisIntegerList = out_VariaveisIList;
        this.variaveisRealList = out_VariaveisRList;
        
        
    }
    
    public void SEMANTICO() throws NovaException{
        
    }
    
    private void PercorreArvoreE_D_R(Nodulo arvore) {
        if (arvore != null) {
            PercorreArvoreE_D_R(arvore.esq);
            PercorreArvoreE_D_R(arvore.dir);
            //expressao = expressao.concat(arvore.raiz + " ");
        }
    }
    
    private void PercorreArvoreE_R_D(Nodulo arvore) {
        if (arvore != null) {
            PercorreArvoreE_R_D(arvore.esq);
            //expressao = expressao.concat(arvore.raiz + " ");
            PercorreArvoreE_R_D(arvore.dir);
        }
    }
}
