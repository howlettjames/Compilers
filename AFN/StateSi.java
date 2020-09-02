import java.util.Set;
import java.util.HashSet;

public class StateSi
{
	private static int idCurrentStateSi = 0;
	public int id;
	public Set<State> setStates;
	public Set<TransitionSi> transitionsSi;
	public int acceptation;
	public String tokenString;

	public StateSi()
	{
		id = idCurrentStateSi++;
		setStates = new HashSet<State>();
		transitionsSi = new HashSet<TransitionSi>();
		acceptation = -1;
		setStates.clear();
	}

	public StateSi(int id)
	{
		this.id = id;
		setStates = new HashSet<State>();
		transitionsSi = new HashSet<TransitionSi>();
		acceptation = -1;
		setStates.clear();
	}
}