package br.com.matheuscirillo.desafios.jsonserializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * Desafio que propunha criar um serializador de objetos para JSON que suporte
 * arrays e objetos. Suporta toda a Collections API, Maps, Objetos, arrays
 * primitivos (de tipos primitivos ou não).
 * 
 * @author Matheus Cirillo
 *
 */
public class JsonWriter {

	/**
	 * Constante que indica o início de um objeto no JSON
	 */
	private final String START_OBJECT = "{";

	/**
	 * Constante que indica o fim de um objeto no JSON
	 */
	private final String END_OBJECT = "}";

	/**
	 * Constante que indica o início de um array no JSON
	 */
	private final String START_ARRAY = "[";

	/**
	 * Constante que indica o fim de um array no JSON
	 */
	private final String END_ARRAY = "]";

	/**
	 * Constante que indica o separador entre chave e valor no JSON
	 */
	private final String KEY_VALUE_SEP = ":";

	/**
	 * Constante que indica o separador entre objetos / valores no JSON
	 */
	private final String SEP = ",";

	/**
	 * Constante que indica o caractere que envolve um valor do tipo String no JSON
	 */
	private final String STRING_ENCLOSURE = "\"";

	/**
	 * Constante que indica o caractere que envolve a chave no JSON
	 */
	private final String KEY_ENCLOSURE = "\"";

	/**
	 * Constante que indica a ausência de um caractere que envolve um valor no JSON
	 * (ex.: boolean, integer, double, etc são literais que não possuem aspas em sua
	 * representação no JSON)
	 */
	private final String NO_ENCLOSURE = "";

	/**
	 * 
	 * Método que recebe o objeto do invocador e delega a construção do JSON para o
	 * método <code>doWrite</code>
	 * 
	 * Para simplificar, faz um catch de todas as Checked Exceptions e lança uma
	 * RuntimeException passando a rootCause. Assim, o invocador não precisa se
	 * preocupar em dar <code>throws</code> ou tratá-las
	 * 
	 * @param object Objeto a ser serializado
	 * @return JSON
	 * 
	 */
	public String writeAsJson(Object object) {
		if (object == null)
			throw new IllegalArgumentException("Parameter 'object' cannot be null");

		try {
			return doWrite(object);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * Método helper que inicializa o StringBuilder e os contadores necessários
	 * 
	 * @param object
	 * @return JSON
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private String doWrite(Object object) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return doWrite(object, new StringBuilder(), 0, 0);
	}

	/**
	 * 
	 * Método que de fato escreve o JSON
	 * 
	 * @param object       Objeto a ser serializado
	 * @param builder      Referência do StringBuilder
	 * @param depth        Profundidade (iniciada em zero)
	 * @param currentDepth Profundidade atual (iniciada em zero)
	 * @return JSON
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private String doWrite(Object object, StringBuilder builder, int depth, int currentDepth)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (object instanceof Collection || object.getClass().isArray()) {
			if (object.getClass().isArray()) {
				object = collectAsList(object);
			}

			builder.append(START_ARRAY);
			depth = ((Collection<?>) object).size();
			currentDepth = 0;
			for (Object obj : (Collection<?>) object) {
				currentDepth++;
				doWrite(obj, builder, depth, currentDepth);
				if (currentDepth < depth)
					builder.append(SEP);
			}
			builder.append(END_ARRAY);
		} else {
			if (isPrimitiveType(object.getClass())) {
				builder.append(findEnclosureChar(object.getClass()));
				builder.append(object instanceof String ? escape((String) object) : object);
				builder.append(findEnclosureChar(object.getClass()));
			} else {
				builder.append(START_OBJECT);
				if (object instanceof Map) {
					depth = ((Map<?, ?>) object).size();
					currentDepth = 0;
					for (Object key : ((Map<?, ?>) object).keySet()) {
						currentDepth++;
						Object obj = ((Map<?, ?>) object).get(key);
						builder.append(KEY_ENCLOSURE);
						builder.append(String.valueOf(key));
						builder.append(KEY_ENCLOSURE);
						builder.append(KEY_VALUE_SEP);
						if (obj == null) {
							builder.append("null");
						} else {
							doWrite(obj, builder, depth, currentDepth);
						}
						if (currentDepth < depth)
							builder.append(SEP);
					}
				} else {
					Field[] fields = object.getClass().getDeclaredFields();
					depth = fields.length;
					currentDepth = 0;
					for (Field field : fields) {
						currentDepth++;
						builder.append(KEY_ENCLOSURE);
						builder.append(field.getName());
						builder.append(KEY_ENCLOSURE);
						builder.append(KEY_VALUE_SEP);
						Method getter = object.getClass().getMethod(guessGetMethod(field.getName()));
						Object result = getter.invoke(object);
						if (result == null) {
							builder.append("null");
						} else {
							doWrite(result, builder, depth, currentDepth);
						}
						if (currentDepth < depth)
							builder.append(SEP);
					}
				}
				builder.append(END_OBJECT);
			}
		}

		return builder.toString();

	}

	/**
	 * Método utilizado para escapar os caracteres de Strings
	 * 
	 * @param object
	 * @return
	 */
	private String escape(String object) {
		return object.replace("\\", "\\\\").replace("\t", "\\t").replace("\b", "\\b").replace("\n", "\\n")
				.replace("\r", "\\r").replace("\f", "\\f").replace("\'", "\\'").replace("\"", "\\\"");
	}

	/**
	 * Utiliza reflections para converter arrays primitivos para uma lista - se faz
	 * necessário, pois, dessa forma é possível iterar todos os arrays da mesma
	 * forma durante a escrita do JSON
	 * 
	 * @param object Array
	 * @return Lista com os valores do array
	 */
	private List<?> collectAsList(Object object) {
		List<Object> list = new ArrayList<>();
		int length = Array.getLength(object);
		for (int i = 0; i < length; i++) {
			list.add(Array.get(object, i));
		}

		return list;
	}

	/**
	 * Identifica se o tipo recebido é primitivo
	 * 
	 * @param type
	 * @return <code>true</code> quando é primitivo e <code>false</code> quando não
	 *         é primitivo
	 */
	private boolean isPrimitiveType(Class<?> type) {
		return type == String.class || type == Integer.class || type == Long.class || type == Double.class
				|| type == Float.class || type == Boolean.class || type == Byte.class;
	}

	/**
	 * Retorna o caractere que deve envolver o valor durante a serialização - no
	 * caso de String, é a constante STRING_ENCLOSURE, em outros casos é a constante
	 * NO_ENCLOSURE
	 * 
	 * @param type Tipo do campo
	 * @return o caractere que deve ser utilizado para envolver o valor
	 */
	private String findEnclosureChar(Class<?> type) {
		if (type != Integer.class && type != Long.class && type != Double.class && type != Float.class)
			return STRING_ENCLOSURE;
		else
			return NO_ENCLOSURE;
	}

	/**
	 * Retorna o getter utilizado para obter o dado do campo
	 * 
	 * @param fieldName Nome do campo
	 * @return o nome do método
	 */
	private String guessGetMethod(String fieldName) {
		return "get" + capitalize(fieldName);
	}

	/**
	 * Torna a primeira letra da String maiúscula
	 * 
	 * @param str
	 * @return
	 */
	private String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

}
