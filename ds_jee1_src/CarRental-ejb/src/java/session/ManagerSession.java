/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import rental.CarRentalCompany;
import rental.RentalStore;
import rental.Reservation;


//TODO Roles aanmaken voor manager
@Stateless
@RolesAllowed("Manager")
public class ManagerSession implements ManagerSessionRemote {
    
    // TODO moet dit in RentalAgency
    @Override
    public int getNumberOfReservationsByRenter(String clientName) {
        Collection<CarRentalCompany> companies = RentalStore.getRentals().values();
        Set<Reservation> reservations = new HashSet<Reservation>();
        for(CarRentalCompany company: companies) {
	    reservations.addAll(company.getReservationsBy(clientName));
	}
	return reservations.size();
    }
    
    
    private List<Reservation> getAllReservations()  {
	Collection<CarRentalCompany> companies = RentalStore.getRentals().values();
	List<Reservation> reservations = new ArrayList<Reservation>();
	for(CarRentalCompany company: companies) {
		reservations.addAll(company.getReservations());
	}
	return reservations;
	}

    @Override
    public int getNumberOfReservationsForCarType(String carRentalName, String carType) {
	int nbOfReservationsForCarType = 0;
	for(Reservation res: getAllReservations()) {
	    if(res.getCarType().equals(carType) && res.getRentalCompany().equals(carRentalName)) {
	        nbOfReservationsForCarType++;
	    }
	}
	return nbOfReservationsForCarType;
    }
}
