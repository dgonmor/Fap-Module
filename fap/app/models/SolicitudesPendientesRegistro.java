
package models;

import java.util.*;
import javax.persistence.*;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import play.data.validation.*;
import org.joda.time.DateTime;
import models.*;
import messages.Messages;
import validation.*;
import audit.Auditable;

// === IMPORT REGION START ===
			
// === IMPORT REGION END ===
	

@Auditable
@Entity
public class SolicitudesPendientesRegistro extends Singleton {
	// Código de los atributos
	
	@OneToMany(cascade=CascadeType.ALL ,  fetch=FetchType.LAZY)
	@JoinTable(name="solicitudespendientesregistro_solicitudes")
	public List<SolicitudPendienteRegistro> solicitudes;
	
	
	public SolicitudesPendientesRegistro (){
		init();
	}
	

	public void init(){
		super.init();
		
						if (solicitudes == null)
							solicitudes = new ArrayList<SolicitudPendienteRegistro>();
						
	}
		
	

// === MANUAL REGION START ===
			
// === MANUAL REGION END ===
	
	
	}
		