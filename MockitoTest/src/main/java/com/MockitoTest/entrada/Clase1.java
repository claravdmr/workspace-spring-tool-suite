package com.MockitoTest.entrada;

import org.springframework.beans.factory.annotation.Autowired;

import com.MockitoTest.negocio.Interfaz2;

public class Clase1 implements Interfaz1{
	
	@Autowired
	Interfaz2 clase2;

}
