package Taller.IngenieriaSoftware.yiskar.repository;

import Taller.IngenieriaSoftware.yiskar.entities.Servicio;

import java.io.*;

public class ServiciosRepository
{
    private static ServiciosRepository instancia;
    private Servicio[] servicios;
    private int cantServicios;
    private int cantMaxServicios;
    private final String serviciosDir = "\\src\\main\\resources\\Taller\\IngenieriaSoftware\\yiskar\\Data\\Servicios.txt";

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

    public static ServiciosRepository getInstancia()
    {
        if(instancia==null)
        {
            instancia = new ServiciosRepository();
        }
        return instancia;
    }

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

    public Servicio[] obtenerServicios(){return servicios;}

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
