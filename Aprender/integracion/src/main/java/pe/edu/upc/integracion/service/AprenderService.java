package pe.edu.upc.integracion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.integracion.model.Aprender;
import pe.edu.upc.integracion.repository.AprenderRepository;

@Service
public class AprenderService {
	@Autowired
	AprenderRepository aprenderRepository;

	public boolean aprender(Aprender aprender){
		return aprenderRepository.aprender(aprender);
	}
}
