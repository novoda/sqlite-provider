
package novoda.rest.parsers;


import novoda.rest.exception.ParserException;
import novoda.rest.parsers.Node.ParsingOptions;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.BufferedHttpEntity;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public abstract class NodeParser<T extends Node<?>> implements ResponseHandler<T> {

    protected NodeParser() {
    }

    ParsingOptions options;

    abstract public T parse(InputStream response, ParsingOptions options) throws ParserException;

    /**
     * The expected result HTTP response code. Do NOT return NOT MODIFIED (304)
     * as this will be handled automatically by the RESTProvider.
     * 
     * @return the expected status code. Usually OK 200 but could be NO CONTENT
     *         204
     */
    public int getExpectedResponse() {
        return HttpStatus.SC_OK;
    }

    int statusCode = -1;

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        if (response == null) {
            throw new IOException("Response can not be null");
        }
        final int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = null;
        InputStream in = null;
        try {
            this.statusCode = statusCode;
            if (statusCode == getExpectedResponse()) {
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                entity = new BufferedHttpEntity(response.getEntity());
                in = entity.getContent();
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    in = new GZIPInputStream(in);
                }
                return parse(in, options);
            } else if (statusCode == HttpStatus.SC_NOT_MODIFIED) {
                // special case;
                return null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        } finally {
            if (entity != null) {
                entity.consumeContent();
            }
        }
        throw new ParserException("unknown error");
    }

    public static class Builder {

        ParsingOptions option;

        Builder builder;

        public Builder() {
            option = new ParsingOptions();
        }

        public Builder withRootNode(String root) {
            option.rootNode = root;
            return this;
        }

        public Builder withNodeName(String nodeName) {
            option.nodeName = nodeName;
            return this;
        }

        public Builder withMappedFields(Map<String, String> mapper) {
            option.mapper = mapper;
            return this;
        }

        public Builder withTableName(String table) {
            option.table = table;
            return this;
        }

        public Builder withChildren(Map<String, ParsingOptions> children) {
            option.children.putAll(children);
            return this;
        }

        public Builder withInsertUri(Uri uri) {
            option.insertUri = uri;
            return this;
        }

        public <T extends NodeParser<?>> NodeParser<?> build(Class<T> klass) {
            T t = null;
            try {
                t = klass.newInstance();
                t.options = this.option;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return t;
        }
    }
}
