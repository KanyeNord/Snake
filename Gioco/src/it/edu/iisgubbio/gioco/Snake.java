package it.edu.iisgubbio.gioco;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Snake extends Application {
	GridPane griglia = new GridPane();
	Label eTitolo = new Label("Snake");
	Button bInizia = new Button("inizia");
	Boolean Mele[][]= new Boolean [15][15];

	Timeline timeline = new Timeline(new KeyFrame(
			Duration.millis(90),
			x -> aggiornaTimer()));
	//TODO difficoltà(velocità timer)
//	creare la griglia di label
//	fare il css
//	creare il serpente
//	creare le mele
//	usare delle immagini oppure disegnarle direttamente
//	riempire la matrice
	public void start(Stage finestra) {

		griglia.add(eTitolo, 0, 0, 3, 1);
		griglia.add(bInizia, 0, 1, 3, 1);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.stop();
		
		bInizia.setOnAction(e-> inizia());

		griglia.setId("griglia");
		griglia.setGridLinesVisible(false);
		griglia.getStylesheets().add("it/edu/iisgubbio/gioco/Snake.css");
		Scene scena = new Scene(griglia);
		finestra.setTitle("Snake");
		finestra.setScene(scena);
		finestra.show();
	}
	public void inizia() {
		bInizia.setVisible(false);
		eTitolo.setVisible(false);
		timeline.play();
	}
	private void aggiornaTimer() {
		//conteggio punti
		if (x<=10) {
			destra=true;
			pallino.setCenterX(x=200);
			pallino.setCenterY(y=150);
			timeline.stop();
			p2+=1;
			puntiG2.setText("PUNTI "+p2);
			spazio.setVisible(true);
			int range = 255 - 100;
			int r=(int)(Math.random() * range);
			int g=(int)(Math.random() * range);
			int b=(int)(Math.random() * range);
			pallino.setFill(Color.rgb(r,g,b ));
		} 
		if (x>=LARGHEZZA_SCHERMO+15) {
			destra=false;
			pallino.setCenterX(x=200);
			pallino.setCenterY(y=150);
			timeline.stop();
			p1+=1;
			puntiG1.setText("PUNTI "+p1);
			spazio.setVisible(true);
			int range = 255 - 100;
			int r=(int)(Math.random() * range);
			int g=(int)(Math.random() * range);
			int b=(int)(Math.random() * range);
			pallino.setFill(Color.rgb(r,g,b ));
		}
	}
	private void pigiato(KeyEvent evento) {
//		if(evento.getCode()==KeyCode.SPACE) {
//			timeline.play();
//			spazio.setVisible(false);
//		}
		if(evento.getText().equals("w") || evento.getText().equals("W") || evento.getCode() == KeyCode.UP) {
//			if (rettangoloS.getY()>0) {
//				rettangoloS.setY( rettangoloS.getY() -5 );
//			}
		}
		if(evento.getText().equals("s") || evento.getText().equals("S") || evento.getCode() == KeyCode.DOWN) {
//			if (rettangoloS.getY()<200) {
//				rettangoloS.setY( rettangoloS.getY() +5 );
//			}
		}
		if(evento.getText().equals("a") || evento.getText().equals("A") || evento.getCode() == KeyCode.LEFT) {
//			if (rettangoloS.getY()<200) {
//				rettangoloS.setY( rettangoloS.getY() +5 );
//			}
		}
		if(evento.getText().equals("d") || evento.getText().equals("D") || evento.getCode() == KeyCode.RIGHT) {
//			if (rettangoloD.getY()>0) {
//				rettangoloD.setY( rettangoloD.getY() -5 );
//			}
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
