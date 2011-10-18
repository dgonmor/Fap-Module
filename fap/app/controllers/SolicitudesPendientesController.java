/*
package controllers;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Util;
import play.mvc.results.RenderJson;

import app.SolicitudesPendientesJob;

import services.PlatinoException;
import services.RegistroException;
import services.RegistroService;
import messages.Messages;
import models.SolicitudGenerica;
import models.SolicitudPendienteRegistro;
import models.SolicitudesPendientesRegistro;
import controllers.gen.SolicitudesPendientesControllerGen;

public class SolicitudesPendientesController extends SolicitudesPendientesControllerGen {

	
	
	public static void registrarSolicitudes(){
		checkAuthenticity();
		
		RegistroService.registrarSolicitudesPendientes();
		
		registrarSolicitudesRender();

	}

	
	public static void tablatabladetablas(Long idSolicitudesPendientesRegistro, Long idEntidad){

		SolicitudesPendientesRegistro solicitudesPendientes = SolicitudesPendientesRegistro.get(SolicitudesPendientesRegistro.class);

		List<SolicitudPendienteRegistro> rowsFiltered = solicitudesPendientes.solicitudes; 

		tables.TableRenderResponse<SolicitudPendienteRegistro> response = new tables.TableRenderResponse<SolicitudPendienteRegistro>(rowsFiltered);
		RenderJson(response.toJSON("solicitud.id", "fecha", "id"));

	}

	
}
*/