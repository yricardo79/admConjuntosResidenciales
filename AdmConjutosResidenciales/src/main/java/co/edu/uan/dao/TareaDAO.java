package co.edu.uan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import co.edu.uan.DBAdapter.DBFactory;
import co.edu.uan.DBAdapter.IDBAdapter;
import javafx.collections.ObservableList;

public class TareaDAO {

	private IDBAdapter dbAdapter;
	private static TareaDAO tareaDAO = null;

	private TareaDAO() {
		dbAdapter = DBFactory.getDefaultDBAdapter();
	}

	public static TareaDAO getInstance() {
		if (tareaDAO == null) {
			tareaDAO = new TareaDAO();
		}
		return tareaDAO;
	}
	
	//se deben obtener las tareas
	public void ObtenerTareas(ObservableList<String> listatarea, String desservicio) {
		Connection connection = dbAdapter.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT tarea.id, tarea.nombretarea FROM tarea, servicio WHERE servicio.id= tarea.id_servicio and servicio.nombreserv=?";
		
	
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, desservicio);			
			rs = ps.executeQuery();
			while (rs.next()) {
				listatarea.add(rs.getString(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}

	}


}
