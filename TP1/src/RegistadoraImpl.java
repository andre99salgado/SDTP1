import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

public class RegistadoraImpl extends java.rmi.server.UnicastRemoteObject implements Registadora {

    public static Vector<Produto> camas = new Vector<Produto>();
    public static Vector<Produto> sofas = new Vector<Produto>();
    public static Vector<Produto> mesas = new Vector<Produto>();
    public static Vector<Compra> compras = new Vector<Compra>();
    public static Vector<Compra> vendas = new Vector<Compra>();

    private static RegistadoraC client;
    private static ArrayList<RegistadoraC> clientes;

    public RegistadoraImpl() throws java.rmi.RemoteException {
        super();
        clientes = new ArrayList<RegistadoraC> ();
    }

    //-------------------------------------------------------------------------------
    //REGISTAR CLIENTES

    //Método remoto
    public void subscribe(String name, RegistadoraC c) throws java.rmi.RemoteException{
        System.out.println("Subscribing " +name);
        clientes.add(c);
    }

    //Método Remoto
    public void vStock() throws java.rmi.RemoteException{
        verificarStock(camas);
        verificarStock(sofas);
        verificarStock(mesas);
    }

    //-------------------------------------------------------------------------------

    @Override
    public void registarProduto(String categoria, int ID, long precoCompra, long precoVenda, int stock , int stockMinimo) throws RemoteException {

        Produto produto = new Produto(categoria,ID,precoCompra,precoVenda,stock, stockMinimo);
        Compra compra = new Compra(categoria,ID,precoCompra,precoVenda,stock);

        if(categoria.equals("mesa")){
            addProduto(produto,compra,mesas);
        }else if(categoria.equals("sofa")){
            addProduto(produto,compra,sofas);
        }else if(categoria.equals("cama")){
            addProduto(produto,compra,camas);
        }else{
            System.out.println("Impossível de carregar produto: Categoria não existente!");
        }

    }

    //------------------------------INTERFACE-------------------------

    @Override
    public void adicionarQuantidadeDeProduto(String categoria, int ID, int stock) throws RemoteException {
        Produto produto = new Produto(categoria,ID,stock);
        Compra compra = new Compra(categoria,ID,stock);

        if(categoria.equals("mesa")){
            addProduto(produto,compra,mesas);
        }else if(categoria.equals("sofa")){
            addProduto(produto,compra,sofas);
        }else if(categoria.equals("cama")){
            addProduto(produto,compra,camas);
        }else{
            System.out.println("Impossível de carregar produto: Categoria não existente!");
        }
    }

    @Override
    public void darSaidaAoProduto(String categoria, int ID, int stock) throws RemoteException {
        Produto produto = new Produto(categoria,ID,stock);
        Compra compra = new Compra(categoria,ID,stock);

        if(categoria.equals("mesa")){
            sellProduto(produto,compra,mesas);
        }else if(categoria.equals("sofa")){
            sellProduto(produto,compra,sofas);
        }else if(categoria.equals("cama")){
            sellProduto(produto,compra,camas);
        }else{
            System.out.println("Impossível de carregar produto: Categoria não existente!");
        }
    }

    @Override
    public void eliminarProduto(String categoria, int ID) throws RemoteException {
        Produto produto = new Produto(categoria,ID,0);

        if(categoria.equals("mesa")){
            removeProduto(produto,mesas);
        }else if(categoria.equals("sofa")){
            removeProduto(produto,sofas);
        }else if(categoria.equals("cama")){
            removeProduto(produto,camas);
        }else{
            System.out.println("Impossível de carregar produto: Categoria não existente!");
        }
    }

    @Override
    public ArrayList<String> consultarProdutosExistentes(String categoria) throws RemoteException {
        if(categoria.equals("mesa")){
            return percorrerListaProdutos(mesas);
        }else if(categoria.equals("sofa")){
            return percorrerListaProdutos(sofas);
        }else if(categoria.equals("cama")){
            return percorrerListaProdutos(camas);
        }else{
            ArrayList<String> produtos1 = new ArrayList<String>();
            produtos1.add("Impossível de carregar produto: Categoria não existente!");
            return produtos1;
        }

    }

    @Override
    public ArrayList<String> consultarVendas() throws RemoteException {
        return percorrerListaTransacoes( vendas);
    }

    @Override
    public ArrayList<String> consultarCompras() throws RemoteException {
        return percorrerListaTransacoes( compras);
    }

    //FIM---------------------------INTERFACE-------------------------FIM


    private void addProduto(Produto produto,Compra compra, Vector<Produto> vetor){

        for(int x = 0; x < vetor.size(); x++){
                if(produto.hashCode() == (vetor.get(x)).hashCode())
                    if(produto.equals(vetor.get(x))) {
                        vetor.get(x).setStock((vetor.get(x).getStock()) + produto.getStock());
                        System.out.println("Produto já existe: Stock adicionado!");
                        addTransacao(compra, compras);
                        return;
                    }
        }

        vetor.add(produto);

        addTransacao(compra, compras);

    }

    private void sellProduto(Produto produto,Compra compra, Vector<Produto> vetor){

        for(int x = 0; x < vetor.size(); x++){
            if(produto.hashCode() == (vetor.get(x)).hashCode())
                if(produto.equals(vetor.get(x))) {
                    vetor.get(x).setStock((vetor.get(x).getStock()) - produto.getStock());
                    System.out.println("Produto existe: Stock subtraído!");
                    addTransacao(compra, vendas);
                    return;
                }
        }
        System.out.println("Produto não existe!");

    }

    private void addTransacao(Compra transacao, Vector<Compra> tipo) {

        for(int x = 0; x < tipo.size(); x++){
            if(transacao.hashCode() == (tipo.get(x)).hashCode())
                if(transacao.equals(tipo.get(x))){
                    tipo.get(x).setQuantidade((tipo.get(x).getQuantidade()) + transacao.getQuantidade());
                    System.out.println("Artigo já transacionado: Item adicionado à lista!");
                    return;
                }
        }

        tipo.add(transacao);
    }

    private void removeProduto(Produto produto, Vector<Produto> vetor){

        for(int x = 0; x < vetor.size(); x++){
            if(produto.hashCode() == (vetor.get(x)).hashCode())
                if(produto.equals(vetor.get(x))) {
                    vetor.remove(x);

                    System.out.println("Produto existe: Eliminado de stock");
                    return;
                }
        }
    }

    private ArrayList<String> percorrerListaProdutos( Vector<Produto> vetor){
        ArrayList<String> produtos1 = new ArrayList<String>();
        for(int x = 0; x < vetor.size(); x++){
            produtos1.add(vetor.get(x).toString());
        }

        return produtos1;
    }

    private ArrayList<String> percorrerListaTransacoes( Vector<Compra> vetor){
        ArrayList<String> produtos1 = new ArrayList<String>();
        for(int x = 0; x < vetor.size(); x++){
            produtos1.add(vetor.get(x).toString());
        }

        return produtos1;
    }


    private void verificarStock(Vector<Produto> vetor){
        for(int x = 0; x < vetor.size(); x++){
            if(vetor.get(x).getStock() < vetor.get(x).getStockMinimo()){
                for(int i=0; i<clientes.size();i++){
                    try {
                        clientes.get(i).printOnClient("Repor Stock! Produto: " + vetor.get(x).getID() + " - " + vetor.get(x).getCategoria() + " - " +
                                " pelo menos --> " + (vetor.get(x).getStockMinimo() - vetor.get(x).getStock()) +" unidades ");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
