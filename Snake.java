package it.edu.iisgubbio.gioco;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Snake extends Application {
	GridPane griglia = new GridPane();
	Pane pannello = new Pane();
	Pane pannelloGioco = new Pane();
	Pane pannelloComandi = new Pane();
	Pane pannelloMenu = new Pane();
	Label eSfondo = new Label();
	Label eTitolo = new Label("Snake");
	Button bGioca = new Button("Gioca");
	Label ePunteggio = new Label();
	Label eSconfitta = new Label("GAME OVER");
	Label eVittoria = new Label("HAI VINTO");
	Button bRigioca = new Button("rigioca");
	Label eVolume = new Label();
	Label eVelocità = new Label("velocità serpente");
	Slider sVelocità = new Slider(1, 3, 2);
	Label eGrandezza = new Label("grandezza campo");
	Slider sGrandezza = new Slider(1, 3, 2);
	CheckBox cPacman = new CheckBox("effetto pacman");
	Button bComandi = new Button("comandi");
	Button bMenu = new Button("menu");
	Region rettangoloMenu= new Region();
	Region rettangoloComandi= new Region();
	Label eComandiW = new Label("W o freccia su: Muovi il serpente verso l'alto");
	Label eComandiA = new Label("A o freccia sinistra: Muovi il serpente a sinistra");
	Label eComandiS = new Label("S o freccia giù: Muovi il serpente verso il basso");
	Label eComandiD = new Label("D o freccia destra: Muovi il serpente a destra");
	Label eComandiE = new Label("E: Muta o riattiva i suoni");
	Label eRegole = new Label("Come si gioca: L'obbiettivo del gioco è mangiare le mele\n"
			+ "per far crescere il serpente, fino ad occupare tutta la\n"
			+ "mappa, attento a non sbattere contro te stesso o contro\n"
			+ "i muri, in caso avessi difficoltà puoi provare la modalità\n"
			+ "pacman nella quale andando contro un muro ti ritroverai\n"
			+ "dal lato opposto della mappa!");

	int coloreR=(int)(Math.random()*220)+20;
	int coloreB=(int)(Math.random()*220)+20;
	Boolean rBoolean=true;
	Boolean bBoolean=true;

	int velocità;
	int grandezzaX=16;
	int grandezzaY=15;
	int dimensioneCampoX=grandezzaX*30;
	int dimensioneCampoY=grandezzaY*30;
	int punti=0;
	int snakeX;
	int snakeY;
	int melaX;
	int melaY;

	Boolean mele[][];
	Boolean serpente[][];
	Label campo[][];
	int codaSerpente[][];

	boolean alto=false;
	boolean basso=false;
	boolean sinistra=false;
	boolean destra=true;
	boolean pacman=false;
	boolean tastiPremibili=false;
	boolean volume=true;

	final Image I_VOLUME = new Image(getClass().getResourceAsStream("VolumeAttivo.png"));
	final ImageView IMMAGINE_VOLUME= new ImageView(I_VOLUME);
	final Image I_VOLUME_MUTATO = new Image(getClass().getResourceAsStream("VolumeMutato.png"));
	final ImageView IMMAGINE_VOLUME_MUTATO = new ImageView(I_VOLUME_MUTATO);

	final Image I_MELA = new Image(getClass().getResourceAsStream("Mela.png"));
	final ImageView IMMAGINE_MELA = new ImageView(I_MELA);
	final Image I_OCCHI_ALTO = new Image(getClass().getResourceAsStream("OcchiAlto.png"));
	final ImageView IMMAGINE_OCCHI_ALTO = new ImageView(I_OCCHI_ALTO);
	final Image I_OCCHI_SINISTRA = new Image(getClass().getResourceAsStream("OcchiSinistra.png"));
	final ImageView IMMAGINE_OCCHI_SINISTRA = new ImageView(I_OCCHI_SINISTRA);
	final Image I_OCCHI_BASSO = new Image(getClass().getResourceAsStream("OcchiBasso.png"));
	final ImageView IMMAGINE_OCCHI_BASSO = new ImageView(I_OCCHI_BASSO);
	final Image I_OCCHI_DESTRA = new Image(getClass().getResourceAsStream("OcchiDestra.png"));
	final ImageView IMMAGINE_OCCHI_DESTRA = new ImageView(I_OCCHI_DESTRA);

	final Image I_SFONDO_PICCOLO = new Image(getClass().getResourceAsStream("SfondoCampoPiccolo.png"));
	final ImageView IMMAGINE_SFONDO_CAMPO_PICCOLO = new ImageView(I_SFONDO_PICCOLO);
	final Image I_SFONDO_MEDIO = new Image(getClass().getResourceAsStream("SfondoCampoMedio.png"));
	final ImageView IMMAGINE_SFONDO_CAMPO_MEDIO = new ImageView(I_SFONDO_MEDIO);
	final Image I_SFONDO_GRANDE = new Image(getClass().getResourceAsStream("SfondoCampoGrande.png"));
	final ImageView IMMAGINE_SFONDO_CAMPO_GRANDE = new ImageView(I_SFONDO_GRANDE);

	final AudioClip musica= new AudioClip(getClass().getResource("MusicaSottofondo.mp3").toString());
	final AudioClip melaMangiata= new AudioClip(getClass().getResource("MorsoMela.mp3").toString());
	final AudioClip vittoria= new AudioClip(getClass().getResource("SuonoVittoria.mp3").toString());
	final AudioClip sconfitta= new AudioClip(getClass().getResource("SuonoSconfitta.mp3").toString());
	Timeline timeline;
	Stage finestraRidimensionata;

	public void start(Stage finestra) {
		//fa partire la musica di sottofondo
		musica.setVolume(0.7);
		musica.play();
		musica.setCycleCount(AudioClip.INDEFINITE);
		eVolume.setGraphic(IMMAGINE_VOLUME);
		eSfondo.setGraphic(IMMAGINE_SFONDO_CAMPO_MEDIO);
		//ridimensiona le immagini
		IMMAGINE_MELA.setFitHeight(30);
		IMMAGINE_MELA.setPreserveRatio(true);
		IMMAGINE_OCCHI_ALTO.setFitHeight(30);
		IMMAGINE_OCCHI_ALTO.setPreserveRatio(true);
		IMMAGINE_OCCHI_SINISTRA.setFitHeight(30);
		IMMAGINE_OCCHI_SINISTRA.setPreserveRatio(true);
		IMMAGINE_OCCHI_BASSO.setFitHeight(30);
		IMMAGINE_OCCHI_BASSO.setPreserveRatio(true);
		IMMAGINE_OCCHI_DESTRA.setFitHeight(30);
		IMMAGINE_OCCHI_DESTRA.setPreserveRatio(true);
		IMMAGINE_VOLUME.setFitHeight(40);
		IMMAGINE_VOLUME.setPreserveRatio(true);
		IMMAGINE_VOLUME_MUTATO.setFitHeight(40);
		IMMAGINE_VOLUME_MUTATO.setPreserveRatio(true);
		//ridimensiona i pannelli e aggiunge gli elementi ai pannelli relativi
		pannello.setPrefSize(dimensioneCampoX, dimensioneCampoY+50);
		pannelloGioco.setPrefSize(dimensioneCampoX, dimensioneCampoY+50);
		pannelloMenu.setPrefSize(dimensioneCampoX, dimensioneCampoY+50);
		pannelloComandi.setPrefSize(dimensioneCampoX, dimensioneCampoY+50);

		pannello.getChildren().add(eSfondo);
		pannello.getChildren().add(pannelloGioco);
		pannello.getChildren().add(pannelloMenu);
		pannello.getChildren().add(pannelloComandi);
		pannello.getChildren().add(eVolume);
		pannello.getChildren().add(bMenu);

		pannelloGioco.getChildren().add(ePunteggio);
		pannelloGioco.getChildren().add(griglia);
		pannelloGioco.getChildren().add(eSconfitta);
		pannelloGioco.getChildren().add(eVittoria);
		pannelloGioco.getChildren().add(bRigioca);

		pannelloMenu.getChildren().add(eTitolo);
		pannelloMenu.getChildren().add(bGioca);
		pannelloMenu.getChildren().add(rettangoloMenu);
		pannelloMenu.getChildren().add(eVelocità);
		pannelloMenu.getChildren().add(sVelocità);
		pannelloMenu.getChildren().add(eGrandezza);
		pannelloMenu.getChildren().add(sGrandezza);
		pannelloMenu.getChildren().add(cPacman);
		pannelloMenu.getChildren().add(bComandi);

		pannelloComandi.getChildren().add(rettangoloComandi);
		pannelloComandi.getChildren().add(eComandiW);
		pannelloComandi.getChildren().add(eComandiA);
		pannelloComandi.getChildren().add(eComandiS);
		pannelloComandi.getChildren().add(eComandiD);
		pannelloComandi.getChildren().add(eComandiE);
		pannelloComandi.getChildren().add(eRegole);

		pannelloGioco.setVisible(false);
		bMenu.setVisible(false);
		pannelloComandi.setVisible(false);
		//posiziona i vari elementi
		griglia.setLayoutY(50);

		eTitolo.setPrefWidth(220);
		eTitolo.setLayoutX(dimensioneCampoX/2-110);
		eTitolo.setLayoutY(60);

		bGioca.setPrefWidth(180);
		bGioca.setPrefHeight(40);
		bGioca.setLayoutX(dimensioneCampoX/2-90);
		bGioca.setLayoutY(140);

		eVelocità.setPrefWidth(130);
		eVelocità.setLayoutX(dimensioneCampoX/2-65);
		eVelocità.setLayoutY(200);

		sVelocità.setPrefWidth(130);
		sVelocità.setLayoutX(dimensioneCampoX/2-65);
		sVelocità.setLayoutY(230);

		eGrandezza.setPrefWidth(130);
		eGrandezza.setLayoutX(dimensioneCampoX/2-65);
		eGrandezza.setLayoutY(260);

		sGrandezza.setPrefWidth(130);
		sGrandezza.setLayoutX(dimensioneCampoX/2-65);
		sGrandezza.setLayoutY(290);

		cPacman.setLayoutX(dimensioneCampoX/2-65);
		cPacman.setLayoutY(335);

		eVolume.setLayoutX(dimensioneCampoX-45);
		eVolume.setLayoutY(5);

		rettangoloMenu.setPrefWidth(170);
		rettangoloMenu.setPrefHeight(240);
		rettangoloMenu.setLayoutX(dimensioneCampoX/2-85);
		rettangoloMenu.setLayoutY(190);
		rettangoloMenu.setOpacity(0.4);
		rettangoloMenu.getStyleClass().add("rettangolo");

		rettangoloComandi.setPrefWidth(400);
		rettangoloComandi.setPrefHeight(300);
		rettangoloComandi.setLayoutX(dimensioneCampoX/2-200);
		rettangoloComandi.setLayoutY(60);
		rettangoloComandi.setOpacity(0.4);
		rettangoloComandi.getStyleClass().add("rettangolo");

		ePunteggio.setLayoutX(5);
		ePunteggio.setLayoutY(5);

		bComandi.setPrefWidth(130);
		bComandi.setPrefHeight(40);
		bComandi.setLayoutX(dimensioneCampoX/2-65);
		bComandi.setLayoutY(375);

		bMenu.setPrefWidth(130);
		bMenu.setPrefHeight(40);
		bMenu.setLayoutX(dimensioneCampoX/2-65);
		bMenu.setLayoutY(375);

		eComandiW.setPrefWidth(380);
		eComandiW.setPrefHeight(40);
		eComandiW.setLayoutX(dimensioneCampoX/2-190);
		eComandiW.setLayoutY(60);

		eComandiA.setPrefWidth(380);
		eComandiA.setPrefHeight(40);
		eComandiA.setLayoutX(dimensioneCampoX/2-190);
		eComandiA.setLayoutY(90);

		eComandiS.setPrefWidth(380);
		eComandiS.setPrefHeight(40);
		eComandiS.setLayoutX(dimensioneCampoX/2-190);
		eComandiS.setLayoutY(120);

		eComandiD.setPrefWidth(380);
		eComandiD.setPrefHeight(40);
		eComandiD.setLayoutX(dimensioneCampoX/2-190);
		eComandiD.setLayoutY(150);

		eComandiE.setPrefWidth(380);
		eComandiE.setPrefHeight(40);
		eComandiE.setLayoutX(dimensioneCampoX/2-190);
		eComandiE.setLayoutY(180);

		eRegole.setPrefWidth(380);
		eRegole.setPrefHeight(130);
		eRegole.setLayoutX(dimensioneCampoX/2-190);
		eRegole.setLayoutY(210);

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
		//aggiunge un font ai testi
		Font fontTitolo = Font.loadFont(getClass().getResource("retro_computer_personal_use.ttf").toString(), 50);
		Font fontTesto = Font.loadFont(getClass().getResource("Pixellari.ttf").toString(), 15);
		Font fontPunti = Font.loadFont(getClass().getResource("retro_computer_personal_use.ttf").toString(), 25);

		eTitolo.setFont(fontTitolo);
		eTitolo.setTextFill(Color.BLACK);
		ePunteggio.setFont(fontPunti);
		ePunteggio.setTextFill(Color.BLACK);
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
		//aggiunge un'ombra ai testi
		DropShadow shadowTitolo = new DropShadow();
		shadowTitolo.setOffsetY(5.0);
		shadowTitolo.setColor(Color.WHITE);
		DropShadow shadowTesto = new DropShadow();
		shadowTesto.setOffsetY(2.0);
		shadowTesto.setColor(Color.YELLOW);
		DropShadow shadowSconfitta = new DropShadow();
		shadowSconfitta.setOffsetY(5.0);
		shadowSconfitta.setColor(Color.RED);

		eTitolo.setEffect(shadowTitolo);
		ePunteggio.setEffect(shadowTesto);
		eVittoria.setEffect(shadowTesto);
		eSconfitta.setEffect(shadowSconfitta);

		bGioca.setOnAction(e-> iniziaPartita());
		bRigioca.setOnAction(e-> iniziaPartita());
		bComandi.setOnAction(e-> comandi());
		bMenu.setOnAction(e-> menu());

		pannello.getStylesheets().add("it/edu/iisgubbio/gioco/Snake.css");
		finestraRidimensionata=finestra;
		Scene scena = new Scene(pannello);
		finestra.setTitle("Snake");
		finestra.setScene(scena);
		scena.setOnKeyPressed(e -> pigiato(e));
		finestra.show();
	}
	//porta al menu, ridimensiona la finestra di conseguenza
	public void menu() {
		if(volume && !musica.isPlaying()) {
			musica.play();
		}
		sconfitta.stop();
		pannelloMenu.setVisible(true);
		pannelloComandi.setVisible(false);
		pannelloGioco.setVisible(false);
		bMenu.setVisible(false);

		grandezzaX=16;
		grandezzaY=15;
		dimensioneCampoX=grandezzaX*30;
		dimensioneCampoY=grandezzaY*30;
		//reimposta la direzione del serpente in caso si passi al menù quando si finisce una partita e si clicca sul bottone menu
		alto=false;
		basso=false;
		sinistra=false;
		destra=true;

		eSfondo.setGraphic(IMMAGINE_SFONDO_CAMPO_MEDIO);
		pannello.setPrefSize(dimensioneCampoX, dimensioneCampoY+50);
		finestraRidimensionata.sizeToScene();
		eVolume.setLayoutX(dimensioneCampoX-45);
	}
	//porta alla sezione dei comandi
	public void comandi() {
		if(volume && !musica.isPlaying()) {
			musica.play();
		}
		pannelloComandi.setVisible(true);
		pannelloMenu.setVisible(false);
		bMenu.setVisible(true);
		bMenu.setLayoutX(dimensioneCampoX/2-65);
		bMenu.setLayoutY(375);
	}
	//prepara le varie matrici e le variabili per una nuova partita
	public void iniziaPartita() {
		if(volume && !musica.isPlaying()) {
			musica.play();
		}
		Font fontWL=(null);
		tastiPremibili=true;
		ePunteggio.setText("punti: "+ punti);
		sconfitta.stop();

		pannelloGioco.setVisible(true);
		pannelloMenu.setVisible(false);
		eSconfitta.setVisible(false);
		eVittoria.setVisible(false);
		bRigioca.setVisible(false);
		bMenu.setVisible(false);
		//imposta la velocità del serpente
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

		//imposta la grandezza del campo e ne imposta lo sfondo relativo
		int sliderG = (int) sGrandezza.getValue();
		switch (sliderG) {
		case 1:
			grandezzaX=8;
			grandezzaY=7;
			snakeX=0;
			snakeY=3;
			eSfondo.setGraphic(IMMAGINE_SFONDO_CAMPO_PICCOLO);
			break;
		case 2:
			grandezzaX=16;
			grandezzaY=15;
			snakeX=1;
			snakeY=7;
			eSfondo.setGraphic(IMMAGINE_SFONDO_CAMPO_MEDIO);
			break;
		case 3:
			grandezzaX=20;
			grandezzaY=19;
			snakeX=3;
			snakeY=9;
			eSfondo.setGraphic(IMMAGINE_SFONDO_CAMPO_GRANDE);
			break;
		}
		mele = new Boolean [grandezzaX][grandezzaY];
		serpente = new Boolean [grandezzaX][grandezzaY];
		campo = new Label [grandezzaX][grandezzaY];
		codaSerpente = new int [grandezzaX][grandezzaY];

		dimensioneCampoX=grandezzaX*30;
		dimensioneCampoY=grandezzaY*30;
		pannello.setPrefSize(dimensioneCampoX, dimensioneCampoY+50);
		finestraRidimensionata.sizeToScene();

		//in caso il campo sia piccolo ridimensiona la scritta
		if(sliderG==1) {
			fontWL = Font.loadFont(getClass().getResource("retro_computer_personal_use.ttf").toString(), 25);
			eSconfitta.setFont(fontWL);
			eVittoria.setFont(fontWL);
			eSconfitta.setLayoutY(dimensioneCampoY/2-40);
			eVittoria.setLayoutY(dimensioneCampoY/2-40);
		} else {
			fontWL = Font.loadFont(getClass().getResource("retro_computer_personal_use.ttf").toString(), 50);
			eSconfitta.setFont(fontWL);
			eVittoria.setFont(fontWL);
			eSconfitta.setLayoutY(dimensioneCampoY/2-90);
			eVittoria.setLayoutY(dimensioneCampoY/2-90);
		}
		eSconfitta.setPrefWidth(360);
		eSconfitta.setLayoutX(dimensioneCampoX/2-180);
		eSconfitta.setTextFill(Color.BLACK);

		eVittoria.setPrefWidth(360);
		eVittoria.setLayoutX(dimensioneCampoX/2-180);
		eVittoria.setTextFill(Color.BLACK);

		bRigioca.setPrefWidth(130);
		bRigioca.setPrefHeight(40);
		bRigioca.setLayoutX(dimensioneCampoX/2-65);
		bRigioca.setLayoutY(dimensioneCampoY/2+30);

		eVolume.setLayoutX(dimensioneCampoX-45);
		eVolume.setLayoutY(5);
		//controlla se effetto pacman è attivo
		if( cPacman.isSelected() ) {
			pacman=true;
		}else {
			pacman=false;
		}
		//crea il campo e alterna stile chiaro e scuro ad ognuno per ricreare l'effetto a scacchiera
		for(int y=0; y<grandezzaY;y++) {
			for(int x=0; x<grandezzaX;x++) {
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
		for(int y=0; y<grandezzaY;y++) {
			for(int x=0; x<grandezzaX;x++) {
				serpente[x][y]= false;
			}
		}
		//crea la griglia delle mele
		for(int y=0; y<grandezzaY;y++) {
			for(int x=0; x<grandezzaX;x++) {
				mele[x][y]= false;
			}
		}
		//crea la griglia di che controlla quale è la coda del serpente
		for(int y=0; y<grandezzaY;y++) {
			for(int x=0; x<grandezzaX;x++) {
				codaSerpente[x][y]=0;
			}
		}
		//genera la prima mela
		mele[grandezzaX-snakeX-2][snakeY] = true;
		campo[grandezzaX-snakeX-2][snakeY].setGraphic(IMMAGINE_MELA);
	}
	private void aggiornaTimer() {
		int codaX=0;
		int codaY=0;
		int coda=0;
		//controlla se il giocatore ha vinto, se il giocatore ha vinto bloccaMorte diventa true ed evita che il serpente si possa muovere ulteriormente dopo aver vinto
		boolean bloccaMorte=false;
		if(punti==(grandezzaX*grandezzaY)-2) {
			timeline.stop();
			if(volume) {
				vittoria.play();
			}
			schermataVittoria();
			bloccaMorte=true;
		}
		if(!bloccaMorte) {
			//cambia il colore del serpente cambiando i valori R e B del colore rgb
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
				if(volume) {
					melaMangiata.play();
				}	
				int meleLibere[];
				int i=0;
				//vettore in cui vengono inserite le posizioni in cui la mela può essere generata
				meleLibere =new int[grandezzaX*grandezzaY];
				for(int y=0; y<grandezzaY;y++) {
					for(int x=0; x<grandezzaX;x++) {
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
				campo[melaX][melaY].setGraphic(IMMAGINE_MELA);
				campo[snakeX][snakeY].setGraphic(null);
				punti+=1;
				ePunteggio.setText("punti: "+ punti);
			}else {
				//quando il serpente non prende una mela viene eseguito questo codice che rimuove il pezzo di serpente presente da più tempo, cioè la coda
				//quando invece prende una mela non lo esegue, allungando il serpente
				for(int y=0; y<grandezzaY;y++) {
					for(int x=0; x<grandezzaX;x++) {
						if(serpente[x][y]==true) {
							if(codaSerpente[x][y]>coda) {
								coda=codaSerpente[x][y];
								codaX=x;
								codaY=y;
							}
						}
					}
				}
				serpente[codaX][codaY]=false;
				campo[codaX][codaY].setStyle("");
				codaSerpente[codaX][codaY]=0;
			}
			//aumenta di 1 il valore di ogni pezzo di serpente nella matrice codaSerpente
			for(int y=0; y<grandezzaY;y++) {
				for(int x=0; x<grandezzaX;x++) {
					if
					(serpente[x][y]==true) {
						codaSerpente[x][y]+=1;
						campo[snakeX][snakeY].setGraphic(null);
					}
				}
			}
			//controlla quale boolean del movimento è attiva e inoltre controlla se effetto pacman è attivo
			//in caso sia così al posto di non far muovere il serpente quando si trova nel muro lo teletrasporta dall'altro lato del campo
			if (alto) {
				snakeY-=1;
				if (snakeY==-1) {
					if (pacman) {
						snakeY=grandezzaY-1;
						serpente[snakeX][snakeY]=true;
						campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
						campo[snakeX][snakeY].setGraphic(IMMAGINE_OCCHI_ALTO);
					}
				} else {
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(IMMAGINE_OCCHI_ALTO);
				}
			}
			if (basso) {
				snakeY+=1;
				if(snakeY==grandezzaY) {
					if (pacman) {
						snakeY=0;
						serpente[snakeX][snakeY]=true;
						campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
						campo[snakeX][snakeY].setGraphic(IMMAGINE_OCCHI_BASSO);
					}
				} else {
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(IMMAGINE_OCCHI_BASSO);
				}
			}
			if (sinistra) {
				snakeX-=1;
				if(snakeX==-1) {
					if (pacman) {
						snakeX=grandezzaX-1;
						serpente[snakeX][snakeY]=true;
						campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
						campo[snakeX][snakeY].setGraphic(IMMAGINE_OCCHI_SINISTRA);
					}
				} else {
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(IMMAGINE_OCCHI_SINISTRA);
				}
			}
			if (destra) {
				snakeX+=1;
				if(snakeX==grandezzaX) {
					if (pacman) {
						snakeX=0;
						serpente[snakeX][snakeY]=true;
						campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
						campo[snakeX][snakeY].setGraphic(IMMAGINE_OCCHI_DESTRA);
					}
				} else {
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(IMMAGINE_OCCHI_DESTRA);
				}
			}
			//controlla se il serpente è ha sbattuto contro un muro 
			if(snakeY==grandezzaY || snakeY==-1 || snakeX==grandezzaX || snakeX==-1) {
				timeline.stop();
				schermataSconfitta();
				musica.stop();
				if(volume) {
					sconfitta.play();
				}
			}
			//controlla se il serpente ha sbattuto contro se stesso
			if(codaSerpente[snakeX][snakeY]>1) {
				timeline.stop();
				musica.stop();
				if(volume) {
					sconfitta.play();
				}
				sconfitta.setCycleCount(AudioClip.INDEFINITE);
				schermataSconfitta();
			}
		}
	}
	private void pigiato(KeyEvent evento) {
		//fa in modo che non si possa far muovere il serpente prima di avviare la partita
		if(tastiPremibili==true) {
			boolean blockW=false;
			boolean blockA=false;
			boolean blockS=false;
			boolean blockD=false;
			for(int y=0; y<grandezzaY; y++) {
				for(int x=0; x<grandezzaX; x++) {
					if(codaSerpente[x][y]==1) {
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
			//controlla i pulsanti premuti e imposta la direzione del serpente in relazione ad esso
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
		//muta la musica di sottofondo
		if(evento.getText().equals("e") || evento.getText().equals("E")) {
			if(eVolume.getGraphic()==IMMAGINE_VOLUME) {
				eVolume.setGraphic(IMMAGINE_VOLUME_MUTATO);
				vittoria.stop();
				sconfitta.stop();
				musica.stop();
				volume=false;
			} else {
				eVolume.setGraphic(IMMAGINE_VOLUME);
				musica.play();
				volume=true;
			}
		}
	}
	//schermata della sconfitta
	private void schermataSconfitta() {
		//disabilita la matrice del campo, riporta il serpente alla sua posizione originale e lo ridireziona
		for(int y=0; y<grandezzaY;y++) {
			for(int x=0; x<grandezzaX;x++) {
				campo[x][y].setVisible(false);
			}
		}
		eSconfitta.setVisible(true);
		bRigioca.setVisible(true);
		bMenu.setVisible(true);
		bMenu.setLayoutX(dimensioneCampoX/2-65);
		bMenu.setLayoutY(dimensioneCampoY/2+80);
		snakeX=2;
		snakeY=grandezzaY/2;
		punti=0;
		alto=false;
		basso=false;
		sinistra=false;
		destra=true;
	}
	//schermata della vittoria
	private void schermataVittoria() {
		//disabilita la matrice del campo, riporta il serpente alla sua posizione originale e lo ridireziona
		for(int y=0; y<grandezzaY;y++) {
			for(int x=0; x<grandezzaX;x++) {
				campo[x][y].setVisible(false);
			}
		}
		eVittoria.setVisible(true);
		bRigioca.setVisible(true);
		bMenu.setVisible(true);
		bMenu.setLayoutX(dimensioneCampoX/2-65);
		bMenu.setLayoutY(dimensioneCampoY/2+80);
		snakeX=2;
		snakeY=grandezzaY/2;
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