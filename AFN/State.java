import java.util.Set;
import java.util.HashSet;

public class State
{
	private static int idCurrentState = 0;
	public int id;
	public Set<Transition> transitions;
	public int acceptation;
	public String tokenString;

	public State()
	{
		id = idCurrentState++;
		acceptation = -1;
		tokenString = null;
		transitions = new HashSet<Transition>();
		transitions.clear();
	}
}