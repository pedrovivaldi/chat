package chat;



import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Caio CÃ©sar
 */
public class Salas implements Serializable{
    private ArrayList<Sala> salas;
    
    
    public Salas() {
        salas = new ArrayList();
    }
    
    public void addSala(Sala sala){
        salas.add(sala);
    }
    
    public void removeSala(Sala sala){
        salas.remove(sala);
    }
    
    public void removeAt(int i){
        salas.remove(i);
    }
    
    public void setSala(int i, Sala sala){
        salas.set(i, sala);
    }
    
    public Sala getSala(int i){
        return salas.get(i);
    }
    
    public Sala getSala(String nome){
        for (int i = 0;i<salas.size();i++) {
            if (salas.get(i).getNome().equals(nome)){
                return salas.get(i);
            }
        }
        return null;
    }
    
    public int qntasSalas(){
        return salas.size();
    }
}
