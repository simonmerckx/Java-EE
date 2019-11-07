package session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateful;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {

    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }
    
    
    @Override
    public Set<CarType> getAvailableCarTypes(Date start,Date end) {
        Collection<CarRentalCompany> companies = RentalStore.getRentals().values();
    	Set<CarType> carTypes = new HashSet<CarType>();
    	for(CarRentalCompany company: companies) {
            carTypes.addAll(company.getAvailableCarTypes(start, end));	 
        }
    	return carTypes;
    	
    }
    
    @Override
    public List<Quote> getCurrentQuotes()  {
	return pendingQuotes;
    }
    
    
    @Override
    public Quote createQuote(String clientName, Date start, Date end, String carType, String region) throws Exception {
		ReservationConstraints constraints = new ReservationConstraints(start, end, carType, region);
		Quote acceptedQuote = null;
		for(String currName: getAllRentalCompanies()){
			try{
                            CarRentalCompany currCompany =  RentalStore.getRental(currName);
			    acceptedQuote = currCompany.createQuote(constraints, clientName);
			    this.pendingQuotes.add(acceptedQuote);
			    break;
			}
			catch(ReservationException e){
                                
				continue;
			}	
		}
		if (acceptedQuote == null) {
			throw new ReservationException("No company able to satisfy");
		}
                
		return acceptedQuote;
    }
    
    
    @Override
    public List<Reservation> confirmQuotes() throws ReservationException {
        List<Reservation> reservations = new ArrayList<Reservation>();
	//List<Reservation> confirmedQuotes = new HashMap<Reservation, CarRentalCompany>();
	for (Quote currQuote: this.pendingQuotes) {
            try{
	        Reservation res = RentalStore.getRental(currQuote.getRentalCompany()).confirmQuote(currQuote);
		reservations.add(res);
	    } catch (ReservationException e){
                for (Reservation currRes: reservations) {
                    RentalStore.getRental(currRes.getRentalCompany()).cancelReservation(currRes);
                }
                throw new ReservationException("Not all quotes could be confirmed");    
            }
        }
        
    	return reservations;
        
    }
    
    private List<Quote> pendingQuotes = new ArrayList<Quote>();
   
}
