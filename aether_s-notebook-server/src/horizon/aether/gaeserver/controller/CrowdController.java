package horizon.aether.gaeserver.controller;

import horizon.aether.gaeserver.PMF;
import horizon.aether.gaeserver.model.EntryPack;
import horizon.aether.gaeserver.utilities.CompressionUtils;
import horizon.aether.gaeserver.utilities.JacksonUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class CrowdController {

    private static final Logger log = Logger.getLogger(CrowdController.class.getName());

    @RequestMapping(value = "/crowd/*", method = RequestMethod.GET)
    public String showCrowdGetScreen() {
        return "empty";
    }

    @RequestMapping(value = "/crowd/*", method = RequestMethod.POST)
    public String showCrowdPostScreen(HttpServletRequest req) {
        try {
            // receive file
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iterator = upload.getItemIterator(req);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                InputStream stream = item.openStream();

                if (!item.isFormField()) {
                    // uncompress
                    OutputStream uncompressedStream = new ByteArrayOutputStream();
                    if (!CompressionUtils.uncompress(stream, uncompressedStream)) {
                        throw new ServletException("Failed to uncompress file");
                    }
                                            
                    // parse and persist file entries
                    EntryPack entryPack = JacksonUtils.parseEntries(uncompressedStream.toString());
                    PersistenceManager pm = PMF.get().getPersistenceManager();
                    pm.makePersistentAll(entryPack.getEntries());
                    pm.makePersistentAll(entryPack.getBlobs());
                    pm.close();
                    
//                    for (int i=0; i<entryPack.getEntries().size(); i++) {
//                        log.warning("persisting entry.." + entryPack.getEntries().get(i));
//                        pm.makePersistent(entryPack.getEntries().get(i));
//                        pm.makePersistent(entryPack.getBlobs().get(i));
//                    }
//                    pm.close();
                }
            }
        }
        catch (Exception ex) {
            log.warning(ex.toString());
        }
        
        return "empty";
    }
}
