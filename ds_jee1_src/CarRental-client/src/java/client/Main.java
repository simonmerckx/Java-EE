package client;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import rental.Reservation;
import session.CarRentalSessionRemote;
import session.ManagerSessionRemote;

public class Main extends AbstractTestAgency<CarRentalSessionRemote, ManagerSessionRemote>  {
    
    //@EJB
    //static CarRentalSessionRemote session;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      //  System.out.println("found rental companies: "+session.getAllRentalCompanies());
    }

    //TODO ??? dit hebben wij toegevoegd, maar niet zeker
    public Main(String scriptFile) {
        super(scriptFile);
    }

    
    @Override
    protected void getAvailableCarTypes(CarRentalSessionRemote session, Date start, Date end) throws Exception {
	Iterator value = session.getAvailableCarTypes(start, end).iterator(); 
	while (value.hasNext()) { 
            System.out.println(value.next().toString()); 
        } //TODO print remove
    }

    @Override
    protected void createQuote(CarRentalSessionRemote session, String name, Date start, Date end, String carType, String region) throws Exception {
        session.createQuote(name, start, end, carType, region);
    }

    @Override
    protected List<Reservation> confirmQuotes(CarRentalSessionRemote session, String name) throws Exception {
        return session.confirmQuotes(); //TODO voor Simon
    }

    @Override
    protected int getNumberOfReservationsBy(ManagerSessionRemote ms, String clientName) throws Exception {
        return ms.getNumberOfReservationsByRenter(clientName);
    }

    @Override
    protected int getNumberOfReservationsForCarType(ManagerSessionRemote ms, String carRentalName, String carType) throws Exception {
        return ms.getNumberOfReservationsForCarType(carRentalName, carType);
    }

    
    // TODO
    @Override
    @EJB
    protected CarRentalSessionRemote getNewReservationSession(String name) throws Exception {
        InitialContext context = new InitialContext();
        CarRentalSessionRemote session = (CarRentalSessionRemote) context.lookup(CarRentalSessionRemote.class.getName());
        return session;
    }

    @Override
    @EJB
    protected ManagerSessionRemote getNewManagerSession(String name) throws Exception {
        InitialContext context = new InitialContext();
        ManagerSessionRemote session = (ManagerSessionRemote) context.lookup(ManagerSessionRemote.class.getName());
        return session;
    }
}
