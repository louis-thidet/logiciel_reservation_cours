/**
 * FxtopServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package FxtopAPI;

public class FxtopServicesLocator extends org.apache.axis.client.Service implements FxtopAPI.FxtopServices {

/**
 * This document describes the Fxtop services, for more information,
 * please contact us at webmaster@fxtop.com see more on https://fxtop.com/en/developpers.php#ws
 */

    public FxtopServicesLocator() {
    }


    public FxtopServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FxtopServicesLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FxtopServicesPort
    private java.lang.String FxtopServicesPort_address = "https://fxtop.com/dev/FxtopServices.php";

    public java.lang.String getFxtopServicesPortAddress() {
        return FxtopServicesPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FxtopServicesPortWSDDServiceName = "FxtopServicesPort";

    public java.lang.String getFxtopServicesPortWSDDServiceName() {
        return FxtopServicesPortWSDDServiceName;
    }

    public void setFxtopServicesPortWSDDServiceName(java.lang.String name) {
        FxtopServicesPortWSDDServiceName = name;
    }

    public FxtopAPI.FxtopServicesPortType getFxtopServicesPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FxtopServicesPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFxtopServicesPort(endpoint);
    }

    public FxtopAPI.FxtopServicesPortType getFxtopServicesPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            FxtopAPI.FxtopServicesBindingStub _stub = new FxtopAPI.FxtopServicesBindingStub(portAddress, this);
            _stub.setPortName(getFxtopServicesPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFxtopServicesPortEndpointAddress(java.lang.String address) {
        FxtopServicesPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (FxtopAPI.FxtopServicesPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                FxtopAPI.FxtopServicesBindingStub _stub = new FxtopAPI.FxtopServicesBindingStub(new java.net.URL(FxtopServicesPort_address), this);
                _stub.setPortName(getFxtopServicesPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("FxtopServicesPort".equals(inputPortName)) {
            return getFxtopServicesPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:FxtopAPI", "FxtopServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:FxtopAPI", "FxtopServicesPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FxtopServicesPort".equals(portName)) {
            setFxtopServicesPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
