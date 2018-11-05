import java.util.LinkedList;

public class Semantico {
    private LinkedList<Nodulo> expressoesList  = new LinkedList<>();
    private LinkedList<String> variaveisStringList  = new LinkedList<>();
    private LinkedList<String> variaveisIntegerList = new LinkedList<>();
    private LinkedList<String> variaveisRealList    = new LinkedList<>();
    private LinkedList<String> stringExpressoesList = new LinkedList<>();
    //private String temp = "";
    private int cont = 0;
    
    public Semantico(LinkedList<Nodulo> out_expList,
                     LinkedList<String> out_VariaveisSList,
                     LinkedList<String> out_VariaveisIList,
                     LinkedList<String> out_VariaveisRList) throws NovaException{
        
        this.expressoesList = out_expList;
        this.variaveisStringList = out_VariaveisSList;
        this.variaveisIntegerList = out_VariaveisIList;
        this.variaveisRealList = out_VariaveisRList;
    }
    
    public LinkedList<String> SEMANTICS() throws NovaException{
        String temp = "";
        
        while (!expressoesList.isEmpty()) {           
            if(expressoesList.peekFirst().raiz.getId().equals(":=")){
                System.out.println("ARITMETICA");
                temp = temp.concat(expressoesList.peekFirst().esq.raiz.getId() + " " + expressoesList.peekFirst().raiz.getId());
                stringExpressoesList.add(PercorreArvoreE_D_R(expressoesList.pop().dir));
                stringExpressoesList.add(temp);
            }else{
                System.out.println("RELACIONAL");
                PercorreArvoreE_D_R(expressoesList.pop().dir);
            }
        }
        return stringExpressoesList;
    }
    
    private String PercorreArvoreE_D_R(Nodulo arvore) {
        String temp = "";
        
        if (arvore != null) {
            PercorreArvoreE_D_R(arvore.esq);
            PercorreArvoreE_D_R(arvore.dir);
            
            if(arvore.raiz.getLexema().equals("")){               
                if(arvore.raiz.getId().equals("NUMERICO")){
                    temp = temp.concat(Integer.toString(arvore.raiz.getValor()));                   
                    arvore = null;
                    return temp;
                }else{
                    temp = temp.concat(arvore.raiz.getId());
                    arvore = null;
                    return temp;
                }
            }else{
                System.out.println(arvore.raiz.getLexema());
                return temp;
            }
        }
        return temp;
    }
    
    private void PercorreArvoreE_R_D(Nodulo arvore) {
        if (arvore != null) {
            PercorreArvoreE_R_D(arvore.esq);
            
            if(arvore.raiz.getLexema().equals("")){               
                if(arvore.raiz.getId().equals("NUMERICO")){
                    System.out.println(arvore.raiz.getValor());
                }else{
                    System.out.println(arvore.raiz.getId());
                }
            }else{
                System.out.println(arvore.raiz.getLexema());
            }
            //expressao = expressao.concat(arvore.raiz + " ");
            PercorreArvoreE_R_D(arvore.dir);
        }
    }
}
