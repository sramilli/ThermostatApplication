/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import thermostatapplication.entity.TemperatureMeasure;
import thermostatapplication.helper.Helper;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Collection;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.bson.Document;
import thermostatapplication.properties.ThermostatProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//http://mongodb.github.io/mongo-java-driver/3.0/driver/getting-started/quick-tour/

/**
 *
 * @author Ste
 */
class TemperaturePersisterTimerTask extends TimerTask {
    //TODO concurrency problems!!!!
    //continue refactoring
    
    static Logger logger = LoggerFactory.getLogger(TemperaturePersisterTimerTask.class);
    
    Collection<TemperatureMeasure> iStoredTemperatures = null;
    Collection<TemperatureMeasure> iPersistedTemperatures = null;
    
    TemperatureStore iTemperatureStore;

    public TemperaturePersisterTimerTask(TemperatureStore aTemperatureStore) {
        iTemperatureStore = aTemperatureStore;
        //need to use a new list to freeze it
        iPersistedTemperatures = new ArrayList<TemperatureMeasure>();
        //System.out.println("TemperaturePersisterTimerTask instantiated!!!");
        logger.info("TemperaturePersisterTimerTask instantiated");
    }

    @Override
    public void run() {
        this.persistDataOnMongolab();
    }
    
    public synchronized void persistDataOnMongolab(){
        //disable console logging
        //Logger mongoLogger = Logger.getLogger("org.mongodb.driver"); 
        //mongoLogger.setLevel(Level.SEVERE);

        
        iStoredTemperatures = iTemperatureStore.getTemperatures();
        if (iStoredTemperatures.isEmpty()) return;
        logger.info("Prepairing to persist [{}] Temps in the cloud", iStoredTemperatures.size());
        //System.out.println("Prepairing to persist "+iStoredTemperatures.size()+" Temps in the cloud");
        MongoCollection<Document> mongoCollection = null;
        MongoClient client = null;
        List<Document> documents = new ArrayList<>();
        
        for (TemperatureMeasure tTemp: iStoredTemperatures){ //Exception in thread "Timer-2" java.util.ConcurrentModificationException
            Document doc = new Document();
            doc.put("Location", tTemp.getLocation());      //Location
            doc.put("Group", tTemp.getGroup());         //Group
            doc.put("Date", Helper.getDateAsString(tTemp.getDate()));   //Date
            doc.put("Day", Helper.getDayAsString(tTemp.getDate()));
            doc.put("Time", Helper.getTimeAsString(tTemp.getDate()));
            doc.put("Temp", Helper.getTempAsString(tTemp.getTemp()));   //Temp
            documents.add(doc);
            iPersistedTemperatures.add(tTemp);
        }

        try{
            MongoClientURI uri  = new MongoClientURI(ThermostatProperties.ML_URL); 
            client = new MongoClient(uri);
            MongoDatabase database = (MongoDatabase) client.getDatabase(uri.getDatabase());
            mongoCollection = database.getCollection("dailytemps");
            mongoCollection.insertMany(documents);
            //eliminate stored Temps from the collection
            iTemperatureStore.removeAll(iPersistedTemperatures);
            client.close();
            logger.info("Temperatures persisted on mongolab: [{}]. Exiting.", iPersistedTemperatures.size());
            //System.out.println("Temperatures persisted on mongolab: "+iPersistedTemperatures.size()+". Exiting");
            iPersistedTemperatures.clear();
        } catch (Throwable e){
            System.out.println("Failed to store Temps in the cloud. Stacktrace:");
            iPersistedTemperatures.clear();
            e.printStackTrace();
            System.out.println("End stacktrace.");
        } finally{
            if (client != null){
                client.close();
            }
            iPersistedTemperatures.clear();
        }
    }
    
}
