import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DisplayableState
{
	public SimpleIntegerProperty id;
	public SimpleStringProperty symbol;
	public SimpleIntegerProperty destination;
	public SimpleIntegerProperty acceptState;

	public DisplayableState()
	{

	}

	public DisplayableState(int id, String symbol, int destination, int acceptState)
	{
		this.id = new SimpleIntegerProperty(id);
		this.symbol = new SimpleStringProperty(symbol);
		this.destination = new SimpleIntegerProperty(destination);
		this.acceptState = new SimpleIntegerProperty(acceptState);
	}

	public int getId()
	{
		return id.get();
	}

	public void setId(int id1)
	{
		id.set(id1);
	}

	public String getSymbol()
	{
		return symbol.get();
	}

	public void setSymbol(String symbol1)
	{
		symbol.set(symbol1);
	}

	public int getDestination()
	{
		return destination.get();
	}

	public void setDestination(int destination1)
	{
		destination.set(destination1);
	}

	public int getAcceptState()
	{
		return acceptState.get();
	}

	public void setAcceptState(int accept1)
	{
		acceptState.set(accept1);
	}
}