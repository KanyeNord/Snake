package it.edu.iisgubbio.gioco;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

//TODO ridimensionare le scritte di vittoria e sconfitta
//TODO controllare se si può ottimizzare effetto pacman
//TODO trovare modo di abbassare il volume invece di fare ripartire la musica
//TODO aggiungi i commenti, aggiungere i valori final e indentare bene
//TODO sistemare gli sfondi
//TODO controllare la musica, la musica si ferma quando si perde ma non riparte nonostante play sia su true
//TODO sostituire i +1 con grandezzaY e rendere grandezza X quello più grande per sistemare lo spazio 
//TODO fixare bug vittoria e sconfitta insieme, provare metodo attuale e provare a mettere la vittoria dopo il movimento
public class Snake extends Application {
	int coloreR=(int)(Math.random()*220)+20;	;
	int coloreB=(int)(Math.random()*220)+20;
	Boolean rBoolean=true;
	Boolean bBoolean=true;

	int velocità;
	int grandezza=15;
	int punti=0;
	GridPane griglia = new GridPane();
	Pane pannello = new Pane();
	Label eTitolo = new Label("Snake");
	Button bGioca = new Button("Gioca");
	Label ePunteggio = new Label();
	Label eSconfitta = new Label("GAME OVER");
	Label eVittoria = new Label("HAI VINTO");
	Button bRigioca = new Button("rigioca");
	Label eVolume = new Label();
	Label eVelocità = new Label("velocità serpente");
	Label eGrandezza = new Label("grandezza campo");
	Button bComandi = new Button("comandi");
	Button bMenu = new Button("menu");
	Label eComandiW = new Label("W: Muovi il serpente su");
	Label eComandiA = new Label("A: Muovi il serpente a sinistra");
	Label eComandiS = new Label("S: Muovi il serpente giù");
	Label eComandiD = new Label("D: Muovi il serpente a destra");
	Label eComandiE = new Label("E: Muta o riattiva i suoni");
	Label eRegole = new Label("Come si gioca: L'obbiettivo del gioco è mangiare le mele\n"
			+ "per far crescere il serpente, fino ad occupare tutta la\n"
			+ "mappa, attento a non sbattere contro te stesso o contro\n"
			+ "i muri, in caso avessi difficoltà puoi provare la modalità\n"
			+ "pacman nella quale andando contro un muro ti ritroverai\n"
			+ "dal lato opposto della mappa!");
	Boolean mele[][];
	Boolean serpente[][];
	Label campo[][];
	int uccidiSerpente[][];

	Slider sVelocità = new Slider(1, 3, 2);
	Slider sGrandezza = new Slider(1, 3, 2);

	CheckBox cPacman = new CheckBox("effetto pacman");
	boolean pacman=false;

	boolean alto=false;
	boolean basso=false;
	boolean sinistra=false;
	boolean destra=true;

	int snakeX;
	int snakeY;

	int melaX;
	int melaY;

	boolean tastiPremibili=false;

	Image iMela = new Image(getClass().getResourceAsStream("Mela.png"));
	ImageView immagineMela = new ImageView(iMela);

	Image iOcchiAlto = new Image(getClass().getResourceAsStream("Occhi Alto.png"));
	ImageView immagineOcchiAlto = new ImageView(iOcchiAlto);
	Image iOcchiSinistra = new Image(getClass().getResourceAsStream("Occhi Sinistra.png"));
	ImageView immagineOcchiSinistra = new ImageView(iOcchiSinistra);
	Image iOcchiBasso = new Image(getClass().getResourceAsStream("Occhi Basso.png"));
	ImageView immagineOcchiBasso = new ImageView(iOcchiBasso);
	Image iOcchiDestra = new Image(getClass().getResourceAsStream("Occhi Destra.png"));
	ImageView immagineOcchiDestra = new ImageView(iOcchiDestra);

	Image iSfondo = new Image(getClass().getResourceAsStream("Sfondo.png"));
	ImageView immagineSfondo = new ImageView(iSfondo);


	Image iSfondoMini = new Image(getClass().getResourceAsStream("SfondoMini.png"));
	ImageView immagineSfondoMini = new ImageView(iSfondoMini);

	Image iSfondoMedio = new Image(getClass().getResourceAsStream("SfondoMedio.png"));
	ImageView immagineSfondoMedio = new ImageView(iSfondoMedio);

	Image iSfondoMaxi = new Image(getClass().getResourceAsStream("SfondoMaxi.png"));
	ImageView immagineSfondoMaxi = new ImageView(iSfondoMaxi);

	Image iVolume = new Image(getClass().getResourceAsStream("volume-up.png"));
	ImageView immagineVolume= new ImageView(iVolume);

	Image iVolumeMutato = new Image(getClass().getResourceAsStream("volume-down.png"));
	ImageView immagineVolumeMutato = new ImageView(iVolumeMutato);

	boolean volume= true;

	Region rettangoloMenu= new Region();
	Region rettangoloComandi= new Region();

	int dimensioneCampo=grandezza*30;
	Label eSfondo = new Label();
	final AudioClip musica= new AudioClip(getClass().getResource("Snake.io Music.mp3").toString());
	final AudioClip melaMangiata= new AudioClip(getClass().getResource("apple-bite.mp3").toString());
	final AudioClip vittoria= new AudioClip(getClass().getResource("suono-vittoria.mp3").toString());
	final AudioClip sconfitta= new AudioClip(getClass().getResource("suono-sconfitta.mp3").toString());
	Timeline timeline;
	Stage finestraRidimensionata;

	public void start(Stage finestra) {
		musica.setVolume(40);
		//				musica.play();
		musica.setCycleCount(AudioClip.INDEFINITE);
		eVolume.setGraphic(immagineVolume);
		pannello.getChildren().add(eSfondo);
		eSfondo.setGraphic(immagineSfondoMedio);
		immagineSfondoMedio.setPreserveRatio(true);

		immagineMela.setFitHeight(30);
		immagineMela.setPreserveRatio(true);
		immagineOcchiAlto.setFitHeight(30);
		immagineOcchiAlto.setPreserveRatio(true);
		immagineOcchiSinistra.setFitHeight(30);
		immagineOcchiSinistra.setPreserveRatio(true);
		immagineOcchiBasso.setFitHeight(30);
		immagineOcchiBasso.setPreserveRatio(true);
		immagineOcchiDestra.setFitHeight(30);
		immagineOcchiDestra.setPreserveRatio(true);
		immagineVolume.setFitHeight(40);
		immagineVolume.setPreserveRatio(true);
		immagineVolumeMutato.setFitHeight(40);
		immagineVolumeMutato.setPreserveRatio(true);

		finestraRidimensionata=finestra;

		pannello.setPrefSize(dimensioneCampo, dimensioneCampo+80);
		pannello.getChildren().add(eTitolo);
		pannello.getChildren().add(griglia);
		pannello.getChildren().add(bGioca);
		pannello.getChildren().add(rettangoloMenu);
		pannello.getChildren().add(eVelocità);
		pannello.getChildren().add(sVelocità);
		pannello.getChildren().add(eGrandezza);
		pannello.getChildren().add(sGrandezza);
		pannello.getChildren().add(cPacman);
		pannello.getChildren().add(ePunteggio);
		pannello.getChildren().add(eSconfitta);
		pannello.getChildren().add(eVittoria);
		pannello.getChildren().add(bRigioca);
		pannello.getChildren().add(eVolume);
		pannello.getChildren().add(bComandi);
		pannello.getChildren().add(rettangoloComandi);
		pannello.getChildren().add(bMenu);
		pannello.getChildren().add(eComandiW);
		pannello.getChildren().add(eComandiA);
		pannello.getChildren().add(eComandiS);
		pannello.getChildren().add(eComandiD);
		pannello.getChildren().add(eComandiE);
		pannello.getChildren().add(eRegole);

		ePunteggio.setVisible(false);
		eSconfitta.setVisible(false);
		eVittoria.setVisible(false);
		bRigioca.setVisible(false);
		bMenu.setVisible(false);
		rettangoloComandi.setVisible(false);
		eComandiW.setVisible(false);
		eComandiA.setVisible(false);
		eComandiS.setVisible(false);
		eComandiD.setVisible(false);
		eComandiE.setVisible(false);
		eRegole.setVisible(false);

		griglia.setLayoutY(50);

		eTitolo.setPrefWidth(220);
		eTitolo.setLayoutX(dimensioneCampo/2-110);
		eTitolo.setLayoutY(60);

		bGioca.setPrefWidth(180);
		bGioca.setPrefHeight(40);
		bGioca.setLayoutX(dimensioneCampo/2-90);
		bGioca.setLayoutY(140);

		eVelocità.setPrefWidth(130);
		eVelocità.setLayoutX(dimensioneCampo/2-65);
		eVelocità.setLayoutY(200);

		sVelocità.setPrefWidth(130);
		sVelocità.setLayoutX(dimensioneCampo/2-65);
		sVelocità.setLayoutY(230);

		eGrandezza.setPrefWidth(130);
		eGrandezza.setLayoutX(dimensioneCampo/2-65);
		eGrandezza.setLayoutY(260);

		sGrandezza.setPrefWidth(130);
		sGrandezza.setLayoutX(dimensioneCampo/2-65);
		sGrandezza.setLayoutY(290);

		cPacman.setLayoutX(dimensioneCampo/2-65);
		cPacman.setLayoutY(335);

		eVolume.setLayoutX(dimensioneCampo-45);
		eVolume.setLayoutY(5);

		rettangoloMenu.setPrefWidth(170);
		rettangoloMenu.setPrefHeight(240);
		rettangoloMenu.setLayoutX(dimensioneCampo/2-85);
		rettangoloMenu.setLayoutY(190);
		rettangoloMenu.setOpacity(0.4);
		rettangoloMenu.getStyleClass().add("rettangolo");

		rettangoloComandi.setPrefWidth(400);
		rettangoloComandi.setPrefHeight(300);
		rettangoloComandi.setLayoutX(dimensioneCampo/2-200);
		rettangoloComandi.setLayoutY(60);
		rettangoloComandi.setOpacity(0.4);
		rettangoloComandi.getStyleClass().add("rettangolo");

		sVelocità.setShowTickMarks(true);
		sVelocità.setShowTickLabels(true);
		sVelocità.setSnapToTicks(true);
		sVelocità.setMinorTickCount(0);
		sVelocità.setMajorTickUnit(1);

		sGrandezza.setShowTickMarks(true);
		sGrandezza.setShowTickLabels(true);
		sGrandezza.setSnapToTicks(true);
		sGrandezza.setMinorTickCount(0);
		sGrandezza.setMajorTickUnit(1);

		ePunteggio.setLayoutX(5);
		ePunteggio.setLayoutY(5);

		bComandi.setPrefWidth(130);
		bComandi.setPrefHeight(40);
		bComandi.setLayoutX(dimensioneCampo/2-65);
		bComandi.setLayoutY(375);

		bMenu.setPrefWidth(130);
		bMenu.setPrefHeight(40);
		bMenu.setLayoutX(dimensioneCampo/2-65);
		bMenu.setLayoutY(375);

		eComandiW.setPrefWidth(380);
		eComandiW.setPrefHeight(40);
		eComandiW.setLayoutX(dimensioneCampo/2-190);
		eComandiW.setLayoutY(60);

		eComandiA.setPrefWidth(380);
		eComandiA.setPrefHeight(40);
		eComandiA.setLayoutX(dimensioneCampo/2-190);
		eComandiA.setLayoutY(90);

		eComandiS.setPrefWidth(380);
		eComandiS.setPrefHeight(40);
		eComandiS.setLayoutX(dimensioneCampo/2-190);
		eComandiS.setLayoutY(120);

		eComandiD.setPrefWidth(380);
		eComandiD.setPrefHeight(40);
		eComandiD.setLayoutX(dimensioneCampo/2-190);
		eComandiD.setLayoutY(150);

		eComandiE.setPrefWidth(380);
		eComandiE.setPrefHeight(40);
		eComandiE.setLayoutX(dimensioneCampo/2-190);
		eComandiE.setLayoutY(180);

		eRegole.setPrefWidth(380);
		eRegole.setPrefHeight(130);
		eRegole.setLayoutX(dimensioneCampo/2-190);
		eRegole.setLayoutY(210);

		Font fontTitolo = Font.loadFont(getClass().getResource("retro_computer_personal_use.ttf").toString(), 50);
		Font fontTesto = Font.loadFont(getClass().getResource("pixellari.ttf").toString(), 15);
		Font fontPunti = Font.loadFont(getClass().getResource("retro_computer_personal_use.ttf").toString(), 25);

		eTitolo.setFont(fontTitolo);
		eSconfitta.setFont(fontTitolo);
		eVittoria.setFont(fontTitolo);
		ePunteggio.setFont(fontPunti);
		bGioca.setFont(fontTesto);
		eVelocità.setFont(fontTesto);
		eGrandezza.setFont(fontTesto);
		cPacman.setFont(fontTesto);
		bMenu.setFont(fontTesto);
		bComandi.setFont(fontTesto);
		bRigioca.setFont(fontTesto);

		eComandiW.setFont(fontTesto);
		eComandiA.setFont(fontTesto);
		eComandiS.setFont(fontTesto);
		eComandiD.setFont(fontTesto);
		eComandiE.setFont(fontTesto);
		eRegole.setFont(fontTesto);

		DropShadow shadowTitolo = new DropShadow();
		shadowTitolo.setOffsetY(5.0);
		shadowTitolo.setColor(Color.WHITE);
		eTitolo.setEffect(shadowTitolo);
		DropShadow shadowTesto = new DropShadow();
		shadowTesto.setOffsetY(2.0);
		shadowTesto.setColor(Color.YELLOW);
		ePunteggio.setEffect(shadowTesto);
		eVittoria.setEffect(shadowTesto);
		DropShadow shadowSconfitta = new DropShadow();
		shadowSconfitta.setOffsetY(5.0);
		shadowSconfitta.setColor(Color.RED);
		eSconfitta.setEffect(shadowSconfitta);

		bGioca.setOnAction(e-> iniziaPartita());
		bRigioca.setOnAction(e-> iniziaPartita());
		bComandi.setOnAction(e-> comandi());
		bMenu.setOnAction(e-> menu());

		pannello.getStylesheets().add("it/edu/iisgubbio/gioco/Snake.css");
		Scene scena = new Scene(pannello);
		finestra.setTitle("Snake");
		finestra.setScene(scena);
		scena.setOnKeyPressed(e -> pigiato(e));
		finestra.show();
	}
	public void menu() {
		grandezza=15;
		dimensioneCampo=grandezza*30;
		pannello.setPrefSize(dimensioneCampo, dimensioneCampo+80);
		eVolume.setLayoutX(dimensioneCampo-45);
		finestraRidimensionata.sizeToScene();
		ePunteggio.setVisible(false);
		eSconfitta.setVisible(false);
		eVittoria.setVisible(false);
		bRigioca.setVisible(false);
		bMenu.setVisible(false);
		rettangoloComandi.setVisible(false);
		eComandiW.setVisible(false);
		eComandiA.setVisible(false);
		eComandiS.setVisible(false);
		eComandiD.setVisible(false);
		eComandiE.setVisible(false);
		eRegole.setVisible(false);

		bGioca.setVisible(true);
		eTitolo.setVisible(true);
		eVelocità.setVisible(true);
		sVelocità.setVisible(true);
		eGrandezza.setVisible(true);
		sGrandezza.setVisible(true);
		cPacman.setVisible(true);
		rettangoloMenu.setVisible(true);
		bGioca.setVisible(true);
		bComandi.setVisible(true);
	}
	public void comandi() {
		bGioca.setVisible(false);
		eTitolo.setVisible(false);
		eVelocità.setVisible(false);
		sVelocità.setVisible(false);
		eGrandezza.setVisible(false);
		sGrandezza.setVisible(false);
		cPacman.setVisible(false);
		rettangoloMenu.setVisible(false);
		bGioca.setVisible(false);
		bComandi.setVisible(false);

		bMenu.setVisible(true);
		rettangoloComandi.setVisible(true);
		eComandiW.setVisible(true);
		eComandiA.setVisible(true);
		eComandiS.setVisible(true);
		eComandiD.setVisible(true);
		eComandiE.setVisible(true);
		eRegole.setVisible(true);

		bMenu.setLayoutX(dimensioneCampo/2-65);
		bMenu.setLayoutY(375);
	}
	public void iniziaPartita() {
		sconfitta.stop();
		tastiPremibili=true;
		ePunteggio.setText("punti: "+ punti);

		bGioca.setVisible(false);
		eTitolo.setVisible(false);
		eVelocità.setVisible(false);
		sVelocità.setVisible(false);
		eGrandezza.setVisible(false);
		sGrandezza.setVisible(false);
		cPacman.setVisible(false);
		rettangoloMenu.setVisible(false);
		eSconfitta.setVisible(false);
		eVittoria.setVisible(false);
		bRigioca.setVisible(false);
		bComandi.setVisible(false);
		bMenu.setVisible(false);
		ePunteggio.setVisible(true);
		//velocità gioco
		int sliderV = (int) sVelocità.getValue();
		switch (sliderV) {
		case 1:
			velocità=450;
			break;
		case 2:
			velocità=300;
			break;
		case 3:
			velocità=150;
			break;
		}
		timeline = new Timeline(new KeyFrame(
				Duration.millis(velocità),
				x -> aggiornaTimer()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		//grandezza campo
		int sliderG = (int) sGrandezza.getValue();
		switch (sliderG) {
		case 1:
			grandezza=7;
			snakeX=0;
			snakeY=3;
			//			eSfondo.setGraphic(immagineSfondoMini);
			break;
		case 2:
			grandezza=15;
			snakeX=1;
			snakeY=7;
			//			eSfondo.setGraphic(immagineSfondoMedio);
			break;
		case 3:
			grandezza=19;
			snakeX=3;
			snakeY=9;
			//			eSfondo.setGraphic(immagineSfondoMaxi);
			break;
		}
		mele = new Boolean [grandezza][grandezza+1];
		serpente = new Boolean [grandezza][grandezza+1];
		campo = new Label [grandezza][grandezza+1];
		uccidiSerpente = new int [grandezza][grandezza+1];

		dimensioneCampo=grandezza*30;
		pannello.setPrefSize(dimensioneCampo, dimensioneCampo+80);
		finestraRidimensionata.sizeToScene();

		eSconfitta.setPrefWidth(360);
		eSconfitta.setLayoutX(dimensioneCampo/2-180);
		eSconfitta.setLayoutY(dimensioneCampo/2-60);

		eVittoria.setPrefWidth(360);
		eVittoria.setLayoutX(dimensioneCampo/2-180);
		eVittoria.setLayoutY(dimensioneCampo/2-60);

		bRigioca.setPrefWidth(130);
		bRigioca.setPrefHeight(40);
		bRigioca.setLayoutX(dimensioneCampo/2-65);
		bRigioca.setLayoutY(dimensioneCampo/2+30);

		eVolume.setLayoutX(dimensioneCampo-45);
		eVolume.setLayoutY(5);
		//effetto pacman
		if( cPacman.isSelected() ) {
			pacman=true;
		}else {
			pacman=false;
		}
		//crea il campo e lo colora
		for(int y=0; y<campo.length+1;y++) {
			for(int x=0; x<campo.length;x++) {
				campo[x][y]= new Label();
				griglia.add(campo[x][y], x, y);
				campo[x][y].setPrefSize(30, 30);
				int colorX=x;
				if(y%2==0) {
					colorX+=1;
				}
				if(colorX%2==0) {
					campo[x][y].getStyleClass().add("campoChiaro");
				} else {
					campo[x][y].getStyleClass().add("campoScuro");
				}
			}
		}
		//crea la griglia del serpente
		for(int y=0; y<serpente.length+1;y++) {
			for(int x=0; x<serpente.length;x++) {
				serpente[x][y]= false;
			}
		}
		//crea la griglia delle mele
		for(int y=0; y<mele.length+1;y++) {
			for(int x=0; x<mele.length;x++) {
				mele[x][y]= false;
			}
		}
		//crea la griglia uccidiserpente
		for(int y=0; y<mele.length+1;y++) {
			for(int x=0; x<mele.length;x++) {
				uccidiSerpente[x][y]=0;
			}
		}
		//genera la prima mela
		mele[grandezza-snakeX-2][snakeY] = true;
		campo[grandezza-snakeX-2][snakeY].setGraphic(immagineMela);
	}
	private void aggiornaTimer() {
		int codaX=0;
		int codaY=0;
		int coda=0;
		//cambio colore serpente
		if(rBoolean) {
			coloreR+=20;
		} else {
			coloreR-=20;
		}
		if(coloreR>=240) {
			rBoolean=false;
		} else {
			if (coloreR<=20)
				rBoolean=true;
		}
		if(bBoolean) {
			coloreB+=20;
		} else {
			coloreB-=20;
		}
		coloreB+=10;
		if(coloreB>=240) {
			bBoolean=false;
		} else {
			if (coloreB<=20)
				bBoolean=true;
		}
		//conteggio punti e generazione nuova mela
		if (mele[snakeX][snakeY]) {
			mele[snakeX][snakeY] = false;
			melaMangiata.play();
			int meleLibere[];
			int i=0;
			meleLibere =new int[grandezza*(grandezza+1)];
			for(int y=0; y<mele.length+1;y++) {
				for(int x=0; x<mele.length;x++) {
					if(serpente[x][y]==false) {
						meleLibere[i]=x*100+y;
						i++;

					}
				}
			}
			int indiceMela=(int)(Math.random() * i);
			melaX=meleLibere[indiceMela]/100;
			melaY=meleLibere[indiceMela]%100;
			mele[melaX][melaY] = true;
			campo[melaX][melaY].setGraphic(immagineMela);
			campo[snakeX][snakeY].setGraphic(null);
			punti+=1;
			ePunteggio.setText("punti: "+ punti);
		}else {
			//coda del serpente
			for(int y=0; y<mele.length+1;y++) {
				for(int x=0; x<mele.length;x++) {
					if(serpente[x][y]==true) {
						if(uccidiSerpente[x][y]>coda) {
							coda=uccidiSerpente[x][y];
							codaX=x;
							codaY=y;
						}
					}
				}
			}
			serpente[codaX][codaY]=false;
			campo[codaX][codaY].setStyle("");
			uccidiSerpente[codaX][codaY]=0;
		}
		//aumento griglia della coda
		for(int y=0; y<mele.length+1;y++) {
			for(int x=0; x<mele.length;x++) {
				if
				(serpente[x][y]==true) {
					uccidiSerpente[x][y]+=1;
					campo[snakeX][snakeY].setGraphic(null);
				}
			}
		}
		
		//vittoria
		boolean bloccaMorte=false;
		if(punti==grandezza*(grandezza+1)-2) {
			timeline.stop();
			vittoria.play();
			schermataVittoria();
			bloccaMorte=true;
		}
		//movimento
		if(pacman) {
			if (alto && !basso) {
				snakeY-=1;
				if (snakeY==-1) {
					snakeY=grandezza;
				}
				serpente[snakeX][snakeY]=true;
				campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
				campo[snakeX][snakeY].setGraphic(immagineOcchiAlto);
			}
			if (basso && !alto) {
				snakeY+=1;
				if(snakeY==grandezza+1) {
					snakeY=0;
				}
				serpente[snakeX][snakeY]=true;
				campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
				campo[snakeX][snakeY].setGraphic(immagineOcchiBasso);
			}
			if (sinistra && !destra) {
				snakeX-=1;
				if(snakeX==-1) {
					snakeX=grandezza-1;
				}
				serpente[snakeX][snakeY]=true;
				campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
				campo[snakeX][snakeY].setGraphic(immagineOcchiSinistra);
			}
			if (destra && !sinistra) {
				snakeX+=1;
				if(snakeX==grandezza) {
					snakeX=0;
				}
				serpente[snakeX][snakeY]=true;
				campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
				campo[snakeX][snakeY].setGraphic(immagineOcchiDestra);
			}
		}else {
			if (alto && !basso) {
				snakeY-=1;
				if (snakeY!=-1) {
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(immagineOcchiAlto);
				}
			}
			if (basso && !alto) {
				snakeY+=1;
				if(snakeY!=grandezza+1) {
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(immagineOcchiBasso);
				}
			}
			if (sinistra && !destra) {
				snakeX-=1;
				if(snakeX!=-1) {
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(immagineOcchiSinistra);
				}
			}
			if (destra && !sinistra) {
				snakeX+=1;
				if(snakeX!=grandezza) {
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(immagineOcchiDestra);
				}
			}
		}
		if(!bloccaMorte) {
			//sconfitta
			if(snakeY==grandezza+1 || snakeY==-1 || snakeX==grandezza || snakeX==-1) {
				timeline.stop();
				schermataSconfitta();
				musica.stop();
				//			sconfitta.play();
			}
			//sconfitta suicidio
			if(uccidiSerpente[snakeX][snakeY]>1) {
				timeline.stop();
				musica.stop();
				//			sconfitta.play();
				sconfitta.setCycleCount(AudioClip.INDEFINITE);
				schermataSconfitta();
			}
		}
	}
	private void pigiato(KeyEvent evento) {
		if(tastiPremibili==true) {
			boolean blockW=false;
			boolean blockA=false;
			boolean blockS=false;
			boolean blockD=false;
			for(int y=0; y<uccidiSerpente.length+1; y++) {
				for(int x=0; x<uccidiSerpente.length; x++) {
					if(uccidiSerpente[x][y]==1) {
						if(x==snakeX && y<snakeY) {
							blockW=true;
						}
						if(x<snakeX && y==snakeY) {
							blockA=true;
						}
						if(x==snakeX && y>snakeY) {
							blockS=true;
						}
						if(x>snakeX && y==snakeY) {
							blockD=true;
						}
					}
				}
			}
			if(!blockW &&(evento.getText().equals("w") || evento.getText().equals("W") || evento.getCode() == KeyCode.UP)) {
				alto=true;
				basso=false;
				sinistra=false;
				destra=false;
			}
			if(!blockA &&(evento.getText().equals("a") || evento.getText().equals("A") || evento.getCode() == KeyCode.LEFT)) {
				alto=false;
				basso=false;
				sinistra=true;
				destra=false;
			}
			if(!blockS &&(evento.getText().equals("s") || evento.getText().equals("S") || evento.getCode() == KeyCode.DOWN)) {
				alto=false;
				basso=true;
				sinistra=false;
				destra=false;
			}
			if(!blockD &&(evento.getText().equals("d") || evento.getText().equals("D") || evento.getCode() == KeyCode.RIGHT)) {
				alto=false;
				basso=false;
				sinistra=false;
				destra=true;
			}
		}
		if(evento.getText().equals("e") || evento.getText().equals("E")) {
			if(eVolume.getGraphic()==immagineVolume) {
				eVolume.setGraphic(immagineVolumeMutato);
				musica.stop();
			} else {
				eVolume.setGraphic(immagineVolume);
				musica.play();
			}
		}
	}
	private void schermataSconfitta() {
		for(int y=0; y<campo.length+1;y++) {
			for(int x=0; x<campo.length;x++) {
				campo[x][y].setVisible(false);
			}
		}
		eSconfitta.setVisible(true);
		bRigioca.setVisible(true);
		bMenu.setVisible(true);
		bMenu.setLayoutX(dimensioneCampo/2-65);
		bMenu.setLayoutY(dimensioneCampo/2+80);
		snakeX=2;
		snakeY=grandezza+1/2;
		punti=0;
		alto=false;
		basso=false;
		sinistra=false;
		destra=true;
	}
	private void schermataVittoria() {
		for(int y=0; y<campo.length+1;y++) {
			for(int x=0; x<campo.length;x++) {
				campo[x][y].setVisible(false);
			}
		}
		eVittoria.setVisible(true);
		bRigioca.setVisible(true);
		bMenu.setVisible(true);
		bMenu.setLayoutX(dimensioneCampo/2-65);
		bMenu.setLayoutY(dimensioneCampo/2+80);
		snakeX=2;
		snakeY=grandezza+1/2;
		punti=0;
		alto=false;
		basso=false;
		sinistra=false;
		destra=true;
	}
	public static void main(String[] args) {
		launch(args);
	}
}