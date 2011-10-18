
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
public class SolicitudPendienteRegistro extends Model {
	// Código de los atributos
	
	@org.hibernate.annotations.Columns(columns={@Column(name="fecha"),@Column(name="fechaTZ")})
	@org.hibernate.annotations.Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTimeWithZone")
	public DateTime fecha;
	
	
	@OneToOne(cascade=CascadeType.ALL ,  fetch=FetchType.LAZY)
	public SolicitudGenerica solicitud;
	
	

	public void init(){
		
		
	}
		
	

// === MANUAL REGION START ===
			
// === MANUAL REGION END ===
	
	
	}
		