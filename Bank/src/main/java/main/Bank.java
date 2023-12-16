package main;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import FxtopAPI.ConvertResult;
import FxtopAPI.FxtopServicesLocator;
import FxtopAPI.FxtopServicesPortType;

public class Bank {
	
	public double test() {
		return 4.4;
	}

	public double conversion(double sum, String currency1, String currency2) throws ServiceException, RemoteException {
		FxtopServicesPortType service = new FxtopServicesLocator().getFxtopServicesPort();
		
		ConvertResult resultat = service.convert(null, currency1, currency2, null , null , null);
	
		double exchangeRate = Double.parseDouble(resultat.getExchangeRate());
		return sum * exchangeRate;
	}
}
