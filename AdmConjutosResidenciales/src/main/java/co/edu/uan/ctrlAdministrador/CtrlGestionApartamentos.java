package co.edu.uan.ctrlAdministrador;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;


import co.edu.uan.CargueAptoStrategy.provider.XmlProvider;
import co.edu.uan.dao.TorreDAO;
import co.edu.uan.dao.ZonaDAO;
import co.edu.uan.torreBuilder.TorreBuilder;
import co.edu.uan.torreBuilder.TorreCom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class CtrlGestionApartamentos implements Initializable {

	@FXML
	private JFXTextField txtTorre;
	@FXML
	private JFXButton btnCargar;
	@FXML
	private TableColumn<TorreCom, String> clPuestos;

	@FXML
	private TableColumn<TorreCom, String> clCosto;

	@FXML
	private Label txtCostoParqueadero;

	@FXML
	private TableColumn<TorreCom, String> clTorre;

	@FXML
	private JFXTextField txtApartamento;

	@FXML
	private Label txtCostoAdmin;

	@FXML
	private JFXComboBox<String> cbTipo;

	@FXML
	private TableColumn<TorreCom, String> clTipo;

	@FXML
	private JFXButton btnEliminar;

	@FXML
	private TableColumn<TorreCom, String> clApartamento;

	@FXML
	private JFXTextField txtTipoBusqueda;

	@FXML
	private JFXButton btnModificar;

	@FXML
	private JFXButton btnRegistrar;
	
	
	@FXML
	private JFXButton btnBuscar;
	@FXML
	private JFXTextField txtNumeroParZona;

	@FXML
	private TableView<TorreCom> tvTabla;

	// colecciones
	private ObservableList<TorreCom> listaTorre;

	private String idZona;

	@FXML
	void modificar(ActionEvent event) {

	}

	@FXML
	void eliminar(ActionEvent event) {

	}

	@FXML
	
	void registrar(ActionEvent event) {
		
		String tipo = null;
		tipo = cbTipo.getValue();

		if (txtTorre.getText().equals("") || txtApartamento.getText().equals("") || tipo == null
				|| txtNumeroParZona.getText().isEmpty()) {
			displayAlert(AlertType.INFORMATION, "CAMPOS VACIOS", "Debe tener los campos del registro llenos");
		} else if (isNumeric(txtTorre.getText()) == false || isNumeric(txtApartamento.getText()) == false
				|| isNumeric(txtNumeroParZona.getText()) == false) {
			displayAlert(AlertType.INFORMATION, "DATOS INVALIDOS",
					"DEBE INGRESAR DATOS NUMERICOS ENTEROS EN EL CAMPO DE TORRE Y APARTAMENTOS");
		
		} else if(Integer.parseInt(txtApartamento.getText())>24||Integer.parseInt(txtApartamento.getText())<=0){
			displayAlert(AlertType.WARNING, "DATOS INVALIDOS",
					"El numero de apartamentos por cada torre no debe de superar los 24 y no puede ser menor o igual a 0");			
		}else{
		

			ArrayList<String> numeroPuestosParq = new ArrayList<>();

			int cuadruple1 = 0;
			int puestos = 100;

			for (int i = 0; i < Integer.parseInt(txtNumeroParZona.getText()); i++) {
				if (cuadruple1 == 4) {
					cuadruple1 = 0;
					puestos = puestos + 100 - 4;
				}
				cuadruple1++;
				puestos++;
				numeroPuestosParq.add(txtTorre.getText() + "-" + puestos);

			}

			TorreBuilder torreBuilder = new TorreBuilder();
			int cuadruple = 0;
			int apt = 100;
			//int apt = Integer.parseInt(txtTorre.getText() +0+0);
			for (int i = 0; i < Integer.parseInt(txtApartamento.getText()); i++) {

				if (cuadruple == 4) {
					cuadruple = 0;
					apt = apt + 100 - 4;
				}
				cuadruple++;
				apt++;
				torreBuilder.addApartamentos(apt, "", "");
			}

			TorreDAO torreDAO = TorreDAO.getInstace();

			if (torreDAO.verificarTorre(txtTorre.getText())) {
				displayAlert(AlertType.INFORMATION, "TORRE EXISTENTE", "El numero de la torre ya existe");
			} else {
				if (torreDAO.createTorre(torreBuilder.setNumero(Integer.parseInt(txtTorre.getText()))
										.setZona(idZona, tipo, Float.parseFloat(txtCostoAdmin.getText()),
										numeroPuestosParq, Float.parseFloat(txtCostoParqueadero.getText()))
										.build())) {
					displayAlert(AlertType.INFORMATION, "TORRE CREADA", "Torre guardada con exito");

					float suma = Float.parseFloat(txtCostoAdmin.getText())
							+ Float.parseFloat(txtCostoParqueadero.getText());
					//agrega a la tabla
					listaTorre.add(new TorreCom(txtTorre.getText(), txtApartamento.getText(), tipo,
							txtNumeroParZona.getText(), Float.toString(suma)));
					//limpia los campos
					limpiarCampos();
				} else {
					displayAlert(AlertType.ERROR, "ERROR", "ERROR al guardar la torre");
				}
			}

		}

	}

	public void limpiarCampos() {
		txtTorre.setText(null);
		txtApartamento.setText(null);
		txtNumeroParZona.setText(null);
		txtTipoBusqueda.setText(null);
		txtCostoAdmin.setText("0");
		txtCostoParqueadero.setText("0");
		cbTipo.setValue(null);
	}
	
	private boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	@FXML
	void buscar(ActionEvent event) {
		if (txtTipoBusqueda.getText().equals("")) {
			displayAlert(AlertType.INFORMATION, "CAMPO VACIO", "Debe tener el campo de busqueda lleno");
		} else {

			TorreDAO torreDAO = TorreDAO.getInstace();
			listaTorre = FXCollections.observableArrayList();

			torreDAO.buscarZona(listaTorre, txtTipoBusqueda.getText());
			tvTabla.setItems(listaTorre);

			clTorre.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("numero"));
			clApartamento.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("naptos"));
			clTipo.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("zona"));
			clPuestos.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("puestos"));
			clCosto.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("costo"));

		}
	}

	private void displayAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	void cargarDatosZona(ActionEvent event) {
		// se pasa a un zonaDAO que devuelva los datos de esa zona
		ZonaDAO zonaDAO = new ZonaDAO();

		txtCostoAdmin.setText(zonaDAO.traerDatosDeZonaAdmin(cbTipo.getValue()));
		txtCostoParqueadero.setText(zonaDAO.traerDatosDeZonaParq(cbTipo.getValue()));
		idZona = zonaDAO.traerDatosDeZonaId(cbTipo.getValue());
	}

	public void iniciarlizarLista() {
		listaTorre = FXCollections.observableArrayList();
		TorreDAO torreDAO = TorreDAO.getInstace();

		torreDAO.traerDatosTabla(listaTorre);

		tvTabla.setItems(listaTorre);

		clTorre.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("numero"));
		clApartamento.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("naptos"));
		clTipo.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("zona"));
		clPuestos.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("puestos"));
		clCosto.setCellValueFactory(new PropertyValueFactory<TorreCom, String>("costo"));
	}
	@FXML
	void cargar(ActionEvent event) {
		//XmlProvider xml = new XmlProvider();
		
		//xml.CargarTorres(txtCostoAdmin.getText(),txtCostoParqueadero.getText());
		
		TorreDAO torreDAO = TorreDAO.getInstace();
		ZonaDAO zonaDAO = new ZonaDAO();
		String tipo = null;
		try {
			File fXmlFile = new File("target/TorreApto.xml");
			
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(fXmlFile);
	        doc.getDocumentElement().normalize();
	        
	        
	        NodeList nList = doc.getElementsByTagName("torre");
	        
	                
	        
	        for (int temp = 0; temp < nList.getLength(); temp++) {
	        	ArrayList<String> numeroPuestosParq = new ArrayList<>();
	        	Element torrec = (Element) nList.item(temp);
	        	
	        	

	        	int cuadruple1 = 0;
				int puestos = 100;
				
				for (int i = 0; i < Integer.parseInt(torrec.getElementsByTagName("npuestos").item(0).getTextContent()); i++) {
					if (cuadruple1 == 4) {
						cuadruple1 = 0;
						puestos = puestos + 100 - 4;
					}
					cuadruple1++;
					puestos++;
					numeroPuestosParq.add(torrec.getElementsByTagName("numero").item(0).getTextContent() + "-" + puestos);

				}

				TorreBuilder torreBuilder = new TorreBuilder();
				int cuadruple = 0;
				int apt =100;
				for (int i = 0; i < Integer.parseInt(torrec.getElementsByTagName("napto").item(0).getTextContent() ); i++) {

					if (cuadruple == 4) {
						cuadruple = 0;
						apt = apt + 100 - 4;
					}
					cuadruple++;
					apt++;
					torreBuilder.addApartamentos(apt, torrec.getElementsByTagName("reside").item(0).getTextContent(), torrec.getElementsByTagName("parqueadero").item(0).getTextContent());
				}	
				
				 if (torreDAO.verificarTorre(torrec.getElementsByTagName("numero").item(0).getTextContent())) {
     				displayAlert(AlertType.INFORMATION, "TORRE EXISTENTE", "El numero de la torre ya existe");
     			} else {
        				txtCostoAdmin.setText(zonaDAO.traerDatosDeZonaAdmin(torrec.getElementsByTagName("idzona").item(0).getTextContent()));
        				txtCostoParqueadero.setText(zonaDAO.traerDatosDeZonaParq(torrec.getElementsByTagName("idzona").item(0).getTextContent()));
        				idZona = zonaDAO.traerDatosDeZonaId(torrec.getElementsByTagName("idzona").item(0).getTextContent());
        						
        						if (torreDAO.createTorre(torreBuilder.setNumero(Integer.parseInt(torrec.getElementsByTagName("numero").item(0).getTextContent()))
        												.setZona(idZona, torrec.getElementsByTagName("idzona").item(0).getTextContent(), Float.parseFloat(txtCostoAdmin.getText()),
        												numeroPuestosParq, Float.parseFloat(txtCostoParqueadero.getText()))
        												.build())) {
        							displayAlert(AlertType.INFORMATION, "TORRE CREADA", "Torre guardada con exito");

        							float suma = Float.parseFloat(txtCostoAdmin.getText())
        									+ Float.parseFloat(txtCostoParqueadero.getText());
        							//agrega a la tabla
        							listaTorre.add(new TorreCom(torrec.getElementsByTagName("numero").item(0).getTextContent(), torrec.getElementsByTagName("napto").item(0).getTextContent(), torrec.getElementsByTagName("idzona").item(0).getTextContent(),
        									torrec.getElementsByTagName("npuestos").item(0).getTextContent(), Float.toString(suma)));
        							//limpia los campos
        							limpiarCampos();
        						} else {
        							displayAlert(AlertType.ERROR, "ERROR", "ERROR al guardar la torre");
        						}	                    
                    
                  
                }
	        }
            

        }
		catch (Exception e) {
            e.printStackTrace();

        }

	}
	@FXML
	void refresacar(ActionEvent event) {
		iniciarlizarLista();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ZonaDAO zonaDAO = new ZonaDAO();
		ObservableList<String> lista = zonaDAO.listZona();
		cbTipo.setItems(lista);
		iniciarlizarLista();
	}
}
