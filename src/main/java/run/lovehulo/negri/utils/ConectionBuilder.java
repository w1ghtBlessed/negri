/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package run.lovehulo.negri.utils;

import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author nikit
 */
public interface ConectionBuilder {
    Connection getConnection()throws SQLException;
    
}
