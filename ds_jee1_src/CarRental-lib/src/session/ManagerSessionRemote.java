/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Remote;

//TODO remote exception?
@Remote
public interface ManagerSessionRemote {
    
    public int getNumberOfReservationsByRenter(String clientName);
    
    public int getNumberOfReservationsForCarType(String carRentalName, String carType);
    
}
