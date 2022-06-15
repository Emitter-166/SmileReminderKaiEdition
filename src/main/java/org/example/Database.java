package org.example;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.NoSuchElementException;

public class Database extends ListenerAdapter {
    public static MongoCollection collection;
    @Override
    public void onReady(ReadyEvent e){
        String uri = System.getenv("uri");
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
        document.append("funds", 0);
        //database template will go here
        collection.insertOne(document);

    }

    static void createDBConfig(String Id){

        Document document = new Document("config", Id);
        document.append("users", " ");
        //database template will go here
        collection.insertOne(document);

    }


    public static void PositiveupdateDB(String Id, String key, Integer value){
        Document document = null;
        try{
            document = (Document) collection.find(new Document("user", Id)).cursor().next();
        }catch (NoSuchElementException exception){
            createDB(Id);
            document = (Document) collection.find(new Document("user", Id)).cursor().next();
        }
        Document Updatedocument = new Document(key, value +(Integer) document.get(key)); //it will add old funds and new funds
        Bson updateKey = new Document("$set", Updatedocument);
        collection.updateOne(document, updateKey);
    }

    public static void updateUsers(String serverId, String key, String value){
        Document document = null;
        try{
            document = (Document) collection.find(new Document("config", serverId)).cursor().next();
        }catch (NoSuchElementException exception){
            createDBConfig(serverId);
            document = (Document) collection.find(new Document("config", serverId)).cursor().next();
        }
        Document Updatedocument = new Document(key, value +" "+ document.get(key));
        Bson updateKey = new Document("$set", Updatedocument);
        collection.updateOne(document, updateKey);
    }

    public static void Negative(String Id, String key, Integer value){
        //this is to charge members
        Document document = null;
        try{
            document = (Document) collection.find(new Document("user", Id)).cursor().next();
        }catch (NoSuchElementException exception){
            createDB(Id);
            document = (Document) collection.find(new Document("user", Id)).cursor().next();
        }
        Document Updatedocument = new Document(key, (Integer) document.get(key) - value); //it will add old funds and new funds
        Bson updateKey = new Document("$set", Updatedocument);
        collection.updateOne(document, updateKey);
    }
}