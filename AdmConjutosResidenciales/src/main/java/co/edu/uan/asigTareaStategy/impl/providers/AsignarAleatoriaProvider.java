package co.edu.uan.asigTareaStategy.impl.providers;

import java.util.ArrayList;
import java.util.Random;

import com.itextpdf.text.List;

import co.edu.uan.asigTareaStategy.impl.IAsignacionStrategy;
import co.edu.uan.entidad.Tarea;

public class AsignarAleatoriaProvider implements IAsignacionStrategy{

	@Override
	public int asignartarea(ArrayList  listatarea) {		
		int id_tarea;	 
		 
		Random rand = new Random();
		for(int i = 0; i < listatarea.size();i++){
		   boolean existe = false;
		   id_tarea = rand.nextInt(10);
		   for(int j = 0; j < i; j++){
		      if(listatarea.get(i).equals(id_tarea))
		          existe = true;
		      	return id_tarea;
		      
		   }
		   
		   //if(existe) continue;
		   //i++;
		}  
		return 0;
		// TODO Auto-generated method stub
		
	}

}
