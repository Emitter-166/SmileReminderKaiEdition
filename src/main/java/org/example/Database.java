package org.example;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class Database extends ListenerAdapter {
    public static MongoCollection collection;
    @Override
    public void onReady(ReadyEvent e){
        String uri = "mongodb+srv://admin:the2horned@cluster0.5on01.mongodb.net/?retryWrites=true&w=majority";
        MongoClientURI clientURI = new MongoClientURI(uri);
        MongoClient client = new MongoClient(clientURI);
        MongoDatabase database = client.getDatabase("users");
        collection = database.getCollection("users");

    }



    public static Document get(String Id) throws InterruptedException {
        try{
            return (Document) collection.find(new Document("user", Id)).cursor().next();
        } catch (Exception exception){
            createDB(Id);
            Thread.sleep(500);
            return (Document) collection.find(new Document("user", Id)).cursor().next();
        }
    }

    public static Document confiGet(String Id) throws InterruptedException {
        try{
            return (Document) collection.find(new Document("config", Id)).cursor().next();
        } catch (Exception exception){
            createDBConfig(Id);
            Thread.sleep(500);
            return (Document) collection.find(new Document("config", Id)).cursor().next();
        }
    }



     static void createDB(String Id){

        Document document = new Document("user", Id);
        document.append("isSubscribed", false);
        //database template will go here
        collection.insertOne(document);

    }

    static void createDBConfig(String Id){

        Document document = new Document("config", Id)
                .append("toCut", 0)
                .append("prefix", ".r")
                .append("reminderChannel", "0")
                .append("users", " ")
                .append("actionChannel", " ")
                .append("actionMessage", " ");

        //database template will go here
        collection.insertOne(document);

    }


    public static void updateUsers(String serverId, String key, String value, boolean isSubscribed, boolean remove){
        Document document = null;
        try{
            document = (Document) collection.find(new Document("config", serverId)).cursor().next();
        }catch (NoSuchElementException exception){
            createDBConfig(serverId);
            document = (Document) collection.find(new Document("config", serverId)).cursor().next();
        }

        if((!(Arrays.stream(document.get("users").toString().split(" ")).anyMatch(userId -> userId.equals(value)))) && !remove){
            Document Updatedocument = new Document(key, value +" "+ document.get(key));
            Bson updateKey = new Document("$set", Updatedocument);
            collection.updateOne(document, updateKey);
        }else if(remove){

            Document Updatedocument = new Document(key, document.get(key).toString().replace(value, ""));
            Bson updateKey = new Document("$set", Updatedocument);
            collection.updateOne(document, updateKey);
        }

        Document userDoc;
         try{
            userDoc = (Document) collection.find(new Document("user", value));
        } catch (NoSuchElementException exception){
             createDB(value);
             userDoc = (Document) collection.find(new Document("user", value));
         }
        Document userUpdateDoc = new Document()
                 .append("isSubscribed", isSubscribed);
         Bson updateKey = new Document("$set", userUpdateDoc);
         collection.updateOne(userDoc, updateKey);
    }

    public static void updateConfig(String serverId, String key, Object value){
        Document serverConfig = null;
        try{
            serverConfig = (Document) collection.find(new Document("config", serverId)).cursor().next();
        }catch (NoSuchElementException exception){
            createDBConfig(serverId);
            serverConfig = (Document) collection.find(new Document("config", serverId)).cursor().next();
        }

        Document updateDoc = new Document()
                .append(key, value);
        Bson updateKey = new Document("$set", updateDoc);
        collection.updateOne(serverConfig, updateKey);
    }
}