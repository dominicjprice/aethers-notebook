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

    @RequestMapping(value = "/delete_all", method = RequestMethod.POST)
    public ModelAndView deleteAll() {
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	
    	Class<?>[] models = new Class<?>[]{
    	        CellLocationEntry.class,
    	        DataConnectionStateEntry.class,
    	        Location.class,
    	        ServiceStateEntry.class,
    	        SignalStrengthEntry.class,
    	        SignalStrengthOnLocationChangeEntry.class,
    	        TelephonyStateEntry.class,
    	        WifiEntry.class
    	};
    	
    	for(Class<?> clazz : models)
    	{
    	    Query query = pm.newQuery(clazz);
    	    List<?> list = (List<?>)query.execute();
    	    pm.deletePersistentAll(list);
    	}
    	
    	ModelAndView mv = new ModelAndView("page/main");
    	mv.addObject("message", "Everything in database has been deleted!");
    	return mv;
    }

    
}
