
package Data;

import Model.User;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.swing.JOptionPane;

//CRUD and access to file users.txt. 
public class UserDAO {

    private final String ARCHIVO = "users.txt";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    
    
    
    
    //Convert user to line:
    private String toFileString(User u){
        return u.getCedula() + ";" +
               u.getNombre() + ";" +
               sdf.format(u.getFechaNacimiento()) + ";" +
               u.getGenero() + ";" +
               u.getCorreo() + ";" +
               u.getContrasena();
    }

    //Convert line to user:
    private User fromFileString(String line){
        try{
            String[] p = line.split(";");
            return new User(
                    Integer.parseInt(p[0]),   //Cédula
                    p[1],                     //Nombre
                    sdf.parse(p[2]),         //Fecha de nacimiento
                    p[3],                     //Género
                    p[4],                     //Correo
                    p[5]                      //Constraseña
            );
        }catch(ParseException e){
            JOptionPane.showMessageDialog(null, "Error al parsear la fecha de nacimiento",
            "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    
    
    //Load all users to the file:
    public List<User> cargarUsers(){
        List<User> list = new ArrayList<>();
        
        File file = new File(ARCHIVO);
        if(!file.exists()){
            return list; //If the list is empy
        }
        try(BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))){
            String line;
            while((line = br.readLine()) != null){
                User u = fromFileString(line);
                if(u != null){
                    list.add(u);
                }
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al cargar el archivo",
            "Error", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }
    
    
    
    
    
    //Save a new user to the file:
    public void saveUser(User u){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            bw.write("---------------------------------------------------");
            bw.write("|               REGISTRO DE USUARIOS              |");
            bw.write("---------------------------------------------------");
            bw.write(toFileString(u));
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el usuario",
            "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //Search for user by ID number:
    public User buscarPorCedula(int cedula){
        for(User u : cargarUsers()){
            if(u.getCedula() == cedula){
                return u;
            }
        }
        return null;
    }
}
