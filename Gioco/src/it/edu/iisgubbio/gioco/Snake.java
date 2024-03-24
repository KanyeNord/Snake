package it.edu.iisgubbio.gioco;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

//TODO difficoltà(velocità timer)(dimensione campo)
//fare il css
//usare delle immagini oppure disegnarle direttamente
//schermata di vittoria e sconfitta
//menù principale e personalizzazione
//lunghezza serpente

public class Snake extends Application {
	GridPane griglia = new GridPane();
	Label eTitolo = new Label("Snake");
	Button bInizia = new Button("inizia");
	Boolean mele[][] = new Boolean [15][15];
	Boolean serpente[][] = new Boolean [15][15];
	Label campo[][] = new Label [15][15];
	
	boolean alto=false;
	boolean basso=false;
	boolean sinistra=false;
	boolean destra=true;
	
	int snakeX=2;
	int snakeY=7;
	
	int punti=0;
	Label ePunteggio = new Label(punti+"");
	
	Timeline timeline = new Timeline(new KeyFrame(
			Duration.seconds(1.5),
			x -> aggiornaTimer()));

	public void start(Stage finestra) {

		griglia.add(eTitolo, 0, 0, 3, 1);
		griglia.add(bInizia, 0, 1, 3, 1);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.stop();
		
		bInizia.setOnAction(e-> inizia());
		griglia.setPrefSize(450, 450);
		griglia.setId("griglia");
		griglia.setGridLinesVisible(false);
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
		timeline.play();
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
		//crea la griglia di mele
		for(int y=0; y<mele.length;y++) {
			for(int x=0; x<mele.length;x++) {
				mele[x][y]= false;
			}
		}
		//genera la prima mela
		mele[11][7] = true;
		campo[11][7].getStyleClass().add("mela");
	}
	private void aggiornaTimer() {
		//conteggio punti e generazione nuova mela

		int xVecchio=snakeX;
		int yVecchio=snakeY;
		if (mele[snakeX][snakeY]==true) {
			int melaX=(int)(Math.random() * 14);
			int melaY=(int)(Math.random() * 14);
			mele[melaX][melaY] = true;
			campo[melaX][melaY].getStyleClass().add("mela");
			campo[snakeX][snakeY].getStyleClass().remove("mela");
			punti+=1;
		}
		//movimento
	if (alto && snakeY!=0) {
		snakeY-=1;
		serpente[snakeX][snakeY]=serpente[xVecchio][yVecchio];
		campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
	if (basso && snakeY!=14) {
		snakeY+=1;
		serpente[snakeX][snakeY]=serpente[xVecchio][yVecchio];
		campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
	if (sinistra && snakeX!=0) {
		snakeX-=1;
		serpente[snakeX][snakeY]=serpente[xVecchio][yVecchio];
		campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
	if (destra && snakeX!=14) {
		snakeX+=1;
		serpente[snakeX][snakeY]=serpente[xVecchio][yVecchio];
		campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
	}
	private void pigiato(KeyEvent evento) {
		if(evento.getText().equals("w") || evento.getText().equals("W") || evento.getCode() == KeyCode.UP) {
//			if (serpente[][]!=0) {
			alto=true;
			basso=false;
			sinistra=false;
			destra=false;
//			}
		}
		if(evento.getText().equals("s") || evento.getText().equals("S") || evento.getCode() == KeyCode.DOWN) {
//			if (serpente[][]!=15) {
			alto=false;
			basso=true;
			sinistra=false;
			destra=false;
//			}
		}
		if(evento.getText().equals("a") || evento.getText().equals("A") || evento.getCode() == KeyCode.LEFT) {
//			if (serpente[][]!=0) {
			alto=false;
			basso=false;
			sinistra=true;
			destra=false;
//			}
		}
		if(evento.getText().equals("d") || evento.getText().equals("D") || evento.getCode() == KeyCode.RIGHT) {
//			if (serpente[][]!=15) {
			alto=false;
			basso=false;
			sinistra=false;
			destra=true;
//			}
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
