import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Compra implements Serializable {
    private String categoria;
    private int ID;
    private long precoCompra, precoVenda;
    private int quantidade;
    private Date data;

    public Compra(String categoria, int ID, long precoCompra, long precoVenda, int quantidade) {
        this.categoria = categoria;
        this.ID = ID;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.quantidade = quantidade;
        data = new Date();
    }

    public Compra(String categoria, int ID, int quantidade) {
        this.categoria = categoria;
        this.ID = ID;
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(long precoCompra) {
        this.precoCompra = precoCompra;
    }

    public long getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(long precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compra)) return false;
        Compra compra = (Compra) o;
        return getID() == compra.getID() && Objects.equals(getCategoria(), compra.getCategoria());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCategoria(), getID());
    }

    @Override
    public String toString() {
        return "Compra{" +
                "categoria='" + categoria + '\'' +
                ", ID=" + ID +
                ", precoCompra=" + precoCompra +
                ", precoVenda=" + precoVenda +
                ", quantidade=" + quantidade +
                '}';
    }
}
