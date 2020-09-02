public class Token
{
	public String lexema;
	public int token;
	public String string;
	//FOR ARITHMETIC EXPRESSIONS
	public static final String MAS = "+";
	public static final String MENOS = "-";
	public static final String PROD = "*";
	public static final String DIV = "/";
	//FOR REGULAR EXPRESSIONS
	public static final String OR = "|";
	public static final String CONCAT = "&";
	public static final String CERR_EST = "*";
	public static final String CERR_MAS = "+";
	public static final String CERR_OPC = "?";
	public static final String PAR_I = "(";
	public static final String PAR_D = ")";
	public static final String CORCHETE_I = "[";
	public static final String CORCHETE_D = "]";
	public static final String GUION = "-";
	//FOR GRAMATICS
	public static final String PUNTOyCOMA = ";";
	public static final String FLECHA = "->";

	public Token(String lexema, int token, String string)
	{
		this.lexema = lexema;
		this.token = token;
		this.string = string;
	}	
}