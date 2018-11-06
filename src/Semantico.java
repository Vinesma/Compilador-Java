import java.util.LinkedList;

public class Semantico {
    private LinkedList<Token> variaveisStringList  = new LinkedList<>();
    private LinkedList<Token> variaveisIntegerList = new LinkedList<>();
    private LinkedList<Token> variaveisRealList    = new LinkedList<>();
    private LinkedList<String> stringExpressoesList = new LinkedList<>();
    private String expressaoAtual = "";
    private int cont = 1;
    
    public Semantico(LinkedList<Token> out_VariaveisSList,
                     LinkedList<Token> out_VariaveisIList,
                     LinkedList<Token> out_VariaveisRList) throws NovaException{
        
        this.variaveisStringList = out_VariaveisSList;
        this.variaveisIntegerList = out_VariaveisIList;
        this.variaveisRealList = out_VariaveisRList;
    }
    
    public LinkedList<String> SEMANTICS(Nodulo out_exp) throws NovaException{
        String temp = "";
        
        stringExpressoesList.clear();
        if(out_exp.raiz.getId().equals(":=")){                
            temp = temp.concat(out_exp.esq.raiz.getLexema() + " " + out_exp.raiz.getId());
            if(!temFolhas(out_exp.dir)){
                if(out_exp.dir.raiz.getId().equals("NUMERICO")){
                    temp = temp.concat(" " + out_exp.dir.raiz.getValor());
                }else{
                    temp = temp.concat(" " + out_exp.dir.raiz.getLexema());
                }
                stringExpressoesList.add(temp);
                temp = "";
            }else{
                PercorreArvore(out_exp.dir);
                temp = temp.concat(" TMP#" + Integer.toString(cont - 1));
                stringExpressoesList.add(temp);
                temp = "";
            }
        }else{
            PercorreArvore(out_exp.dir);
                stringExpressoesList.add("IF NOT TMP#" + Integer.toString(cont - 1) + " GOTO");
            }
        return stringExpressoesList;
    }
    
    public void SEMANTICS_CHECK_ALL(Token tokenAtual) throws NovaException{
        /*if(!variaveisStringList.contains(tokenAtual.getLexema())){
        if(variaveisIntegerList.contains(tokenAtual.getLexema())){
        throw new NovaException("ERRO 3: Tipos incompatíveis <String> e <Integer>, linha: " + tokenAtual.getPos());
        }else if(variaveisRealList.contains(tokenAtual.getLexema())){
        throw new NovaException("ERRO 3: Tipos incompatíveis <String> e <Real>, linha: " + tokenAtual.getPos());
        }else{
        throw new NovaException("ERRO 4: Identificador nao declarado: '"
        + tokenAtual.getLexema() + "', linha: " + tokenAtual.getPos());
        }
        }*/
    }
    
    private void SEMANTICS_CHECK_ERRO4(Token tokenAtual) throws NovaException{
        
    }
    
    private void SEMANTICS_CHECK_ERRO6(Token tokenAtual) throws NovaException{
        
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
