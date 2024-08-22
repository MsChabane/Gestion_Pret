package com.app.controls;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;

import java.util.Date;
import java.util.ResourceBundle;


import org.controlsfx.control.textfield.CustomTextField;

import com.app.model.Emprunt;
import com.app.model.Lecteur;
import com.app.model.Ouvrage;
import com.app.model.Sanction;
import com.app.model.Sanctionner;
import com.app.utis.LecteurDB;
import com.app.utis.SanctionnerDB;
public class SanctionnerCont  implements Initializable{
	private double x,y;
	  
	  @FXML
	    private TableColumn<Sanctionner,Ouvrage> OvgCol;

	    @FXML
	    private TableColumn<Sanctionner,Date> dateCol;

	    @FXML
	    private TableColumn<Sanctionner, Sanction> dncCol;

	    @FXML
	    private TableColumn<Sanctionner, String> editCol;

	    @FXML
	    private TableColumn<Sanctionner, Integer> idcol;

	    @FXML
	    private TableColumn<Sanctionner, Lecteur> lecCol;

	    @FXML
	    private TableView<Sanctionner> table;
	    @FXML
	    private CustomTextField txtRecherche;
	    
	    @FXML
	    void showAdd(ActionEvent event) {
	    	
	    }
	    private ObservableList<Sanctionner>sanctionners;
	    public void loadData(){
			Task<Void>task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					sanctionners.setAll(SanctionnerDB.getData());
					table.setItems(sanctionners);
					return null;
				}
			};
			task.setOnFailed(e->System.out.println("field"));
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}//dateSaction,Ouvrage ouvrage, Lecteur lecteur, Sanction sanction
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			sanctionners=FXCollections.observableArrayList();
			idcol.setCellValueFactory(new PropertyValueFactory<Sanctionner, Integer>("id"));
			dateCol.setCellValueFactory(new PropertyValueFactory<Sanctionner, Date>("dateSaction"));
			OvgCol.setCellValueFactory(new PropertyValueFactory<Sanctionner, Ouvrage>("ouvrage"));
			lecCol.setCellValueFactory(new PropertyValueFactory<Sanctionner, Lecteur>("lecteur"));
			dncCol.setCellValueFactory(new PropertyValueFactory<Sanctionner, Sanction>("sanction"));
			OvgCol.setCellFactory(col->{
				return new TableCell<Sanctionner, Ouvrage>(){
					protected void updateItem(Ouvrage item,boolean isEmpty) {
						if(item ==null || isEmpty ) {
							setText(null);
							setStyle(null);
						}else {
							setText(item.getTitre());
							
						}
					}
				};
			});
			lecCol.setCellFactory(col->{
				return new TableCell<Sanctionner, Lecteur>(){
					protected void updateItem(Lecteur item,boolean isEmpty) {
						if(item ==null || isEmpty ) {
							setText(null);
							setStyle(null);
						}else {
							setText(item.getNom().toUpperCase()+" "+item.getPrenom());
						}
					}
				};
			});
			
			Callback<TableColumn<Sanctionner, String>,TableCell<Sanctionner, String>>cellfactory= (TableColumn<Sanctionner, String> col)->{
				
				final TableCell<Sanctionner, String> cell = new TableCell<Sanctionner, String>(){
					@Override
					public void updateItem(String item, boolean emplty) {
						if(emplty) {
							setGraphic(null);
							setText(null);
						}else {
							
							ImageView deleteIcon = new ImageView(new Image("/com/app/img/icons8_trash_25px.png"));
							deleteIcon.prefWidth(25);
							deleteIcon.prefHeight(25);
							
							Button btnDelete = new Button(null,deleteIcon);
							btnDelete .setOnAction(e->{
								Sanctionner snc = table.getSelectionModel().getSelectedItem();
								if(snc!=null) {
									FXMLLoader loader = new FXMLLoader();
									loader.setLocation(getClass().getResource("/com/app/fxml/CustomAlert.fxml"));
									try {
										loader.load();
										AlertControl controlle = loader.getController();
										controlle.setControl(SanctionnerCont.this, snc, "Sanctionnement", "Voulais-vous vraiment supprimer sanctionnement de  Lecteur ?",table.getSelectionModel().getSelectedIndex(),false);	
										Parent root = loader.getRoot();
										Stage stage = new Stage();
										Scene scene = new Scene(root);
										stage.setScene(scene);
										stage.initStyle(StageStyle.UNDECORATED);
										stage.initModality(Modality.WINDOW_MODAL);
										stage.initOwner(table.getScene().getWindow());
										stage.getOwner().setOpacity(0.4);
										((Stage )table.getScene().getWindow()).getScene().getStylesheets().add("/com/app/styling/Notif.css");
										stage.show();						
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}else
									System.out.println("select a row ");
								
							});
							
							
							StackPane v = new StackPane(btnDelete);
							setGraphic(v);
							setText(null);
						}
					}
				};
				return cell;
			};
			editCol.setCellFactory(cellfactory);
			
			loadData();
			FilteredList<Sanctionner>filterData=new FilteredList<Sanctionner>(sanctionners, e->true);
			txtRecherche.textProperty().addListener((O,V,newV)->{
				filterData.setPredicate(snc->{
					
					if(newV==null || newV.isEmpty())return true;
					String value=newV.toLowerCase();
				
					if(snc.getOuvrage().getTitre().toLowerCase().contains(value))return true;
					if(snc.getLecteur().getPrenom().toLowerCase().contains(value))return true;
					if(snc.getLecteur().getNom().toLowerCase().contains(value))return true;
					if(snc.getSanction().getIntitule().toLowerCase().contains(value))return true;
				
					return false;
				});
				
				SortedList<Sanctionner>sortedList = new SortedList<Sanctionner>(filterData);
				sortedList.comparatorProperty().bind(table.comparatorProperty());
				table.setItems(sortedList);
				
			});
		
		}
		
		 public void  delete(int index) {
		    	sanctionners.remove(index);
		    }
		@FXML
	    void returnBack(ActionEvent event) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/app/fxml/PanneauControl.fxml"));
			try {
				loader.load();
				Parent root = loader.getRoot();
				Stage stage = (Stage) table.getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		  @FXML
		    void drag(MouseEvent event) {
			  Stage stage = (Stage) table .getScene().getWindow();
			  stage.setX(event.getScreenX()-x);
			  stage.setY(event.getScreenY()-y);
		    }

		    @FXML
		    void press(MouseEvent event) {
	    	 Stage stage = (Stage) table .getScene().getWindow();
	    	 stage.setOpacity(0.8);
		    	x= event.getX();
		    	y= event.getY();
		    }
		    @FXML
		    void releas(MouseEvent event) {
		    	 Stage stage = (Stage) table .getScene().getWindow();
		    	 stage.setOpacity(1);
		    }
		    
		    
}
