import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Vendedor extends java.rmi.server.UnicastRemoteObject implements RegistadoraC{

    public Vendedor() throws RemoteException {
        super();
    }

    @Override
    public void printOnClient(String s) throws RemoteException {
        System.out.println("Message from server: " + s);
    }

    public void mainVendedor(){
        System.setSecurityManager(new SecurityManager());
        try {
            InputStreamReader r=new InputStreamReader(System.in);
            BufferedReader br=new BufferedReader(r);
            RegistadoraCliente ci = new RegistadoraCliente();
            Registadora c = (Registadora) Naming.lookup("rmi://DESKTOP-D7S2GUK/RegistadoraService");
            c.subscribe("Nome da máquina do cliente ...",(RegistadoraC)ci);

            while(true){
                System.out.println("--------------------------MENU----------------------\n\n");
                System.out.println("Carregue numa das teclas seguintes para processar uma operação!\n");
                System.out.println("- Dar saída de um produto - 1");
                System.out.println("- Consultar produtos existentes - 2");
                System.out.println("- Consultar as vendas - 3");
                System.out.println("\n\n ----------------------------------------------------------\n");
                System.out.println("WARNINGS:\n");
                c.vStock();
                System.out.println("\n\n ----------------------------------------------------------\n");
                String cat = "";
                int id = 0;
                long pc, pv;
                int stk = 0;
                int stkMin = 0;
                System.out.print("Insira a o número da operação que pretende realizar: ");

                switch(br.readLine()) {
                    case "1":
                        System.out.println("CATEGORIAS EXISTENTES");
                        System.out.println("-mesa");
                        System.out.println("-cama");
                        System.out.println("-sofa");
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("Inserir categoria: ");
                        cat = br.readLine();
                        System.out.println("Inserir ID: ");
                        id = Integer.parseInt(br.readLine());
                        System.out.println("Inserir Stock: ");
                        stk = Integer.parseInt(br.readLine());
                        System.out.println("\nA processar operação! \n \n");
                        c.darSaidaAoProduto(cat, id, stk);
                        System.out.println("\nA operação processada! \n \n");
                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    case "2":
                        System.out.println("PRODUTOS EXISTENTES");
                        System.out.println("-mesa");
                        System.out.println("-cama");
                        System.out.println("-sofa");
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("Inserir categoria: ");
                        cat = br.readLine();
                        System.out.println("\nA consultar\n");
                        ArrayList<String> arr = c.consultarProdutosExistentes(cat);
                        for(int x = 0; x < arr.size(); x++){
                            System.out.println(arr.get(x));
                        }

                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    case "3":
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("A consultar");
                        ArrayList<String> arr2 = c.consultarVendas();
                        for(int x = 0; x < arr2.size(); x++){
                            System.out.println(arr2.get(x));
                        }
                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    default:
                        return;
                }


            }
        }
        catch (RemoteException re) {
            System.out.println("RemoteException");
            System.out.println(re.getMessage());
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Vendedor vendedor = null;
        try {
            vendedor = new Vendedor();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        vendedor.mainVendedor();
    }
}
