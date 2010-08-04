package horizon.aether.gaeserver.controller;


import horizon.aether.gaeserver.PMF;
import horizon.aether.gaeserver.model.CellLocationBlob;
import horizon.aether.gaeserver.model.DataConnectionStateBlob;
import horizon.aether.gaeserver.model.Entry;
import horizon.aether.gaeserver.model.Location;
import horizon.aether.gaeserver.model.NeighbouringCell;
import horizon.aether.gaeserver.model.ServiceStateBlob;
import horizon.aether.gaeserver.model.SignalStrengthBlob;
import horizon.aether.gaeserver.model.TelephonyStateBlob;
import horizon.aether.gaeserver.model.Wifi;
import horizon.aether.gaeserver.model.WifiBlob;
import horizon.aether.gaeserver.utilities.JacksonUtils;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class PageController {
    
    private static final Logger log = Logger.getLogger(PageController.class.getName());
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/main", method = RequestMethod.GET)
    public ModelAndView showPageScreen(Model model) {
        // show the last 2 entries added
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery(Entry.class);
        q.setRange(0, 2);
        try {
            List<Entry> entries = (List<Entry>) q.execute();
            if (entries.iterator().hasNext()) {
                List<String> ss = JacksonUtils.generateEntries(entries);
                String r ="";
                for (String s : ss) {
                    r += s + "\n";
                }
                log.warning("-----entries----\n" + r );
            }
        }
        finally {
            q.closeAll();
        }
        
        return new ModelAndView("page/main");
    }
    
    @RequestMapping(value = "/page/clearDb", method = RequestMethod.GET)
    public String showClearDbScreen(Model model) {
        deleteClassEntries(CellLocationBlob.class);
        deleteClassEntries(DataConnectionStateBlob.class);
        deleteClassEntries(Entry.class);
        deleteClassEntries(Location.class);
        deleteClassEntries(NeighbouringCell.class);
        deleteClassEntries(ServiceStateBlob.class);
        deleteClassEntries(SignalStrengthBlob.class);
        deleteClassEntries(TelephonyStateBlob.class);
        deleteClassEntries(Wifi.class);        
        deleteClassEntries(WifiBlob.class);
        
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
