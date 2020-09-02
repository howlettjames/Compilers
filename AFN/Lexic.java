import java.io.File;
import java.util.*;
import java.io.PrintWriter;

public class Lexic
{
	private static AFD afd;
	private static String cadena;
	private static int index = 0;
	private static int indexReject = 0;
	private static int indexLastAccept = 0;
	private static StateSi currentState;
	public static boolean usingERLanguage = false;

	public Lexic()
	{

	}

	//Reads the file in which is contained a series of Regular Expressions each with its associated Token,
	//builds a AFN from each one of them and joins them up into one AFN and then turns it into an AFD, the one that
	//the Lexic class will be using to get Tokens and sends it to a file always called AFD.txt to save it. 
	//For this purpose the Lexic class first has to read the table where 
	//the AFD(table) that recognizes Regular Expressions lies, this table is contained into a file called ADF_ER.txt  
	public static void leerTablaConstructor(String tablaConstructor) throws Exception
	{	
		afd = leerTabla("AFD_ER.txt");
		AFD afdC;
		AFN afnAux = new AFN();
		AFN afnT = new AFN();
		File file = new File(tablaConstructor);
		Scanner input = new Scanner(file);
		AFN.primerAFNAT = true;
		usingERLanguage = true;
		
		while(input.hasNext())
		{
			//Load the Lexic analyzer with the current regular expression
			Lexic.cargarCadena(input.next());
			//By calling E() we ensure the regular expression readed is correctly writed and construct the AFN associated to that RE.
			if(Syntactic.E(afnAux))
			{
				//Set the token of this AFN constructed by the grammar
				afnAux.setToken(input.nextInt(), input.next());
				afnT.unirAFNT(afnAux);
				//Creating a new empty AFN for futher use
				afnAux = new AFN();
			}
			else
				System.out.println("Invalid format at: " + tablaConstructor);
		}
		//Free file
		input.close();
		usingERLanguage = false;

		afdC = afnT.convertirAFD();
		try
		{	
			afdC.enviarTabla();
		}  
		catch(Exception e)
		{
			System.out.println("Incapaz de enviar tabla a archivo AFD.txt");
		}
	}
	
	//Function getToken() gets Tokens from the string loaded into the Lexic class using whether the AFD_ER or 
	//the AFD constructed from the file AFD.txt. This is indicated by the variable called usingERLanguage.		
	public static Token getToken()
	{
		boolean ok = false;
		StateSi lastAcceptState = null;
		StringBuilder lexem = new StringBuilder();
		String lexema;
		char ch;
		indexReject = index;

		currentState = afd.initialState;

		if(index == cadena.length())
			return new Token("FIN", 0, null);

		while(index < cadena.length())
		{
			ok = false;
			ch = cadena.charAt(index);
			//CHECK IF THERE'S ANY TRANSITION FROM CURRENT STATE TO ANOTHER
			for(TransitionSi tsi: currentState.transitionsSi)
			{
				//S MEANS ANY SYMBOL, WE DON'T NEED TO CHECK FOR NOTHING
				//When we are using the table that recognizes regular expressions
				//we need to accept ANY symbol if it is preceeded by a '/'(not sure it program does always)	
				//besides recognizing operators like Kleen closure
				if(usingERLanguage)
				{
					if(tsi.symbol == 'S')
					{
						currentState = tsi.destination;
						ok = true;
						lexem.append(ch);
						break;		
					}
					else if(ch == tsi.symbol)
					{
						currentState = tsi.destination;
						ok = true;
						lexem.append(ch);
						break;
					}
				}
				//We are not analyzing a regular expression so we only rely on the transitions given by the
				//regular expression previously analyzed and converted to table, this means we don't accept any
				//symbol like in above case
				else
				{
					if(ch == tsi.symbol)
					{
						currentState = tsi.destination;
						ok = true;
						lexem.append(ch);
						break;
					}
				}	
			}
			//IF THERE WAS A TRANSITION
			if(ok)
			{
				//IF CURRENT STATE IS ACCEPTED STATE, SAVE THE INDEX AND STATE
				if(currentState.acceptation != -1)
				{
					indexLastAccept = index;
					lastAcceptState = currentState;
				}
			}
			else
			{
				//IF THERE HAS BEEN AN ACCEPTED STATE THROUGH THE ANALISYS START FROM THAT INDEX PLUS ONE
				if(lastAcceptState != null)
					index = ++indexLastAccept;
				//IF THERE HADN'T BEEN ANY ACCEPTED STATE THROUGH THE ANALISYS THEN THE STRING IS NOT RECOGNIZED "ERROR"
				else
				{
					index++;
					return new Token("ERROR", -1, null);
				}
				break;
			}
			index++;	
		}
		//There have been transitions but any of them were Accepted therefore return ERROR.
		//This is likely to happen at Arithmetic Expressions where input looks like this "1 + _"
		//Assuming "_" is a whitespace char.
		if(lastAcceptState == null)
		{
			return new Token("ERROR", -1, null);
		}	
		//Do this to leave the lexem without whitespaces
		lexema = lexem.toString();

		return new Token(lexema.trim(), lastAcceptState.acceptation, lastAcceptState.tokenString);
	}
	
 	public static void rejectToken()
 	{
 		index = indexReject;
 	}	

	public static int getCurrentIndex()
	{
		return index;
	}

	public static void setCurrentIndex(int i)
	{
		index = i;
	}
	
	public static boolean analizadorLL1(Node lista) throws Exception
	{
		System.out.println();
		System.out.println("::: LEYENDO TABLA ANALIZADOR LL1");
		afd = leerTabla("AFD.txt");
		File file = new File("LL1.txt");
		Scanner input = new Scanner(file);
		Stack<String> stack = new Stack<>();
		List<String> vtList = new ArrayList<>();
		Set<String> vn = new HashSet<>();
		int matches, numberTerminalSymbols, numberNonTerminalSymbols, i, j, k, action;
		numberNonTerminalSymbols = Integer.parseInt(input.nextLine());
		numberTerminalSymbols = Integer.parseInt(input.nextLine());
		String[][] table = new String[numberTerminalSymbols + numberNonTerminalSymbols][numberTerminalSymbols + 1];
		Token tok = null;
		boolean found, flagPop;
		Node aux, aux1, currentRightSide;
		String[] ladoDerechoArray = new String[20];

		System.out.println("<<<NUMBER OF VN>>>");
		System.out.println(numberNonTerminalSymbols);
		System.out.println("<<<NUMBER OF VT>>>");
		System.out.println(numberTerminalSymbols);

		for(matches = 0; matches < numberTerminalSymbols; matches++)
			vtList.add(input.next());

		Collections.sort(vtList);
		System.out.println("<<<VT>>>");
		for(String s: vtList)
			System.out.println(s);

		//Obtaining table from text into table[][] array
		for(i = 0; i < numberTerminalSymbols + numberNonTerminalSymbols; i++)
		{
			j = 0;
			table[i][j++] = input.next();	
			for(String s: vtList)
			{
				table[i][j] = input.next();
				j++;			
			}	
		}
		
		System.out.println("<<<TABLE>>>");	
		for(i = 0; i < numberTerminalSymbols + numberNonTerminalSymbols; i++)
		{
			for(j = 0; j < numberTerminalSymbols + 1; j++)
				System.out.print(table[i][j] + " ");
			System.out.println();
		}

		System.out.println();
		System.out.println("::: TABLA ANALIZADOR LL1 LEIDA CON EXITO");
		System.out.println();
		System.out.println("::: INICIANDO ANALISIS DE CADENA");
		stack.push("$");
		//Pushing first symbol in the grammar into the stack
		stack.push(lista.value);
		flagPop = true;
		while(true)
		{
			//Obtaining token from input string if there has been a Pop action or is the first iteration.
			if(flagPop)	
			{
				tok = Lexic.getToken();
				System.out.println("Obtained token: " + tok.string);
				flagPop = false;
			}
			if(tok.token == -1)
				return false;
			if(!tok.lexema.equals("FIN"))
			{
				//Searching for next symbol to analyze in the table array. The next symbol to analyze is the symbol at the top of
				//the stack.
				System.out.println();
				System.out.println();
				System.out.println("Peek: " + stack.peek());
				for(i = 0; i < numberTerminalSymbols + numberNonTerminalSymbols; i++)
					if(stack.peek().equals(table[i][0]))
						break;

				//J starts at "1" because "0" position corresponds to the symbol not the action to be taken in the current row (i position)
				j = 1;
				found = false;
				//Searching if there's any movement for the token being analyzed
				for(String s: vtList)
				{
					if(s.equals(tok.string))
					{
						found = true;
						break;
					}
					j++;
				}
				if(found)
				{
					action = Integer.parseInt(table[i][j]);
					System.out.println("Action: " + action);
					//If action is equal to "0" this represent that we have to do a POP
					if(action == 0)
					{
						System.out.println("Pop action");
						flagPop = true;
						System.out.println("Popped element: " + stack.pop());
					}
					//If action is equal to "-1" then there is an Error.
					else if(action == -1)
					{
						System.out.println("::: ANALISIS DE CADENA TERMINADO -> NO ACEPTADA");	
						return false;
					}
					//If action is equal to "-2" this represents string ACCEPTED
					else if(action == -2)
					{
						System.out.println("::: ANALISIS DE CADENA TERMINADO -> ACEPTADA");
						return true;
					}
					//The only possibility left is any other number diferent than 0, -1 or -2. The number given is the
					//number of rule from the grammar (list of lists) that we have to apply.
					else
					{
						//Searching through the list of lists (grammar) the rule we have to apply (the right side)
						aux = lista;
						currentRightSide = null;
						i = 0;
						while(aux != null && i != action)
						{
							aux1 = aux.right;
							currentRightSide = aux1;
							i++;
							while(aux1.down != null && i != action)
							{
								aux1 = aux1.down;
								currentRightSide = aux1;
								i++;	
							}
							aux = aux.down;	
						}
						System.out.println("Popped element: " + stack.pop());
						//If currentRightSIde is epsilon then just do a pop action over the stack
						if(currentRightSide.value.equals("e"))
						{
							System.out.println("Epsilon");
						}
						else
						{
							//Saving Right Side into an array for later purpose
							k = 0;
							System.out.print("Right side: ");
							while(currentRightSide != null)
							{
								ladoDerechoArray[k++] = currentRightSide.value;
								System.out.print(currentRightSide.value + ", ");
								currentRightSide = currentRightSide.right;
							}
							//Now we save the elements in the Right Side into the stack but as you can see in <reverse order>
							k--;
							while(k >= 0)
								stack.push(ladoDerechoArray[k--]);
						}
					}	
				}	
				else
					System.out.println("Not found");	
			}
			else
				break;
		}
		System.out.println("::: ANALISIS DE CADENA TERMINADO");
		return false;
	}

	//Reads an AFD (table format) and creates the AFD that the Lexic analyzer will use for function getToken().
	public static AFD leerTabla(String filetxt) throws Exception
	{
		afd = new AFD();
		File file = new File(filetxt);
		Scanner input = new Scanner(file);
		int numberCharacters, numberStates, aux;
		StateSi siAux;
		List<StateSi> siList = new ArrayList<>();

		System.out.println();
		System.out.println("::: LEYENDO TABLA > " + filetxt);
		//READING NUMBER OF CHARACTERS IN THIS TABLE
		numberCharacters = input.nextInt();
		//System.out.println("NUMBER OF CHARS");
		//System.out.println(numberCharacters);
		//System.out.println();
		input.nextLine();

		//READING NUMBER OF STATES IN THIS TABLE
		numberStates = input.nextInt();
		//System.out.println("NUMBER OF STATES");
		//System.out.println(numberStates);
		//System.out.println();
		input.nextLine();

		//READING ALL CHARACTERS IN THIS TABLE AND CREATING A LIST FOR FURTHER USE
		String lineOfChars = input.nextLine();
		//System.out.println(lineOfChars);
		for(int i = 3, match = 0; match < numberCharacters; i += 3) 
		{
			afd.alphabet.add(new Character(lineOfChars.charAt(i)));
			//System.out.println("Match: " + lineOfChars.charAt(i));
			match++;
		}

		//DO NOT UNCOMMENT this before read
		//This original for worked weel instead of the one above, the problem is that when we're trying to read a table with
		//the blank space as a symbol, this for does not work.
		/*for(int i = 0; i < numberCharacters; i++)
			afd.alphabet.add(new Character(input.next().charAt(0)));
		input.nextLine();*/
		List<Character> charsList = new ArrayList<>(afd.alphabet);
		Collections.sort(charsList);

		//PRINTING ALL CHARACTERS IN THIS AFD ALPHABET
		/*System.out.println("ALPHABET");
		for(Character ch: afd.alphabet)
			System.out.print(ch + ", ");
		System.out.println();
		System.out.println();*/

		//CREATING ALL STATES WITH THEIR TOKEN (ACCEPTATION) AND ADDING ACCEPTATION STATES TO THE AFD.ACCEPTSTATES SET
		siList.add(new StateSi(input.nextInt()));
		afd.initialState = siList.get(0);
		afd.states.add(afd.initialState);
		afd.initialState.acceptation = -1;
		input.nextLine();
		for(int i = 0; i < numberStates - 1; i++)
		{
			siAux = new StateSi(input.nextInt());
			//ADDING TOKEN FOR THIS STATE
			for(int j = 0; j < numberCharacters; j++)
				input.next();
			siAux.acceptation = input.nextInt();
			siAux.tokenString = input.next();
			siList.add(siAux);
			afd.states.add(siAux);
			//ADDING STATE TO THE ACCEPTED STATES SET IF SO
			if(siAux.acceptation != -1)
				afd.acceptStates.add(siAux);
			input.nextLine();
		}

		//PRINTING ALL STATES IN THIS AFD
		/*System.out.println("STATES");
		for(StateSi si: afd.states)
			System.out.print(si.id + ", ");
		System.out.println();
		System.out.println();

		//PRINTING ALL ACCEPTATION STATES IN THIS AFD
		System.out.println("ACCEPTATION STATES");
		for(StateSi si: afd.acceptStates)
			System.out.print(si.id + ", ");
		System.out.println();
		System.out.println();*/

		//RESETING THE SCANNER TO THE BEGINNING OF THE FILE AND SKIPPING PREVIOUS DATA READED
		input.close();
		input = new Scanner(file);
		input.nextLine();
		input.nextLine();
		input.nextLine();

		//CREATING TRANSITIONS
		for(int i = 0; i < numberStates; i++)
		{
			input.next();
			for(int j = 0; j < numberCharacters; j++)
			{
				if((aux = input.nextInt()) != -1)
				{
					for(int k = 0; k < siList.size(); k++)
						if(siList.get(k).id == aux)
						{
							aux = k;
							break;
						}
					siList.get(i).transitionsSi.add(new TransitionSi(charsList.get(j).charValue(), siList.get(aux)));
				}
			}
			//OMITTING TOKEN 
			input.nextLine();
		}
		//PRINTING TRANSITIONS
		/*System.out.println("TRANSITIONS");
		for(StateSi si: afd.states)
			for(TransitionSi tsi: si.transitionsSi)
				System.out.println(si.id + " -> " + tsi.symbol + ", " + tsi.destination.id + " | " + si.acceptation);*/

		System.out.println("::: TABLA LEIDA CON EXITO");		
		return afd;
	}

	//Loads Lexic analyzer with the string to analyze
	public static void cargarCadena(String cadena1)
	{
		currentState = null;
		index = 0;		
		cadena = cadena1;
	}
}