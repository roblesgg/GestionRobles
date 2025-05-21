package model;

public class Venta {
    private int idVenta;
    private boolean conEncargo;
    private Integer idEncargo; // puede ser null

    public Venta(boolean conEncargo, Integer idEncargo) {
        this.conEncargo = conEncargo;
        this.idEncargo = idEncargo;
    }

    // Getters y setters
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public boolean isConEncargo() { return conEncargo; }
    public void setConEncargo(boolean conEncargo) { this.conEncargo = conEncargo; }
    public Integer getIdEncargo() { return idEncargo; }
    public void setIdEncargo(Integer idEncargo) { this.idEncargo = idEncargo; }
}
