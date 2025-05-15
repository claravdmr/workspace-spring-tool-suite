package com.example.mechanical_workshop.test.util;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TestUtils {
	/**
	 * ObjectMapper valido para convertir objetos con HATEOAS
	 */
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules()
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);

	private static final EasyRandomParameters EASY_RANDOM_PARAMETERS = new EasyRandomParameters()
			.stringLengthRange(8, 32).collectionSizeRange(1, 4);
	/**
	 * EasyRandom para generar objetos con valores aleatoreos
	 */
	public static final EasyRandom EASY_RANDOM = new EasyRandom(EASY_RANDOM_PARAMETERS);

	private static final CountDownLatch WAITER = new CountDownLatch(1);

	private TestUtils() {
		// Private Constructor
	}

	public static void waitMilliseconds(long milliseconds) throws InterruptedException {
		WAITER.await(milliseconds, TimeUnit.MILLISECONDS);
	}

	public static void waitSeconds(long seconds) throws InterruptedException {
		WAITER.await(seconds, TimeUnit.SECONDS);
	}

	/**
	 * Devuelve un objeto en formato Json String
	 * 
	 * @param object Objeto a formatear
	 * @return Devuelve el String del objeto en formato Json
	 * @throws JsonProcessingException
	 */
	public static String writeAsString(Object object) throws JsonProcessingException {
		return OBJECT_MAPPER.writeValueAsString(object);
	}

	/**
	 * Genera un objeto con valores aleatorios para sus campos.
	 * 
	 * @param <T>
	 * @param clazz Clase del objeto a generar
	 * @return Objeto generado.
	 */
	public static <T> T createObject(Class<T> clazz) {
		return EASY_RANDOM.nextObject(clazz);
	}

	/**
	 * Genera varios objetos con valores aleatorios para sus campos.
	 * 
	 * @param <T>
	 * @param clazz  Clase de los objetos a generar
	 * @param amount Cantidad de objetos a generar
	 * @return Lista de objetos generados
	 */
	public static <T> List<T> createObjects(Class<T> clazz, int amount) {
		return EASY_RANDOM.objects(clazz, amount).collect(Collectors.toList());
	}
}
