import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import java.util.Set;
import java.util.HashSet;
import javafx.stage.FileChooser;
import java.io.File;

public class TestAFN extends Application
{
	private AFN afn1, afn2, afn3;
	private AFD afd1, afd2, afd3, afdT; 
	private AFN afnER = new AFN();
	private AFN afnT = new AFN();
	private TableView<DisplayableState> tableDisplay = new TableView<>();
	private ObservableList<DisplayableState> dataDisplay;
	private TableView<DisplayableState> tableToken = new TableView<>();
	private ObservableList<DisplayableState> dataToken;
	private TableView<DisplayableState> tableDisplayER = new TableView<>();
	private String tokenUnir;

	@Override
	public void start(Stage primaryStage)
	{
		GridPane pane = new GridPane();
		Label lbCrearBasico = new Label("1 -- Crear Basico");
		Label lbUnir = new Label("2 -- Unir");
		Label lbConcatenar = new Label("3 -- Concatenar");
		Label lbCerraduras = new Label("4 -- Cerraduras");
		Label lbDisplay = new Label("5 -- Display");
		Label lbConvertirAFD = new Label("6 -- Covertir a AFD");
		Label lbAnalizadorLexico = new Label("7 -- Analizador");
		Label lbSalir = new Label("8 -- Salir");
		
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(5));
		pane.setHgap(50);
		pane.setVgap(15);

		pane.add(lbCrearBasico, 0, 0);
		pane.add(lbUnir, 0, 1);
		pane.add(lbConcatenar, 0, 2);
		pane.add(lbCerraduras, 0, 3);
		pane.add(lbDisplay, 0, 4);
		pane.add(lbConvertirAFD, 0, 5);
		pane.add(lbAnalizadorLexico, 0, 6);
		pane.add(lbSalir, 0, 7);

		Scene scene = new Scene(pane, 300, 300);
		primaryStage.setScene(scene);
		primaryStage.setTitle("AFN Test");
		primaryStage.show();

		pane.requestFocus();

		//-----------------------------------CREAR-------------------------------------------------
		
		HBox paneCrear = new HBox(50);
		VBox vbox3 = new VBox(10);
		Label lbIngresaSimbolo = new Label("Ingresa el simbolo: ");
		TextField tfIngresaSimbolo = new TextField();
		Button btIngresaSimbolo = new Button("OK");
		ToggleGroup tgGroupCrear = new ToggleGroup();
		RadioButton rbAFN1Crear = new RadioButton("AFN 1");
		RadioButton rbAFN2Crear = new RadioButton("AFN 2");
		RadioButton rbAFN3Crear = new RadioButton("AFN 3");

		paneCrear.setPadding(new Insets(5));
		tfIngresaSimbolo.setPrefColumnCount(5);
		rbAFN1Crear.setToggleGroup(tgGroupCrear);
		rbAFN2Crear.setToggleGroup(tgGroupCrear);
		rbAFN3Crear.setToggleGroup(tgGroupCrear);

		vbox3.getChildren().addAll(rbAFN1Crear, rbAFN2Crear, rbAFN3Crear);
		paneCrear.getChildren().addAll(vbox3, lbIngresaSimbolo, tfIngresaSimbolo, btIngresaSimbolo);

		Stage crearBasicoStage = new Stage();
		Scene scene1 = new Scene(paneCrear);
		crearBasicoStage.setScene(scene1);
		crearBasicoStage.setTitle("Crear AFN Basico");
	
		btIngresaSimbolo.setOnAction
		(
			e -> 
			{
				String s = tfIngresaSimbolo.getText();			
				if(rbAFN1Crear.isSelected())
					afn1 = new AFN(s.charAt(0));
				else if(rbAFN2Crear.isSelected())
					afn2 = new AFN(s.charAt(0));
				else if(rbAFN3Crear.isSelected())
					afn3 = new AFN(s.charAt(0));
			}
		);
		
		//---------------------------------------------UNIR-----------------------------------------

		HBox paneUnir = new HBox(20);
		VBox vbox1 = new VBox(10);
		VBox vbox2 = new VBox(10);
		VBox vbox12 = new VBox(10);
		ToggleGroup tgGroupUnir = new ToggleGroup();
		ToggleGroup tgGroupUnirA = new ToggleGroup();
		RadioButton rbAFN1 = new RadioButton("AFN 1");
		RadioButton rbAFN2 = new RadioButton("AFN 2");
		RadioButton rbAFN3 = new RadioButton("AFN 3");
		RadioButton rbAFN1A = new RadioButton("AFN 1");
		RadioButton rbAFN2A = new RadioButton("AFN 2");
		RadioButton rbAFN3A = new RadioButton("AFN 3");
		RadioButton rbAFNTA = new RadioButton("AFN T");
		Label lbToken = new Label("Token:");
		TextField tfTokenInt = new TextField();
		TextField tfTokenString = new TextField();
		Button btUnirOK = new Button("OK");

		paneUnir.setAlignment(Pos.CENTER);
		paneUnir.setPadding(new Insets(5));
		
		vbox1.setPadding(new Insets(5));
		vbox2.setPadding(new Insets(5));

		rbAFN1.setToggleGroup(tgGroupUnir);
		rbAFN2.setToggleGroup(tgGroupUnir);
		rbAFN3.setToggleGroup(tgGroupUnir);
		rbAFN1A.setToggleGroup(tgGroupUnirA);
		rbAFN2A.setToggleGroup(tgGroupUnirA);
		rbAFN3A.setToggleGroup(tgGroupUnirA);
		rbAFNTA.setToggleGroup(tgGroupUnirA);
		tfTokenInt.setAlignment(Pos.CENTER);
		tfTokenString.setAlignment(Pos.CENTER);
		tfTokenInt.setPrefColumnCount(5);
		tfTokenString.setPrefColumnCount(10);

		vbox1.getChildren().addAll(rbAFN1, rbAFN2, rbAFN3);
		vbox2.getChildren().addAll(rbAFN1A, rbAFN2A, rbAFN3A, rbAFNTA);
		vbox12.getChildren().addAll(lbToken, tfTokenInt, tfTokenString);
		paneUnir.getChildren().addAll(vbox1, vbox12, vbox2, btUnirOK);

		Stage unirStage = new Stage();
		Scene scene2 = new Scene(paneUnir);
		unirStage.setScene(scene2);
		unirStage.setTitle("UNIR");	

		btUnirOK.setOnAction
		(
			e ->
			{
				if(rbAFN1.isSelected())
				{
					if(rbAFN2A.isSelected())
					{
						afn1.unir(afn2);
						afn2 = null;
					}
					else if(rbAFN3A.isSelected())
					{
						afn1.unir(afn3);	
						afn3 = null;
					}
					else if(rbAFNTA.isSelected())
					{
						afn1.setToken(Integer.parseInt(tfTokenInt.getText()), tfTokenString.getText());
						afnT.unirAFNT(afn1);	
						afn1 = null;
					}
				}
				if(rbAFN2.isSelected())
				{
					if(rbAFN1A.isSelected())
					{
						afn2.unir(afn1);
						afn1 = null;
					}
					else if(rbAFN3A.isSelected())
					{
						afn2.unir(afn3);
						afn3 = null;
					}
					else if(rbAFNTA.isSelected())
					{
						afn2.setToken(Integer.parseInt(tfTokenInt.getText()), tfTokenString.getText());
						afnT.unirAFNT(afn2);	
						afn2 = null;
					}
				}
				if(rbAFN3.isSelected())
				{
					if(rbAFN1A.isSelected())
					{
						afn3.unir(afn1);
						afn1 = null;
					}
					else if(rbAFN2A.isSelected())
					{
						afn3.unir(afn2);
						afn2 = null;
					}
					else if(rbAFNTA.isSelected())
					{
						afn3.setToken(Integer.parseInt(tfTokenInt.getText()), tfTokenString.getText());
						afnT.unirAFNT(afn3);	
						afn3 = null;
					}
				}
			}
		);

		//---------------------------------------------CONCATENAR-----------------------------------------

		HBox paneConcat = new HBox(20);
		VBox vbox4 = new VBox(10);
		VBox vbox5 = new VBox(10);
		ToggleGroup tgGroupConcat = new ToggleGroup();
		ToggleGroup tgGroupConcatA = new ToggleGroup();
		RadioButton rbAFN1C = new RadioButton("AFN 1");
		RadioButton rbAFN2C = new RadioButton("AFN 2");
		RadioButton rbAFN3C = new RadioButton("AFN 3");
		RadioButton rbAFN1CA = new RadioButton("AFN 1");
		RadioButton rbAFN2CA = new RadioButton("AFN 2");
		RadioButton rbAFN3CA = new RadioButton("AFN 3");
		TextField tfConcat = new TextField("CON");
		Button btConcatOK = new Button("OK");

		paneConcat.setAlignment(Pos.CENTER);
		paneConcat.setPadding(new Insets(5));
		
		vbox4.setPadding(new Insets(5));
		vbox5.setPadding(new Insets(5));

		rbAFN1C.setToggleGroup(tgGroupConcat);
		rbAFN2C.setToggleGroup(tgGroupConcat);
		rbAFN3C.setToggleGroup(tgGroupConcat);
		rbAFN1CA.setToggleGroup(tgGroupConcatA);
		rbAFN2CA.setToggleGroup(tgGroupConcatA);
		rbAFN3CA.setToggleGroup(tgGroupConcatA);
		tfConcat.setEditable(false);
		tfConcat.setAlignment(Pos.CENTER);
		tfConcat.setPrefColumnCount(5);

		vbox4.getChildren().addAll(rbAFN1C, rbAFN2C, rbAFN3C);
		vbox5.getChildren().addAll(rbAFN1CA, rbAFN2CA, rbAFN3CA);
		paneConcat.getChildren().addAll(vbox4, tfConcat, vbox5, btConcatOK);

		Stage concatStage = new Stage();
		Scene scene3 = new Scene(paneConcat);
		concatStage.setScene(scene3);
		concatStage.setTitle("CONCATENAR");	

		btConcatOK.setOnAction
		(
			e ->
			{
				if(rbAFN1C.isSelected())
				{
					if(rbAFN2CA.isSelected())
					{
						afn1.concatenar(afn2);
						afn2 = null;
					}
					else if(rbAFN3CA.isSelected())
					{
						afn1.concatenar(afn3);
						afn3 = null;
					}
				}
				if(rbAFN2C.isSelected())
				{
					if(rbAFN1CA.isSelected())
					{
						afn2.concatenar(afn1);
						afn1 = null;
					}
					else if(rbAFN3CA.isSelected())
					{
						afn2.concatenar(afn3);
						afn3 = null;
					}
				}
				if(rbAFN3C.isSelected())
				{
					if(rbAFN1CA.isSelected())
					{
						afn3.concatenar(afn1);
						afn1 = null;
					}
					else if(rbAFN2CA.isSelected())
					{
						afn3.concatenar(afn2);
						afn2 = null;
					}
				}
			}
		);

		//---------------------------------------------CERRADURAS-----------------------------------------

		HBox paneCerraduras = new HBox(20);
		VBox vbox6 = new VBox(10);
		VBox vbox7 = new VBox(10);
		ToggleGroup tgGroupCerraduras = new ToggleGroup();
		ToggleGroup tgGroupCerradurasA = new ToggleGroup();
		RadioButton rbAFN1Cer = new RadioButton("AFN 1");
		RadioButton rbAFN2Cer = new RadioButton("AFN 2");
		RadioButton rbAFN3Cer = new RadioButton("AFN 3");
		RadioButton rbCerOpcion = new RadioButton("Cerradura ?");
		RadioButton rbCerMas = new RadioButton("Cerradura +");
		RadioButton rbCerEstrella = new RadioButton("Cerradura *");
		Button btCerOK = new Button("OK");

		paneCerraduras.setAlignment(Pos.CENTER);
		paneCerraduras.setPadding(new Insets(5));
		
		vbox6.setPadding(new Insets(5));
		vbox7.setPadding(new Insets(5));

		rbAFN1Cer.setToggleGroup(tgGroupCerraduras);
		rbAFN2Cer.setToggleGroup(tgGroupCerraduras);
		rbAFN3Cer.setToggleGroup(tgGroupCerraduras);
		rbCerOpcion.setToggleGroup(tgGroupCerradurasA);
		rbCerMas.setToggleGroup(tgGroupCerradurasA);
		rbCerEstrella.setToggleGroup(tgGroupCerradurasA);

		vbox6.getChildren().addAll(rbAFN1Cer, rbAFN2Cer, rbAFN3Cer);
		vbox7.getChildren().addAll(rbCerOpcion, rbCerMas, rbCerEstrella);
		paneCerraduras.getChildren().addAll(vbox6, vbox7, btCerOK);

		Stage cerradurasStage = new Stage();
		Scene scene4 = new Scene(paneCerraduras);
		cerradurasStage.setScene(scene4);
		cerradurasStage.setTitle("CERRADURAS");	

		btCerOK.setOnAction
		(
			e ->
			{
				if(rbAFN1Cer.isSelected())
				{
					if(rbCerOpcion.isSelected())
						afn1.cerraduraOpcion();
					else if(rbCerMas.isSelected())
						afn1.cerraduraMas();
					else if(rbCerEstrella.isSelected())
						afn1.cerraduraEstrella();	
				}
				if(rbAFN2Cer.isSelected())
				{
					if(rbCerOpcion.isSelected())
						afn2.cerraduraOpcion();
					else if(rbCerMas.isSelected())
						afn2.cerraduraMas();
					else if(rbCerEstrella.isSelected())
						afn2.cerraduraEstrella();
				}
				if(rbAFN3Cer.isSelected())
				{
					if(rbCerOpcion.isSelected())
						afn3.cerraduraOpcion();
					else if(rbCerMas.isSelected())
						afn3.cerraduraMas();
					else if(rbCerEstrella.isSelected())
						afn3.cerraduraEstrella();
				}
			}
		);

		//---------------------------------------------DISPLAY-----------------------------------------

		VBox displayPane = new VBox(10);
		VBox vbox8 = new VBox(15);
		VBox vbox10 = new VBox(15);
		HBox hbox1 = new HBox(30);
		TableColumn tbcInitialState = new TableColumn("Estado");
		TableColumn tbcSymbol = new TableColumn("Simbolo");
		TableColumn tbcDestiny = new TableColumn("Destino");
		TableColumn tbcAcceptation = new TableColumn("Aceptacion");
		ToggleGroup tgGroupDisplay = new ToggleGroup();
		RadioButton rbAFN1Display = new RadioButton("AFN 1");
		RadioButton rbAFN2Display = new RadioButton("AFN 2");
		RadioButton rbAFN3Display = new RadioButton("AFN 3");
		RadioButton rbAFNTDisplay = new RadioButton("AFN T");
		RadioButton rbAFD1Display = new RadioButton("AFD 1");
		RadioButton rbAFD2Display = new RadioButton("AFD 2");
		RadioButton rbAFD3Display = new RadioButton("AFD 3");
		RadioButton rbAFDTDisplay = new RadioButton("AFD T");
		Button btDisplayOK = new Button("OK");

		displayPane.setPadding(new Insets(5));
		hbox1.setPadding(new Insets(5));
		vbox8.setPadding(new Insets(5));
		rbAFN1Display.setToggleGroup(tgGroupDisplay);
		rbAFN2Display.setToggleGroup(tgGroupDisplay);
		rbAFN3Display.setToggleGroup(tgGroupDisplay);
		rbAFNTDisplay.setToggleGroup(tgGroupDisplay);
		rbAFD1Display.setToggleGroup(tgGroupDisplay);
		rbAFD2Display.setToggleGroup(tgGroupDisplay);
		rbAFD3Display.setToggleGroup(tgGroupDisplay);
		rbAFDTDisplay.setToggleGroup(tgGroupDisplay);

		tbcInitialState.setCellValueFactory(new PropertyValueFactory("id"));
		tbcSymbol.setCellValueFactory(new PropertyValueFactory("symbol"));
		tbcDestiny.setCellValueFactory(new PropertyValueFactory("destination"));
		tbcAcceptation.setCellValueFactory(new PropertyValueFactory("acceptState"));

		tableDisplay.getColumns().setAll(tbcInitialState, tbcSymbol, tbcDestiny, tbcAcceptation);
		tableDisplay.setPrefWidth(450);
		tableDisplay.setPrefHeight(500);
		tableDisplay.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		vbox8.getChildren().addAll(rbAFN1Display, rbAFN2Display, rbAFN3Display, rbAFNTDisplay);	
		vbox10.getChildren().addAll(rbAFD1Display, rbAFD2Display, rbAFD3Display, rbAFDTDisplay);	
		hbox1.getChildren().addAll(vbox8, vbox10, btDisplayOK);	
		displayPane.getChildren().addAll(hbox1, tableDisplay);

		Stage displayStage = new Stage();
		Scene scene5 = new Scene(displayPane);
		displayStage.setScene(scene5);
		displayStage.setTitle("DISPLAY");	

		btDisplayOK.setOnAction
		(
			e ->
			{
				if(rbAFN1Display.isSelected())
					dataDisplay = getStatesTransitions(1);

				else if(rbAFN2Display.isSelected())
					dataDisplay = getStatesTransitions(2);

				else if(rbAFN3Display.isSelected())
					dataDisplay = getStatesTransitions(3);

				else if(rbAFNTDisplay.isSelected())
					dataDisplay = getStatesTransitions(4);

				else if(rbAFD1Display.isSelected())
					dataDisplay = getStatesTransitions(5);

				else if(rbAFD2Display.isSelected())
					dataDisplay = getStatesTransitions(6);

				else if(rbAFD3Display.isSelected())
					dataDisplay = getStatesTransitions(7);

				else if(rbAFDTDisplay.isSelected())
					dataDisplay = getStatesTransitions(8);

				tableDisplay.setItems(dataDisplay);	
			}
		);

		//---------------------------------------------CONVERTIR A AFD-----------------------------------------

		HBox convertirPane = new HBox(80);
		VBox vbox9 = new VBox(25);
		ToggleGroup tgGroupConvertir = new ToggleGroup();
		RadioButton rbAFN1Convertir = new RadioButton("AFN 1");
		RadioButton rbAFN2Convertir = new RadioButton("AFN 2");
		RadioButton rbAFN3Convertir = new RadioButton("AFN 3");
		RadioButton rbAFNTConvertir = new RadioButton("AFN T");
		Button btConvertirOK = new Button("OK");

		convertirPane.setPadding(new Insets(5));
		vbox9.setPadding(new Insets(5));
		rbAFN1Convertir.setToggleGroup(tgGroupConvertir);
		rbAFN2Convertir.setToggleGroup(tgGroupConvertir);
		rbAFN3Convertir.setToggleGroup(tgGroupConvertir);
		rbAFNTConvertir.setToggleGroup(tgGroupConvertir);

		vbox9.getChildren().addAll(rbAFN1Convertir, rbAFN2Convertir, rbAFN3Convertir, rbAFNTConvertir);	
		convertirPane.getChildren().addAll(vbox9, btConvertirOK);

		Stage convertirStage = new Stage();
		Scene scene6 = new Scene(convertirPane, 250, 200);
		convertirStage.setScene(scene6);
		convertirStage.setTitle("CONVERTIR A AFD");	

		btConvertirOK.setOnAction
		(
			e ->
			{
				if(rbAFN1Convertir.isSelected())
				{	
					afd1 = afn1.convertirAFD();
					try
					{
						afd1.enviarTabla();	
					}
					catch(Exception ex)
					{
						System.out.println("Error al enviar a tabla");
					}
				}
				else if(rbAFN2Convertir.isSelected())
				{	
					afd2 = afn2.convertirAFD();
					try
					{
						afd2.enviarTabla();	
					}
					catch(Exception ex)
					{
						System.out.println("Error al enviar a tabla");
					}
				}
				else if(rbAFN3Convertir.isSelected())
				{	
					afd3 = afn3.convertirAFD();
					try
					{
						afd3.enviarTabla();	
					}
					catch(Exception ex)
					{
						System.out.println("Error al enviar a tabla");
					}
				}
				else if(rbAFNTConvertir.isSelected())
				{	
					afdT = afnT.convertirAFD();
					try
					{
						afdT.enviarTabla();	
					}
					catch(Exception ex)
					{
						System.out.println("Error al enviar a tabla");
					}
				}
			}
		);

		//---------------------------------------ANALIZADOR LEXICO Y SINTACTICO---------------------------------

		VBox analizadorPane = new VBox(20);
		HBox hbox2 = new HBox(20);
		HBox hbox3 = new HBox(20);
		HBox hbox4 = new HBox(20);
		VBox vbox11 = new VBox(20);
		Label lbAnalizador = new Label("Ingresa una cadena: ");
		TextField tfAnalizador = new TextField();
		TextField tfAnalizadorS = new TextField();
		TextField tfAnalizadorEvaluar = new TextField();
		TextField tfAnalizadorPosfijo = new TextField();
		Button btAnalizadorL = new Button("Analizador Lexico");
		Button btAnalizadorS = new Button("Analizador Sintactico");
		Button btAnalizadorEvaluar = new Button("Evaluar");
		Button btConstruirLexico = new Button("Construir Analizador Lexico");
		Button btLeerTablaLexico = new Button("Leer Tabla Analizador Lexico");
		Button btUsarGramatica = new Button("Usar Gramatica");
		TableColumn tbcLexema = new TableColumn("Lexema");
		TableColumn tbcToken = new TableColumn("Token");
		TableColumn tbcInitialStateER = new TableColumn("Estado");
		TableColumn tbcSymbolER = new TableColumn("Simbolo");
		TableColumn tbcDestinyER = new TableColumn("Destino");
		TableColumn tbcAcceptationER = new TableColumn("Aceptacion");
		ToggleGroup tgGroupSintactico = new ToggleGroup();
		RadioButton rbExpresionesAritmeticas = new RadioButton("Expresiones Aritmeticas");
		RadioButton rbExpresionesRegulares = new RadioButton("Expresiones Regulares");
		RadioButton rbGramaticaLL1 = new RadioButton("Gramatica LL1");
		RadioButton rbGramaticaLR0 = new RadioButton("Gramatica LR(0)");
		DoubleJames resultado = new DoubleJames(0);
		StringBuilder cadenaPos = new StringBuilder();
		Node lista = new Node();
		FileChooser fileChooser = new FileChooser();

		analizadorPane.setPadding(new Insets(5));
		hbox2.setPadding(new Insets(5));
		hbox3.setPadding(new Insets(5));
		hbox4.setPadding(new Insets(5));
		vbox11.setPadding(new Insets(5));
		tfAnalizador.setPrefColumnCount(15);
		tfAnalizadorS.setEditable(false);
		tfAnalizadorEvaluar.setEditable(false);
		tfAnalizadorPosfijo.setEditable(false);

		tbcLexema.setCellValueFactory(new PropertyValueFactory("lexema"));
		tbcToken.setCellValueFactory(new PropertyValueFactory("token"));

		tbcInitialStateER.setCellValueFactory(new PropertyValueFactory("id"));
		tbcSymbolER.setCellValueFactory(new PropertyValueFactory("symbol"));
		tbcDestinyER.setCellValueFactory(new PropertyValueFactory("destination"));
		tbcAcceptationER.setCellValueFactory(new PropertyValueFactory("acceptState"));

		tableToken.getColumns().setAll(tbcLexema, tbcToken);
		tableToken.setPrefWidth(210);
		tableToken.setPrefHeight(250);
		tableToken.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		tableDisplayER.getColumns().setAll(tbcInitialStateER, tbcSymbolER, tbcDestinyER, tbcAcceptationER);
		tableDisplayER.setPrefWidth(400);
		tableDisplayER.setPrefHeight(450);
		tableDisplayER.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		rbExpresionesAritmeticas.setToggleGroup(tgGroupSintactico);
		rbExpresionesRegulares.setToggleGroup(tgGroupSintactico);
		rbGramaticaLL1.setToggleGroup(tgGroupSintactico);
		rbGramaticaLR0.setToggleGroup(tgGroupSintactico);
		
		hbox2.getChildren().addAll(btAnalizadorL, btAnalizadorS, tfAnalizadorS, rbExpresionesAritmeticas, rbExpresionesRegulares, rbGramaticaLL1, rbGramaticaLR0);
		hbox4.getChildren().addAll(btConstruirLexico, btLeerTablaLexico, btUsarGramatica);
		vbox11.getChildren().addAll(btAnalizadorEvaluar, tfAnalizadorEvaluar, tfAnalizadorPosfijo);
		hbox3.getChildren().addAll(tableToken, vbox11, tableDisplayER);
		analizadorPane.getChildren().addAll(lbAnalizador, tfAnalizador, hbox2, hbox4, hbox3);
		
		Stage analizadorStage = new Stage();
		Scene scene7 = new Scene(analizadorPane, 1050, 500);
		analizadorStage.setScene(scene7);
		analizadorStage.setTitle("ANALIZADOR LEXICO Y SINTACTICO");	

		btAnalizadorL.setOnAction
		(
			e ->
			{
				Lexic.cargarCadena(tfAnalizador.getText());			

				dataToken = getTokenData();
				tableToken.setItems(dataToken);				
			}
		);

		btAnalizadorS.setOnAction
		(
			e -> 
			{
				Lexic.cargarCadena(tfAnalizador.getText());			
				if(rbExpresionesAritmeticas.isSelected())
				{
					cadenaPos.setLength(0);

					if(Syntactic.E_A(resultado, cadenaPos))
						tfAnalizadorS.setText("CORRECTO");
					else
						tfAnalizadorS.setText("INCORRECTO");
				}
				else if(rbExpresionesRegulares.isSelected())
				{
					afnER.clear();

					Lexic.usingERLanguage = true;
					if(Syntactic.E(afnER))
					{
						System.out.println("AFNER FINAL");
						for(State er: afnER.states)
							System.out.println("States: " + er.id);

						tfAnalizadorS.setText("CORRECTO");
						dataDisplay = getStatesTransitions(9);
						tableDisplayER.setItems(dataDisplay);	
					}
					else
						tfAnalizadorS.setText("INCORRECTO");
					Lexic.usingERLanguage = false;
				}				
				else if(rbGramaticaLL1.isSelected())
				{
					Set<String> vn = new HashSet<>();
					if(Syntactic.G(lista, vn))
					{
						tfAnalizadorS.setText("CORRECTO");
						try
						{
							Syntactic.construirTablaLL1(lista, vn);						
						}
						catch(Exception ex)
						{
							System.out.println("Failed to build LL1 table");
						}
					}
					else
						tfAnalizadorS.setText("INCORRECTO");				
				}
				else if(rbGramaticaLR0.isSelected())
				{
					Set<String> vn = new HashSet<>();
					if(Syntactic.G(lista, vn))
					{
						tfAnalizadorS.setText("CORRECTO");
						try
						{
							Syntactic.construirTablaLR0(lista, vn);						
						}
						catch(Exception ex)
						{
							System.out.println("Failed to build LR0 table");
						}
					}
					else
						tfAnalizadorS.setText("INCORRECTO");				
				}
			}
		);

		btUsarGramatica.setOnAction
		(
			e ->
			{
				Lexic.cargarCadena(tfAnalizador.getText());			
				try
				{
					if(Lexic.analizadorLL1(lista))
						tfAnalizadorS.setText("CORRECTO");						
					else
						tfAnalizadorS.setText("INCORRECTO");							
				}
				catch(Exception ex)
				{
					System.out.println("Couldn't validate string with table LL1");
				}
			}
		);

		btAnalizadorEvaluar.setOnAction
		(
			e -> 
			{
				tfAnalizadorEvaluar.setText(resultado.value + "");
				tfAnalizadorPosfijo.setText(cadenaPos.toString());
			}
		);

		btConstruirLexico.setOnAction
		(
			e -> 
			{
				File file = fileChooser.showOpenDialog(analizadorStage);
				if(file != null)
				{
					try
					{
						Lexic.leerTablaConstructor(file.getName());
					}
					catch(Exception ex)
					{
						System.out.println("Incapaz de leer tabla de constructor Lexico: " + file.getName());
					}
					try
					{
						Lexic.leerTabla("AFD.txt");
					}
					catch(Exception ex)
					{
						System.out.println("Error al intentar leer la tabla AFD.txt");
					}
				}
			}
		);

		btLeerTablaLexico.setOnAction
		(
			e -> 
			{
				File file = fileChooser.showOpenDialog(analizadorStage);
				if(file != null)
				{
					try
					{
						Lexic.leerTabla(file.getName());
					}
					catch(Exception ex)
					{
						System.out.println("Error al intentar leer la tabla: " + file.getName());
					}
				}
			}
		);		
		
		pane.setOnKeyPressed
		(
			e -> 
			{
				switch(e.getCode())
				{
					case DIGIT1:
						primaryStage.toBack();
						crearBasicoStage.show();
					break;
					case DIGIT2:
						if(afn1 == null)
						{
							rbAFN1.setDisable(true);
							rbAFN1A.setDisable(true);
						}
						else
						{
							rbAFN1.setDisable(false);
							rbAFN1A.setDisable(false);	
						}
						if(afn2 == null)
						{
							rbAFN2.setDisable(true);
							rbAFN2A.setDisable(true);
						}
						else
						{
							rbAFN2.setDisable(false);
							rbAFN2A.setDisable(false);	
						}
						if(afn3 == null)
						{
							rbAFN3.setDisable(true);
							rbAFN3A.setDisable(true);
						}
						else
						{
							rbAFN3.setDisable(false);
							rbAFN3A.setDisable(false);	
						}
						primaryStage.toBack();
						unirStage.show();
					break;
					case DIGIT3:
						if(afn1 == null)
						{
							rbAFN1C.setDisable(true);
							rbAFN1CA.setDisable(true);
						}
						else
						{
							rbAFN1C.setDisable(false);
							rbAFN1CA.setDisable(false);	
						}
						if(afn2 == null)
						{
							rbAFN2C.setDisable(true);
							rbAFN2CA.setDisable(true);
						}
						else
						{
							rbAFN2C.setDisable(false);
							rbAFN2CA.setDisable(false);	
						}
						if(afn3 == null)
						{
							rbAFN3C.setDisable(true);
							rbAFN3CA.setDisable(true);
						}
						else
						{
							rbAFN3C.setDisable(false);
							rbAFN3CA.setDisable(false);	
						}
						primaryStage.toBack();
						concatStage.show();
					break;
					case DIGIT4:
						if(afn1 == null)
							rbAFN1Cer.setDisable(true);
						else
							rbAFN1Cer.setDisable(false);
						if(afn2 == null)
							rbAFN2Cer.setDisable(true);
						else
							rbAFN2Cer.setDisable(false);
						if(afn3 == null)
							rbAFN3Cer.setDisable(true);
						else
							rbAFN3Cer.setDisable(false);

						primaryStage.toBack();
						cerradurasStage.show();
					break;
					case DIGIT5:
						if(afn1 == null)
							rbAFN1Display.setDisable(true);
						else
							rbAFN1Display.setDisable(false);
						if(afn2 == null)
							rbAFN2Display.setDisable(true);
						else
							rbAFN2Display.setDisable(false);
						if(afn3 == null)
							rbAFN3Display.setDisable(true);
						else
							rbAFN3Display.setDisable(false);
						if(afnT == null)
							rbAFNTDisplay.setDisable(true);
						else
							rbAFNTDisplay.setDisable(false);

						if(afd1 == null)
							rbAFD1Display.setDisable(true);
						else
							rbAFD1Display.setDisable(false);
						if(afd2 == null)
							rbAFD2Display.setDisable(true);
						else
							rbAFD2Display.setDisable(false);
						if(afd3 == null)
							rbAFD3Display.setDisable(true);
						else
							rbAFD3Display.setDisable(false);
						if(afdT == null)
							rbAFDTDisplay.setDisable(true);
						else
							rbAFDTDisplay.setDisable(false);

						primaryStage.toBack();
						displayStage.show();
					break;
					case DIGIT6:
						if(afn1 == null)
							rbAFN1Convertir.setDisable(true);
						else
							rbAFN1Convertir.setDisable(false);
						if(afn2 == null)
							rbAFN2Convertir.setDisable(true);
						else
							rbAFN2Convertir.setDisable(false);
						if(afn3 == null)
							rbAFN3Convertir.setDisable(true);
						else
							rbAFN3Convertir.setDisable(false);
						if(afnT == null)
							rbAFNTConvertir.setDisable(true);
						else
							rbAFNTConvertir.setDisable(false);

						primaryStage.toBack();
						convertirStage.show();
					break;
					case DIGIT7:
						primaryStage.toBack();
						try
						{
							Lexic.leerTabla("AFD.txt");
						}
						catch(Exception ex)
						{
							System.out.println("Error al intentar leer la tabla AFD.txt");
						}
						analizadorStage.show();
					break;
					case DIGIT8:
						primaryStage.close();
					break;
				}
			}
		);
	}	

	public ObservableList getStatesTransitions(int option)
	{
		List<DisplayableState> list = new ArrayList<DisplayableState>();

		if(option == 1)
		{
			for(State e: afn1.states)
			{
				if(e.acceptation == -1)
					for(Transition t: e.transitions)
						list.add(new DisplayableState(e.id, t.minSymbol + "", t.destination.id, e.acceptation));	
				else
					list.add(new DisplayableState(e.id, "-1", -1, e.acceptation));		
			}				
		}
		else if(option == 2)
		{
			for(State e: afn2.states)
			{
				if(e.acceptation == -1)
					for(Transition t: e.transitions)
						list.add(new DisplayableState(e.id, t.minSymbol + "", t.destination.id, e.acceptation));	
				else
					list.add(new DisplayableState(e.id, "-1", -1, e.acceptation));		
			}
		}
		else if(option == 3)
		{
			for(State e: afn3.states)
			{
				if(e.acceptation == -1)
					for(Transition t: e.transitions)
						list.add(new DisplayableState(e.id, t.minSymbol + "", t.destination.id, e.acceptation));	
				else
					list.add(new DisplayableState(e.id, "-1", -1, e.acceptation));		
			}
		}
		else if(option == 4)
		{
			for(State e: afnT.states)
			{
				if(e.acceptation == -1)
					for(Transition t: e.transitions)
						list.add(new DisplayableState(e.id, t.minSymbol + "", t.destination.id, e.acceptation));	
				else
					list.add(new DisplayableState(e.id, "-1", -1, e.acceptation));		
			}
		}
		else if(option == 5)
		{
			for(StateSi si: afd1.states)
			{
				if(si.acceptation == -1)
					for(TransitionSi ti: si.transitionsSi)
						list.add(new DisplayableState(si.id, ti.symbol + "", ti.destination.id, si.acceptation));	
				else
					list.add(new DisplayableState(si.id, "-1", -1, si.acceptation));	
			}
		}
		else if(option == 6)
		{
			for(StateSi si: afd2.states)
			{
				if(si.acceptation == -1)
					for(TransitionSi ti: si.transitionsSi)
						list.add(new DisplayableState(si.id, ti.symbol + "", ti.destination.id, si.acceptation));	
				else
					list.add(new DisplayableState(si.id, "-1", -1, si.acceptation));	
			}
		}
		else if(option == 7)
		{
			for(StateSi si: afd3.states)
			{
				if(si.acceptation == -1)
					for(TransitionSi ti: si.transitionsSi)
						list.add(new DisplayableState(si.id, ti.symbol + "", ti.destination.id, si.acceptation));	
				else
					list.add(new DisplayableState(si.id, "-1", -1, si.acceptation));	
			}
		}
		else if(option == 8)
		{
			for(StateSi si: afdT.states)
			{
				if(si.acceptation == -1)
					for(TransitionSi ti: si.transitionsSi)
						list.add(new DisplayableState(si.id, ti.symbol + "", ti.destination.id, si.acceptation));	
				else
					list.add(new DisplayableState(si.id, "-1", -1, si.acceptation));	
			}
		}
		else if(option == 9)
		{
			for(State e: afnER.states)
			{
				if(e.acceptation == -1)
					for(Transition t: e.transitions)
						list.add(new DisplayableState(e.id, t.minSymbol + "", t.destination.id, e.acceptation));	
				else
					list.add(new DisplayableState(e.id, "-1", -1, e.acceptation));		
			}	
		}
		ObservableList data = FXCollections.observableList(list);

		return data;
	}

	public ObservableList getTokenData()
	{
		Token tok;
		List<DisplayableToken> tokenList = new ArrayList<DisplayableToken>();

		while((tok = Lexic.getToken()).token != 0)
			tokenList.add(new DisplayableToken(tok.lexema, tok.token));

		tokenList.add(new DisplayableToken(tok.lexema, tok.token));

		ObservableList dataToken = FXCollections.observableList(tokenList);

		return dataToken;
	}
}
