package session;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationException;

@Remote
public interface CarRentalSessionRemote {

    Set<String> getAllRentalCompanies();

    public Set<CarType> getAvailableCarTypes();
    
    public Quote createQuote(String clientName, Date start, Date end, String carType, String region) throws Exception;
    
    public List<Quote> getCurrentQuotes();
    
     public List<Reservation> confirmQuotes() throws ReservationException;
    
}
