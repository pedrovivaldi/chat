package chat;



import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Caio CÃ©sar
 */
public class Usuarios implements Serializable {

    private ArrayList<Usuario> usuarios;

    public Usuarios() {
        usuarios = new ArrayList();
    }

    public void addUsuario(Usuario user) {
        usuarios.add(user);
    }

    public void removeUsuario(Usuario user) {
        usuarios.remove(user);
    }

    public void removeUsuario(String login) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getLogin().equals(login)) {
                usuarios.remove(i);
            }
        }
    }

    public void removeAt(int i) {
        usuarios.remove(i);
    }

    public void setUsuario(int i, Usuario user) {
        usuarios.set(i, user);
    }
    
    public void setUsuario(Usuario antUser, Usuario user) {
        for (int i = 0; i < usuarios.size(); i++){
            if (usuarios.get(i) == antUser)
                usuarios.set(i, user);
        }
    }

    public Usuario getUsuario(int i) {
        return usuarios.get(i);
    }

    public Usuario getUsuario(String login) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getLogin().equals(login)) {
                return usuarios.get(i);
            }
        }
        return null;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public int qntosUsuarios() {
        return usuarios.size();
    }
}
