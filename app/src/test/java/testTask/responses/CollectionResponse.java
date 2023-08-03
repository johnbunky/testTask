package testTask.responses;

import java.util.List;

public class CollectionResponse {
    private String userId;
    private List<String> collectionOfIsbns;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getCollectionOfIsbns() {
        return collectionOfIsbns;
    }

    public void setCollectionOfIsbns(List<String> collectionOfIsbns) {
        this.collectionOfIsbns = collectionOfIsbns;
    }
}
