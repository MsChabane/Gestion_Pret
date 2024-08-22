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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;

import java.util.Date;
import java.util.ResourceBundle;


import org.controlsfx.control.textfield.CustomTextField;


import com.app.model.Lecteur;

import com.app.utis.LecteurDB;
public class LecteurControl  implements Initializable{
	private double x,y;
	  @FXML
	    private TableColumn<Lecteur, Date> dateInsCol;

	    @FXML
	    private TableColumn<Lecteur, Date> dateNaCol;

	    @FXML
	    private TableColumn<Lecteur, String> idcol;

	    @FXML
	    private TableColumn<Lecteur, String> nomCol;

	    @FXML
	    private TableColumn<Lecteur, String> prenomCol;

	    @FXML
	    private TableColumn<Lecteur, Integer> sanCol;

	    @FXML
	    private TableView<Lecteur> table;
	    @FXML
	    private TableColumn<Lecteur, String> editCol;
	    @FXML
	    private CustomTextField txtRecherche;
	    
	    @FXML
	    void showAdd(ActionEvent event) {
	    	 FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/AjouteLec.fxml"));
				try {
					loader.load();
					AjouterLec controlle = loader.getController();
					controlle.setOwnerControl(LecteurControl.this);
					Parent root = loader.getRoot();
					Stage stage = new Stage();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					//scene.getStylesheets().add("/com/app/styling/Notif.css");
					stage.initStyle(StageStyle.UNDECORATED);
					stage.initModality(Modality.WINDOW_MODAL);
					stage.initOwner(table.getScene().getWindow());
					stage.getOwner().setOpacity(0.4);
					((Stage )table.getScene().getWindow()).getScene().getStylesheets().add("/com/app/styling/Notif.css");
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    }
	    private ObservableList<Lecteur>lecteurs;
	    public void loadData(){
			Task<Void>task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					
					lecteurs.setAll(LecteurDB.getData(false)) ;
					table.setItems(lecteurs);
					return null;
				}
			};
			task.setOnFailed(e->System.out.println("field"));
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			lecteurs=FXCollections.observableArrayList();
			idcol.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("id"));
			nomCol.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("nom"));
			prenomCol.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("prenom"));
			sanCol.setCellValueFactory(new PropertyValueFactory<Lecteur, Integer>("nombreSection"));
			dateInsCol.setCellValueFactory(new PropertyValueFactory<Lecteur, Date>("dateInscrire"));
			dateNaCol.setCellValueFactory(new PropertyValueFactory<Lecteur, Date>("dateNaissance"));
			
			
			Callback<TableColumn<Lecteur, String>,TableCell<Lecteur, String>>cellfactory= (TableColumn<Lecteur, String> col)->{
				
				final TableCell<Lecteur, String> cell = new TableCell<Lecteur, String>(){
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
							
							Button btnDelete = new Button(null,deleteIcon);
							btnDelete .setOnAction(e->{
								Lecteur lecteur = table.getSelectionModel().getSelectedItem();
								if(lecteur!=null) {
									FXMLLoader loader = new FXMLLoader();
									loader.setLocation(getClass().getResource("/com/app/fxml/CustomAlert.fxml"));
									try {
										loader.load();
										AlertControl controlle = loader.getController();
										controlle.setControl(LecteurControl.this, lecteur, "Lecteur", "Voulais-vous vraiment blocker Lecteur ?",table.getSelectionModel().getSelectedIndex(),true);	
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
							Button btnUpdate = new Button(null,editIcon);
							btnUpdate.setOnAction(e->{
								Lecteur lecteur = table.getSelectionModel().getSelectedItem();
								if(lecteur!=null) {
									FXMLLoader loader = new FXMLLoader();
									loader.setLocation(getClass().getResource("/com/app/fxml/modifierLecteur.fxml"));
									try {
										loader.load();
										ModifierLec controlle = loader.getController();						
										controlle.setOwnerConrol( LecteurControl.this,table.getSelectionModel().getSelectedIndex(),lecteur);	
										Parent root = loader.getRoot();
										Stage stage = new Stage();
										Scene scene = new Scene(root);
										scene.getStylesheets().add(getClass().getResource("/com/app/styling/Notif.css").toExternalForm());
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
							
							HBox v = new HBox(btnUpdate,btnDelete);
							v.setAlignment(Pos.CENTER);
							v.setSpacing(10);
							setGraphic(v);
							setText(null);
						}
					}
				};
				return cell;
			};
			editCol.setCellFactory(cellfactory);
			sanCol.setCellFactory(col->{
				return new TableCell<Lecteur, Integer>(){
					protected void updateItem(Integer item,boolean isEmpty) {
						if(item ==null || isEmpty ) {
							setText(null);
							setStyle(null);
						}else {
							setText(item+"");	
							if(item>=3 ){
								setStyle("-fx-text-fill:rgb(255,0,0);");
							}
						}
					}
				};
			});
			loadData();
			FilteredList<Lecteur>filterData=new FilteredList<Lecteur>(lecteurs, e->true);
			txtRecherche.textProperty().addListener((O,V,newV)->{
				filterData.setPredicate(lecteur->{
					
					if(newV==null || newV.isEmpty())return true;
					String value=newV.toLowerCase();
					
					//if(emprunt.getId().contains(value))return true;
					if(lecteur.getNom().toLowerCase().contains(value))return true;
					if(lecteur.getPrenom().toLowerCase().contains(value))return true;
				//	if(lecteur.f().toLowerCase().contains(value))return true;
					return false;
				});
				
				SortedList<Lecteur>sortedList = new SortedList<Lecteur>(filterData);
				sortedList.comparatorProperty().bind(table.comparatorProperty());
				table.setItems(sortedList);
				
			});
		
		}
		public void setNew(Lecteur lecteur) {
			lecteur.setDateInscrire(new java.sql.Date(lecteur.getDateInscrire().getTime()));
			lecteur.setDateNaissance(new java.sql.Date(lecteur.getDateNaissance().getTime()));
			lecteurs.add( lecteur);
		}
		 public void  delete(int index) {
		    	lecteurs.remove(index);
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
		    public void setUpdadeItem(Lecteur lecteur,int index) {
		    	lecteur.setDateNaissance(new java.sql.Date(lecteur.getDateNaissance().getTime()));
				lecteurs.set(index, lecteur);
			}
		    @FXML
		    void showCobaille(ActionEvent event) {
		    	FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/LecteurCorbaille.fxml"));
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
}
