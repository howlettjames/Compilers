import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DisplayableToken
{
	public SimpleStringProperty lexema;
	public SimpleIntegerProperty token;

	public DisplayableToken()
	{

	}

	public DisplayableToken(String lexema, int token)
	{
		this.lexema = new SimpleStringProperty(lexema);
		this.token = new SimpleIntegerProperty(token);
	}	

	public String getLexema()
	{
		return lexema.get();
	}

	public void setLexema(String lexema1)
	{
		lexema.set(lexema1);
	}

	public int getToken()
	{
		return token.get();
	}

	public void setToken(int token1)
	{
		token.set(token1);
	}
}