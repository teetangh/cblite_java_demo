package com.couchbase.gettingstarted;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.couchbase.lite.Blob;
import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Scope;

public class CouchbaseLitePlugin {

    private Database database;

    public void createDatabase(String dbName) throws CouchbaseLiteException {
        database = new Database(dbName);
    }

    public void createCollection(String collectionName, String scopeName) throws CouchbaseLiteException {
        database.createCollection(collectionName, scopeName);
    }

    public List<String> listScopes() throws CouchbaseLiteException {
        List<String> scopes = new ArrayList<>();
        for (Scope scope : database.getScopes()) {
            scopes.add(scope.getName());
        }
        return scopes;
    }

    public List<String> listCollections(String scopeName) throws CouchbaseLiteException {
        List<String> collections = new ArrayList<>();
        for (Collection collection : database.getCollections(scopeName)) {
            collections.add(collection.getName());
        }
        return collections;
    }

    public void importDataFromJson(String json, String collectionName, String scopeName) throws CouchbaseLiteException {
        // Parse the JSON data
        // For simplicity, let's assume it's a single document
        MutableDocument doc = new MutableDocument(json);

        // Save the document to the collection
        try {
            database.getCollection(collectionName, scopeName).save(doc);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public Document viewDocument(String docId, String collectionName, String scopeName) {
        Document doc = null;
        try {
            doc = database.getCollection(collectionName, scopeName).getDocument(docId);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public void editDocument(String docId, String collectionName, String scopeName, Map<String, Object> data) throws CouchbaseLiteException {
        MutableDocument doc = null;
        try {
            doc = database.getCollection(collectionName, scopeName).getDocument(docId).toMutable();
            // Update the document with the new data
            doc.setData(data);
            // Save the updated document
            database.getCollection(collectionName, scopeName).save(doc);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public void addBlobAttachment(String docId, String collectionName, String scopeName, Blob blob) throws CouchbaseLiteException {
        MutableDocument doc = null;
        try {
            doc = database.getCollection(collectionName, scopeName).getDocument(docId).toMutable();
            // Add the blob attachment
            doc.setBlob("attachment", blob);
            // Save the updated document
            database.getCollection(collectionName, scopeName).save(doc);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabase() {
        try {
            database.close();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}
