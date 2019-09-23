package pe.edu.upc.integracion.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.integracion.model.Aprender;
import pe.edu.upc.integracion.service.AprenderService;

@RestController
@RequestMapping("/api")
public class AprenderResource {
	@Autowired
	AprenderService aprenderService;

	@PostMapping("/aprender")
	public boolean aprender(@RequestBody Aprender aprender){
		return aprenderService.aprender(aprender);
	}
}
