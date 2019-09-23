package pe.edu.upc.integracion.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Aprender {
	private String capital;
	private String pais;
	private String nombre;
	private Integer celular;
}
