package horizon.aether.gaeserver.controller;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import horizon.aether.gaeserver.PMF;
import horizon.aether.gaeserver.model.*;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controllers that handles the logic for the main page.
 */
@Controller
public class PageController {
    
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main() {
    	return new ModelAndView("page/main", "message", "");
    }

    @SuppressWarnings("unchecked")	   
    @RequestMapping(value = "/delete_all", method = RequestMethod.POST)
    public ModelAndView deleteAll() {
    	PersistenceManager pm = PMF.get().getPersistenceManager();

    	Query query = pm.newQuery(SignalStrengthEntry.class);
    	List<SignalStrengthEntry> sseList = (List<SignalStrengthEntry>) query.execute();
    	pm.deletePersistentAll(sseList);

    	query = pm.newQuery(Location.class);
    	List<Location> locList = (List<Location>) query.execute();
    	pm.deletePersistentAll(locList);

    	query = pm.newQuery(TelephonyStateEntry.class);
    	List<TelephonyStateEntry> tseList = (List<TelephonyStateEntry>) query.execute();
    	pm.deletePersistentAll(tseList);

    	ModelAndView mv = new ModelAndView("page/main");
    	mv.addObject("message", "Everything in database has been deleted!");
    	return mv;
    }

    
}
