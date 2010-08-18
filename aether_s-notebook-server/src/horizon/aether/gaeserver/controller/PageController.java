package horizon.aether.gaeserver.controller;


import horizon.aether.gaeserver.PMF;
import horizon.aether.gaeserver.model.CellLocationEntry;
import horizon.aether.gaeserver.model.DataConnectionStateEntry;
import horizon.aether.gaeserver.model.Location;
import horizon.aether.gaeserver.model.NeighbouringCell;
import horizon.aether.gaeserver.model.ServiceStateEntry;
import horizon.aether.gaeserver.model.SignalStrengthEntry;
import horizon.aether.gaeserver.model.TelephonyStateEntry;
import horizon.aether.gaeserver.model.WifiEntry;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class PageController {
    
    private static final Logger log = Logger.getLogger(PageController.class.getName());
    
    @RequestMapping(value = "/page/clearDb", method = RequestMethod.GET)
    public String showClearDbScreen(Model model) {
        deleteClassEntries(CellLocationEntry.class);
        deleteClassEntries(DataConnectionStateEntry.class);
        deleteClassEntries(Location.class);
        deleteClassEntries(NeighbouringCell.class);
        deleteClassEntries(ServiceStateEntry.class);
        deleteClassEntries(SignalStrengthEntry.class);
        deleteClassEntries(TelephonyStateEntry.class);
        deleteClassEntries(WifiEntry.class);        
        
        return "page/main";
    }

    private void deleteClassEntries(Class<?> c) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query q = pm.newQuery(c);
            q.deletePersistentAll();
            q.execute();
            q.closeAll();        
        }
        catch (Exception e) { log.warning(e.toString()); }
        pm.close();
    }
}
