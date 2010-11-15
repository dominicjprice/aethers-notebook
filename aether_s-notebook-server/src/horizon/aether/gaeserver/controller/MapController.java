package horizon.aether.gaeserver.controller;

import horizon.aether.gaeserver.PMF;
import horizon.aether.gaeserver.model.DataConnectionStateEntry;
import horizon.aether.gaeserver.model.ServiceStateEntry;
import horizon.aether.gaeserver.model.SignalStrengthOnLocationChangeEntry;
import horizon.aether.gaeserver.model.TelephonyStateEntry;
import horizon.aether.utilities.JspUtils;
import horizon.aether.utilities.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import org.datanucleus.store.appengine.query.JDOCursorHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.datastore.Cursor;

/**
 * Controller that handles the logic for the web mashup. 
 * 
 * To build a map, a pagination technique was used where several requests are 
 * being made to the server. 
 * 
 * The first one simply gets the view. The second one, which is an AJAX request, starts retrieving
 * data to project on the view. The third, and all subsequent requests, retrieve more data
 * using a cursor to grab the next results.
 */
@Controller
public class MapController {

    // DATA CONNECTION STATE 

    /**
     * First request for a Data Connection State map.
     */
    @RequestMapping(value = "/map/dataConnectionState", method = RequestMethod.GET)
    public ModelAndView dataConnectionState() {
        return new ModelAndView("map/dataConnectionState");
    }

    /**
     * Second request for a Data Connection State map.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/map/drawDataConnectionState", method = RequestMethod.GET)
    public @ResponseBody String drawDataConnectionState() {
        // prepare query
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(DataConnectionStateEntry.class);
        query.setRange(0, 100);

        // execute
        List<DataConnectionStateEntry> entries = (List<DataConnectionStateEntry>) query.execute();

        // get cursor
        String cursorString = "null";
        Cursor cursor = JDOCursorHelper.getCursor(entries);
        if (cursor != null) {
            cursorString = JDOCursorHelper.getCursor(entries).toWebSafeString();
        }
        
        // prepare model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("entries", entries);
        model.put("cursorString", cursorString);

        // finalise
        entries.size();
        query.closeAll();
        pm.close();

        // draw
        String result = JspUtils.drawDataConnectionStateMap(entries);
        result += "\ncursorStr = '" + cursorString + "';";
        result += "\n$('#footer').html('" + entries.size() + " entries processed');";
        result += "\n$('#footer').show();";
        result += "\nonWindowResized();";
        result += "\ndrawMore();";

        // and return
        return result;
    }

    /**
     * Third/subsequent requests for a Data Connection State map. 
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/map/drawMoreDataConnectionState/{cursorString}", method = RequestMethod.GET)
    public @ResponseBody String drawMoredataConnectionState(@PathVariable String cursorString) {
        // prepare query
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Cursor cursor = Cursor.fromWebSafeString(cursorString);
        Map<String, Object> extensionMap = new HashMap<String, Object>();
        extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
        Query query = pm.newQuery(DataConnectionStateEntry.class);
        query.setExtensions(extensionMap);
        query.setRange(0, 100);

        // execute
        List<DataConnectionStateEntry> entries = (List<DataConnectionStateEntry>) query.execute();

        // draw map
        String result = JspUtils.drawDataConnectionStateMap(entries);

        // set new cursor
        String newCursorString  = (entries.size() > 0) ? JDOCursorHelper.getCursor(entries).toWebSafeString() : "null";
        result += "\ncursorStr = '" + newCursorString + "';";
        result += "\n$('#footer').html('" + entries.size() + " entries processed');";
        result += "\n$('#footer').show();";
        result += "\nonWindowResized();";
        result += "\ndrawMore();";

        // finalise
        query.closeAll();
        pm.close();

        // and return
        return result;
    }

    ////////////////////////////////////////////////////////////////////

    // SIGNAL STRENGTH

    /**
     * First request for a Signal Strength map.
     */
    @RequestMapping(value = "/map/signalStrength", method = RequestMethod.GET)
    public ModelAndView signalStrength() {
        return new ModelAndView("map/signalStrength");
    }

    /**
     * Second request for a Signal Strength map.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/map/drawSignalStrength", method = RequestMethod.GET)
    public @ResponseBody String drawSignalStrength() {
        // prepare query
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(SignalStrengthOnLocationChangeEntry.class);
        query.setRange(0, 100);

        // execute
        List<SignalStrengthOnLocationChangeEntry> entries = (List<SignalStrengthOnLocationChangeEntry>) query.execute();

        // get cursor
        String cursorString = "null";
        Cursor cursor = JDOCursorHelper.getCursor(entries);
        if (cursor != null) {
            cursorString = JDOCursorHelper.getCursor(entries).toWebSafeString();
        }
        
        // prepare model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("entries", entries);
        model.put("cursorString", cursorString);

        // finalise
        entries.size();
        query.closeAll();
        pm.close();

        // draw
        String result = JspUtils.drawSignalStrengthMap(entries);
        result += "\ncursorStr = '" + cursorString + "';";
        result += "\n$('#footer').html('" + entries.size() + " entries processed');";
        result += "\n$('#footer').show();";
        result += "\nonWindowResized();";
        result += "\ndrawMore();";

        // and return
        return result;
    }

    /**
     * Third/subsequent requests for a Signal Strength map. 
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/map/drawMoreSignalStrength/{cursorString}", method = RequestMethod.GET)
    public @ResponseBody String drawMoreSignalStrength(@PathVariable String cursorString) {
        // prepare query
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Cursor cursor = Cursor.fromWebSafeString(cursorString);
        Map<String, Object> extensionMap = new HashMap<String, Object>();
        extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
        Query query = pm.newQuery(SignalStrengthOnLocationChangeEntry.class);
        query.setExtensions(extensionMap);
        query.setRange(0, 100);

        // execute
        List<SignalStrengthOnLocationChangeEntry> entries = (List<SignalStrengthOnLocationChangeEntry>) query.execute();

        // draw map
        String result = JspUtils.drawSignalStrengthMap(entries);

        // set new cursor
        String newCursorString  = (entries.size() > 0) ? JDOCursorHelper.getCursor(entries).toWebSafeString() : "null";
        result += "\ncursorStr = '" + newCursorString + "';";
        result += "\n$('#footer').html('" + entries.size() + " entries processed');";
        result += "\n$('#footer').show();";
        result += "\nonWindowResized();";
        result += "\ndrawMore();";

        // finalise
        query.closeAll();
        pm.close();

        // and return
        return result;
    }

    ////////////////////////////////////////////////////////////////////

    // SERVICE STATE

    /**
     * First request for a Service State map.
     */
    @RequestMapping(value = "/map/serviceState", method = RequestMethod.GET)
    public ModelAndView serviceState() {
        return new ModelAndView("map/serviceState");
    }

    /**
     * Second request for a Service State map.
     */
    @RequestMapping(value = "/map/drawServiceState", method = RequestMethod.GET)
    public @ResponseBody String drawServiceState(HttpServletRequest request) {
        String operatorAlphaLong = getOperatorAlphaLong(request);
        int roaming = getRoaming(request);
        boolean checkOperator = getOperatorFlag(operatorAlphaLong);
        boolean checkRoaming = getRoamingFlag(roaming);

        PersistenceManager pm = PMF.get().getPersistenceManager();

        // prepare and execute query
        Query query = prepareServiceStateQuery(checkOperator, operatorAlphaLong, checkRoaming, roaming, pm, null);
        List<ServiceStateEntry> entries = executeServiceStateQuery(query, checkOperator, operatorAlphaLong, checkRoaming, roaming);
        pm.close();

        // get cursor
        String cursorString = "null";
        Cursor cursor = JDOCursorHelper.getCursor(entries);
        if (cursor != null) {
            cursorString = JDOCursorHelper.getCursor(entries).toWebSafeString();
        }

        // draw
        String result = JspUtils.drawServiceStateMap(entries);        
        result += "\ncursorStr = '" + cursorString + "';";
        result += "\n$('#footer').html('" + entries.size() + " entries processed');";
        result += "\n$('#footer').show();";
        result += "\nonWindowResized();";
        result += "\ndrawMore();";

        // and return
        return result;
    }

    /**
     * Third/subsequent requests for a Data Connection State map. 
     */
    @RequestMapping(value = "/map/drawMoreServiceState/{cursorString}", method = RequestMethod.GET)
    public @ResponseBody String moreServiceState(HttpServletRequest request, @PathVariable String cursorString) {
        String operatorAlphaLong = getOperatorAlphaLong(request);
        int roaming = getRoaming(request);
        boolean checkOperator = getOperatorFlag(operatorAlphaLong);
        boolean checkRoaming = getRoamingFlag(roaming);

        PersistenceManager pm = PMF.get().getPersistenceManager();

        // prepare and execute query
        Query query = prepareServiceStateQuery(checkOperator, operatorAlphaLong, checkRoaming, roaming, pm, cursorString);
        List<ServiceStateEntry> entries = executeServiceStateQuery(query, checkOperator, operatorAlphaLong, checkRoaming, roaming);
        pm.close();

        // draw map
        String result = JspUtils.drawServiceStateMap(entries);

        // set new cursor
        String newCursorString  = (entries.size() > 0) ? JDOCursorHelper.getCursor(entries).toWebSafeString() : "null";
        result += "\ncursorStr = '" + newCursorString + "';";
        result += "\n$('#footer').html('" + entries.size() + " entries processed');";
        result += "\n$('#footer').show();";
        result += "\nonWindowResized();";
        result += "\ndrawMore();";

        // and return
        return result;
    }

    ////////////////////////////////////////////////////////////////////

    // TELEPHONY STATE

    /**
     * First request for a Telephony State map.
     */
    @RequestMapping(value = "/map/telephonyState", method = RequestMethod.GET)
    public ModelAndView telephonyState() {
        return new ModelAndView("map/telephonyState");
    }

    /**
     * Second request for a Telephony State map.
     */
    @RequestMapping(value = "/map/drawTelephonyState", method = RequestMethod.GET)
    public @ResponseBody String drawTelephonyState(HttpServletRequest request) {
        // prepare query
        String[] selectedTypes = request.getParameter("selectedTypes").split("\\,");

        if (selectedTypes.length == 0) return "";
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = prepareTelephonyStateQuery(selectedTypes, pm, null);

        // execute
        List<TelephonyStateEntry> entries = executeTelephonyStateQuery(query, selectedTypes);
        pm.close();

        // get cursor
        String cursorString = "null";
        Cursor cursor = JDOCursorHelper.getCursor(entries);
        if (cursor != null) {
            cursorString = JDOCursorHelper.getCursor(entries).toWebSafeString();
        }

        // draw      
        String result = JspUtils.drawTelephonyStateMap(entries);
        result += "\ncursorStr = '" + cursorString + "';";
        result += "\n$('#footer').html('" + entries.size() + " entries processed');";
        result += "\n$('#footer').show();";
        result += "\nonWindowResized();";
        result += "\ndrawMore();";

        return result;
    }

    /**
     * Third/subsequent requests for a Telephony State map. 
     */
    @RequestMapping(value = "/map/drawMoreTelephonyState/{cursorString}", method = RequestMethod.GET)
    public @ResponseBody String moreTelephonyState(HttpServletRequest request, @PathVariable String cursorString) {
        // prepare query
        String[] selectedTypes = request.getParameter("selectedTypes").split("\\,");

        if (selectedTypes.length == 0) return "";
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = prepareTelephonyStateQuery(selectedTypes, pm, cursorString);

        // execute
        List<TelephonyStateEntry> entries = executeTelephonyStateQuery(query, selectedTypes);
        pm.close();

        // draw map        
        String result = JspUtils.drawTelephonyStateMap(entries);

        // set new cursor
        String newCursorString  = (entries.size() > 0) ? JDOCursorHelper.getCursor(entries).toWebSafeString() : "null";
        result += "\ncursorStr = '" + newCursorString + "';";
        result += "\n$('#footer').html('" + entries.size() + " entries processed');";
        result += "\n$('#footer').show();";
        result += "\nonWindowResized();";
        result += "\ndrawMore();";

        // and return
        return result;
    }

    ////////////////////////////////////////////////////////////////////

    // HELPER METHODS - Service State

    private static Query prepareServiceStateQuery(boolean checkOperator, String operatorAlphaLong, boolean checkRoaming, int roamingVal, PersistenceManager pm, String cursorString) {
        Query query = pm.newQuery(ServiceStateEntry.class);

        // get filters/params
        String filter = "";
        String parameters = "";
        if (checkOperator & !checkRoaming) {
            filter = "operatorAlphaLong == operatorParam";
            parameters = "String operatorParam";
        }
        else if (!checkOperator && checkRoaming) {
            filter = "roaming == roamingParam";
            parameters = "Boolean roamingParam";
        }
        else if (checkOperator && checkRoaming) {
            filter = "operatorAlphaLong == operatorParam && roaming == roamingParam";
            parameters = "String operatorParam, Boolean roamingParam";
        }

        // set filters/params
        if (!StringUtils.isNullOrEmpty(filter)) {
            query.declareParameters(parameters);
            query.setFilter(filter);
        }

        // set range
        query.setRange(0, 100);

        // set cursor (if applicable)
        if (!StringUtils.isNullOrEmpty(cursorString)) {
            Cursor cursor = Cursor.fromWebSafeString(cursorString);
            Map<String, Object> extensionMap = new HashMap<String, Object>();
            extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
            query.setExtensions(extensionMap);
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    private static List<ServiceStateEntry> executeServiceStateQuery(Query query, boolean checkOperator, String operatorAlphaLong, boolean checkRoaming, int roamingVal) {
        List<ServiceStateEntry> entries = null;
        Boolean roaming = (roamingVal == 2) ? new Boolean(true) : new Boolean(false);
        
        if (!checkOperator & !checkRoaming) {
            entries = (List<ServiceStateEntry>) query.execute();
        }
        else if (checkOperator & !checkRoaming) {
            entries = (List<ServiceStateEntry>) query.execute(operatorAlphaLong);
        }
        else if (!checkOperator & checkRoaming) {
            entries = (List<ServiceStateEntry>) query.execute(roaming);
        }
        else if (checkOperator && checkRoaming) {
            entries = (List<ServiceStateEntry>) query.execute(operatorAlphaLong, roaming);
        }

        query.closeAll();
        entries.size();
        return entries;
    }

    private static String getOperatorAlphaLong(HttpServletRequest request) {
        return request.getParameter("operator");
    }

    private static int getRoaming(HttpServletRequest request) {
        String roaming = request.getParameter("roaming");

        int roamingVal = 1;
        if (!StringUtils.isNullOrEmpty(roaming)) {
            roamingVal = Integer.parseInt(roaming);
        }

        return roamingVal;
    }

    private static boolean getOperatorFlag(String operator) {
        return !StringUtils.isNullOrEmpty(operator);
    }

    private static boolean getRoamingFlag(int roaming) {
        return roaming != 1;
    }

    ////////////////////////////////////////////////////////////////////

    // HELPER METHODS - Telephony State

    private static Query prepareTelephonyStateQuery(String[] selectedTypes, PersistenceManager pm, String cursorString) {
        Query query = pm.newQuery(TelephonyStateEntry.class);

        // filters/params
        if (selectedTypes.length > 0 && selectedTypes.length < 4) {
            String filter = "networkType == '" + selectedTypes[0] + "'";
            for (int i=1; i<selectedTypes.length; i++) {
                filter += " || networkType == '" + selectedTypes[i] + "'";
            }

            query.setFilter(filter);
        }

        // set range
        query.setRange(0, 100);

        // set cursor (if applicable)
        if (!StringUtils.isNullOrEmpty(cursorString)) {
            Cursor cursor = Cursor.fromWebSafeString(cursorString);
            Map<String, Object> extensionMap = new HashMap<String, Object>();
            extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
            query.setExtensions(extensionMap);
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    private static List<TelephonyStateEntry> executeTelephonyStateQuery(Query query, String[] selectedTypes) {
        List<TelephonyStateEntry> entries = (List<TelephonyStateEntry>) query.execute();

        query.closeAll();
        entries.size();
        return entries;
    }
}
