/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import mvc.model.Clientes;

import mvc.vista.FiltroCliente;

/**
 *
 * @author SERVIDOR
 */
public class Controller_Nuevo_Cliente {
    private static Connection con;
    private  static List client;
    
   public static int ValidarID(String identificacion){
       int id = 0;
       try{
            if(identificacion.equals("")){
                identificacion = "0";
            }
            id = Integer.parseInt(identificacion);
        }catch(NumberFormatException e){
           JOptionPane.showMessageDialog(null, "Error, El Identificacion Ingresado No es Correcto" ,"Error", JOptionPane.ERROR_MESSAGE);
            id = 0;
            
        }
        return id;
   }
   
   public static int ValidarTelefono(String telefono){
       int tel = 0;
       try{
            if(telefono.equals("")){
                telefono = "0";
            }
            tel = Integer.parseInt(telefono);
        }catch(NumberFormatException e){
           JOptionPane.showMessageDialog(null, "Error, El Dato Ingresado No es un Numero" ,"Error", JOptionPane.ERROR_MESSAGE);
            tel = 0;
            
        }
        return tel;
   }
   

   
       public static Connection getCon() {
        return con;
    }

    /**
     * @param aCon the con to set
     */
    public static void setCon(Connection aCon) {
        con = aCon;
    }
   
   public static void RegistrarNuevoCliente(Clientes cliente) throws Exception{
       ManejadorBaseDatos mbd=ManejadorBaseDatos.getInstancia();
        mbd.conectar();
        setCon((Connection) mbd.getConexion());
        if (getCon() == null ) {
            JOptionPane.showMessageDialog(null, " No hay Conexion ");
        }
        PreparedStatement pst = null;
        try {
              pst = (PreparedStatement) getCon().prepareStatement("Insert Into clientes values(?,?,?,?,?,?)");
              pst.setInt(1,cliente.getCedula());
              pst.setString(2,cliente.getNombres());
              pst.setString(3,cliente.getApellidos());
              pst.setString(4,cliente.getDireccion());
              pst.setString(5,cliente.getTelefono());
              pst.setString(6,cliente.getTipo_documento());
              pst.executeUpdate();
        }finally {
            if (pst != null) {
                 pst.close();
            }
        }

       }
   
    public static Clientes Buscar(String identificacion) throws SQLException{
    if(identificacion == null) {
       JOptionPane.showMessageDialog(null,"No Existe ese Pedido");
     }
     ResultSet rs = null;
     PreparedStatement pst = null;
     Clientes cliente = null;
     try {
       ManejadorBaseDatos mbd=ManejadorBaseDatos.getInstancia();
       mbd.conectar();
       setCon((Connection) mbd.getConexion());
       pst = (PreparedStatement) getCon().prepareStatement("select * from clientes where identificacion = ?");
       pst.setString(1, identificacion.trim());
       rs = pst.executeQuery();
       while(rs.next()) {
         cliente= Clientes.load(rs);
       }
     }finally {
         if (rs != null) {
          rs.close();
         }
         if (pst != null) {
           pst.close();
          }
       return cliente;
     }
 }
    public static void listarClientes() throws SQLException, Exception {
     ResultSet rs = null;
     PreparedStatement pst = null;
     setClient(new LinkedList());
     try {
         ManejadorBaseDatos mbd=ManejadorBaseDatos.getInstancia();
         mbd.conectar();
         setCon((Connection) mbd.getConexion());
         pst = (PreparedStatement) getCon().prepareStatement("select * from clientes");
         rs = pst.executeQuery();
         while(rs.next()) {
           getClient().add(Clientes.load(rs));
         }
     } finally {
        if (rs != null) {
           rs.close();
        }
        if (pst != null) {
          pst.close();
        }
    }
 }
   
public static Clientes enlistarClientes() throws Exception{
  Clientes clie = null;  
  
  FiltroCliente.dtm.setNumRows(0);

           listarClientes();
            Iterator it = getClient().iterator();
            while(it.hasNext()){
                Clientes clies = (Clientes) it.next();
                
                   clie = clies;
                
                String[] fila = {clies.getTipo_documento(), clies.getCedula()+"", clies.getNombres(),clies.getApellidos(),clies.getTelefono()};
               FiltroCliente.dtm.addRow(fila);
            }
        return clie;
        
}
    /**
     * @return the client
     */
    public static List getClient() {
        return client;
    }

    /**
     * @param xclient
     */
    public static void setClient(List xclient) {
        client = xclient;
    }
    
    }
