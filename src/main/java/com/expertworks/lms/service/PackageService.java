package com.expertworks.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expertworks.lms.model.Pack;
import com.expertworks.lms.repository.PackageRepository;

@Service
public class PackageService {

	@Autowired
	private PackageRepository packageRepository;

	public Pack getPack(String packId) {
		return packageRepository.getPack(packId);
	}













}
