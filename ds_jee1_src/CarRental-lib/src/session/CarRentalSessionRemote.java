package session;

import java.util.Set;
import javax.ejb.Remote;
import rental.CarType;

@Remote
public interface CarRentalSessionRemote {

    Set<String> getAllRentalCompanies();

    public Set<CarType> getAvailableCarTypes();
    
}
