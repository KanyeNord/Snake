package it.edu.iisgubbio.gioco;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

//TODO
//usare delle immagini oppure disegnarle direttamente
//schermata di vittoria e sconfitta
//menù principale e personalizzazione
//collisioni con il serpente
//sistemare le dimensioni diverse
//sistemare bug mele multiple, forse è sistemato
//effetto pacman
//impostare la posizione iniziale del serpente in base alla grandezza del campo
//fare in modo che una mela non possa generarsi sul serpente
//sistemare il problema del suicidio premendo tanti tasti, usare la griglia uccidiserpente=1
//inserire il punteggio in alto
//modificare il suono
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
	Label ePunteggio = new Label("punti= "+punti);
	Boolean mele[][];
	Boolean serpente[][];
	Label campo[][];
	Integer uccidiSerpente[][];

	Slider sVelocità = new Slider(1, 3, 2);
	Slider sGrandezza = new Slider(1, 3, 2);
	boolean alto=false;
	boolean basso=false;
	boolean sinistra=false;
	boolean destra=true;

	int snakeX;
	int snakeY;


	int dimensioneCampo=grandezza*30;



	Timeline timeline;
	Stage finestra1;

	public void start(Stage finestra) {
		final AudioClip pino= new AudioClip(getClass().getResource("Note Killer.mp3").toString());
		pino.play();

		finestra1=finestra;
		eTitolo.setAlignment(Pos.CENTER);
		griglia.add(pannello, 0, 1, 15, 15);

		pannello.setPrefSize(dimensioneCampo, dimensioneCampo);
		pannello.getChildren().add(eTitolo);
		pannello.getChildren().add(bInizia);
		pannello.getChildren().add(sVelocità);
		pannello.getChildren().add(sGrandezza);

		eTitolo.setLayoutX(dimensioneCampo/2);
		eTitolo.setLayoutY(30);

		bInizia.setLayoutX(dimensioneCampo/2);
		bInizia.setLayoutY(60);

		sVelocità.setLayoutX(dimensioneCampo/2);
		sVelocità.setLayoutY(90);

		sGrandezza.setLayoutX(dimensioneCampo/2);
		sGrandezza.setLayoutY(120);

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

		bInizia.setOnAction(e-> inizia());

		griglia.setPrefSize(dimensioneCampo, dimensioneCampo);
		griglia.setId("griglia");
		griglia.setGridLinesVisible(true);
		griglia.getStylesheets().add("it/edu/iisgubbio/gioco/Snake.css");
		Scene scena = new Scene(griglia);
		finestra.setTitle("Snake");
		finestra.setScene(scena);
		scena.setOnKeyPressed(e -> pigiato(e));
		finestra.show();
	}
	public void inizia() {

		bInizia.setVisible(false);
		eTitolo.setVisible(false);
		sVelocità.setVisible(false);
		sGrandezza.setVisible(false);
		pannello.setVisible(false);

		griglia.add(ePunteggio, 0, 0);
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
			break;
		case 2:
			grandezza=15;
			snakeX=1;
			snakeY=7;
			break;
		case 3:
			grandezza=30;
			snakeX=3;
			snakeY=14;
			break;
		}
		mele = new Boolean [grandezza][grandezza];
		serpente = new Boolean [grandezza][grandezza];
		campo = new Label [grandezza][grandezza];
		uccidiSerpente = new Integer [grandezza][grandezza];
		dimensioneCampo=grandezza*30;

		finestra1.setWidth(dimensioneCampo);
		finestra1.setHeight(dimensioneCampo);

		//crea il campo e lo colora
		for(int y=0; y<campo.length;y++) {
			for(int x=0; x<campo.length;x++) {
				campo[x][y]= new Label();
				griglia.add(campo[x][y], x+1, y);
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
		//crea la griglia di mele
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
		campo[grandezza-snakeX-2][snakeY].getStyleClass().add("mela");
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
			int melaX=(int)(Math.random() * grandezza-1);
			int melaY=(int)(Math.random() * grandezza-1);
			mele[melaX][melaY] = true;
			campo[melaX][melaY].getStyleClass().add("mela");
			mele[snakeX][snakeY] = false;
			campo[snakeX][snakeY].getStyleClass().remove("mela");
			punti+=1;
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
					//debug
					campo[x][y].setText(uccidiSerpente[x][y]+"");

				}
			}
		}
		//vittoria
		if(punti==grandezza*grandezza) {
			timeline.stop();
			//			schermataVittoria();
		}
		//movimento
		if (alto && !basso) {
			snakeY-=1;
			if (snakeY!=-1) {
				serpente[snakeX][snakeY]=true;
				campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
			}
		}
		if (basso && !alto) {
			snakeY+=1;
			if(snakeY!=grandezza) {
				serpente[snakeX][snakeY]=true;
				campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
			}
		}
		if (sinistra && !destra) {
			snakeX-=1;
			if(snakeX!=-1) {
				serpente[snakeX][snakeY]=true;
				campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
			}
		}
		if (destra && !sinistra) {
			snakeX+=1;
			if(snakeX!=grandezza) {
				serpente[snakeX][snakeY]=true;
				campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
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
		if(evento.getText().equals("w") || evento.getText().equals("W") || evento.getCode() == KeyCode.UP) {
			if(!basso) {
				alto=true;
				basso=false;
				sinistra=false;
				destra=false;
			}
		}
		if(evento.getText().equals("s") || evento.getText().equals("S") || evento.getCode() == KeyCode.DOWN) {
			if(!alto) {
				alto=false;
				basso=true;
				sinistra=false;
				destra=false;
			}
		}
		if(evento.getText().equals("a") || evento.getText().equals("A") || evento.getCode() == KeyCode.LEFT) {
			if(!destra) {
				alto=false;
				basso=false;
				sinistra=true;
				destra=false;
			}
		}
		if(evento.getText().equals("d") || evento.getText().equals("D") || evento.getCode() == KeyCode.RIGHT) {
			if(!sinistra) {
				alto=false;
				basso=false;
				sinistra=false;
				destra=true;
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
		Label eSconfitta = new Label("Hai perso");
		Button bRigioca = new Button("rigioca");
		pannello.getChildren().add(eSconfitta);
		pannello.getChildren().add(bRigioca);

		eSconfitta.setLayoutX(dimensioneCampo/2);
		eSconfitta.setLayoutY(dimensioneCampo/2);

		bRigioca.setLayoutX(dimensioneCampo/2);
		bRigioca.setLayoutY(dimensioneCampo/2+30);

		snakeX=2;
		snakeY=grandezza/2;
		punti=0;
		alto=false;
		basso=false;
		sinistra=false;
		destra=true;

		bRigioca.setOnAction(e-> inizia());
	}
	public static void main(String[] args) {
		launch(args);
	}
}