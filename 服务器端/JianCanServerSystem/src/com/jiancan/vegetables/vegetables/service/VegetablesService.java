package com.jiancan.vegetables.vegetables.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiancan.entity.vegetables.Vegetables;
import com.jiancan.vegetables.vegetables.dao.VegetablesDao;

@Service
public class VegetablesService {

	@Resource
	private VegetablesDao dao;
	
	public Vegetables addVegetables(Vegetables vegetables) {
		
		return dao.saveVegetables(vegetables);
		
	}
	
	public boolean deleteVegetables(int id) {
		
		return dao.deleteVegetables(id);
		
	}
	
	public boolean updateVegetables(Vegetables vegetables) {
		
		return dao.editVegetables(vegetables);
		
	}
	
	public List<Vegetables> listVegetables() {
		
		return dao.selectAll();
		
	}
	
	
}
