package session;

import java.util.ArrayList;
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
    public Set<CarType> getAvailableCarTypes(){
        return null;
    }
    
    
    // removed region
    // TODO -> removed RemoteException
    public Set<CarType> getAvailableCarTypes(Date start,Date end) {
    	Set<CarRentalCompany> companies = new HashSet<CarRentalCompany>();
    	companies = new HashSet<CarRentalCompany>(RentalStore.getRentals().values()); 
    	Set<CarType> carTypes = new HashSet<CarType>();
    	for(CarRentalCompany company: companies) {
            carTypes.addAll(company.getAvailableCarTypes(start, end));	 
        }
    	return carTypes;
    	
    }
    
    @Override
    public List<Quote> getCurrentQuotes()  {
	List<Quote> quotes = new ArrayList<Quote>(this.pendingQuotes.keySet());
	return quotes;
    }
    
    
    @Override
    public Quote createQuote(String clientName, Date start, Date end, String carType, String region) throws Exception {
		ReservationConstraints constraints = new ReservationConstraints(start, end, carType, region);
		Quote acceptedQuote = null;
		for(String currName: getAllRentalCompanies()){
			try{
                                CarRentalCompany currCompany =  RentalStore.getRental(currName);
				acceptedQuote = currCompany.createQuote(constraints, clientName);
				getPendingQuotes().put(acceptedQuote, currCompany);
				break;
			}
			catch(ReservationException e){
				continue;
			}
			catch(Exception e){
				System.out.println("No cars available to satisfy the given constraints in company: " + currName);
			}
		}
		
		if (acceptedQuote == null) {
			throw new Exception();
		}
			
		return acceptedQuote;
    }
    
    
    @Override
    public List<Reservation> confirmQuotes() throws ReservationException {
        List<Reservation> reservations = new ArrayList<Reservation>();
	HashMap<Reservation, CarRentalCompany> confirmedQuotes = new HashMap<Reservation, CarRentalCompany>();
	Iterator it = getPendingQuotes().entrySet().iterator();
    	while (it.hasNext()) {
        	Map.Entry pair = (Map.Entry) it.next();
			Quote quote = (Quote) pair.getKey();
			CarRentalCompany company = (CarRentalCompany) pair.getValue();
			try{
				Reservation res = company.confirmQuote(quote);
				confirmedQuotes.put(res, company);
			}
			
			catch(ReservationException e){
				Iterator it2 = confirmedQuotes.entrySet().iterator();
				while (it2.hasNext()) {
		        	Map.Entry pair2 = (Map.Entry) it2.next();
					Reservation res2 = (Reservation) pair2.getKey();
					CarRentalCompany company2 = (CarRentalCompany) pair.getValue();
					company2.cancelReservation(res2);
			    }
				
				it2.remove();
				throw new ReservationException("Not all quotes could be confirmed");
  
		    }
			
		    for (Reservation res: confirmedQuotes.keySet()) {
			    reservations.add(res);
		    }
			
	    }
    	
    	return reservations;
        
	}
    
    
    
    
    private HashMap<Quote, CarRentalCompany> pendingQuotes = new HashMap<Quote, CarRentalCompany>();
    
    public HashMap<Quote, CarRentalCompany> getPendingQuotes(){
		return this.pendingQuotes;
    }
    
    
    
    
    
}
