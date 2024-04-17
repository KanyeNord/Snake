package it.edu.iisgubbio.gioco;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
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
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

//TODO
//TODO di vittoria e sconfitta
//TODO menù principale e personalizzazione
//TODO controllare se si può ottimizzare effetto pacman
//TODO inserire lo sfondo per il punteggio e aggiungere alla barra superiore il tasto per mutare il gioco
//TODO modificare il suono
//TODO sistemare la grandezza della finestra
//TODO aggiungere font
//TODO aggiungi i commenti
//TODO indentare bene

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
	Button bInizia = new Button("inizia");
	Label ePunteggio = new Label("punti: "+punti);
	Label eSconfitta = new Label("GAME OVER");
	Button bRigioca = new Button("rigioca");
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

	int dimensioneCampo=grandezza*30;
	Label eSfondo = new Label();
	final AudioClip musica= new AudioClip(getClass().getResource("Snake.io Music.mp3").toString());

	Timeline timeline;
	Stage finestraRidimensionata;

	public void start(Stage finestra) {
//		if(timeline.getStatus()==Status.RUNNING) {
//			
//		}
		musica.setVolume(50);
				musica.play();
		immagineSfondo.setFitHeight(dimensioneCampo);
		immagineSfondo.setFitWidth(dimensioneCampo);
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

		finestraRidimensionata=finestra;
		eTitolo.setAlignment(Pos.CENTER);


		pannello.setPrefSize(dimensioneCampo, dimensioneCampo);
		pannello.getChildren().add(griglia);
		pannello.getChildren().add(eTitolo);
		pannello.getChildren().add(bInizia);
		pannello.getChildren().add(sVelocità);
		pannello.getChildren().add(sGrandezza);
		pannello.getChildren().add(cPacman);
		pannello.getChildren().add(ePunteggio);
		pannello.getChildren().add(eSconfitta);
		pannello.getChildren().add(bRigioca);
		ePunteggio.setVisible(false);
		eSconfitta.setVisible(false);
		bRigioca.setVisible(false);

		griglia.setLayoutY(50);
		
		eTitolo.setPrefWidth(400);
		eTitolo.setLayoutX(dimensioneCampo-400/2);
		eTitolo.setLayoutY(30);
		eTitolo.setLayoutX(dimensioneCampo/2);
		eTitolo.setLayoutY(30);

		bInizia.setLayoutX(dimensioneCampo/2);
		bInizia.setLayoutY(60);

		sVelocità.setLayoutX(dimensioneCampo/2);
		sVelocità.setLayoutY(90);

		sGrandezza.setLayoutX(dimensioneCampo/2);
		sGrandezza.setLayoutY(120);

		cPacman.setLayoutX(dimensioneCampo/2);
		cPacman.setLayoutY(150);
		
		eSconfitta.setLayoutX(dimensioneCampo/2);
		eSconfitta.setLayoutY(dimensioneCampo/2);

		bRigioca.setLayoutX(dimensioneCampo/2);
		bRigioca.setLayoutY(dimensioneCampo/2+30);

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

		Font font = Font.loadFont(getClass().getResource("retro_computer_personal_use.ttf").toString(), 30);
		eTitolo.setFont(font);
		ePunteggio.setFont(font);
		
		 DropShadow shadowTitolo = new DropShadow();
	      shadowTitolo.setOffsetY(5.0);
	      shadowTitolo.setColor(Color.WHITE);
		 DropShadow shadowTesto = new DropShadow();
	      shadowTesto.setOffsetY(1.0);
	      shadowTesto.setColor(Color.DARKGOLDENROD);
	      eTitolo.setEffect(shadowTitolo);
	      ePunteggio.setEffect(shadowTesto);
			eSconfitta.setEffect(shadowTesto);
		
		bInizia.setOnAction(e-> inizia());
		bRigioca.setOnAction(e-> inizia());

		griglia.setGridLinesVisible(true);
		pannello.getStylesheets().add("it/edu/iisgubbio/gioco/Snake.css");
		Scene scena = new Scene(pannello);
		finestra.setTitle("Snake");
		finestra.setScene(scena);
		
		scena.setOnKeyPressed(e -> pigiato(e));
		finestra.show();
	}
	public void inizia() {
		tastiPremibili=true;
		bInizia.setVisible(false);
		eTitolo.setVisible(false);
		sVelocità.setVisible(false);
		sGrandezza.setVisible(false);
		cPacman.setVisible(false);
		ePunteggio.setVisible(true);
		eSconfitta.setVisible(false);
		bRigioca.setVisible(false);

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
			grandezza=30;
			snakeX=3;
			snakeY=14;
			//			eSfondo.setGraphic(immagineSfondoMaxi);
			break;
		}
		mele = new Boolean [grandezza][grandezza];
		serpente = new Boolean [grandezza][grandezza];
		campo = new Label [grandezza][grandezza];
		uccidiSerpente = new int [grandezza][grandezza];
		dimensioneCampo=grandezza*30;
		//FIXME
		pannello.setPrefSize(dimensioneCampo+20, dimensioneCampo);
		finestraRidimensionata.setWidth(dimensioneCampo+15);
		finestraRidimensionata.setHeight(dimensioneCampo+90);
		//		immagineSfondo.setFitWidth(dimensioneCampo);
		
		//effetto pacman
		if( cPacman.isSelected() ) {
            pacman=true;
        }else {
        	pacman=false;
        }

		//crea il campo e lo colora
		for(int y=0; y<campo.length;y++) {
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
		for(int y=0; y<serpente.length;y++) {
			for(int x=0; x<serpente.length;x++) {
				serpente[x][y]= false;
			}
		}
		//crea la griglia delle mele
		for(int y=0; y<mele.length;y++) {
			for(int x=0; x<mele.length;x++) {
				mele[x][y]= false;
			}
		}
		//crea la griglia uccidiserpente
		for(int y=0; y<mele.length;y++) {
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
			
			
			int meleLibere[];
			int i=0;
			meleLibere =new int[grandezza*grandezza];
			for(int y=0; y<mele.length;y++) {
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
			for(int y=0; y<mele.length;y++) {
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
		for(int y=0; y<mele.length;y++) {
			for(int x=0; x<mele.length;x++) {
				if
				(serpente[x][y]==true) {
					uccidiSerpente[x][y]+=1;
					//					debug
					//					campo[x][y].setText(uccidiSerpente[x][y]+"");
					campo[snakeX][snakeY].setGraphic(null);
				}
			}
		}
		//vittoria
		if(punti==grandezza*grandezza) {
			timeline.stop();
			//			schermataVittoria();
		}
		//movimento
		if(pacman) {
			if (alto && !basso) {
				snakeY-=1;
				if (snakeY==-1) {
					snakeY=grandezza-1;
				}
					serpente[snakeX][snakeY]=true;
					campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
					campo[snakeX][snakeY].setGraphic(immagineOcchiAlto);
			}
			if (basso && !alto) {
				snakeY+=1;
				if(snakeY==grandezza) {
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
			if(snakeY!=grandezza) {
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
		//sconfitta
				if(snakeY==grandezza || snakeY==-1 || snakeX==grandezza || snakeX==-1) {
					timeline.stop();
					schermataSconfitta();
				}
				//sconfitta suicidio
				if(uccidiSerpente[snakeX][snakeY]>1) {
					timeline.stop();
					schermataSconfitta();
	}
		
	}
	private void pigiato(KeyEvent evento) {
		if(tastiPremibili==true) {
		boolean blockW=false;
		boolean blockA=false;
		boolean blockS=false;
		boolean blockD=false;
		for(int y=0; y<uccidiSerpente.length; y++) {
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
		if(evento.getText().equals("e") || evento.getText().equals("E")) {
			musica.stop();
		}
		}
	}
	private void schermataSconfitta() {
		for(int y=0; y<campo.length;y++) {
			for(int x=0; x<campo.length;x++) {
				campo[x][y].setVisible(false);
			}
		}
		pannello.setVisible(true);
		eSconfitta.setVisible(true);
		bRigioca.setVisible(true);

		snakeX=2;
		snakeY=grandezza/2;
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