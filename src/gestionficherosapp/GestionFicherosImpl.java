package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzará una excepción
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("Alerta. No se puede crear "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		//que no exista -> lanzará una excepción
		if (file.exists()) {
			throw new GestionFicherosException("Alerta. No se puede crear "
					+ file.getAbsolutePath() + ". Ya existe");
		}
		file.mkdir();
		//crear la carpeta -> lanzará una excepción
		if (!file.mkdir()) {
			throw new GestionFicherosException("Alerta. No se ha podido crear"
					+ file.getAbsolutePath() + ".");
		}
		
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);

		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("Alerta. No se puede crear "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		
		if (file.exists()) {
			throw new GestionFicherosException("Alerta. No se puede crear "
					+ file.getAbsolutePath() + ". Ya existe");
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new GestionFicherosException("Alerta. No se puede crear "
					+ file.getAbsolutePath() + ".");
		}
		actualiza();
		
		
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {

		File file = new File(carpetaDeTrabajo,arg0);

		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("Alerta. No se puede borrar "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		
		if (!carpetaDeTrabajo.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede borrar "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		
		if (!file.exists()) {
			throw new GestionFicherosException("Alerta. No se puede borrar "
					+ file.getAbsolutePath() + ". No existe");
		}
		
		file.delete();
		actualiza();

	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzará una excepción
		if (!file.exists()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No existe");
		}
		
		
		
		//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		
		//Título
		strBuilder.append("INFORMACIÓN DEL SISTEMA");
		strBuilder.append("\n\n");
		
		//Nombre
		strBuilder.append("Nombre: ");
		strBuilder.append(arg0);
		strBuilder.append("\n");
		
		//Tipo: fichero o directorio
		strBuilder.append("Tipo: ");
		
		if (!file.isDirectory()) {
			strBuilder.append("Fichero");
		}
		else
			{
			strBuilder.append("Directorio");
		}
		strBuilder.append("\n");
		
		//Ubicación
		strBuilder.append("Ubicación: ");
		strBuilder.append(file.getPath());
		strBuilder.append("\n");
		
		
		//Fecha de última modificación
		
		SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ROOT);
		
		GregorianCalendar calendario = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
		calendario.setTimeInMillis(file.lastModified());
		
		strBuilder.append("Ultima modificacion: ");
		strBuilder.append(fecha.format(calendario.getTime()));
		//strBuilder.append(file.lastModified());
		strBuilder.append("\n");
		
		//Si es un fichero oculto o no
		strBuilder.append("Oculto: ");
		
		if (!file.isHidden()) {
			strBuilder.append("No");
		}
		else
			{
			strBuilder.append("Si");
		}
		
		strBuilder.append("\n");
		
		//Si es un fichero: Tamaño en bytes
		if (!file.isDirectory()) {
			strBuilder.append("Tamaño: ");
			strBuilder.append(file.getTotalSpace());
			strBuilder.append(" bytes.");
			strBuilder.append("\n");
		}
		
		//Si es directorio: Número de elementos que contiene, 
		if (!file.isFile()) {
			String[] elem;
			elem=file.list();
			strBuilder.append("Elementos: ");
			strBuilder.append(elem.length);
			strBuilder.append("\n");
		}
		//Si es directorio: Espacio libre, espacio disponible, espacio total (bytes)
		if (!file.isFile()) {
			strBuilder.append("Espacio libre: ");
			strBuilder.append(file.getFreeSpace());
			strBuilder.append(" bytes.");
			strBuilder.append("\n");
			strBuilder.append("Espacio disponible: ");
			strBuilder.append(file.getUsableSpace());
			strBuilder.append(" bytes.");
			strBuilder.append("\n");
			strBuilder.append("Espacio total: ");
			strBuilder.append(file.getTotalSpace());
			strBuilder.append(" bytes.");
			strBuilder.append("\n");
		}
				
		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)
			throws GestionFicherosException {
		
		File file = new File(carpetaDeTrabajo,arg0);
		File fileNew = new File(carpetaDeTrabajo,arg1);

		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("Alerta. No se puede renombrar "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		
		if (!carpetaDeTrabajo.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede renombrar "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		
		if (!file.exists()) {
			throw new GestionFicherosException("Alerta. No se puede renombrar "
					+ file.getAbsolutePath() + ". No existe");
		}
		
		if (fileNew.exists()) {
			throw new GestionFicherosException("Alerta. No se puede renombrar "
					+ file.getAbsolutePath() + ". Ya existe un fichero con ese nombre");
		}
		
		file.renameTo(fileNew);
		actualiza();

	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
