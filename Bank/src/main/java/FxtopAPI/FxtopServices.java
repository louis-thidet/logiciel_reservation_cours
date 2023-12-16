/**
 * FxtopServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package FxtopAPI;

public interface FxtopServices extends javax.xml.rpc.Service {

/**
 * This document describes the Fxtop services, for more information,
 * please contact us at webmaster@fxtop.com see more on https://fxtop.com/en/developpers.php#ws
 */
    public java.lang.String getFxtopServicesPortAddress();

    public FxtopAPI.FxtopServicesPortType getFxtopServicesPort() throws javax.xml.rpc.ServiceException;

    public FxtopAPI.FxtopServicesPortType getFxtopServicesPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
