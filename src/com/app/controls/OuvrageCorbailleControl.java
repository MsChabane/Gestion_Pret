package com.app.controls;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import org.controlsfx.control.textfield.CustomTextField;
import com.app.model.Categorie;
import com.app.model.Ouvrage;
import com.app.utis.OuvrageDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class OuvrageCorbailleControl implements Initializable {
	
	private double x,y;
    @FXML
    private TableColumn<Ouvrage, Categorie> categoriecol;

    @FXML
    private TableColumn<Ouvrage, Date> dateCol;
    

    @FXML
    private TableColumn<Ouvrage, String> idcol;

    @FXML
    private TableColumn<Ouvrage, Integer> nbrCol;
    @FXML
    private CustomTextField txtRecherche;

    @FXML
    private TableColumn<Ouvrage,Double> prixCol;
    @FXML
    private TableView<Ouvrage> table;
    @FXML
    private TableColumn<Ouvrage, String> editCol;
    @FXML
    private TableColumn<Ouvrage,String> titrecol;
    private ObservableList<Ouvrage>ouv;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
			idcol.setCellValueFactory(new PropertyValueFactory<Ouvrage, String>("id"));
			dateCol.setCellValueFactory(new PropertyValueFactory<Ouvrage, Date>("dateEntree"));
			prixCol.setCellValueFactory(new PropertyValueFactory<Ouvrage, Double>("prix"));
			nbrCol.setCellValueFactory(new PropertyValueFactory<Ouvrage, Integer>("nombreExamplaire"));
			titrecol.setCellValueFactory(new PropertyValueFactory<Ouvrage, String>("titre"));
			categoriecol.setCellValueFactory(new PropertyValueFactory<Ouvrage, Categorie>("categorie"));
			Callback<TableColumn<Ouvrage, String>,TableCell<Ouvrage,String>>cellfactory= (TableColumn<Ouvrage, String> col)->{
				final TableCell<Ouvrage, String> cell = new TableCell<Ouvrage, String>(){
					@Override
					public void updateItem(String item, boolean emplty) {
						if(emplty) {
							setGraphic(null);
							setText(null);
						}else {
							
							ImageView restoreIcon = new ImageView(new Image("/com/app/img/icons8_trash_restore_25px.png"));
							restoreIcon.prefWidth(25);
							restoreIcon.prefHeight(25);
							
							Button btnRestor = new Button(null,restoreIcon);
							btnRestor.setOnAction(e->{
								Ouvrage ouvrage = table.getSelectionModel().getSelectedItem();
								if(ouvrage!=null) {
									try {
										OuvrageDB.restor(ouvrage);
										ouv.remove(table.getSelectionModel().getSelectedIndex());
									} catch (SQLException e1) {
										
									}
								}
							});
							StackPane v = new StackPane(btnRestor);

							setGraphic(v);
							setText(null);
						}
					}
				};
				return cell;
			};
			editCol.setCellFactory(cellfactory);
			ouv=FXCollections.observableArrayList();
			loadData();
			FilteredList<Ouvrage>filtedData = new FilteredList<Ouvrage>(ouv, p->true);
			txtRecherche.textProperty().addListener((Obs,oldV,newV)->{
				filtedData.setPredicate(ouvrg->{
					if(newV==null || newV.isEmpty())return true;
					String value = newV.toLowerCase();
					if(ouvrg.getTitre().toLowerCase().contains(value))return true;
					if(ouvrg.getCategorie().getIntitule().toLowerCase().contains(value))return true;
					return false;
				});
				SortedList<Ouvrage>sorteddata = new SortedList<Ouvrage>(filtedData);
				sorteddata.comparatorProperty().bind(table.comparatorProperty());
				table.setItems(sorteddata);
			});
	}
	
	public void loadData(){
		
		Task<Void>task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				table.getItems().clear();
				ouv.setAll(OuvrageDB.getDataSup()) ;
				table.setItems(ouv);
				return null;
			}
		};
		task.setOnFailed(e->System.out.println("field"));
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
 
	 @FXML
	    void retournToOwner(ActionEvent event) {
		 FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/app/fxml/Ouvrage.fxml"));
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
