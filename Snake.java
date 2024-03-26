package it.edu.iisgubbio.gioco;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

//TODO difficoltà(velocità timer)(dimensione campo)
//fare il css
//usare delle immagini oppure disegnarle direttamente
//schermata di vittoria e sconfitta
//menù principale e personalizzazione
//lunghezza serpente
//collisioni con il serpente
//sistemare le dimensioni diverse
//sistemare bug mele multiple
//effetto pacman

public class Snake extends Application {
	int campoX=15;
	int campoY=15;

	GridPane griglia = new GridPane();
	Pane pannello = new Pane();
	Label eTitolo = new Label("Snake");
	Button bInizia = new Button("inizia");
	Boolean mele[][] = new Boolean [campoX][campoY];
	Boolean serpente[][] = new Boolean [campoX][campoY];
	Label campo[][] = new Label [campoX][campoY];

	TextField cVelocità = new TextField();
	TextField cGrandezza = new TextField();

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

	Timeline timeline = new Timeline(new KeyFrame(
			Duration.millis(300),
			x -> aggiornaTimer()));

	public void start(Stage finestra) {
		eTitolo.setAlignment(Pos.CENTER);
		griglia.add(pannello, 0, 0, 15, 15);

		pannello.setPrefSize(larghezza, lunghezza);
		pannello.getChildren().add(eTitolo);
		pannello.getChildren().add(bInizia);
		pannello.getChildren().add(cVelocità);
		pannello.getChildren().add(cGrandezza);

		eTitolo.setLayoutX(larghezza/2);
		eTitolo.setLayoutY(30);

		bInizia.setLayoutX(larghezza/2);
		bInizia.setLayoutY(60);

		cVelocità.setLayoutX(larghezza/2);
		cVelocità.setLayoutY(90);

		cGrandezza.setLayoutX(larghezza/2);
		cGrandezza.setLayoutY(120);

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.stop();


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
		//genera la prima mela
		mele[campoX-3][campoY/2] = true;
		campo[campoX-3][campoY/2].getStyleClass().add("mela");
		//genera la prima mela
				serpente[2][campoY/2] = true;
				campo[2][campoY/2].getStyleClass().add("serpente");
	}
	private void aggiornaTimer() {
		//conteggio punti e generazione nuova mela

		int xVecchio=snakeX-punti;
		int yVecchio=snakeY-punti;

		if (mele[snakeX][snakeY]==true) {
			int melaX=(int)(Math.random() * campoX-1);
			int melaY=(int)(Math.random() * campoY-1);
			mele[melaX][melaY] = true;
			campo[melaX][melaY].getStyleClass().add("mela");
			campo[snakeX][snakeY].getStyleClass().remove("mela");
			punti+=1;
		}
		//movimento
		if (alto && snakeY!=0) {
			snakeY-=1;
			serpente[snakeX][snakeY]=true;
			serpente[xVecchio][yVecchio]=false;
		//	campo[snakeX][snakeY].getStyleClass().add("serpente");
			
		}
		if (basso && snakeY!=campoY-1) {
			snakeY+=1;
			serpente[snakeX][snakeY]=serpente[xVecchio][yVecchio];
		//	campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
		if (sinistra && snakeX!=0) {
			snakeX-=1;
			serpente[snakeX][snakeY]=serpente[xVecchio][yVecchio];
		//	campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
		if (destra && snakeX!=campoX-1) {
			snakeX+=1;
			serpente[snakeX][snakeY]=serpente[xVecchio][yVecchio];
		//	campo[snakeX][snakeY].getStyleClass().add("serpente");
		}
		
//		for(int y=0; y<serpente.length;y++) {
//			for(int x=0; x<serpente.length;x++) {
//				if (serpente[x][y]) {
//					campo[x][y].getStyleClass().add("serpente");
//				} else {
//					campo[x][y].getStyleClass().remove("serpente");
//				}
//			}
//		}
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
