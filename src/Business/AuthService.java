
package Business;

import Data.UserDAO;
import Model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.swing.JOptionPane;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    
    
    
    
    
    //LOGIN: Validate ID and encrypted password: 
    public User login(int cedula, String contrasena){
        
        //Look up user by ID number:
        User found = userDAO.buscarPorCedula(cedula);
        if(found == null){
            JOptionPane.showMessageDialog(null, "Usuario con la cédula " + cedula +
            "\nno existente en el sistema", "Error",JOptionPane.ERROR_MESSAGE);
            return null;
        }
        //Encrypt the entered password:
        String contrasenaEncrip = encriptar(contrasena);
        //Comparation between entered password and password in the file:
        if(found.getContrasena().equals(contrasena)){
            return found; //Successful login
        }
        return null; //Incorrect password 
   }
    
    
    
    
    public boolean registrarUser(User nuevo){
        //Check if a user with this Id number exists:
        User exists = userDAO.buscarPorCedula(nuevo.getCedula());
        if(exists != null){
            JOptionPane.showMessageDialog(null, "Usuario ya existente con el número de cédula:" + getClass(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false; //User exist
        }
        //Encrypts password before saving.
        String passEnc = encriptar(nuevo.getContrasena());
        nuevo.setContrasena(passEnc);
        
        //Saving in the file:
        JOptionPane.showMessageDialog(null, "Usuario guardado exitosamente", "Operación Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
        userDAO.saveUser(nuevo);
        return true;
    }
    
    
    
    
    
    
    //Encrypt the entered password(SHA-256):
    //**************************************
    public String encriptar(String contr){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contr.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for(byte b : hash){
                sb.append(String.format("%02x", b));
            }
            return  sb.toString();//Send to file users.txt
        }catch(NoSuchAlgorithmException ex){
            throw  new RuntimeException("Error al encriptar la contraseña ingresada: " + ex.getMessage());
        }
    }
}
