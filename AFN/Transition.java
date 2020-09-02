public class Transition
{
	public char minSymbol;
	public char maxSymbol;
	public State destination;

	public Transition(char s, State e)
	{
		minSymbol = s;
		maxSymbol = s;
		destination = e;
	}

	public Transition(char minSymbol, char maxSymbol, State e)
	{
		this.minSymbol = minSymbol;
		this.maxSymbol = maxSymbol;
		destination = e;
	}	
}