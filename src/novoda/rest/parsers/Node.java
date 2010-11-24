
package novoda.rest.parsers;

import android.content.ContentValues;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple Tree object with back reference to the parent Node. It enables tree
 * traversal of a random response being JSON, XML or others. Each node within
 * the tree usually represent a node within the response as in a tag in XML or
 * an Object in JSON. For each Node, we should be able to insert it into the
 * database by extracting the necessary data.
 */
public abstract class Node<T> {

    /**
     *  
     */
    public static class ParsingOptions {
        public String rootNode;

        public String nodeName;

        public String table;

        public Uri insertUri;

        public Map<String, String> mapper = new HashMap<String, String>();

        public Map<String, ParsingOptions> children = new HashMap<String, ParsingOptions>();
    }

    /*
     * The representation of a node within the response object. This will be
     * specific to each format and lets implementator the chance to parse the
     * object into understandable content values.
     */
    protected T data;

    /*
     * The inserted row id in the database. It is populated after an insert.
     */
    long databaseId = -1;

    /*
     * In case of One to many relationship, we need the database column name of
     * the parent for which to insert the child elements.
     */
    String idFieldName = null;

    private List<Node<T>> children;

    /*
     * Keep track of the parent
     */
    private Node<T> parent;

    private ParsingOptions options;

    public T getData() {
        return data;
    }

    public List<Node<T>> getChildren() {
        if (this.children == null) {
            return new ArrayList<Node<T>>();
        }
        return this.children;
    }

    public int getNumberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    public void addChild(Node<T> child) {
        if (children == null) {
            children = new ArrayList<Node<T>>();
        }
        child.parent = this;
        children.add(child);
    }

    /*
     * Gives you a chance to hook into the node before the insert. if One 2
     * Many, we will add the parent's field name and the id of the parent's row
     * with the new values to be inserted.
     */
    public void onPreInsert(final ContentValues values) {
        if (parent != null && parent.databaseId > 0) {
            values.put(parent.getIdFieldName(), parent.getDatabaseId());
        }
    }

    /*
     * After the insert we get the DB id back and set it to the node for the
     * children to be used.
     */
    public void onPostInsert(long id) {
        setDatabaseId(id);
    }

    public void setDatabaseId(long databaseId) {
        this.databaseId = databaseId;
    }

    public long getDatabaseId() {
        return databaseId;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getIdFieldName() {
        return getTableName() + "_id";
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setOptions(ParsingOptions options) {
        this.options = options;
    }

    public ParsingOptions getOptions() {
        return options;
    }

    /**
     * @return The values to be inserted into the DB for this node
     */
    abstract public ContentValues getContentValue();

    /**
     * @return the table Name for which this row will be inserted
     */
    abstract public String getTableName();

    /**
     * @return true if we should insert the node into DB, false otherwise
     */
    abstract public boolean shouldInsert();

    /**
     * @return the amount of nodes within this level of the tree
     */
    abstract public int getCount();

    /**
     * @param index
     * @return the node at the specified index
     */
    abstract public Node<T> getNode(int index);

    abstract public void applyOptions(ParsingOptions options);

    abstract public String[] getColumns();
}
