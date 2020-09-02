import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

public class AFN
{
	public State initialState;
	public Set<Character> alphabet;
	public Set<State> states;
	public Set<State> acceptStates;
	public static boolean primerAFNAT = true;

	public AFN()
	{
		alphabet = new HashSet<Character>();
		states = new HashSet<State>();
		acceptStates = new HashSet<State>();	
	}
	
	public AFN(char s)
	{
		alphabet = new HashSet<Character>();
		states = new HashSet<State>();
		acceptStates = new HashSet<State>();
		State e1 = new State();
		State e2 = new State();

		e1.transitions.add(new Transition(s, e2));
		e2.acceptation = 1;
		initialState = e1;
		alphabet.add(new Character(s));
		acceptStates.add(e2);
		states.add(e1);	
		states.add(e2);	
	}
	
	// User has the option to choose the token of this AFN when joining it to the AFNTotal	
	public void setToken(int token, String tokenString)
	{
		for(State e: this.acceptStates)
		{
			e.acceptation = token;
			e.tokenString = tokenString;
		}
	}	

	// Creates a basic AFN, from the AFN and symbol given. Used by gramatic rules.
	public static void crearBasico(AFN f, char s)
	{
		State e1 = new State();
		State e2 = new State();

		e1.transitions.add(new Transition(s, e2));
		e2.acceptation = 1;
		f.initialState = e1;
		f.alphabet.add(new Character(s));
		f.acceptStates.add(e2);
		f.states.add(e1);	
		f.states.add(e2);	
	}

	// Clears an AFN
	public void clear()
	{
		this.alphabet.clear();
		this.states.clear();	
		this.acceptStates.clear();
		this.initialState = null;
	}

	//Joins ANF2 with AFN1
	public AFN unir(AFN f2)
	{
		State e1, e2;
		e1 = new State();
		e2 = new State();
		e2.acceptation = 1;

		e1.transitions.add(new Transition('\u03B5', this.initialState));
		e1.transitions.add(new Transition('\u03B5', f2.initialState));

		for(State e: this.acceptStates)
		{
			e.transitions.add(new Transition('\u03B5', e2));
			e.acceptation = -1;
		}
		for(State e: f2.acceptStates)
		{
			e.transitions.add(new Transition('\u03B5', e2));
			e.acceptation = -1;
		}

		this.acceptStates.clear();
		this.acceptStates.add(e2);
		this.initialState = e1;
		this.alphabet.addAll(f2.alphabet);
		this.states.addAll(f2.states);
		this.states.add(e1);
		this.states.add(e2);
		f2 = null;

		return this;
	}
	
	//Concatenates ANF2 with AFN1
	public AFN concatenar(AFN f2)
	{
		for(State e: this.acceptStates)
		{
			for(Transition t: f2.initialState.transitions)
				e.transitions.add(t);
			e.acceptation = -1;
		}
		f2.states.remove(f2.initialState);
		this.acceptStates.clear();
		this.acceptStates.addAll(f2.acceptStates);
		this.alphabet.addAll(f2.alphabet);
		this.states.addAll(f2.states);
		f2 = null;
		
		return this;
	}

	public AFN cerraduraEstrella()
	{
		State e1 = new State();
		State e2 = new State();

		e1.transitions.add(new Transition('\u03B5', this.initialState));
		e1.transitions.add(new Transition('\u03B5', e2));	
		e2.acceptation = 1;
		for(State e: this.acceptStates)
		{
			e.transitions.add(new Transition('\u03B5', e2));	
			e.transitions.add(new Transition('\u03B5', this.initialState));	
			e.acceptation = -1;
		}
		this.initialState = e1;
		this.acceptStates.clear();
		this.acceptStates.add(e2);
		this.states.add(e1);
		this.states.add(e2);

		return this;
	}

	public AFN cerraduraMas()
	{
		State e1 = new State();
		State e2 = new State();

		e1.transitions.add(new Transition('\u03B5', this.initialState));
		e2.acceptation = 1;
		for(State e: this.acceptStates)
		{
			e.transitions.add(new Transition('\u03B5', e2));	
			e.transitions.add(new Transition('\u03B5', this.initialState));	
			e.acceptation = -1;
		}
		this.initialState = e1;
		this.acceptStates.clear();
		this.acceptStates.add(e2);
		this.states.add(e1);
		this.states.add(e2);

		return this;
	}	

	public AFN cerraduraOpcion()
	{
		State e1 = new State();
		State e2 = new State();

		e1.transitions.add(new Transition('\u03B5', this.initialState));
		e1.transitions.add(new Transition('\u03B5', e2));	
		e2.acceptation = 1;
		for(State e: this.acceptStates)
		{
			e.transitions.add(new Transition('\u03B5', e2));	
			e.acceptation = -1;
		}
		this.initialState = e1;
		this.acceptStates.clear();
		this.acceptStates.add(e2);
		this.states.add(e1);
		this.states.add(e2);

		return this;
	}

	// Joins the given AFN to the AFNTotal
	public AFN unirAFNT(AFN f1)
	{
		if(primerAFNAT == true)
		{
			State e1 = new State();

			e1.transitions.add(new Transition('\u03B5', f1.initialState));
			this.initialState = e1;
			this.alphabet.addAll(f1.alphabet);
			this.states.addAll(f1.states);
			this.states.add(this.initialState);
			this.acceptStates.addAll(f1.acceptStates);

			primerAFNAT = false;
		}
		else
		{
			this.initialState.transitions.add(new Transition('\u03B5', f1.initialState));
			this.alphabet.addAll(f1.alphabet);
			this.states.addAll(f1.states);
			this.acceptStates.addAll(f1.acceptStates);			
		}
		return this;
	}

	public Set<State> mover(State e, char s)
	{
		Set<State> r = new HashSet<>();

		r.clear();
		for(Transition transition: e.transitions)
			if(transition.minSymbol <= s && transition.maxSymbol >= s)
				r.add(transition.destination);

		return r;
	}

	public Set<State> mover(Set<State> a, char s)
	{
		Set<State> r = new HashSet<>();

		r.clear();
		for(State e: a)
			r.addAll(mover(e, s));

		return r;		
	}

	public Set<State> ir_A(State e, char s)
	{
		return cerraduraEpsilon(mover(e, s));
	}

	public Set<State> ir_A(Set<State> a, char s)
	{
		Set<State> r = new HashSet<State>();

		r.clear();
		for(State e: a)
			r.addAll(ir_A(e, s));

		if(!r.isEmpty())
			return r;
		else
			return null;
	}

	public Set<State> cerraduraEpsilon(State e)
	{
		Set<State> c = new HashSet<State>();
		Stack<State> stack = new Stack<>();
		State aux;

		c.clear();
		stack.push(e);
		while(!stack.empty())
		{
			aux = stack.pop();
			if(!c.contains(aux))
			{
				c.add(aux);
				for(Transition transition: aux.transitions)
				{
					if(transition.minSymbol == '\u03B5' && transition.maxSymbol == '\u03B5')
						stack.push(transition.destination);
				}
			}
		}
		return c;	
	}

	public Set<State> cerraduraEpsilon(Set<State> a)
	{
		Set<State> r = new HashSet<State>();
		State aux = new State();

		r.clear();
		for(State e: a)
		{
			r.addAll(cerraduraEpsilon(e));
		}

		return r;	
	}
	
	//Transforms this AFN to an AFD
	//Probably the hardest function I had to write for this program. 
	public AFD convertirAFD()
	{
		System.out.println();
		System.out.println("::: CONVIRTIENDO A AFD");
		AFD afd = new AFD();
		Set<StateSi> siSet = new HashSet<StateSi>();
		Stack<StateSi> stack = new Stack<StateSi>();
		StateSi initial = new StateSi();
		StateSi aux1, aux2;
		Set<State> aux3;
		boolean createNew;

		initial.setStates = cerraduraEpsilon(this.initialState);
		stack.push(initial);
		siSet.add(initial);
		
		while(!stack.empty())
		{
			aux1 = stack.pop();
			for(Character ch: this.alphabet)
			{
				//Program does not check for epsilon since it is not in the alphabet
				//If aux3 is an empty set then don't continue
				if((aux3 = ir_A(aux1.setStates, ch)) != null)
				{
					createNew = true;
					//Searches if any of the states in the set does already has the set of states in aux3
					//if so, then add a new transition from the current analyzed state to that state with the current symbol
					for(StateSi sir: siSet)
					{
						//System.out.println("Analizando: " + sir.id);
						if(sir.setStates.equals(aux3))
						{
							//System.out.println("true");
							aux1.transitionsSi.add(new TransitionSi(ch.charValue(), sir));
							createNew = false;
						}
					}
					//System.out.println();
					//If non state into the set had already aux3 then that means we need to create a new state with the set of states
					//contained into aux3
					if(createNew)
					{
						aux2 = new StateSi();
						aux2.setStates = aux3;
						aux1.transitionsSi.add(new TransitionSi(ch.charValue(), aux2));
						siSet.add(aux2);
						stack.push(aux2);
					}					
				}
			}
		}
		//Printing each StateSi with it's respective set of States		
		/*for(StateSi si: siSet)
		{
			System.out.print(si.id + ": ");
			for(State e: si.setStates)
				System.out.print(e.id + ", ");
			System.out.println();
		}
		System.out.println();*/

		//AFD BUILD STARTS FROM HERE
		afd.initialState = initial;
		afd.states = siSet;
		for(StateSi si: afd.states)
		{
			for(State e: si.setStates)
			{
				if(e.acceptation != -1)
				{
					si.acceptation = e.acceptation;
					si.tokenString = e.tokenString;
					afd.acceptStates.add(si);
				}
			}
		}
		for(Character ch: this.alphabet)
			afd.alphabet.add(ch);

		/*
		System.out.println("ESTADOS");
		for(StateSi sir: afd.states)
			System.out.println(sir.id);

		System.out.println("ESTADOS DE ACEPTACION - TOKEN");
		for(StateSi sir: afd.acceptStates)
			System.out.println(sir.id + ", " + sir.acceptation);

		System.out.println("ALFABETO");
		for(Character ch: afd.alphabet)
			System.out.println(ch);

		System.out.println("TRANSICIONES");
		for(StateSi si: afd.states)
			for(TransitionSi tsi: si.transitionsSi)
				System.out.println(si.id + "->" + tsi.symbol + ", " + tsi.destination.id);
		*/
		System.out.println("::: CONVERSION A AFD FINALIZADA");		
		return afd;	
	}
}