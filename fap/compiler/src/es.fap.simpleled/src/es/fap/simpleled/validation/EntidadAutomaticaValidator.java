package es.fap.simpleled.validation;

import es.fap.simpleled.led.Attribute;
import es.fap.simpleled.led.Entity;
import es.fap.simpleled.led.util.LedEntidadUtils;

public class EntidadAutomaticaValidator extends LedElementValidator{

	@Override
	public boolean aceptaEntidad(Entity entidad) {
		return true;
	}

	@Override
	public boolean aceptaAtributo(Attribute atributo) {
		return LedEntidadUtils.xToOne(atributo);
	}

	@Override
	public String mensajeError() {
		return "El campo tiene que ser una entidad o una referencia individual";
	}
	
}
