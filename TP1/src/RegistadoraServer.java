import java.rmi.*;
import java.util.ArrayList;

public class RegistadoraServer {



    public RegistadoraServer() {
// i)
        System.setSecurityManager(new SecurityManager());
        try { //Iniciar a execução do registry no porto desejado
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("Exception starting RMI registry:");
            e.printStackTrace();
        }
        try {
// ii)
            Registadora c = new RegistadoraImpl();
// iii)
            Naming.rebind("rmi://localhost:1099/RegistadoraService", c);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }

    }



    public static void main(String args[]) {
        new RegistadoraServer();
    }


}