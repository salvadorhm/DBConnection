# DBConnection AcmeStore Demo
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acmestore;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sax.DBConnection;

/**
 *
 * @author sax
 */
public class Productos extends javax.swing.JFrame {

//    DBConnection conexion = new DBConnection(3306, "localhost", "acme_store", "root", "toor");
    DBConnection conexion;

    private int id_producto;
    private String producto;
    private int existencias;
    private String descripcion;
    private float precio_compra;
    private float precio_venta;

    private String sql = "";

    private DefaultTableModel table_productos;

    /**
     * Creates new form Productos
     */
    public Productos() {
        try {
            conexion = new DBConnection(3306, "localhost", "acme_store", "root", "toor");
            initComponents();
        } catch (DBConnection ex) {
            JOptionPane.showMessageDialog(this, "AcmeStore 102 " + ex.getMessage());
        }
    }

    
    private void jbtn_primeroActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            conexion.moveFirst();
            get_datos_bd();
            mostrar_datos_vista();
        } catch (DBConnection ex) {
            JOptionPane.showMessageDialog(this, "AcmeStore 107 " + ex.getMessage());
        }
    }

    private void jbtn_anteriorActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            conexion.movePrevious();
            get_datos_bd();
            mostrar_datos_vista();
        } catch (DBConnection ex) {
            JOptionPane.showMessageDialog(this, "AcmeStore 108 " + ex.getMessage());
        }
    }

    private void jbtn_siguienteActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            conexion.moveNext();
            get_datos_bd();
            mostrar_datos_vista();
        } catch (DBConnection ex) {
            JOptionPane.showMessageDialog(this, "AcmeStore 109 " + ex.getMessage());
        }
    }

    private void jbtn_utlimoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            conexion.moveLast();
            get_datos_bd();
            mostrar_datos_vista();
        } catch (DBConnection ex) {
            JOptionPane.showMessageDialog(this, "AcmeStore 110 " + ex.getMessage());
        }
    }

    private void jbtn_agregarActionPerformed(java.awt.event.ActionEvent evt) {
        if (jbtn_agregar.getText().equals("Agregar")) {
            limpiar_cajas();
            jbtn_agregar.setText("Guardar");
            habiltar_cajas(true);
            habilitar_botonoes(false);
            jbtn_agregar.setEnabled(true);
            jbtn_cancelar.setEnabled(true);
        } else {
            if (get_datos_vista()) {
                /*sql = "INSERT INTO productos ("
                        + "producto,"
                        + "existencias,"
                        + "descripcion,"
                        + "precio_compra,"
                        + "precio_venta"
                        + ") values ("
                        + "'" + getProducto() + "',"
                        + "" + getExistencias() + ","
                        + "'" + getDescripcion() + "',"
                        + "" + getPrecio_compra() + ","
                        + "" + getPrecio_venta() + ");";

                conexion.executeUpdate(sql);*/
                try {

                    sql = "Insert into productos(producto,existencias,descripcion,precio_compra,precio_venta) values(?,?,?,?,?)";
                    conexion.prepareStatement(sql);
                    conexion.setPreparedStatement(1, getProducto());
                    conexion.setPreparedStatement(2, getExistencias());
                    conexion.setPreparedStatement(3, getDescripcion());
                    conexion.setPreparedStatement(4, getPrecio_compra());
                    conexion.setPreparedStatement(5, getPrecio_venta());

                    conexion.executePreparedStatement();
                } catch (DBConnection err) {
                    JOptionPane.showMessageDialog(this, "AcmeStore 104 " + err.getMessage());
                }

                iniciar_formulario();
                jbtn_agregar.setText("Agregar");
                habiltar_cajas(false);
                habilitar_botonoes(true);
            }
        }
    } 

    private void jbtn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {
        iniciar_formulario();
        jbtn_agregar.setText("Agregar");
        jbtn_editar.setText("Editar");
        jbtn_buscar.setText("Buscar");
        habiltar_cajas(false);
        habilitar_botonoes(true);
    } 

    private void jbtn_eliminarActionPerformed(java.awt.event.ActionEvent evt) { 

        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Desea borrar el registro?",
                "Acme Store",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                null,
                null);

        if (opcion == 0) {
            try {
                //respuesta si
                setId_producto(Integer.parseInt(jtf_id_producto.getText()));
                sql = "DELETE FROM productos WHERE id_producto = ?;";
                conexion.prepareStatement(sql);
                conexion.setPreparedStatement(1, getId_producto());
                conexion.executePreparedStatement();

                iniciar_formulario();
            } catch (DBConnection ex) {
                JOptionPane.showMessageDialog(this, "AcmeStore 105 " + ex.getMessage());
            }
        }
    } 

    private void jbtn_editarActionPerformed(java.awt.event.ActionEvent evt) {
        if (jbtn_editar.getText().equals("Editar")) {
            jbtn_editar.setText("Guardar");
            habiltar_cajas(true);
            habilitar_botonoes(false);
            jbtn_editar.setEnabled(true);
            jbtn_cancelar.setEnabled(true);
        } else {
            try {
                get_datos_vista();
                setId_producto(Integer.parseInt(jtf_id_producto.getText()));

                /*sql = "UPDATE productos set "
                + "producto = '" + getProducto() + "',"
                + "existencias = " + getExistencias() + ","
                + "descripcion = '" + getDescripcion() + "',"
                + "precio_compra = " + getPrecio_compra() + ","
                + "precio_venta = " + getPrecio_venta() + " "
                + "Where id_producto = " + getId_producto() + ";";
                conexion.executeUpdate(sql);
                 */
                sql = "UPDATE productos set producto = ?,existencias = ?,descripcion=?,precio_compra=?,precio_venta=? where id_producto=?";
                conexion.prepareStatement(sql);
                conexion.setPreparedStatement(1, getProducto());
                conexion.setPreparedStatement(2, getExistencias());
                conexion.setPreparedStatement(3, getDescripcion());
                conexion.setPreparedStatement(4, getPrecio_compra());
                conexion.setPreparedStatement(5, getPrecio_venta());
                conexion.setPreparedStatement(6, getId_producto());
                conexion.executePreparedStatement();

                iniciar_formulario();
                jbtn_editar.setText("Editar");
                habiltar_cajas(false);
                habilitar_botonoes(true);
            } catch (DBConnection ex) {
                JOptionPane.showMessageDialog(this, "AcmeStore 106 " + ex.getMessage());
            }
        }
    }

    private void jbtn_buscarActionPerformed(java.awt.event.ActionEvent evt) {
        if (jbtn_buscar.getText().equals("Buscar")) {
            limpiar_cajas();
            jbtn_buscar.setText("Ejecutar");
            habiltar_cajas(true);
            habilitar_botonoes(false);
            jbtn_buscar.setEnabled(true);
        } else {
            try {
                sql = "Select * from productos where producto like '%" + jtf_producto.getText() + "%';";
                System.out.println(sql);
                conexion.updateTable(sql);
                table_productos = conexion.getTableModel();
                jt_productos.setModel(table_productos);
                conexion.moveFirst();
                get_datos_bd();
                mostrar_datos_vista();
                jbtn_buscar.setText("Buscar");
                habiltar_cajas(false);
                habilitar_botonoes(true);
            } catch (DBConnection ex) {
                JOptionPane.showMessageDialog(this, "AcmeStore 111 " + ex.getMessage());
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Productos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Productos objeto = new Productos();
                objeto.setVisible(true);
                objeto.iniciar_formulario();
                objeto.setTitle("ACME STORE V0.1");
                objeto.setLocationRelativeTo(null);
            }
        });
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descipcion) {
        this.descripcion = descipcion;
    }

    public float getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(float precio_compra) {
        this.precio_compra = precio_compra;
    }

    public float getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(float precio_venta) {
        this.precio_venta = precio_venta;
    }

    public void iniciar_formulario() {
        try {
            conexion.version();
            sql = "SELECT * FROM productos;";
            conexion.updateTable(sql);
            table_productos = conexion.getTableModel();
            jt_productos.setModel(table_productos);
            conexion.moveFirst();
            get_datos_bd();
            mostrar_datos_vista();
        } catch (IllegalArgumentException | NullPointerException | DBConnection err) {
            JOptionPane.showMessageDialog(this, "AcmeStore 103 " + err.getMessage());
            System.exit(101);
        }

    }

    public void get_datos_bd() {
        try {
            setId_producto(conexion.getInteger("id_producto"));
            setProducto(conexion.getString("producto"));
            setExistencias(conexion.getInteger("existencias"));
            setDescripcion(conexion.getString("descripcion"));
            setPrecio_compra(conexion.getFloat("precio_compra"));
            setPrecio_venta(conexion.getFloat("precio_venta"));
        } catch (DBConnection ex) {
            JOptionPane.showMessageDialog(this, "AcmeStore 113 " + ex.getMessage());
            iniciar_formulario();
        }
    }

    public void mostrar_datos_vista() {
        jtf_id_producto.setText(String.valueOf(getId_producto()));
        jtf_producto.setText(String.valueOf(getProducto()));
        jtf_existencias.setText(String.valueOf(getExistencias()));
        jta_descripcion.setText(String.valueOf(getDescripcion()));
        jtf_precio_compra.setText(String.valueOf(getPrecio_compra()));
        jtf_precio_venta.setText(String.valueOf(getPrecio_venta()));
    }

    public void limpiar_cajas() {
        jtf_id_producto.setText("");
        jtf_producto.setText("");
        jtf_existencias.setText("");
        jta_descripcion.setText("");
        jtf_precio_compra.setText("");
        jtf_precio_venta.setText("");
    }

    public boolean get_datos_vista() {
        boolean result = false;
        try {
            setProducto(jtf_producto.getText());
            setExistencias(Integer.parseInt(jtf_existencias.getText()));
            setDescripcion(jta_descripcion.getText());
            setPrecio_compra(Float.parseFloat(jtf_precio_compra.getText()));
            setPrecio_venta(Float.parseFloat(jtf_precio_venta.getText()));
            result = true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "AcmeStore 101 " + e.getMessage());
        }
        return result;
    }

    public void habiltar_cajas(boolean estado) {
        jtf_producto.setEditable(estado);
        jtf_existencias.setEditable(estado);
        jta_descripcion.setEditable(estado);
        jtf_precio_compra.setEditable(estado);
        jtf_precio_venta.setEditable(estado);
    }

    public void habilitar_botonoes(boolean estado) {
        jbtn_primero.setEnabled(estado);
        jbtn_anterior.setEnabled(estado);
        jbtn_siguiente.setEnabled(estado);
        jbtn_utlimo.setEnabled(estado);
        jbtn_cancelar.setEnabled(!estado);
        jbtn_agregar.setEnabled(estado);
        jbtn_editar.setEnabled(estado);
        jbtn_eliminar.setEnabled(estado);
        jbtn_buscar.setEnabled(estado);
    }
}

