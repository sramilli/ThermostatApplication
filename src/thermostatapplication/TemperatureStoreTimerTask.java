/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermostatapplication;

import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;
//http://mongodb.github.io/mongo-java-driver/3.0/driver/getting-started/quick-tour/

/**
 *
 * @author Ste
 */
class TemperatureStoreTimerTask extends TimerTask {
    
    Collection<TemperatureMeasure> iTemperatures = null;
    Collection<TemperatureMeasure> iStoredTemperatures = null;

    public TemperatureStoreTimerTask(Collection<TemperatureMeasure> aTemperatures) {
        iTemperatures = aTemperatures;
        //need to use a new list to freeze it
        iStoredTemperatures = new ArrayList<TemperatureMeasure>();
    }

    @Override
    public void run() {
        this.persistDataOnMongolab();
    }
    
    public void persistDataOnMongolab(){
        if (iTemperatures.isEmpty()) return;
        System.out.println("Prepairing to store "+iTemperatures.size()+" Temps in the cloud");
        MongoCollection<Document> mongoCollection = null;
        MongoClient client = null;
        List<Document> documents = new ArrayList<Document>();
        
        for (TemperatureMeasure tTemp: iTemperatures){
            Document doc = new Document();
            doc.put("Name", tTemp.getName());
            doc.put("Date", Helper.getDateAsString(tTemp.getDate()));
            doc.put("Temp", Helper.getTempAsString(tTemp.getTemp()));
            documents.add(doc);
            iStoredTemperatures.add(tTemp);
        }

        try{
            MongoClientURI uri  = new MongoClientURI("mongodb://heaterusr:heater66@ds047602.mongolab.com:47602/heaterdb"); 
            client = new MongoClient(uri);
            MongoDatabase database = (MongoDatabase) client.getDatabase(uri.getDatabase());
            mongoCollection = database.getCollection("dailytemps");
            mongoCollection.insertMany(documents);
            //eliminate stored Temps from the collection
            iTemperatures.removeAll(iStoredTemperatures);
            client.close();
            System.out.println("Temperatures inserted: "+iStoredTemperatures.size()+". Exiting");
            iStoredTemperatures.clear();
        } catch (Throwable e){
            System.out.println("Failed to store Temps in the cloud. Stacktrace:");
            e.printStackTrace();
            System.out.println("End stacktrace.");
        } finally{
            if (client != null){
                client.close();
            }
        }
    }
    
}
