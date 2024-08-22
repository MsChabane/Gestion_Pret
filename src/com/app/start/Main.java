package com.app.start;
	

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/com/app/fxml/Connection.fxml"));
			/*ImageView imge = new ImageView(new Image("/com/app/img/icons8_edit_25px.png"));
			imge.prefWidth(25);
			imge.prefHeight(25);
			Button btn = new Button(null,imge);
			btn.setStyle("-fx-background-color: rgb(255,255,255,0);-fx-border-color:rgb(255,255,255)");
			*/
			
		/*	DatePicker date = new DatePicker();
			
			date.setValue(LocalDate.parse("2021-12-10"));
			
			date.valueProperty().addListener(e->{
				if(date.getValue().isBefore(LocalDate.parse("2021-10-20"))) {
					date.setValue(LocalDate.parse("2021-10-20"));
				}
			});
			HBox root = new HBox(date,btn);*/
			/*Button btn = new Button("check");
			btn.setOnAction(e->{
				
				System.out.println("execute");
				
			});
			HBox root = new HBox(btn);*/
			
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("/com/app/styling/ouvrage.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("/com/app/img/img.png"));
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
		
		//System.out.println("ss.s".matches("(\\d+.\\d+)||(\\d+)"));
		//System.out.println(Double.parseDouble("12.5")+12);
		
		/*try {
			//System.out.println(EmpruntDB.canDo(new Lecteur("06062025000", null, null, null)));
			System.out.println(SanctionnerDB.add(new Sanctionner(0, new Date(),new Lecteur("06062025000", null, null, null) , new Sanction(1, null))));
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
}
