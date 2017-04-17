/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.tools.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.Context;
import javax.sql.DataSource;
import org.apache.tomcat.jdbc.pool.ConnectionPool;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.XADataSource;

/**
 *
 * @author andresfelipegarciaduran
 */
public class EncryptedDataSourceFactory extends DataSourceFactory {

    private Encryptor encryptor = null;

    public EncryptedDataSourceFactory() {
        try {
            encryptor = new Encryptor(); // If you've used your own secret key, pass it in...
        } catch (InvalidKeyException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchPaddingException ex) {
            throw new RuntimeException(ex);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public DataSource createDataSource(Properties properties, Context context, boolean XA) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, SQLException {

        try {
            PoolConfiguration poolProperties = EncryptedDataSourceFactory.parsePoolProperties(properties);
            poolProperties.setPassword(encryptor.decrypt(poolProperties.getPassword()));
            if (poolProperties.getDataSourceJNDI() != null && poolProperties.getDataSource() == null) {
                performJNDILookup(context, poolProperties);
            }
            org.apache.tomcat.jdbc.pool.DataSource dataSource = XA ? new XADataSource(poolProperties)
                    : new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
            ConnectionPool createPool = dataSource.createPool();

            return dataSource;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(EncryptedDataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(EncryptedDataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (BadPaddingException ex) {
            Logger.getLogger(EncryptedDataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptedDataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncryptedDataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (SQLException ex) {
            Logger.getLogger(EncryptedDataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

}
