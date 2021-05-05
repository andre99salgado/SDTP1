import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

public class RegistadoraImpl extends java.rmi.server.UnicastRemoteObject implements Registadora {

    public static Vector<Produto> camas = new Vector<Produto>();
    public static Vector<Produto> sofas = new Vector<Produto>();
    public static Vector<Produto> mesas = new Vector<Produto>();
    public static Vector<Transacao> compras = new Vector<Transacao>();
    public static Vector<Transacao> vendas = new Vector<Transacao>();
    //private static Vector<Object> objectsList = new Vector<Object>();

    private static RegistadoraC client;
    private static ArrayList<RegistadoraC> clientes;
    FileInputStream f;
    ObjectInputStream memory;

    public RegistadoraImpl() throws java.rmi.RemoteException {
        super();
        clientes = new ArrayList<RegistadoraC> ();


        try {
            f = new FileInputStream(new File("memory.txt"));
            memory = new ObjectInputStream(f);
            camas = (Vector<Produto>) memory.readObject();
            sofas = (Vector<Produto>) memory.readObject();
            mesas = (Vector<Produto>) memory.readObject();
            compras = (Vector<Transacao>) memory.readObject();
            vendas = (Vector<Transacao>) memory.readObject();
        }catch (EOFException e){
            System.out.println("\n\nA base de dados ainda está vazia \n\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Método Remoto
    public void guardar() throws java.rmi.RemoteException{
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(new File("memory.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObjectOutputStream ow = null;
        try {
            ow = new ObjectOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ow.writeObject(camas);
            ow.flush();
            ow.writeObject(sofas);
            ow.flush();
            ow.writeObject(mesas);
            ow.flush();
            ow.writeObject(compras);
            ow.flush();
            ow.writeObject(vendas);
            ow.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //-------------------------------------------------------------------------------
    //REGISTAR CLIENTES

    //Método remoto
    public synchronized void subscribe(String name, RegistadoraC c) throws java.rmi.RemoteException{
        System.out.println("Subscribing " +name);
        clientes.add(c);
    }

    //Método Remoto
    public synchronized void vStock() throws java.rmi.RemoteException{
        verificarStock(camas);
        verificarStock(sofas);
        verificarStock(mesas);
    }

    //-------------------------------------------------------------------------------

    @Override
    public synchronized void registarProduto(String categoria, int ID, long precoCompra, long precoVenda, int stock , int stockMinimo) throws RemoteException {

        Produto produto = new Produto(categoria,ID,precoCompra,precoVenda,stock, stockMinimo);
        Transacao compra = new Transacao(categoria,ID,precoCompra,precoVenda,stock);

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
    public synchronized  void adicionarQuantidadeDeProduto(String categoria, int ID, int stock) throws RemoteException {
        Produto produto = new Produto(categoria,ID,stock);
        Transacao compra = new Transacao(categoria,ID,stock);

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
    public synchronized  void darSaidaAoProduto(String categoria, int ID, int stock) throws RemoteException {
        Produto produto = new Produto(categoria,ID,stock);
        Transacao compra = new Transacao(categoria,ID,stock);

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
    public synchronized  void eliminarProduto(String categoria, int ID) throws RemoteException {
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
    public synchronized ArrayList<String> consultarProdutosExistentes(String categoria) throws RemoteException {
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
    public  synchronized ArrayList<String> consultarVendas() throws RemoteException {
        return percorrerListaTransacoes( vendas);
    }

    @Override
    public  synchronized ArrayList<String> consultarCompras() throws RemoteException {
        return percorrerListaTransacoes( compras);
    }

    @Override
    public synchronized String maisVendido() throws RemoteException {
        int count = 0 , tempCount , posicao = 0;
        Transacao  venda = new Transacao("ct",0,0);
        //percorrer lista das vendas registadas
        for (int i = 0; i < vendas.size(); i++)
        {
            venda = vendas.get(i);
            tempCount = 0;
            for(int j = 0 ; j < vendas.size();j++){
                //verificar se o produto da venda i == ao produto da venda j
                System.out.println(venda.getQuantidade());
                if(venda.getQuantidade()==vendas.get(j).getQuantidade())
                {
                    tempCount++; // conta
                }
            }

            if (tempCount > count){
                posicao = i;
                count = tempCount;
            }

        }
        return vendas.get(posicao).toString();
    }

    @Override
    public synchronized String menosVendido() throws RemoteException {
        int count = 0 , tempCount , posicao = 0;
        Transacao  venda = new Transacao("ct",0,0);
        for (int i = 0; i < vendas.size(); i++)
        {
            venda = vendas.get(i);
            tempCount = 0;
            for(int j = 0 ; j < vendas.size();j++){
                if(venda.getQuantidade()==vendas.get(j).getQuantidade())
                {
                    tempCount++;
                }
            }

            if (tempCount < count){
                posicao = i;
                count = tempCount;
            }

        }
        return vendas.get(posicao).toString();
    }

    @Override
    public synchronized String maisComprado() throws RemoteException {
        int count = 0 , tempCount , posicao = 0;
        Transacao  venda = new Transacao("ct",0,0);
        //percorrer lista das vendas registadas
        for (int i = 0; i < compras.size(); i++)
        {
            venda = compras.get(i);
            tempCount = 0;
            for(int j = 0 ; j < compras.size();j++){
                //verificar se o produto da venda i == ao produto da venda j
                if(venda.getQuantidade()==compras.get(j).getQuantidade())
                {
                    tempCount++; // conta
                }
            }

            if (tempCount > count){
                posicao = i;
                count = tempCount;
            }

        }
        return compras.get(posicao).toString();
    }

    @Override
    public synchronized String menosComprado() throws RemoteException {
        int count = 0 , tempCount , posicao = 0;
        Transacao  venda = new Transacao("ct",0,0);
        for (int i = 0; i < compras.size(); i++)
        {
            venda = compras.get(i);
            tempCount = 0;
            for(int j = 0 ; j < compras.size();j++){
                if(venda.getQuantidade()==compras.get(j).getQuantidade())
                {
                    tempCount++;
                }
            }

            if (tempCount < count){
                posicao = i;
                count = tempCount;
            }

        }
        return compras.get(posicao).toString();
    }

    //FIM---------------------------INTERFACE-------------------------FIM


    private void addProduto(Produto produto, Transacao compra, Vector<Produto> vetor){

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

    private void sellProduto(Produto produto, Transacao compra, Vector<Produto> vetor){

        for(int x = 0; x < vetor.size(); x++){
            if(produto.hashCode() == (vetor.get(x)).hashCode())
                if(produto.equals(vetor.get(x)) && vetor.get(x).getStock() > 0) {
                    vetor.get(x).setStock((vetor.get(x).getStock()) - produto.getStock());
                    System.out.println("Produto existe: Stock subtraído!");
                    addTransacao(compra, vendas);
                    return;
                }
        }
        System.out.println("Produto não existe!");

    }

    private void addTransacao(Transacao transacao, Vector<Transacao> tipo) {

        /*for(int x = 0; x < tipo.size(); x++){
            if(transacao.hashCode() == (tipo.get(x)).hashCode())
                if(transacao.equals(tipo.get(x))){
                    tipo.get(x).setQuantidade((tipo.get(x).getQuantidade()) + transacao.getQuantidade());
                    System.out.println("Artigo já transacionado: Item adicionado à lista!");
                    return;
                }
        } */

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

    private ArrayList<String> percorrerListaTransacoes( Vector<Transacao> vetor){
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
