import java.io.Serializable;
import java.util.Objects;

public class Produto implements Serializable {
    private String categoria;
    private int ID;
    private long precoCompra, precoVenda;
    private int stock;
    private int stockMinimo;

    public Produto(String categoria, int ID, long precoCompra, long precoVenda, int stock, int stockMinimo)  {
        this.categoria = categoria;
        this.ID = ID;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Produto(String categoria, int ID, int stock)  {
        this.categoria = categoria;
        this.ID = ID;
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getID() {
        return ID;
    }

    public long getPrecoCompra() {
        return precoCompra;
    }

    public long getPrecoVenda() {
        return precoVenda;
    }

    public int getStock() {
        return stock;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPrecoCompra(long precoCompra) {
        this.precoCompra = precoCompra;
    }

    public void setPrecoVenda(long precoVenda) {
        this.precoVenda = precoVenda;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto produto = (Produto) o;
        return getID() == produto.getID() && getPrecoCompra() == produto.getPrecoCompra() && getPrecoVenda() == produto.getPrecoVenda() && getStock() == produto.getStock() && getStockMinimo() == produto.getStockMinimo() && Objects.equals(getCategoria(), produto.getCategoria());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCategoria(), getID(), getPrecoCompra(), getPrecoVenda(), getStock(), getStockMinimo());
    }

    @Override
    public String toString() {
        return "Produto{" +
                "categoria='" + categoria + '\'' +
                ", ID=" + ID +
                ", precoCompra=" + precoCompra +
                ", precoVenda=" + precoVenda +
                ", stock=" + stock +
                ", stockMinimo=" + stockMinimo +
                '}';
    }

}
