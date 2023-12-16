/**
 * GustaveTutorServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package main;

public class GustaveTutorServiceServiceLocator extends org.apache.axis.client.Service implements main.GustaveTutorServiceService {

    public GustaveTutorServiceServiceLocator() {
    }


    public GustaveTutorServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GustaveTutorServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GustaveTutorService
    private java.lang.String GustaveTutorService_address = "http://localhost:8080/GustaveTutorService/services/GustaveTutorService";

    public java.lang.String getGustaveTutorServiceAddress() {
        return GustaveTutorService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GustaveTutorServiceWSDDServiceName = "GustaveTutorService";

    public java.lang.String getGustaveTutorServiceWSDDServiceName() {
        return GustaveTutorServiceWSDDServiceName;
    }

    public void setGustaveTutorServiceWSDDServiceName(java.lang.String name) {
        GustaveTutorServiceWSDDServiceName = name;
    }

    public main.GustaveTutorService getGustaveTutorService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GustaveTutorService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGustaveTutorService(endpoint);
    }

    public main.GustaveTutorService getGustaveTutorService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            main.GustaveTutorServiceSoapBindingStub _stub = new main.GustaveTutorServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getGustaveTutorServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGustaveTutorServiceEndpointAddress(java.lang.String address) {
        GustaveTutorService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (main.GustaveTutorService.class.isAssignableFrom(serviceEndpointInterface)) {
                main.GustaveTutorServiceSoapBindingStub _stub = new main.GustaveTutorServiceSoapBindingStub(new java.net.URL(GustaveTutorService_address), this);
                _stub.setPortName(getGustaveTutorServiceWSDDServiceName());
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
        if ("GustaveTutorService".equals(inputPortName)) {
            return getGustaveTutorService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://main", "GustaveTutorServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://main", "GustaveTutorService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GustaveTutorService".equals(portName)) {
            setGustaveTutorServiceEndpointAddress(address);
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
