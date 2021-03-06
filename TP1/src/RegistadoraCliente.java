import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RegistadoraCliente extends java.rmi.server.UnicastRemoteObject implements RegistadoraC {

    public RegistadoraCliente() throws RemoteException {
        super();
    }

    @Override
    public void printOnClient(String s) throws RemoteException {
        System.out.println("Message from server: " + s);
    }

    public void mainCliente() {
        System.setSecurityManager(new SecurityManager());
        try {
            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(r);
            RegistadoraCliente ci = new RegistadoraCliente();
            Registadora c = (Registadora) Naming.lookup("rmi://DESKTOP-D7S2GUK/RegistadoraService");
            c.subscribe("Nome da máquina do cliente ...", (RegistadoraC) ci);

            while (true) {
                System.out.println("--------------------------MENU----------------------\n\n");
                System.out.println("Carregue numa das teclas seguintes para processar uma operação!\n");
                System.out.println("– Registar um produto - 1");
                System.out.println("- Adicionar uma certa quantidade de um produto já existente - 2");
                System.out.println("- Dar saída de um produto - 3");
                System.out.println("- Eliminar um produto - 4");
                System.out.println("- Consultar produtos existentes - 5");
                System.out.println("- Consultar as vendas - 6");
                System.out.println("– Consultar as compras feitas a um fornecedor - 7");
                System.out.println("\n\n ----------------------------------------------------------\n");
                System.out.println("WARNINGS:\n");
                c.vStock();
                System.out.println("\n\n ----------------------------------------------------------\n");
                String cat = "";
                int id = 0;
                long pc, pv;
                int stk = 0;
                int stkMin = 0;

                switch (br.readLine()) {
                    case "1":
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("Inserir categoria: ");
                        cat = br.readLine();
                        System.out.println("Inserir ID: ");
                        id = Integer.parseInt(br.readLine());
                        System.out.println("Inserir Preço de Transacao: ");
                        pc = Long.parseLong(br.readLine());
                        System.out.println("Inserir Preço de Venda: ");
                        pv = Long.parseLong(br.readLine());
                        System.out.println("Inserir Stock: ");
                        stk = Integer.parseInt(br.readLine());
                        System.out.println("Inserir Stock Mínimo: ");
                        stkMin = Integer.parseInt(br.readLine());
                        System.out.println("\nA processar operação! \n \n");
                        c.registarProduto(cat, id, pc, pv, stk, stkMin);
                        System.out.println("\nA operação processada! \n \n");
                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    case "2":
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("Inserir categoria: ");
                        cat = br.readLine();
                        System.out.println("Inserir ID: ");
                        id = Integer.parseInt(br.readLine());
                        System.out.println("Inserir Stock: ");
                        stk = Integer.parseInt(br.readLine());
                        System.out.println("\nA processar operação! \n \n");
                        c.adicionarQuantidadeDeProduto(cat, id, stk);
                        System.out.println("\nA operação processada! \n \n");
                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    case "3":
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
                    case "4":
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("Inserir categoria: ");
                        cat = br.readLine();
                        System.out.println("Inserir ID: ");
                        id = Integer.parseInt(br.readLine());
                        System.out.println("\nA processar operação! \n \n");
                        c.eliminarProduto(cat, id);
                        System.out.println("\nA operação processada! \n \n");
                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    case "5":
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("Inserir categoria: ");
                        cat = br.readLine();
                        System.out.println("\nA consultar\n");
                        ArrayList<String> arr = c.consultarProdutosExistentes(cat);
                        for (int x = 0; x < arr.size(); x++) {
                            System.out.println(arr.get(x));
                        }

                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    case "6":
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("A consultar");
                        ArrayList<String> arr2 = c.consultarVendas();
                        for (int x = 0; x < arr2.size(); x++) {
                            System.out.println(arr2.get(x));
                        }
                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    case "7":
                        System.out.println("\n\n\n-------------------------------------------\n");
                        System.out.println("A consultar");
                        ArrayList<String> arr3 = c.consultarCompras();
                        for (int x = 0; x < arr3.size(); x++) {
                            System.out.println(arr3.get(x));
                        }
                        System.out.println("\n-------------------------------------------\n\n\n");
                        break;
                    default:
                        return;
                }


            }
        } catch (RemoteException re) {
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
        try {
            Vendedor vendedor = new Vendedor();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            Fornecedor fornecedor = new Fornecedor();
        } catch (RemoteException e) {
            e.printStackTrace();
        }



    }
}
