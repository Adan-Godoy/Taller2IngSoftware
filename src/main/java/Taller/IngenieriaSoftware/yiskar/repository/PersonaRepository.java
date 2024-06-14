package Taller.IngenieriaSoftware.yiskar.repository;

import Taller.IngenieriaSoftware.yiskar.entities.Cliente;
import Taller.IngenieriaSoftware.yiskar.entities.ClienteAutenticado;
import Taller.IngenieriaSoftware.yiskar.entities.JefeLocal;
import Taller.IngenieriaSoftware.yiskar.entities.Persona;
import Taller.IngenieriaSoftware.yiskar.interfaces.IObserver;
import Taller.IngenieriaSoftware.yiskar.util.AlertBox;
import javafx.scene.control.Alert;

import java.io.*;

public class PersonaRepository implements IObserver {
    /**
     * Direccion del archivo "CLiente.txt" que contiene la información de los clientes.
     */
    private String clientesDir = "\\src\\main\\resources\\Taller\\IngenieriaSoftware\\yiskar\\Data\\Cliente.txt";
    /**
     * Direccion el archivo "JefeLocal.txt" que contiene la información de los jefes de local.
     */
    private String jefesDir  = "\\src\\main\\resources\\Taller\\IngenieriaSoftware\\yiskar\\Data\\JefeLocal.txt";
    /**
     * Arreglo que almacena a los clientes.
     */
    private Persona[] clientes;
    /**
     * Numero que maneja el espacio que ocupa el arreglo de clientes.
     */
    private int espacioMaxClientes;
    /**
     * Numero que maneja la cantidad de clientes registrados en el arreglo de clientes.
     */
    private int cantidadClientes;
    /**
     * Arreglo que almacena a los jefes de local.
     */
    private Persona[] jefes;
    /**
     * Numero que maneja el espacio que ocupa el arreglo de jefes de local.
     */
    private int espacioMaxJefes;
    /**
     * Numero que maneja la cantidad de clientes registrados en el arreglo de jefes de local.
     */
    private int cantidadJefes;
    /**
     * Instancia de la clase.
     */
    private static PersonaRepository personaRepository;
    /**
     * Método constructor de la clase, lee los archivos "Cliente.txt" y "JefeLocal.txt" y almacena su información en arreglos distintos.
     */
    private PersonaRepository()
    {
        espacioMaxJefes = 100;
        espacioMaxClientes = 100;
        cantidadClientes = 0;
        cantidadJefes = 0;
        clientes = new Persona[espacioMaxClientes];
        jefes = new Persona[espacioMaxJefes];

        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + clientesDir))){
            {
                String line, nombre, edad, correo, contrasenia, puntos;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    String[] datos = line.split(",");
                    nombre = datos[0];
                    edad = datos[1];
                    correo = datos[2];
                    contrasenia = datos[3];
                    puntos = datos[4];
                    Cliente cliente = new Cliente(nombre,Integer.parseInt(edad),correo, contrasenia, Integer.parseInt(puntos));
                    clientes[i] = cliente;
                    i++;
                    cantidadClientes++;
                    if(i==espacioMaxClientes)
                    {
                        clientes = this.expandirEspacio(true);
                    }
                }
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + jefesDir))){
            {
                String line, nombre, edad, correo, contrasenia;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    String[] datos = line.split(",");
                    nombre = datos[0];
                    edad = datos[1];
                    correo = datos[2];
                    contrasenia = datos[3];
                    JefeLocal persona = new JefeLocal(nombre,Integer.parseInt(edad),correo,contrasenia);
                    jefes[i] = persona;
                    i++;
                    cantidadJefes++;
                    if(i==espacioMaxJefes)
                    {
                        jefes = this.expandirEspacio(false);
                    }
                }
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que duplica el espacio de memoria que ocupan las arreglos de clientes o jefes.
     * @param cliente True si se quiere modificar la lista de clientes, false si se quiere modificar la lista de jefes.
     * @return La lista especificada con el doble de tamaño.
     */
    private Persona[] expandirEspacio(boolean cliente)
    {
        if(cliente)
        {
            espacioMaxClientes = espacioMaxClientes*2;
            Persona[] nuevaLista = new Persona[espacioMaxClientes];
            for(int i = 0; i<espacioMaxClientes/2;i++)
            {
                nuevaLista[i] = clientes[i];
            }
            return nuevaLista;
        }
        espacioMaxJefes = espacioMaxJefes*2;
        Persona[] nuevaLista = new Persona[espacioMaxJefes];
        for(int i = 0; i<espacioMaxJefes/2;i++)
        {
            nuevaLista[i] = clientes[i];
        }
        return nuevaLista;
    }

    /**
     * Método que retorna la instancia de la clase.
     * @return Instancia de la clase.
     */
    public static PersonaRepository getInstance()
    {
        if(personaRepository==null)
        {
            personaRepository = new PersonaRepository();
        }
        return personaRepository;
    }

    /**
     * Método que loguea a la persona según sea cliente o jefe de local por su correo y contraseña.
     * @param correo correo de la persona.
     * @param contrasenia contraseña de la persona.
     * @return
     */
    public static String loguearPersona(String correo, String contrasenia)
    {
        for(int i = 0; i< personaRepository.cantidadClientes;i++)
        {
            if(personaRepository.clientes[i].getEmail().equals(correo) && personaRepository.clientes[i].getContrasenia().equals(contrasenia))
            {
                ClienteAutenticado clienteAutenticado = ClienteAutenticado.getInstancia();
                clienteAutenticado.setCliente(personaRepository.clientes[i]);
                return "Cliente";
            }
        }
        for(int i = 0; i< personaRepository.cantidadJefes;i++)
        {
            if(personaRepository.jefes[i].getEmail().equals(correo) && personaRepository.jefes[i].getContrasenia().equals(contrasenia))
            {
                return "Jefe";
            }
        }
        return null;
    }

    public boolean registrarCliente(String nombre, String edad, String correo, String contrasenia)
    {
        for(int i=0;i<cantidadClientes;i++)
        {
            if(clientes[i].getEmail().equals(correo))
            {
                AlertBox.mostrarError("El correo electrónico ingresado ya existe en el sistema","Error", Alert.AlertType.ERROR);
                return false;
            }
        }
        for(int i=0;i<cantidadClientes;i++)
        {
            if(jefes[i].getEmail().equals(correo))
            {
                AlertBox.mostrarError("El correo electrónico ingresado ya existe en el sistema","Error", Alert.AlertType.ERROR);
                return false;
            }
        }
        Cliente cliente = new Cliente(nombre,Integer.parseInt(edad),correo, contrasenia, 0);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + personaRepository.clientesDir, true))) {
            writer.write(String.format("%s,%s,%s,%s,%s%n", nombre, edad, correo, contrasenia,0));
            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        clientes[cantidadClientes] = cliente;
        cantidadClientes = cantidadClientes+1;
        if(cantidadClientes == espacioMaxClientes)
        {
            expandirEspacio(true);
        }
        return true;
    }

    @Override
    public void actualizarPuntos(int puntos)
    {
        ClienteAutenticado clienteAutenticado = ClienteAutenticado.getInstancia();
        String correo = clienteAutenticado.obtenerCorreo();

        for(Persona cliente:clientes)
        {
            if(cliente.getEmail().equals(correo))
            {
                ((Cliente) cliente).setPuntos(puntos);
                actualizarPuntosTXT(puntos,correo);
                return;
            }
        }
    }

    private void actualizarPuntosTXT(int puntos, String correo){
        String rutaArchivo = System.getProperty("user.dir") + clientesDir;
        StringBuilder contenido = new StringBuilder();
        String lineaAReemplazar = "Línea a reemplazar";
        String nuevaLinea = "Esta es la nueva línea.";

        // Leer el contenido del archivo
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if(datos[2].equals(correo))
                {
                    lineaAReemplazar = linea;
                    String[] datosAux = lineaAReemplazar.split(",");
                    nuevaLinea = datosAux[0]+","+datosAux[1]+","+datosAux[2]+","+datosAux[3]+","+puntos;
                    contenido.append(nuevaLinea).append(System.lineSeparator());
                } else {
                    contenido.append(linea).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Escribir el contenido modificado de nuevo en el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            bw.write(contenido.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
