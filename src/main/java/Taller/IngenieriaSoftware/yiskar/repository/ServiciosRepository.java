package Taller.IngenieriaSoftware.yiskar.repository;

import Taller.IngenieriaSoftware.yiskar.entities.Servicio;

import java.io.*;

public class ServiciosRepository
{
    /**
     * Instancia de la clase.
     */
    private static ServiciosRepository instancia;
    /**
     * Lista de servicios en el sistema.
     */
    private Servicio[] servicios;
    /**
     * Variable que almacena la cantidad de servicios existentes en el sistema.
     */
    private int cantServicios;
    /**
     * Variable que controla el espacio de la lista de servicios del sistema.
     */
    private int cantMaxServicios;
    /**
     * Variable que contiene la dirección del archivo de texto de los servicios del sistema.
     */
    private final String serviciosDir = "\\src\\main\\resources\\Taller\\IngenieriaSoftware\\yiskar\\Data\\Servicios.txt";

    /**
     * Método constructor de la clase, que lee el archivo correspondiente a los servicios y los almacena en la variable servicios.
     */
    private ServiciosRepository()
    {
        cantServicios = 0;
        cantMaxServicios = 100;
        servicios = new Servicio[100];
        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + serviciosDir))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0];
                String precio = datos[1];
                Servicio servicio = new Servicio(nombre,Integer.parseInt(precio));
                servicios[cantServicios] = servicio;
                cantServicios++;
                if(cantServicios==cantMaxServicios)
                {
                    expandirMemoria();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que retorna la instancia de la clase, si no existe, crea una y la devuelve.
     * @return
     */
    public static ServiciosRepository getInstancia()
    {
        if(instancia==null)
        {
            instancia = new ServiciosRepository();
        }
        return instancia;
    }

    /**
     * Método que duplica el espacio de la lista de servicios almacenados en el sistema.
     */
    private void expandirMemoria()
    {
        cantMaxServicios = cantServicios*2;
        Servicio[] nuevo = new Servicio[cantMaxServicios];

        for(int i = 0; i<cantServicios;i++)
        {
            nuevo[i] = servicios[i];
        }
        servicios = nuevo;
    }

    /**
     * Método que retorna la lista de servicios del sistema.
     * @return
     */
    public Servicio[] obtenerServicios(){return servicios;}

    /**
     * Método que permite agregar un servicio al sistenma.
     * @param nombre Nombre del nuevo servicio.
     * @param precio Precio del nuevo servicio.
     * @return True si se agregó el servicio al sistema, false de lo contrario.
     */
    public boolean agregarServicio(String nombre, int precio)
    {
        for(Servicio servicio:servicios)
        {
            if(servicio==null){break;}
            if(servicio.getNombre().equalsIgnoreCase(nombre))
            {
                return false;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + serviciosDir, true))) {
            writer.write(String.format("%s,%s%n", nombre, precio));
            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        Servicio nuevo = new Servicio(nombre,precio);
        servicios[cantServicios] = nuevo;
        cantServicios++;
        if(cantServicios==cantMaxServicios){expandirMemoria();}
        return true;
    }

    /**
     * Método que permite eliminar un servicio del sistema.
     * @param nombre Nombre del servicio a eliminar.
     * @return True si el servicio fué eliminado, false de lo contrario.
     */
    public boolean eliminarServicio(String nombre)
    {
        for(int i=0;i<cantServicios;i++)
        {
            if(servicios[i].getNombre().equalsIgnoreCase(nombre))
            {
                int j = i;
                Servicio aux = servicios[j+1];
                do
                {
                    servicios[j] = aux;
                    j++;
                    aux = servicios[j+1];

                }
                while(aux!=null);
                {
                    servicios[j] = aux;
                    j++;
                    aux = servicios[j+1];
                }
                StringBuilder contenido = new StringBuilder();

                // Leer el contenido del archivo
                try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + serviciosDir))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        String[] datos = linea.split(",");
                        if(datos[0].equalsIgnoreCase(nombre))
                        {
                            //No copiar la linea
                        } else {
                            contenido.append(linea).append(System.lineSeparator());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Escribir el contenido modificado de nuevo en el archivo
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + serviciosDir))) {
                    bw.write(contenido.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cantServicios--;
                return true;
            }
        }
        return false;
    }

    /**
     * Método que permite editar el precio de un servicio registrado en el sistema.
     * @param nombre Nombre del servicio.
     * @param precio Nuevo precio del servicio.
     * @return True si la modificación fue existosa, false de lo contrario.
     */
    public boolean editarPrecio(String nombre, int precio)
    {
        for(Servicio servicio: servicios)
        {
            if(servicio==null)
            {
                break;
            }
            if(servicio.getNombre().equalsIgnoreCase(nombre))
            {
                servicio.setPrecio(precio);

                StringBuilder contenido = new StringBuilder();
                String lineaNueva;

                // Leer el contenido del archivo
                try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + serviciosDir))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        String[] datos = linea.split(",");
                        if(datos[0].equalsIgnoreCase(nombre))
                        {
                            lineaNueva = nombre+","+precio;
                            contenido.append(lineaNueva).append(System.lineSeparator());
                        } else {
                            contenido.append(linea).append(System.lineSeparator());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Escribir el contenido modificado de nuevo en el archivo
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + serviciosDir))) {
                    bw.write(contenido.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Método que permite editar el nombre de un servicio registrado en el sistema.
     * @param nombre Nombre del servicio.
     * @param nuevoNombre Nuevo nombre del servicio.
     * @return True si la modificación fue existosa, false de lo contrario.
     */
    public boolean editarNombre(String nombre, String nuevoNombre)
    {
        for(Servicio servicio: servicios)
        {
            if(servicio==null)
            {
                break;
            }
            if(servicio.getNombre().equalsIgnoreCase(nuevoNombre))
            {
                return false;
            }
        }
        for(Servicio servicio: servicios)
        {
            if(servicio==null)
            {
                break;
            }
            if(servicio.getNombre().equalsIgnoreCase(nombre))
            {
                servicio.setNombre(nuevoNombre);

                StringBuilder contenido = new StringBuilder();
                String lineaNueva;

                // Leer el contenido del archivo
                try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + serviciosDir))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        String[] datos = linea.split(",");
                        if(datos[0].equalsIgnoreCase(nombre))
                        {
                            lineaNueva = nuevoNombre+","+servicio.getPrecio();
                            contenido.append(lineaNueva).append(System.lineSeparator());
                        } else {
                            contenido.append(linea).append(System.lineSeparator());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Escribir el contenido modificado de nuevo en el archivo
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + serviciosDir))) {
                    bw.write(contenido.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}
