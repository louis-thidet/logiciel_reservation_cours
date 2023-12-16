/**
 * FxtopServicesPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package FxtopAPI;

public interface FxtopServicesPortType extends java.rmi.Remote {

    /**
     * Returns amount (OriginalAmount) converted from source currency
     * (C1 is a 3 letters Iso Code) to destination currency (C2 is a 3 letters
     * Iso Code) at a specific date (Date format DD/MM/YYYY) or today if
     * Date parameter is left blank.  If User and Password are not provided,
     * conversion will be performed with a date in 1999.
     */
    public FxtopAPI.ConvertResult convert(java.lang.String originalAmount, java.lang.String c1, java.lang.String c2, java.lang.String date, java.lang.String user, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Returns description of a currency (Isocode defined by a 3 letter)
     * in language Lang (supported languages : EN, FR, DE,ES, IT, PT, DK,
     * SE, FI, NO, NL)
     */
    public FxtopAPI.CurrencyDescription descCurrency(java.lang.String lang, java.lang.String isocode) throws java.rmi.RemoteException;

    /**
     * Returns a string with list of ISO code of supported currencies
     * (3 letters Iso codes like USD) separated by a slash, User and Password
     * is optional and is not used yet
     */
    public java.lang.String listCurrencies(java.lang.String user, java.lang.String password) throws java.rmi.RemoteException;
}
