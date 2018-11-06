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
        
        SEMANTICS_CHECK_ERRO6(variaveisStringList, variaveisRealList);
        SEMANTICS_CHECK_ERRO6(variaveisStringList, variaveisIntegerList);
        SEMANTICS_CHECK_ERRO6(variaveisStringList, variaveisStringList);
        SEMANTICS_CHECK_ERRO6(variaveisRealList, variaveisIntegerList);
        SEMANTICS_CHECK_ERRO6(variaveisRealList, variaveisRealList);
        SEMANTICS_CHECK_ERRO6(variaveisIntegerList, variaveisIntegerList);
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
        boolean variavelFoiDeclarada = false;
        
        for (int i = 0; i < variaveisIntegerList.size(); i++) {
            if (tokenAtual.getLexema().equals(variaveisIntegerList.get(i).getLexema())) {
                throw new NovaException("ERRO 3: Tipos incompatíveis <STRING> e <INTEGER>, linha: " + tokenAtual.getPos());
            }
        }
        
        for (int i = 0; i < variaveisRealList.size(); i++) {
            if (tokenAtual.getLexema().equals(variaveisRealList.get(i).getLexema())) {
                throw new NovaException("ERRO 3: Tipos incompatíveis <STRING> e <REAL>, linha: " + tokenAtual.getPos());
            }
        }
        
        for (int i = 0; i < variaveisStringList.size(); i++) {
            if (tokenAtual.getLexema().equals(variaveisStringList.get(i).getLexema())) {
                variavelFoiDeclarada = true;
            }
        }
        
        if(variavelFoiDeclarada == false){
            throw new NovaException("ERRO 4: Variavel '" 
                    + tokenAtual.getLexema() + "' nao foi declarada, linha: " + tokenAtual.getPos());
        }
    }
    
    public void SEMANTICS_CHECK_ERRO4(Token tokenAtual) throws NovaException{
        boolean variavelExiste1 = false;
        boolean variavelExiste2 = false;
        boolean variavelExiste3 = false;
        
        for (int i = 0; i < variaveisIntegerList.size(); i++) {
            if (tokenAtual.getLexema().equals(variaveisIntegerList.get(i).getLexema())) {
                variavelExiste1 = true;
                i = variaveisIntegerList.size();
            }
        }
        
        for (int i = 0; i < variaveisStringList.size(); i++) {
            if (tokenAtual.getLexema().equals(variaveisStringList.get(i).getLexema())) {
                variavelExiste2 = true;
                i = variaveisStringList.size();
            }
        }
        
        for (int i = 0; i < variaveisRealList.size(); i++) {
            if (tokenAtual.getLexema().equals(variaveisRealList.get(i).getLexema())) {
                variavelExiste3 = true;
                i = variaveisRealList.size();
            }
        }
        
        if((variavelExiste1 || variavelExiste2 || variavelExiste3) == false){
            throw new NovaException("ERRO 4: Variavel '" 
                    + tokenAtual.getLexema() + "' nao foi declarada, linha: " + tokenAtual.getPos());
        }
    }
    
    private void SEMANTICS_CHECK_ERRO6(LinkedList<Token> l1,LinkedList<Token> l2) throws NovaException{
        boolean temp = true;
        if(l1 == l2){
            temp = false;
        }
        
        for (int i = 0; i < l1.size(); i++) {
            for (int j = 0; j < l2.size(); j++) {
                if(l1.get(i).getLexema().equals(l2.get(j).getLexema()) && temp == true){
                    throw new NovaException("ERRO 6: Variavel '"+ l1.get(i).getLexema() +"' declarada em duplicidade, linha: " + l1.get(i).getPos());
                }else if(l1.get(i).getLexema().equals(l2.get(j).getLexema()) && temp == false){
                    temp = true;
                }
            }
            if(l1 == l2){
                temp = false;
            }
        }
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
