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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

//TODO difficoltà(velocità timer)(dimensione campo)
//fare il css
//usare delle immagini oppure disegnarle direttamente
//schermata di vittoria e sconfitta
//menù principale e personalizzazione
//riparare lunghezza serpente
//collisioni con il serpente
//sistemare le dimensioni diverse
//sistemare bug mele multiple
//effetto pacman

public class Snake extends Application {
	int campoX=15;
	int campoY=15;
	
	int coloreR=0;
	int coloreB=0;
	Boolean rBoolean=true;
	Boolean bBoolean=true;
	
	int velocità;
	
	GridPane griglia = new GridPane();
	Pane pannello = new Pane();
	Label eTitolo = new Label("Snake");
	Button bInizia = new Button("inizia");
	Boolean mele[][] = new Boolean [campoX][campoY];
	Boolean serpente[][] = new Boolean [campoX][campoY];
	Label campo[][] = new Label [campoX][campoY];
	Integer uccidiSerpente[][] = new Integer [campoX][campoY];

	Slider sVelocità = new Slider(1, 3, 2);
	Slider sGrandezza = new Slider(1, 3, 2);

	boolean alto=false;
	boolean basso=false;
	boolean sinistra=false;
	boolean destra=true;

	int snakeX=2;
	int snakeY=campoY/2;

	int punti=0;

	int larghezza=campoY*30;
	int lunghezza=campoX*30;
	
	Label ePunteggio = new Label(punti+"");
	
	Timeline timeline;

	public void start(Stage finestra) {
		eTitolo.setAlignment(Pos.CENTER);
		griglia.add(pannello, 0, 0, 15, 15);

		pannello.setPrefSize(larghezza, lunghezza);
		pannello.getChildren().add(eTitolo);
		pannello.getChildren().add(bInizia);
		pannello.getChildren().add(sVelocità);
		pannello.getChildren().add(sGrandezza);

		eTitolo.setLayoutX(larghezza/2);
		eTitolo.setLayoutY(30);

		bInizia.setLayoutX(larghezza/2);
		bInizia.setLayoutY(60);

		sVelocità.setLayoutX(larghezza/2);
		sVelocità.setLayoutY(90);

		sGrandezza.setLayoutX(larghezza/2);
		sGrandezza.setLayoutY(120);

		 sVelocità.setShowTickMarks(true);
		 sVelocità.setShowTickLabels(true);
		 sVelocità.setSnapToTicks(true);
		 sVelocità.setMinorTickCount(0);
		 sVelocità.setMajorTickUnit(1);

		bInizia.setOnAction(e-> inizia());
		griglia.setPrefSize(larghezza, lunghezza);
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
		pannello.setVisible(false);
		
		//velocità gioco
		//set style ("") per rimuovere lo stile della coda
		
		 int sliderV = (int) sVelocità.getValue();
		 switch (sliderV) {
			case 1:
				velocità=5;
				break;
			case 3:
				velocità=300;
				break;
			case 2:
			default:
				velocità=1;
			
		}
		 timeline = new Timeline(new KeyFrame(
					Duration.millis(velocità),
					x -> aggiornaTimer()));
		 timeline.setCycleCount(Animation.INDEFINITE);
		 timeline.play();
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
		mele[campoX-3][campoY/2] = true;
		campo[campoX-3][campoY/2].getStyleClass().add("mela");
		//genera la prima mela
		serpente[2][campoY/2] = true;
		campo[2][campoY/2].getStyleClass().add("serpente");
	}
	private void aggiornaTimer() {
		//conteggio punti e generazione nuova mela
		int codaX=0;
		int codaY=0;
		int coda=0;
		
		//cambio colore serpente
		coloreR+=10;
		if(coloreR>=255) {
			rBoolean=false;
		} else {
			if (coloreR<=0)
			rBoolean=true;
		}
		coloreB+=10;
		if(coloreB>=255) {
			bBoolean=false;
		} else {
			if (coloreB<=0)
			bBoolean=true;
		}
		campo[snakeX][snakeY].setStyle("-fx-background-color:rgb("+coloreR+", 0, "+coloreB+")");
		//perso
		if(snakeY==campoY || snakeY==-1 || snakeX==campoX || snakeX==-1 || uccidiSerpente[snakeX][snakeY]>1) {
			//					for(int y=0; y<campo.length;y++) {
			//						for(int x=0; x<campo.length;x++) {
			//							campo[x][y]= new Label();
			//							campo[x][y].setVisible(false);
			//						}
			//					}
			timeline.stop();
		}
		if (mele[snakeX][snakeY]==true) {
			int melaX=(int)(Math.random() * campoX-1);
			int melaY=(int)(Math.random() * campoY-1);
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
			campo[codaX][codaY].getStyleClass().remove("serpente");
		}
		//aumento griglia della coda
		for(int y=0; y<mele.length;y++) {
			for(int x=0; x<mele.length;x++) {
				if
				(serpente[x][y]==true) {
					uccidiSerpente[x][y]+=1;
				}
			}
		}

		//movimento
		if (alto && !basso) {
			snakeY-=1;
			serpente[snakeX][snakeY]=true;
			campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
		if (basso && !alto) {
			snakeY+=1;
			serpente[snakeX][snakeY]=true;
			campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
		if (sinistra && !destra) {
			snakeX-=1;
			serpente[snakeX][snakeY]=true;
			campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
		if (destra && !sinistra) {
			snakeX+=1;
			serpente[snakeX][snakeY]=true;
			campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
	}
	private void pigiato(KeyEvent evento) {
		if(evento.getText().equals("w") || evento.getText().equals("W") || evento.getCode() == KeyCode.UP) {
			alto=true;
			basso=false;
			sinistra=false;
			destra=false;
		}
		if(evento.getText().equals("s") || evento.getText().equals("S") || evento.getCode() == KeyCode.DOWN) {
			alto=false;
			basso=true;
			sinistra=false;
			destra=false;
		}
		if(evento.getText().equals("a") || evento.getText().equals("A") || evento.getCode() == KeyCode.LEFT) {
			alto=false;
			basso=false;
			sinistra=true;
			destra=false;
		}
		if(evento.getText().equals("d") || evento.getText().equals("D") || evento.getCode() == KeyCode.RIGHT) {
			alto=false;
			basso=false;
			sinistra=false;
			destra=true;
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
