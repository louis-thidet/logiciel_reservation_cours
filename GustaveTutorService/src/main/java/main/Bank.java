/**
 * Bank.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package main;

public interface Bank extends java.rmi.Remote {
    public double test() throws java.rmi.RemoteException;
    public double conversion(double sum, java.lang.String currency1, java.lang.String currency2) throws java.rmi.RemoteException;
}
