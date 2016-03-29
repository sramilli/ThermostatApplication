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
import java.util.Date;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;
import thermostatapplication.properties.ThermostatProperties;
//http://mongodb.github.io/mongo-java-driver/3.0/driver/getting-started/quick-tour/

/**
 *
 * @author Ste
 */
class TemperaturePersisterTimerTask extends TimerTask {
    //TODO concurrency problems!!!!
    //continue refactoring
    
    Collection<TemperatureMeasure> iStoredTemperatures = null;
    Collection<TemperatureMeasure> iPersistedTemperatures = null;
    
    TemperatureStore iTemperatureStore;

    public TemperaturePersisterTimerTask(TemperatureStore aTemperatureStore) {
        iTemperatureStore = aTemperatureStore;
        //need to use a new list to freeze it
        iPersistedTemperatures = new ArrayList<TemperatureMeasure>();
        System.out.println("TemperaturePersisterTimerTask instantiated!!!");
    }

    @Override
    public void run() {
        this.persistDataOnMongolab();
    }
    
    public void persistDataOnMongolab(){
        iStoredTemperatures = iTemperatureStore.getTemperatures();
        if (iStoredTemperatures.isEmpty()) return;
        System.out.println("Prepairing to store "+iStoredTemperatures.size()+" Temps in the cloud");
        MongoCollection<Document> mongoCollection = null;
        MongoClient client = null;
        List<Document> documents = new ArrayList<Document>();
        
        for (TemperatureMeasure tTemp: iStoredTemperatures){
            Document doc = new Document();
            doc.put("Location", tTemp.getLocation());
            doc.put("Group", tTemp.getGroup());
            doc.put("Date", Helper.getDateAsString(tTemp.getDate()));
            doc.put("Day", Helper.getDayAsString(tTemp.getDate()));
            doc.put("Time", Helper.getTimeAsString(tTemp.getDate()));
            doc.put("Temp", Helper.getTempAsString(tTemp.getTemp()));
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
            iStoredTemperatures.removeAll(iPersistedTemperatures);
            client.close();
            System.out.println("Temperatures inserted: "+iPersistedTemperatures.size()+". Exiting");
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
