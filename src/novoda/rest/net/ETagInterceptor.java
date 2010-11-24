package novoda.rest.net;

import java.io.IOException;

import novoda.rest.database.etag.ETagSQLiteHelper;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

public class ETagInterceptor implements HttpRequestInterceptor,
		HttpResponseInterceptor {

	/* package */ETagSQLiteHelper helper;

	public ETagInterceptor(Context context, String databaseName) {
		helper = new ETagSQLiteHelper(context, databaseName);
	}

	@Override
	public void process(HttpRequest request, HttpContext context)
			throws HttpException, IOException {
		if (isGetMethod(request)) {
			ETag etag = helper.getETag(((HttpUriRequest) request).getURI()
					.toString());
			if (etag != null) {
				if (!request.containsHeader(ETag.IF_NONE_MATCH)) {
					Header inm = new BasicHeader(ETag.IF_NONE_MATCH, etag.etag);
					request.addHeader(inm);
				}
				if (!request.containsHeader(ETag.IF_MODIFIED_SINCE)) {
					Header inm = new BasicHeader(ETag.IF_MODIFIED_SINCE,
							etag.lastModified);
					request.addHeader(inm);
				}
			}
		}
	}

	@Override
	public void process(HttpResponse response, HttpContext context)
			throws HttpException, IOException {

		/*
		 * Only save if we have the ETAG in the header and the response has been
		 * successful.
		 */
		if (response != null && response.containsHeader(ETag.ETAG)
				&& response.containsHeader(ETag.LAST_MODIFIED)
				&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			final Header etagHeader = response.getFirstHeader(ETag.ETAG);
			final Header lastModified = response
					.getFirstHeader(ETag.LAST_MODIFIED);

			final HttpRequest reqWrapper = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);

			final String request = reqWrapper.getRequestLine().getUri();

			ETag etag = new ETag();
			etag.etag = etagHeader.getValue();
			etag.lastModified = lastModified.getValue();
			helper.insertETagForUri(etag, request);
		}
	}

	private boolean isGetMethod(HttpRequest request) {
		if (((HttpUriRequest) request).getMethod().equals(HttpGet.METHOD_NAME))
			return true;
		return false;
	}
}
