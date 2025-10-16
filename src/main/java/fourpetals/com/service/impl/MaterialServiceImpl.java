package fourpetals.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fourpetals.com.entity.Material;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.service.MaterialService;

@Service
public class MaterialServiceImpl implements MaterialService{

	@Autowired
	private MaterialRepository materialRepository;
	
	@Override
	public List<Material> findAll(){
		return materialRepository.findAll();
	}
}
