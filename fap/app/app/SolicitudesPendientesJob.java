/*package app;

import java.util.ArrayList;
import java.util.List;

import controllers.SolicitudesPendientesController;

import models.SolicitudGenerica;
import models.SolicitudPendienteRegistro;
import models.SolicitudesPendientesRegistro;
import play.db.jpa.JPAPlugin;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import services.RegistroException;
import services.RegistroService;

@OnApplicationStart
@Every("1h")
public class SolicitudesPendientesJob extends Job {
	
	public void doJob() {
		JPAPlugin.startTx(false);
		
		RegistroService.registrarSolicitudesPendientes();
		
		JPAPlugin.closeTx(false);
	}

}
*/