import java.util.Set;
import java.util.HashSet;
import java.io.PrintWriter;
import java.io.File;
import java.util.*;

public class AFD
{
	public StateSi initialState;
	public Set<Character> alphabet;
	public Set<StateSi> states;
	public Set<StateSi> acceptStates;
	private static File file = new File("AFD.txt");
	
	public AFD()
	{
		alphabet = new HashSet<Character>();
		states = new HashSet<StateSi>();
		acceptStates = new HashSet<StateSi>();
	}
	
	//Sends this AFD in format of a table to file AFD.txt always.
	public void enviarTabla() throws Exception
	{
		System.out.println();
		System.out.println("::: ENVIANDO AFD A TABLA");
		PrintWriter output = new PrintWriter(file);
		List<Character> alphabetList = new ArrayList<>(this.alphabet);
		boolean match;
		StateSi auxInit;

		Collections.sort(alphabetList);

		//PRINTING NUMBER OF SYMBOLS(COLUMNS) AND NUMBER OF STATES(ROWS)
		output.printf(alphabetList.size() + "\n");
		output.printf(states.size() + "\n");

		//PRINTING THE ALPHABET
		output.printf("  ");
		for(Character ch: alphabetList)
			output.printf(" " + ch.charValue() + " ");
		output.printf("Token\n");

		////////////////ESTADO INICIAL//////////////////////////
		output.printf(this.initialState.id + " ");
		for(Character ch: alphabetList)
		{
			match = false;
			for(TransitionSi tsi: this.initialState.transitionsSi)
			{
				if(tsi.symbol == ch.charValue())
				{
					output.printf(" " + tsi.destination.id + " ");
					match = true;
					break;
				}
			}
			if(!match)
				output.printf("-1 ");
		}
		output.printf(" " + this.initialState.acceptation + " " + this.initialState.tokenString);
		output.printf("\n");

		//Important to save Initial State, see below comment why
		auxInit = this.initialState;
		this.states.remove(this.initialState);
		//////////////RESTO DE ESTADOS/////////////////////////
		for(StateSi si: this.states)
		{
			output.printf(si.id + " ");
			for(Character ch: alphabetList)
			{
				match = false;
				for(TransitionSi tsi: si.transitionsSi)
				{
					if(tsi.symbol == ch.charValue())
					{
						output.printf(" " + tsi.destination.id + " ");
						match = true;
						break;
					}
				}
				if(!match)
					output.printf("-1 ");
			}
			output.printf(" " + si.acceptation + " " + si.tokenString);
			output.printf("\n");
		}
		output.close();
		//Retrieving Initial State to set of States into this AFD, this is important because if we do not do so
		//After this operation, we will not be able to correctly see the AFD from option 5 (Display)
		this.states.add(auxInit);
		System.out.println("::: AFD ENVIADO A TABLA CON EXITO");
	}
}

	