package client;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import rental.Quote;
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
        Main main;
        main = new Main("simpleTrips");
        try {
            main.run();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public Main(String scriptFile) {
        super(scriptFile);
    }

    
    @Override
    protected void getAvailableCarTypes(CarRentalSessionRemote session, Date start, Date end) throws Exception {
	Iterator value = session.getAvailableCarTypes(start, end).iterator(); 
	while (value.hasNext()) { 
            System.out.println(value.next().toString()); 
        } 
    }

    @Override
    protected void createQuote(CarRentalSessionRemote session, String name, Date start, Date end, String carType, String region) throws Exception {
        Quote quote = session.createQuote(name, start, end, carType, region);
        System.out.println(quote.toString());
    }

    @Override
    protected List<Reservation> confirmQuotes(CarRentalSessionRemote session, String name) throws Exception {
        List<Reservation> reservations = session.confirmQuotes();
        for(Reservation res: reservations) {
            System.out.println(res.toString());
        }
        return reservations; 
    }

    @Override
    protected int getNumberOfReservationsBy(ManagerSessionRemote ms, String clientName) throws Exception {
        int number = ms.getNumberOfReservationsByRenter(clientName);
        System.out.println(number + " reservatios by " + clientName);
        return number;
    }

    @Override
    protected int getNumberOfReservationsForCarType(ManagerSessionRemote ms, String carRentalName, String carType) throws Exception {
        int number = ms.getNumberOfReservationsForCarType(carRentalName, carType);
        System.out.println(number + " reservatios for " + carType);
        return number;
    }

    
    
    @Override
    protected CarRentalSessionRemote getNewReservationSession(String name) throws Exception {
        InitialContext context = new InitialContext();
        CarRentalSessionRemote session = (CarRentalSessionRemote) context.lookup(CarRentalSessionRemote.class.getName());
        return session;
    }

    @Override
    protected ManagerSessionRemote getNewManagerSession(String name) throws Exception {
        InitialContext context = new InitialContext();
        ManagerSessionRemote session = (ManagerSessionRemote) context.lookup(ManagerSessionRemote.class.getName());
        return session;
    }
}
