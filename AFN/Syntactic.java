import java.util.*;
import java.io.File;
import java.io.PrintWriter;

public class Syntactic
{
	public static Set<String> c = new HashSet<>();
	public static Set<Item> items = new HashSet<>();
	public static Set<SetItem> r = new HashSet<>();
	//Builds an LR(0) table using the Gramatic (List of Lists) previously built by recursive descent (Grammars of Grammars)
	public static void construirTablaLR0(Node lista, Set<String> vn) throws Exception
	{
		Node ladoIzquierdo = lista, ladoDerecho, aux, aux1, aux2, listaItem, auxItem1, auxItem2;
		File file = new File("LR0.txt");
		PrintWriter output = new PrintWriter(file);
		List<String> vtList, vnList;
		int noRule = 1, i;
		boolean match;
		//We obtain VT from VN
		Set<String> vt = new HashSet<>();
		int[] rules;
		String initialString;
		Item item = new Item();
		Set<Item> s0 = new HashSet<>();

		System.out.println();
		System.out.println("::: LISTA DE LISTAS");
		//Set flag to inital symbol
		lista.initial = true;
		initialString = lista.value;
		//Prints all items in this list of lists					
		aux = lista;
		while(aux != null)
		{
			System.out.print(aux.value + " -> ");
			aux1 = aux.right;
			while(aux1 != null)
			{
				aux2 = aux1;
				while(aux2 != null)
				{
					System.out.print(aux2.value + " ");
					//If the value of this node matches any of the symbols included in set Vn then this node is non-terminal
					for(String symbol: vn)
						if(aux2.value.equals(symbol))
							aux2.terminal = false;
					//Adding aux2 to VT set in case of being terminal
					if(aux2.terminal)
						vt.add(aux2.value);
					//Setting the flag initial if symbol of aux2 matches initialString
					if(aux2.value.equals(initialString))
						aux2.initial = true;

					aux2 = aux2.right;
				}
				System.out.print("\n      ");
				aux1 = aux1.down;	
			}
			System.out.println();
			aux = aux.down;	
		}
		System.out.println("::: LISTA DE LISTAS FINALIZADA");
		//VN is the set of non-terminal symbols
		System.out.println();
		System.out.println("<<< VN >>>");
		for(String symbol: vn)
			System.out.println(symbol);

		System.out.println();
		System.out.println("<<< VT >>>");
		vt.add("$");
		vt.remove("e");
		for(String symbol: vt)
			System.out.println(symbol);					
		
		System.out.println("<<< EXPANDIENDO LISTA >>>");
		aux = lista;
		listaItem = new Node();
		auxItem1 = listaItem;
		while(aux != null)
		{
			System.out.print(aux.value + " -> ");
			auxItem1.value = aux.value;
			auxItem1.terminal = aux.terminal; //Terminal flag will serve for further purposes
			aux1 = aux.right;
			while(aux1 != null)
			{
				auxItem2 = auxItem1; 
				aux2 = aux1;
				while(aux2 != null)
				{
					System.out.print(aux2.value + " ");
					auxItem2.right = new Node();
					auxItem2 = auxItem2.right;
					auxItem2.value = aux2.value;
					auxItem2.terminal = aux2.terminal;
					aux2 = aux2.right;
				}
				System.out.print("\n      ");
				aux1 = aux1.down;	
				if(aux1 != null)
				{
					auxItem1.down = new Node();
					auxItem1.down.value = auxItem1.value;
					auxItem1.down.terminal = auxItem1.terminal;
					auxItem1 = auxItem1.down;
				}
			}
			System.out.println();
			aux = aux.down;	
			if(aux != null)
			{
				auxItem1.down = new Node();
				auxItem1 = auxItem1.down;
			}
		}

		System.out.println();
		System.out.println("<<< LISTA EXPANDIDA >>>");
		auxItem1 = listaItem;
		while(auxItem1 != null)
		{
			auxItem2 = auxItem1;
			while(auxItem2 != null)
			{
				System.out.print(auxItem2.value + " ");
				auxItem2 = auxItem2.right;
			}
			System.out.println();
			auxItem1 = auxItem1.down;
		}

		System.out.println();
		System.out.println("<<< CERRADURA >>>");
		item.rule = 0;
		item.position = 0;
		cerraduraItem(listaItem, item);

		System.out.println();
		System.out.println("ITEMS");
		for(Item in: items)
			System.out.println("(" + in.rule + ", " + in.position + ")");

		/*
		System.out.println();
		System.out.println("<< MOVER >>");
		s0 = moverItem(listaItem, items, "E");
		for(Item in: s0)
			System.out.println("(" + in.rule + ", " + in.position + ")");

		System.out.println();
		System.out.println();
		System.out.println("<< IR_A >>");
		r.add(new SetItem(ir_AItem(listaItem, items, "E")));
		for(SetItem si: r)
			for(Item in: si.itemList)
				System.out.println("(" + in.rule + ", " + in.position + ")");
		*/
		System.out.println();
		System.out.println("::: CONSTUYENDO TABLA LR0");

		vtList = new ArrayList<>(vt);
		rules = new int[vtList.size()];
		Collections.sort(vtList);

		vnList = new ArrayList<>(vn);
		Collections.sort(vnList);

		//Printing number of non-terminal symbols
		output.println(vn.size());
		//Printing number of terminal symbols (i.e. columns of the table)
		output.println(vt.size());
		//Printing terminal symbols row
		output.print("   ");
		for(String s: vtList)
			output.print(s + "   ");
		output.println();
		
		output.close();
		System.out.println("::: TABLA LR0 CONSTRUIDA CON EXITO");
	}

	public static Set<Item> ir_AItem(Node listaItem, Set<Item> itemS, String string)
	{
		Set<Item> set = new HashSet<>();

		items = moverItem(listaItem, itemS, string);
		for(Item it: items)
		{
			set.add(new Item(it.rule, it.position));
			System.out.println("ITEM (" + it.rule + ", " + it.position + ")");
		}
		items.clear();
		for(Item it: set)
		{
			cerraduraItem(listaItem, it);
			System.out.println();
			System.out.println("LO ITEMS");
			for(Item in: items)
				System.out.println("ITEM: (" + in.rule + ", " + in.position + ")");
		}

		return items;
	}	

	public static void cerraduraItem(Node listaItem, Item item)
	{
		//Set<Item> c = new HashSet<>();
		Node position, rule;
		int i = 0;

		//Rule 0 is the first rule as Item 0 is the first Item
		//Looking for the desired rule. Naive aproximation, not covering all cases
		rule = listaItem;
		while(rule != null && i < item.rule)
		{
			i++;
			rule = rule.down;
		}
		//Looking for the desired position of the dot
		i = 0;
		position = rule.right;
		while(position.right != null && i < item.position)
		{
			position = position.right;
			i++;
		}
		//Si el símbolo es terminal o si esta al final de la regla e.g. E -> E + T.
		if(position.terminal || position == null)
		{
			System.out.println();
			System.out.println("Terminal: " + position.value);
			for(Item in: items)
				if(in.rule == item.rule && in.position == item.position)
					return ;
			System.out.println("Adding Item(" + item.rule + ", " + item.position);
			System.out.println("Regla actual: " + rule.value + " -> " + rule.right.value + " ");
			items.add(item);
			return ;
			//return c;
		}
		System.out.println();
		System.out.println("NO Terminal: " + position.value);
		for(Item in: items)
			if(in.rule == item.rule && in.position == item.position)
				return ;
			
		System.out.println("Adding Item(" + item.rule + ", " + item.position + ")");
		items.add(item);
		System.out.print("Regla actual: " + rule.value + " -> " + rule.right.value + " ");
		if(rule.right.right != null)
			System.out.println(rule.right.right.value);
		else
			System.out.println();
		
		i = 0;
		rule = listaItem;
		while(true)
		{
			while(rule != null)
			{
				if(rule.value.equals(position.value))
				{
					if(i == item.rule)
					{
						i++;
						rule = rule.down;
					}
					else
					{
						break;
					}
				}
				else
				{
					i++;
					rule = rule.down;	
				}	
			}
			if(rule == null)
				break;
			System.out.println();
			System.out.println("Buscando: " + rule.value + " -> " + rule.right.value + "\ni = " + i);
			cerraduraItem(listaItem, new Item(i, 0));
			i++;
			rule = rule.down;
		}
		
		return ;
	}
	
	public static Set<Item> moverItem(Node listaItem, Set<Item> items, String string)
	{
		Set<Item> set = new HashSet<>();
		Node position, rule;
		int i = 0, j = 0;

		for(Item item: items)
		{
			//Rule 0 is the first rule as Item 0 is the first Item
			//Looking for the desired rule. Naive aproximation, not covering all cases
			i = 0;
			rule = listaItem;
			while(rule != null && i < item.rule)
			{
				i++;
				rule = rule.down;
			}
			//Looking for the desired position of the dot
			j = 0;
			position = rule.right;
			while(position.right != null && j < item.position)
			{
				position = position.right;
				j++;
			}

			if(position.value.equals(string))
				set.add(new Item(i, j + 1));
		}
		return set;
	}

	//Builds an LL1 using the Gramatic (List of Lists) previously built by recursive descent (Grammars of Grammars)
	public static void construirTablaLL1(Node lista, Set<String> vn) throws Exception
	{
		Node ladoIzquierdo = lista, ladoDerecho, aux, aux1, aux2;
		Set<String> c;
		File file = new File("LL1.txt");
		PrintWriter output = new PrintWriter(file);
		List<String> vtList;
		int noRule = 1, i;
		boolean match;
		//We obtain VT from VN
		Set<String> vt = new HashSet<>();
		int[] rules;
		String initialString;

		System.out.println();
		System.out.println("::: LISTA DE LISTAS");
		//Set flag to inital symbol
		lista.initial = true;
		initialString = lista.value;
		//Prints all items in this list of lists					
		aux = lista;
		while(aux != null)
		{
			System.out.print(aux.value + " -> ");
			aux1 = aux.right;
			while(aux1 != null)
			{
				aux2 = aux1;
				while(aux2 != null)
				{
					System.out.print(aux2.value + " ");
					//If the value of this node matches any of the symbols included in set Vn then this node is non-terminal
					for(String symbol: vn)
						if(aux2.value.equals(symbol))
							aux2.terminal = false;
					//Adding aux2 to VT set in case of being terminal
					if(aux2.terminal)
						vt.add(aux2.value);
					//Setting the flag initial if symbol of aux2 matches initialString
					if(aux2.value.equals(initialString))
						aux2.initial = true;

					aux2 = aux2.right;
				}
				System.out.print("\n      ");
				aux1 = aux1.down;	
			}
			System.out.println();
			aux = aux.down;	
		}
		System.out.println("::: LISTA DE LISTAS FINALIZADA");
		//VN is the set of non-terminal symbols
		System.out.println();
		System.out.println("<<< VN >>>");
		for(String symbol: vn)
			System.out.println(symbol);

		System.out.println();
		System.out.println("<<< VT >>>");
		vt.add("$");
		vt.remove("e");
		for(String symbol: vt)
			System.out.println(symbol);					

		/*This part of the code was intended to prove First() and Follow() functions, with the grammar given by the teacher, i.e. just for LL1 grammars:
		E -> T E';     E' -> + T E' | - T E' | e;   T -> F T';   T' -> * F T' | / F T' | e;   F -> ( E ) | NUM; 
		System.out.println();
		System.out.println("<<< FIRST >>>");
		c = Syntactic.first(lista, lista.right);
		for(String s: c)
			System.out.println(s);

		System.out.println();
		System.out.println("<<< FOLLOW >>>");
		c = Syntactic.follow(lista, lista.down.down.down.down);
		for(String s: c)
			System.out.println(s);
		*/
			
		System.out.println();
		System.out.println("::: CONSTUYENDO TABLA LL1");

		vtList = new ArrayList<>(vt);
		rules = new int[vtList.size()];
		Collections.sort(vtList);
		//Printing number of non-terminal symbols
		output.println(vn.size());
		//Printing number of terminal symbols (i.e. columns of the table)
		output.println(vt.size());
		//Printing terminal symbols row
		output.print("   ");
		for(String s: vtList)
			output.print(s + "   ");
		output.println();
		//Filling information for non-terminal symbols
		while(ladoIzquierdo != null)
		{	
			//Printing left side
			output.print(ladoIzquierdo.value + "  ");
			//Filling this row with -1
			for(i = 0; i < rules.length; i++)
				rules[i] = -1;	
			ladoDerecho = ladoIzquierdo.right;
			while(ladoDerecho != null)
			{
				//System.out.println("Calling LL1_1First(" + ladoDerecho.value + ")");
				c = first(lista, ladoDerecho);
				if(c.contains("e"))
				{
					c.remove("e");
					c.addAll(follow(lista, ladoIzquierdo));
				}
				i = 0;
				for(String ss: vtList)
				{
					for(String s: c)
					{
						if(s.equals(ss))
							rules[i] = noRule;
					}
					i++;
				}
				noRule++;
				ladoDerecho = ladoDerecho.down;			
			}
			//Printing row over the table
			for(i = 0; i < rules.length; i++)
				output.print(rules[i] + "  ");
			output.println();
			//Moving to the next rule
			ladoIzquierdo = ladoIzquierdo.down;	
		}
		//Filling information for terminal symbols
		for(String s: vtList)
		{
			for(i = 0; i < rules.length; i++)
				rules[i] = -1;
			output.print(s + " ");
			
			i = 0;
			for(String ss: vtList)
			{
				if(s.equals(ss))
				{
					if(ss.equals("$"))
						rules[i] = -2;		
					else
						rules[i] = 0;
				}
				i++;
			}
			for(i = 0; i < rules.length; i++)
				output.print(rules[i] + "  ");
			output.println();
		}
		output.close();
		System.out.println("::: TABLA LL1 CONSTRUIDA CON EXITO");
	}

	//First function should always receive Right Sides. First function is used to build the LL1 table
	public static Set<String> first(Node lista, Node ladoDerecho)
	{
		Set<String> c = new HashSet<>();			
		Node n = lista;
		Node aux;

		//System.out.println("Analizando lado derecho: " + ladoDerecho.value);
		if(ladoDerecho.terminal)
		{
			//System.out.println("Terminal");
			c.add(ladoDerecho.value);
			return c;
		}
		else
		{
			//Explore all left sides searching for ladoDerecho
			while(n != null)
			{	
				if(n.value.equals(ladoDerecho.value))
					break;
				n = n.down;
			}
			//System.out.println("Lado derecho encontrado: " + n.value);
			//Now n points to ladoDerecho position in list of left sides
			n = n.right;
			aux = n;
			while(aux != null)
			{
				c.addAll(first(lista, aux));
				aux = aux.down;
			}
			if(c.contains("e") && ladoDerecho.right != null)
			{
				c.remove("e");
				c.addAll(first(lista, ladoDerecho.right));
			}
			return c;		
		}
	}

	//Follow always receives non-terminal symbols, that's why parameter is named symbolNT
	//Inital symbol will contain "$" symbol in its follow
	//Folloe function is used to build LL1 table
	public static Set<String> follow(Node lista, Node symbolNT)
	{
		Set<String> c = new HashSet<>();
		Node aux, aux1, aux2;

		if(symbolNT.initial)
			c.add("$");

		//aux moves through all left sides
		//aux1 moves through all productions of aux
		//aux2 moves through all the right side of aux1
		aux = lista;
		while(aux != null)
		{
			aux1 = aux.right;
			while(aux1 != null)
			{
				aux2 = aux1;
				while(aux2 != null)
				{
					if(aux2.value.equals(symbolNT.value))
					{
						//We found symbolNT but not at the end of the production then, we doo First of what comes next to symbolNT
						if(aux2.right != null)
						{
							//System.out.println("Calling 1First(" + aux2.right.value + ")");
							c.addAll(first(lista, aux2.right));	
							if(c.contains("e"))
							{
								c.remove("e");
								//????Not in pseudocode????
								if(!aux2.value.equals(aux.value))
								{
									//System.out.println("Calling 2Follow(" + aux.value + ")");
									c.addAll(follow(lista, aux));				
								}
							}					
						}
						else
						{
							//If this node is a node containing symbolNT and is the last on the rule and it is not at the same time equal to 
							//its left side, then calculate Follow of its left side.
							//For example: E' -> + T E'
							//If we need to calculate Follow(E') (original follow), then we need to do First of whatever comes next to E'
							//but because there's nothing else we need to do Follow(E') but because it is equal to the
							//original Follow, then we do not calculate it. 
							if(!aux2.value.equals(aux.value))
							{
								//System.out.println("Calling 3Follow(" + aux.value + ")");
								c.addAll(follow(lista, aux));				
							}
						}
					}		 				
					aux2 = aux2.right;
				}
				aux1 = aux1.down;	
			}
			aux = aux.down;	
		}
		return c;
	}

	/////////////////////////////////////////////////////////GRAMATICA DE GRAMATICAS

	public static boolean G(Node l, Set<String> vn)
	{
		//System.out.println("G");
		if(ListaReglas(l, vn))
			return true;
		return false;
	}

	public static boolean ListaReglas(Node l, Set<String> vn)
	{
		//System.out.println("ListaReglas");
		Token t;
		Node l2 = new Node();

		if(Regla(l, vn))
		{
			t = Lexic.getToken();
			if(t.lexema.equals(Token.PUNTOyCOMA))
				if(ListaReglasP(l2, vn))
				{
					if(l2.value == null)
						l2 = null;
					l.down = l2;

					return true;
				}
		}
		return false;
	}

	public static boolean ListaReglasP(Node l, Set<String> vn)
	{
		//System.out.println("ListaReglasP");
		Token t;
		int indexLexico = Lexic.getCurrentIndex();
		Node l2 = new Node();

		if(Regla(l, vn))
		{
			t = Lexic.getToken();
			if(t.lexema.equals(Token.PUNTOyCOMA))
				if(ListaReglasP(l2, vn))
				{
					if(l2.value == null)
						l2 = null;
					l.down = l2;

					return true;
				}
			return false;
		}
		Lexic.setCurrentIndex(indexLexico);

		return true;
	}

	public static boolean Regla(Node l, Set<String> vn)
	{
		//System.out.println("Regla");
		Token t;
		Node l2 = new Node();

		if(LadoIzquierdo(l, vn))
		{
			//System.out.println("Left Side asigned: " + l.value);
			t = Lexic.getToken();
			if(t.lexema.equals(Token.FLECHA))
				if(ListaLadosDer(l2))
				{
					if(l2.value == null)
						l2 = null;
					l.right = l2;

					return true;
				}
		}
		return false;
	}

	public static boolean LadoIzquierdo(Node l, Set<String> vn)
	{
		//System.out.println("LadoIzquierdo");
		Token t;

		t = Lexic.getToken();
		//If token is a symbol, I.e "10"
		if(t.token == 10)
		{
			l.value = t.lexema;
			//Every non-terminal symbol appears at least one time at the left side of a rule (?)
			l.terminal = false;
			vn.add(l.value);
			return true;
		}
		return false;	
	}

	public static boolean ListaLadosDer(Node l)
	{
		//System.out.println("ListaLadosDer");
		Node l2 = new Node();

		if(LadoDerecho(l))
			if(ListaLadosDerP(l2))
			{
				if(l2.value == null)
					l2 = null;
				l.down = l2;

				return true;
			}
		return false;
	}

	public static boolean ListaLadosDerP(Node l)
	{
		//System.out.println("ListaLadosDerP");
		Token t;
		Node l2 = new Node();

		t = Lexic.getToken();
		if(t.lexema.equals(Token.OR))
		{
			if(LadoDerecho(l))
			{
				if(ListaLadosDerP(l2))
				{
					if(l2.value == null)
						l2 = null;
					l.down = l2;

					return true;
				}
			}
			return false;
		}
		Lexic.rejectToken();

		return true;
	}

	public static boolean LadoDerecho(Node l)
	{
		//System.out.println("LadoDerecho");
		Token t;
		Node l2 = new Node();

		t = Lexic.getToken();

		if(t.token == 10)
		{
			l.value = t.lexema;
			//System.out.println("Valor asignado a lado derecho: " + l.value);
			if(LadoDerechoP(l2))
			{
				if(l2.value == null)
					l2 = null;
				l.right = l2;

				return true;
			}
		}
		return false;
	}

	public static boolean LadoDerechoP(Node l)
	{
		//System.out.println("LadoDerechoP");
		Token t;
		Node n = new Node();

		t = Lexic.getToken();
		
		if(t.token == 10)
		{
			l.value = t.lexema;
			if(LadoDerechoP(n))
			{
				if(n.value == null)
					n = null;
				l.right = n;
				
				return true;
			}
			return false;
		}
		Lexic.rejectToken();

		return true;
	}

	////////////////////////////////////////////////////////GRAMATICA PARA EXPRESIONES REGULARES
	public static boolean E(AFN f)
	{
		if(T(f))
			if(Ep(f))
				return true;
		return false;
	}

	public static boolean Ep(AFN f)
	{
		Token tok;
		AFN f2 = new AFN();

		tok = Lexic.getToken();
		if(tok.lexema.equals(Token.OR))
		{
			if(T(f2))
			{
				f.unir(f2);
				if(Ep(f))
					return true;
			}
			return false;
		}
		Lexic.rejectToken();

		return true;
	}

	public static boolean T(AFN f)
	{
		if(C(f))
			if(Tp(f))
				return true;
		return false;
	}

	public static boolean Tp(AFN f)
	{
		Token tok;
		AFN f2 = new AFN();

		tok = Lexic.getToken();
		if(tok.lexema.equals(Token.CONCAT))
		{
			if(C(f2))
			{
				f = f.concatenar(f2);
				if(Tp(f))
					return true;
			}
			return false;
		}
		Lexic.rejectToken();

		return true;
	}

	public static boolean C(AFN f)
	{
		if(F(f))
			if(Cp(f))
				return true;
		return false;
	}

	public static boolean Cp(AFN f)
	{
		Token tok;

		tok = Lexic.getToken();
		if(tok.lexema.equals(Token.CERR_EST))
			f.cerraduraEstrella();
		else if(tok.lexema.equals(Token.CERR_MAS))
			f.cerraduraMas();
		else if(tok.lexema.equals(Token.CERR_OPC))
			f.cerraduraOpcion();
		else
		{
			Lexic.rejectToken();
			return true;
		}

		if(Cp(f))
			return true;

		return false;
	}

	public static boolean F(AFN f)
	{
		Token tok;
		State auxState;
		char auxMinSymbol;

		tok = Lexic.getToken();
		if(tok.lexema.equals(Token.PAR_I))
		{
			if(E(f))
			{
				tok = Lexic.getToken();
				if(tok.lexema.equals(Token.PAR_D))
					return true;
			}
		}
		else if(tok.lexema.charAt(0) == '/')
		{
			//To be able to accept the whitespace char
			if(tok.lexema.charAt(1) == '_')
				AFN.crearBasico(f, ' ');
			else	
				AFN.crearBasico(f, tok.lexema.charAt(1));
			
			return true;	
		}
		//Checking for [symbol-symbol]
		else if(tok.lexema.equals(Token.CORCHETE_I))
		{
			tok = Lexic.getToken();
			//If we see a / then the token is a symbol or number or whatever
			if(tok.lexema.charAt(0) == '/')
			{
				AFN.crearBasico(f, tok.lexema.charAt(1));
				tok = Lexic.getToken();
				if(tok.lexema.equals(Token.GUION))
				{
					tok = Lexic.getToken();
					if(tok.lexema.charAt(0) == '/')
					{
						//We know that is only one transition but for is needed to get iterator over the only one transition.
						//In this for we recover the state e2 to which e1 points to
						//And the symbol i.e. [<<<symbol>>>-symbol]
						for(Transition t: f.initialState.transitions)
						{
							auxState = t.destination;
							auxMinSymbol = t.minSymbol;

							//ONLY because we know there will be just ONE iteration we move all the code above here
							//This is to avoid compilation errors
							f.initialState.transitions.clear();
							//tok.lexema.charAt(1) now contains the maxSymbol of this transition i.e. [symbol-<<<symbol>>>]
							f.initialState.transitions.add(new Transition(auxMinSymbol, tok.lexema.charAt(1), auxState));
							//Add maxSymbol to AFN alphabet
							f.alphabet.add(tok.lexema.charAt(1));
							//Adding all chars in between minSymbol and maxSymbol for conversion to AFD
							auxMinSymbol++;
							for(char c = auxMinSymbol; c < tok.lexema.charAt(1); c++)
								f.alphabet.add(c);
						}
						
						tok = Lexic.getToken();
						if(tok.lexema.equals(Token.CORCHETE_D))
							return true;
					}
				}				
			}
		}
		return false;
	}

	////////////////////////////////////////GRAMATICA PARA EXPRESIONES ARITMETICAS
	
	public static boolean E_A(DoubleJames v1, StringBuilder cadena)
	{
		if(T_A(v1, cadena))
			if(Ep_A(v1, cadena))
				return true;
		return false;
	}

	public static boolean Ep_A(DoubleJames v1, StringBuilder cadena)
	{
		DoubleJames v2 = new DoubleJames(0);
		Token tok;
		tok = Lexic.getToken();

		if(tok.lexema.equals(Token.MAS))
		{
			if(T_A(v2, cadena))
			{
				v1.value = v1.value + v2.value;
				cadena.append("+ ");

				if(Ep_A(v1, cadena))
					return true;
			}
			return false;
		}
		else if(tok.lexema.equals(Token.MENOS))
		{
			if(T_A(v2, cadena))
			{
				v1.value = v1.value - v2.value;
				cadena.append("- ");

				if(Ep_A(v1, cadena))
					return true;
			}
			return false;
		}
		Lexic.rejectToken();

		return true;
	}

	public static boolean T_A(DoubleJames v1, StringBuilder cadena)
	{
		if(F_A(v1, cadena))
		  	if(Tp_A(v1, cadena))
				return true;
		return false;
	}

	public static boolean Tp_A(DoubleJames v1, StringBuilder cadena)
	{
		DoubleJames v2 = new DoubleJames(0);
		Token tok;
		tok = Lexic.getToken();

		if(tok.lexema.equals(Token.PROD))
		{
			if(F_A(v2, cadena))
			{
				v1.value = v1.value * v2.value;
				cadena.append("* ");

				if(Tp_A(v1, cadena))
					return true;
			}
			return false;
		}
		else if(tok.lexema.equals(Token.DIV))
		{
			if(F_A(v2, cadena))
			{
				v1.value = v1.value / v2.value;
				cadena.append("/ ");

				if(Tp_A(v1, cadena))
					return true;
			}
			return false;	
		}
		Lexic.rejectToken();
		return true;	
	}

	public static boolean F_A(DoubleJames v1, StringBuilder cadena)
	{
		Token tok;
		tok = Lexic.getToken();

		if(tok.lexema.equals(Token.PAR_I))
		{
			if(E_A(v1, cadena))
			{
				tok = Lexic.getToken();
				if(tok.lexema.equals(Token.PAR_D))
					return true;
			}
			return false;
		}
		else if(tok.token == 10 || tok.token == 20)
		{
			v1.value = Double.parseDouble(tok.lexema);
			cadena.append(tok.lexema + " ");
			return true;
		}
		return false;
	}
}