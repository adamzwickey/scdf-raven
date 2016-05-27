package com.pivotal.comcast.scdf;

import com.mongodb.ServerAddress;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class MongoDBProperties {

    private String _user, _pwd, _db, _collection, _exp;
    private ServerAddress[] _hostAddresses = { };

    @NotEmpty(message = "Query is required")
    public String getQuery() {
        return _exp;
    }

    public void setQuery(String query) {
        _exp = query;
    }

    @NotBlank(message = "Database name is required")
    public String getDb() {
        return _db;
    }

    public void setDb(String db) {
        _db = db;
    }

    public void setCollection(String collection) {
        _collection = collection;
    }

    @NotBlank(message = "Collection name is required")
    public String getCollection() {
        return _collection;
    }

    public ServerAddress[] getHostAddresses() {
        return _hostAddresses;
    }

    public void setHostAddresses(ServerAddress[] hostAddresses) {
        _hostAddresses = hostAddresses;
    }
}
