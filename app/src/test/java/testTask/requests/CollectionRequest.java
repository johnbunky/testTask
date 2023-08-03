package testTask.requests;

import testTask.stuff.Isbn;

import java.util.List;

public class CollectionRequest {
    private String userId;
    private List<Isbn> collectionOfIsbns;

    public CollectionRequest(String userId, List<Isbn> collectionOfIsbns) {
        this.userId = userId;
        this.collectionOfIsbns = collectionOfIsbns;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Isbn> getCollectionOfIsbns() {
        return collectionOfIsbns;
    }

    public void setCollectionOfIsbns(List<Isbn> collectionOfIsbns) {
        this.collectionOfIsbns = collectionOfIsbns;
    }
}

