package session;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.Stateful;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.ReservationConstraints;

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
    
    public Quote createQuote(String clientName, Date start, Date end, String carType, String region) throws Exception {
		ReservationConstraints constraints = new ReservationConstraints(start, end, carType, region);
		Quote acceptedQuote = null;
		for(String currCompany: getAllRentalCompanies()){
			try{
				acceptedQuote = currCompany.createQuote(constraints, clientName);
				getPendingQuotes().put(acceptedQuote, currCompany);
				break;
			}
			catch(ReservationException e){
				continue;
			}
			catch(Exception e){
				System.out.println("No cars available to satisfy the given constraints in company: " + currCompany.getName());
			}
		}
		
		if (acceptedQuote == null) {
			throw new Exception();
		}
			
		return acceptedQuote;
	}
    
    
}
