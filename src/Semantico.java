import java.util.LinkedList;

public class Semantico {
    private LinkedList<Nodulo> expressoesList  = new LinkedList<>();
    private LinkedList<String> variaveisStringList  = new LinkedList<>();
    private LinkedList<String> variaveisIntegerList = new LinkedList<>();
    private LinkedList<String> variaveisRealList    = new LinkedList<>();
    private LinkedList<String> stringExpressoesList = new LinkedList<>();
    private String expressaoAtual = "";
    private int cont = 1;
    
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
                temp = temp.concat(expressoesList.peekFirst().esq.raiz.getLexema() + " " + expressoesList.peekFirst().raiz.getId());
                if(!temFolhas(expressoesList.peek().dir)){
                    if(expressoesList.peek().dir.raiz.getId().equals("NUMERICO")){
                        temp = temp.concat(" " + expressoesList.pop().dir.raiz.getValor());
                    }else{
                        temp = temp.concat(" " + expressoesList.pop().dir.raiz.getLexema());
                    }
                    stringExpressoesList.add(temp);
                    temp = "";
                }else{
                    PercorreArvore(expressoesList.pop().dir);
                    temp = temp.concat(" TMP#" + Integer.toString(cont - 1));
                    stringExpressoesList.add(temp);
                    temp = "";
                }
            }else{
                PercorreArvore(expressoesList.pop().dir);
                stringExpressoesList.add("IF NOT TMP#" + Integer.toString(cont - 1) + " GOTO");
            }
        }
        return stringExpressoesList;
    }
    
    private void PercorreArvore(Nodulo arvore) {
        if(temFolhas(arvore)){
            if(temFolhas(arvore.esq) || temFolhas(arvore.dir)){
                PercorreArvore(arvore.esq);
                PercorreArvore(arvore.dir);
                PercorreArvore(arvore);
            }else{
                if(!arvore.esq.raiz.getId().equals("NUMERICO")){
                    expressaoAtual = expressaoAtual.concat(arvore.esq.raiz.getLexema());
                }else{
                    expressaoAtual = expressaoAtual.concat(Integer.toString(arvore.esq.raiz.getValor()));
                }
                expressaoAtual = expressaoAtual.concat(" " + arvore.raiz.getId() + " ");
                if(!arvore.dir.raiz.getId().equals("NUMERICO")){
                    expressaoAtual = expressaoAtual.concat(arvore.dir.raiz.getLexema());
                }else{
                    expressaoAtual = expressaoAtual.concat(Integer.toString(arvore.dir.raiz.getValor()));
                }
                arvore.raiz = new Token(expressaoAtual ,"TMP#" + Integer.toString(cont),arvore.raiz.getPos());
                stringExpressoesList.add(arvore.raiz.getLexema() + " := " + expressaoAtual);
                expressaoAtual = "";
                arvore.esq = null;
                arvore.dir = null;
                cont++;
            }
        }
    }
    
    private boolean temFolhas(Nodulo arvore){
        return !(arvore.esq == null && arvore.dir == null);
    }
}
