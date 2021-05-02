import java.io.*;
import java.rmi.*;
import java.util.ArrayList;

import static java.lang.System.exit;

public class RegistadoraServer {

    private Registadora c;

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
            c = new RegistadoraImpl();

// iii)
            Naming.rebind("rmi://localhost:1099/RegistadoraService", c);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }

    }

    public Registadora getImpl() {
        return c;
    }

    public static void main(String args[]) throws IOException {


        RegistadoraServer server = new RegistadoraServer();

        InputStreamReader r=new InputStreamReader(System.in);
        BufferedReader br=new BufferedReader(r);

        while(true) {
            System.out.println("Insira o valor 0 para desligar o servidor e guardar");
            if (br.readLine().strip().equals("0")) {
                System.out.println("Closing Server.");
                try {
                    server.getImpl().guardar();
                    Naming.unbind("rmi://localhost:1099/RegistadoraService");
                    exit(1);
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }
        }




    }


}