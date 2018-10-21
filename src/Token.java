
public class Token {
    
    public static final String FINAL = ""; //representa o fim do parser
    public static final String PONTOVIRGULA = ";";
    public static final String VIRGULA = ",";
    public static final String ADD = "+";
    public static final String SUB = "-";
    public static final String MULT = "*";
    public static final String DIV = "/";
    public static final String ABRE_PARENTESES = "(";
    public static final String FECHA_PARENTESES = ")";
    public static final int FUNCTION = 4;
    
    private String id, lexema;
    private int pos, valor;
    
    public Token(String id, String lexema, int pos, int valor){
        this.id = id;
        this.lexema = lexema;
        this.pos = pos;
        this.valor = valor;
    }
    
    public Token(String id, String lexema, int pos){
        this.id = id;
        this.lexema = lexema;
        this.pos = pos;
        this.valor = 0;
    }
    
    public Token(){
        this.id = null;
        this.lexema = null;
        this.pos = 0;
        this.valor = 0;
    }
    
    public String getId(){
	return this.id;
    }
    
    public String getLexema(){
	return this.lexema;
    }
    
    public int getPos(){
	return this.pos;
    }
    
    public int getValor(){
	return this.valor;
    }
    
    public void dados(){
        System.out.println("DEBUG: "
                + "\nID: " + this.getId()
                + "\nLEXEMA: " + this.getLexema()
                + "\nPOSIÇÃO: " + this.getPos()
                + "\nVALOR: " + this.getValor() + "\n");
    }
}
