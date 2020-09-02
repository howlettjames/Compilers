public class TransitionSi
{
	public char symbol;
	public StateSi destination;

	public TransitionSi(char s, StateSi e)
	{
		symbol = s;
		destination = e;
	}	
}