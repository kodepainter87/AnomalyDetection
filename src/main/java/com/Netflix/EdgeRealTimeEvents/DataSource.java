package main.java.com.Netflix.EdgeRealTimeEvents;

import io.reactivex.Observable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DataSource {
	private BufferedReader bufferedReader;
	private String URL_PATH = "https://tweet-service.herokuapp.com/stream";
	private String TWEET_PREFIX = "data: ";

	DataSource() throws Exception {
		  URL url = new URL(this.URL_PATH);
		  URLConnection connection = url.openConnection(); 
		  this.bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}
	
	public Observable<Tweet> getStream() {
		return Observable.create(subcription -> {
			try {
				while (!subcription.isCancelled()) {
					String line = null;
					// Read each line from the buffered reader and do sanity checks.
					while ((line = this.bufferedReader.readLine()) != null
							&& !line.isEmpty() && line.startsWith(TWEET_PREFIX)) {
						try {
							Tweet tweet = new Tweet(line);
							subcription.onNext(tweet);
						} catch (Exception exception) {
							// Will be ignoring malformed tweets.
						}
					}
				}
			} catch (Exception exception) {
				if(this.bufferedReader != null) {
					this.bufferedReader.close();
				}
                subcription.onError(exception);
            }
       });
	}
}
