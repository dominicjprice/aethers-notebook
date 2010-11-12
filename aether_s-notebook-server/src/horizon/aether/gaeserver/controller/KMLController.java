package horizon.aether.gaeserver.controller;

import horizon.aether.gaeserver.PMF;
import horizon.aether.gaeserver.model.SignalStrengthEntry;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller that handles the logic for KML file generation. 
 * 
 */
@Controller
public class KMLController {

    ////////////////////////////////////////////////////////////////////

    // SIGNAL STRENGTH

    /**
     * Request for a Signal Strength KML
     */
	
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/kml/signalStrength", method = RequestMethod.GET)    
    public ModelAndView signalStrength() {
		// prepare query
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(SignalStrengthEntry.class);
		//query.setRange(0, 6000);

		// execute
		List<SignalStrengthEntry> entries = (List<SignalStrengthEntry>) query.execute();

		ModelAndView mAndV = new ModelAndView("kml/signalStrength");
		mAndV.addObject("size", entries.size());
		mAndV.addObject("message", "Signal Strength Entries");
		mAndV.addObject("entries", entries);

		// finalise
/*		query.closeAll();
		pm.close();*/

		return mAndV;
	}

	@SuppressWarnings("unchecked")	   
    @RequestMapping(value = "/kml/stats", method = RequestMethod.GET)
    public ModelAndView main() {
    	ModelAndView mAndV = new ModelAndView("kml/stats");
		// prepare query
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(SignalStrengthEntry.class);
		query.setRange(0, 500);
		// execute
		List<SignalStrengthEntry> entries = (List<SignalStrengthEntry>) query.execute();
		String statsString = "Points of data: " + entries.size(); 

		// finalise
		query.closeAll();
		pm.close();
		mAndV.addObject("message", "Signal Strength");
		mAndV.addObject("stats", statsString);

    	return mAndV;
    }

	
}
