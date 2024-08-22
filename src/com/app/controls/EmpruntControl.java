package com.app.controls;


import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;

import com.app.model.Emprunt;
import com.app.model.Lecteur;
import com.app.model.Ouvrage;
import com.app.utis.EmpruntDB;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

public class EmpruntControl implements Initializable {
	private double x,y;
    @FXML
    private TableColumn<Emprunt, Date> datePretCol;

    @FXML
    private TableColumn<Emprunt, Date> dateRemisCol;

    @FXML
    private TableColumn<Emprunt, String> idCol;

    @FXML
    private TableColumn<Emprunt, Lecteur> lectCol;

    @FXML
    private TableColumn<Emprunt, Ouvrage> ouvgCol;

    @FXML
    private TableColumn<Emprunt, String> statusCol;
    @FXML
    private TableColumn<Emprunt, String> edit;
    @FXML
    private CustomTextField txtRecherche;
    @FXML
    private TableView<Emprunt> table;
    private ObservableList<Emprunt>emprunts;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(new PropertyValueFactory<Emprunt, String>("id"));
		datePretCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Date>("datePret"));
		dateRemisCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Date>("dateRmis"));
		ouvgCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Ouvrage>("ouvrage"));
		
		lectCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Lecteur>("lecteur"));
		statusCol.setCellValueFactory(new PropertyValueFactory<Emprunt, String>("etat"));
		ouvgCol.setCellFactory(col->{
			return new TableCell<Emprunt, Ouvrage>(){
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
		lectCol.setCellFactory(col->{
			return new TableCell<Emprunt, Lecteur>(){
				protected void updateItem(Lecteur item,boolean isEmpty) {
					if(item ==null || isEmpty ) {
						setText(null);
						setStyle(null);
					}else {
						setText(item.getNom().toUpperCase().charAt(0)+"."+item.getPrenom());
					}
				}
			};
		});
		statusCol.setCellFactory(col->{
			return new TableCell<Emprunt, String>(){
				protected void updateItem(String item,boolean isEmpty) {
					if(item ==null || isEmpty ) {
						setText(null);
						setStyle(null);
					}else {
						setText(item);
						if(item.equalsIgnoreCase("En Attente")){
							setStyle("-fx-text-fill:rgb(255,0,0);");
						}else {
							setStyle("-fx-text-fill:rgb(0,255,0);");
						}
					}
				}
			};
		});
		
		Callback<TableColumn<Emprunt, String>,TableCell<Emprunt, String>>cellfactory= (TableColumn<Emprunt, String> col)->{
			
			final TableCell<Emprunt, String> cell = new TableCell<Emprunt, String>(){
				@Override
				public void updateItem(String item, boolean emplty) {
					if(emplty) {
						setGraphic(null);
						setText(null);
					}else {
						ImageView editIcon = new ImageView(new Image("/com/app/img/icons8_edit_25px.png"));
						editIcon.prefWidth(25);
						editIcon.prefHeight(25);
						ImageView deleteIcon = new ImageView(new Image("/com/app/img/icons8_trash_25px.png"));
						deleteIcon.prefWidth(25);
						deleteIcon.prefHeight(25);
						ImageView showIcon = new ImageView(new Image("/com/app/img/icons8_eye_25px_1.png"));
						deleteIcon.prefWidth(25);
						deleteIcon.prefHeight(25);
						Button btnDelete = new Button(null,deleteIcon);
						btnDelete .setOnAction(e->{
							Emprunt emprunt = table.getSelectionModel().getSelectedItem();
							if(emprunt!=null) {
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(getClass().getResource("/com/app/fxml/CustomAlert.fxml"));
								try {
									loader.load();
									AlertControl controlle = loader.getController();						
									controlle.setControl(EmpruntControl.this, emprunt, "Emprunt", "Voulais-vous vraiment supprimer L'emprunt ?",table.getSelectionModel().getSelectedIndex(),false );
									Parent root = loader.getRoot();
									Stage stage = new Stage();
									Scene scene = new Scene(root);
									//scene.getStylesheets().add(getClass().getResource("/com/app/styling/modigierOuv.css").toExternalForm());
									stage.setScene(scene);
									stage.initStyle(StageStyle.UNDECORATED);
									stage.initModality(Modality.WINDOW_MODAL);
									stage.initOwner(table.getScene().getWindow());
									stage.getOwner().setOpacity(0.4);
									stage.show();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}else
								System.out.println("select a row ");	
						});
						Button btnUpdate = new Button(null,editIcon);
						btnUpdate.setOnAction(e->{
							Emprunt emprunt = table.getSelectionModel().getSelectedItem();
							if(emprunt!=null) {
								if(emprunt.getEtat().equalsIgnoreCase("en attente")) {
									FXMLLoader loader = new FXMLLoader();
									loader.setLocation(getClass().getResource("/com/app/fxml/ModifierEmprunt.fxml"));
									try {
										loader.load();
										ModifierEmprunt controlle = loader.getController();						
										controlle.setControl(EmpruntControl.this, emprunt, table.getSelectionModel().getSelectedIndex());
										Parent root = loader.getRoot();
										Stage stage = new Stage();
										Scene scene = new Scene(root);
										//scene.getStylesheets().add(getClass().getResource("/com/app/styling/modigierOuv.css").toExternalForm());
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
								}else {
									Notifications.create().hideAfter(Duration.seconds(3)).position(Pos.TOP_RIGHT).text("Emprunt est Archifie").showInformation();
								}
							}else
								System.out.println("select a row ");
							
						});
						Button btnshow = new Button(null, showIcon);
						btnshow.setOnAction(e->{
							Emprunt emprunt = table.getSelectionModel().getSelectedItem();
							if(emprunt!=null) {
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(getClass().getResource("/com/app/fxml/showEmprunt.fxml"));
								try {
									loader.load();
									AfficheDetails controlle = loader.getController();						
									controlle.set(emprunt);
									Parent root = loader.getRoot();
									Stage stage = new Stage();
									Scene scene = new Scene(root);
									//scene.getStylesheets().add(getClass().getResource("/com/app/styling/modigierOuv.css").toExternalForm());
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
						HBox v = new HBox(btnshow,btnUpdate,btnDelete);
						v.setAlignment(Pos.CENTER);
						v.setSpacing(10);
						setGraphic(v);
						setText(null);
					}
				}
			};
			return cell;
		};
		edit.setCellFactory(cellfactory);
		emprunts= FXCollections.observableArrayList();
		loadData();
		FilteredList<Emprunt>filterData=new FilteredList<Emprunt>(emprunts, e->true);
		txtRecherche.textProperty().addListener((obsList,oldV,newV)->{
			filterData.setPredicate(emprunt->{
				if(newV==null || newV.isEmpty())return true;
				String value=newV.toLowerCase();
				
				//if(emprunt.getId().contains(value))return true;
				if(emprunt.getLecteur().getNom().toLowerCase().contains(value))return true;
				if(emprunt.getOuvrage().getTitre().toLowerCase().contains(value))return true;
				if(emprunt.getLecteur().getPrenom().toLowerCase().contains(value))return true;
				if(emprunt.getEtat().toLowerCase().contains(value))return true;
				return false;
			});
			SortedList<Emprunt>sortedList = new SortedList<Emprunt>(filterData);
			sortedList.comparatorProperty().bind(table.comparatorProperty());
			table.setItems(sortedList);
		});
		
	}
	public void loadData(){
		Task<Void>task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				table.getItems().clear();
				emprunts.addAll(EmpruntDB.getData());
				table.setItems(emprunts);
				return null;
			}
		};
		task.setOnFailed(e->System.out.println("field"));
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
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
    @FXML
    void retournToOwner(ActionEvent event) {
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
    public void setNew(Emprunt  emprunt) {
    	emprunt.setDatePret(new java.sql.Date(emprunt.getDatePret().getTime()));
    	emprunt.setDateRmis(new java.sql.Date(emprunt.getDateRmis().getTime()));
    	emprunts.add(emprunt);
    }
    public void  delete(int index) {
    	emprunts.remove(index);
    }
    @FXML
    void showAdd(ActionEvent event) {
    	 FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/app/fxml/AjouterEmprunt.fxml"));
			try {
				loader.load();
				AjouterEmprunt controlle = loader.getController();
				controlle.setControl(EmpruntControl.this);
				Parent root = loader.getRoot();
				Stage stage = new Stage();
				Scene scene = new Scene(root);
				//scene.getStylesheets().add(getClass().getResource("/com/app/styling/ajouterOuv.css").toExternalForm());
				stage.setScene(scene);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.initOwner(table.getScene().getWindow());
				stage.getOwner().setOpacity(0.4);
				stage.initModality(Modality.WINDOW_MODAL);
				//((Stage )table.getScene().getWindow()).hide();
				((Stage )table.getScene().getWindow()).getScene().getStylesheets().add("/com/app/styling/Notif.css");
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
    public void setUpdate(Emprunt emprunt,int index) {
    	emprunt.setDateRmis(new java.sql.Date(emprunt.getDateRmis().getTime()));
    	emprunts.set(index, emprunt);
	}
   
    
	
}

