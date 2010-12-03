package novoda.lib.sqliteprovider.sqlite;

import android.net.Uri;


public class SQLiteUri {

    private Uri uri;
    
    public SQLiteUri (Uri uri) {
        this.setUri(uri);
    }
    
    

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }
}
