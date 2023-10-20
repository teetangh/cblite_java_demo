
package com.couchbase.gettingstarted;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;

public class CouchbaseLitePluginDriver {
    public static void main(String[] args) throws CouchbaseLiteException {
        CouchbaseLite.init();
        CouchbaseLitePlugin plugin = new CouchbaseLitePlugin();

        // Create a database
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDirectory("db");
        plugin.createDatabase("mydb", config);
        System.out.println("Database created: mydb inside db");

        // Create a new collection in the database
        plugin.createCollection("myCollection", "myScope");
        System.out.println("Collection created: myCollection in myScope");

        // List scopes
        List<String> scopes = plugin.listScopes();
        System.out.println("Scopes: " + scopes);

        // List collections in a scope
        List<String> collections = plugin.listCollections("myScope");
        System.out.println("Collections in myScope: " + collections);

        // Import data from JSON into a collection
        String json = "{\"language\":\"Java\"}";
        plugin.importDataFromJson(json, "myCollection", "myScope");
        System.out.println("Imported data from JSON into myCollection");

        // Create a new document and save it in the collection
        String docId = "doc1";
        MutableDocument doc = new MutableDocument(docId);
        doc.setString("language", "Java");
        plugin.saveDocument(doc, "myCollection", "myScope");
        System.out.println("Document created with ID: " + docId);

        // Retrieve the document and print its properties
        Document document = plugin.viewDocument(docId, "myCollection", "myScope");
        System.out.println("Retrieved document with ID: " + document.getId());
        System.out.println("Document language: " + document.getString("language"));

        // Update the document
        Map<String, Object> data = Map.of("language", "Kotlin");
        plugin.editDocument(docId, "myCollection", "myScope", data);

        // Retrieve the updated document and print its properties
        Document updatedDocument = plugin.viewDocument(docId, "myCollection", "myScope");
        System.out.println("Updated document with ID: " + updatedDocument.getId());
        System.out.println("Updated document language: " + updatedDocument.getString("language"));

        // Create a Blob object from some binary data
        byte[] byteData = "dummy data".getBytes();
        Blob blob = new Blob("text/plain", byteData);

        // Add the Blob as an attachment to a document
        docId = "doc1";
        plugin.addBlobAttachment(docId, "myCollection", "myScope", blob);

        document = plugin.viewDocument(docId, "myCollection", "myScope");
        Blob retrievedBlob = document.getBlob("attachment");

        if (retrievedBlob != null) {
            String content = new String(Objects.requireNonNull(retrievedBlob.getContent()));
            System.out.println("Retrieved blob content: " + content);
        }        

        // Close the database
        plugin.closeDatabase();
    }
}
