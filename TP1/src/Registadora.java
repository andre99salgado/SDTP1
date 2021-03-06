import java.util.ArrayList;

public interface Registadora extends java.rmi.Remote{


    //----------------------FORNECEDOR---------------------------------------
    public void registarProduto(String categoria, int ID, long precoCompra, long precoVenda, int stock, int stockMinimo) throws java.rmi.RemoteException;
    public void adicionarQuantidadeDeProduto(String categoria, int ID, int stock) throws java.rmi.RemoteException;
    public ArrayList<String> consultarCompras() throws java.rmi.RemoteException;
    public void eliminarProduto(String categoria, int ID) throws java.rmi.RemoteException;
    public String maisComprado() throws java.rmi.RemoteException;
    public String menosComprado() throws java.rmi.RemoteException;
    //------------------------------------------------------------------------

    //----------------------Vendedor---------------------------------------
    public ArrayList<String> consultarProdutosExistentes(String categoria) throws java.rmi.RemoteException;
    public ArrayList<String> consultarVendas() throws java.rmi.RemoteException;
    public void darSaidaAoProduto(String categoria, int ID, int stock) throws java.rmi.RemoteException;
    public String maisVendido() throws java.rmi.RemoteException;
    public String menosVendido() throws java.rmi.RemoteException;
    //------------------------------------------------------------------------

    public void subscribe(String name, RegistadoraC c) throws java.rmi.RemoteException;
    public void vStock() throws java.rmi.RemoteException;


    public void guardar() throws java.rmi.RemoteException;
}
