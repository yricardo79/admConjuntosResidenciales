package co.edu.uan.envioCorreoProxy;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.edu.uan.entidad.Propietario;
import co.edu.uan.entidad.Servicio;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProxyEnvioCorreo implements Correo{


	@Override
	public boolean enviarCorreoResidente(Propietario prop) {
		boolean envio=false;
		if(isNumeric(prop.getDocumento())) {
			if(isNumeric(prop.getTelefono())) {
				if(isEmail(prop.getCorreo())) {
					if(mayorEdad(prop.getNacimiento())) {
						Correo envioCorreo = new EnvioCorreo();
						envio= envioCorreo.enviarCorreoResidente(prop);
					}else {
						displayAlert(AlertType.WARNING, "Menor de edad", "el propietario tiene que ser mayor de edad");
					}
				}else {
					displayAlert(AlertType.WARNING, "correo electronico invalido", "Correo electronico invalido");
				}
			}else {
				displayAlert(AlertType.WARNING, "Numero telefono invalido", "Numero de telefono invalido");
			}
		}else {
			displayAlert(AlertType.WARNING, "Numero documento invalido", "Numero de documento invalido");
		}
		return envio;
	}
	
	@Override
	public boolean enviarCorreoPServicio(Servicio servicio) {
		boolean envio=false;
		if(isNumeric(servicio.getDocumento())) {
			if(isNumeric(servicio.getTelefono())) {
				if(isEmail(servicio.getCorreo())) {
					if(mayorEdad(servicio.getNacimiento())) {
						Correo envioCorreo = new EnvioCorreo();
						envio= envioCorreo.enviarCorreoPServicio(servicio);
					}else {
						displayAlert(AlertType.WARNING, "Menor de edad", "el propietario tiene que ser mayor de edad");
					}
				}else {
					displayAlert(AlertType.WARNING, "correo electronico invalido", "Correo electronico invalido");
				}
			}else {
				displayAlert(AlertType.WARNING, "Numero telefono invalido", "Numero de telefono invalido");
			}
		}else {
			displayAlert(AlertType.WARNING, "Numero documento invalido", "Numero de documento invalido");
		}
		return envio;
	}
	
	private boolean isNumeric(String cadena) {
		int i=0;
		if(cadena.charAt(0)=='-') {
			if(cadena.length()>1) {
			i++;
			}else {
				return false;
			}
		}
		for(; i< cadena.length(); i++) {
			if(!Character.isDigit(cadena.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	private boolean isEmail(String email) {
		// Patrón para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        return mather.find();
	}
	private boolean mayorEdad(String fecha) {
		boolean mayor = false;
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNac = LocalDate.parse(fecha, fmt);
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNac, ahora);
        
        if(periodo.getYears()>=18) {
        	mayor=true;
        }
		
		return mayor;
	}
	private void displayAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	
}
