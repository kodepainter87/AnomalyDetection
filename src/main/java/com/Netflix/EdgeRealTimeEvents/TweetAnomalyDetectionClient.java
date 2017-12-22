package main.java.com.Netflix.EdgeRealTimeEvents;

import io.reactivex.functions.Predicate;

public class TweetAnomalyDetectionClient {
	// Stores the maximum reTweet count observed so far.
	private static long maxReTweetCount = 250;
	
	// Stores the rolling average of reTweets.
	private static long reTweetRollingAverage = 100;
	
	// Size of the rolling window.
	private static int ROLLING_WINDOW = 500;
	
	// Minimum anomaly offset is number of times a reTweet count should be greater than rolling average
	// for it to be considered an anomaly.
	private static double MIN_ANOMALY_OFFSET = 1.5;
	
	// For a reTweet to be considered an anomaly (reTweet / highestReTweetCount) should be greater than ANAMOLY_RATIO. 
	private static double ANOMALY_RATIO = 0.85;
	
	public static void main(String[] args) throws Exception {
		DataSource source = new DataSource();
		source.getStream()
		.filter(new Predicate<Tweet>() {
			@Override
			public boolean test(Tweet tweet) {
				// Tracks the highest reTweet seen so far.
				if (tweet.reTweetCount > maxReTweetCount) {
				    maxReTweetCount = tweet.reTweetCount;
				}
				
				// Calculates rolling average of user reTweet count.				
				reTweetRollingAverage -= Math.ceil(reTweetRollingAverage / ROLLING_WINDOW);
				reTweetRollingAverage += Math.ceil(tweet.reTweetCount / ROLLING_WINDOW);
				
				// Anomaly detection, if reTweet count is greater than rolling average by offset
				// and reTweet count is less than the maxReTweetCount by ratio, flag it as anomaly.
				// reTweetRollingAverage and maxReTweetCount gets auto adjusted to fluctuations.
				// Reason for using two indices reTweetRollingAverage, maxReTweetCount to compute anomaly,
				// rolling average moves slowly and is resistant to fluctuations, while maxReTweetCount helps
				// us to keep track of changes is upward trends.
				if (tweet.reTweetCount > MIN_ANOMALY_OFFSET * (reTweetRollingAverage)
						&& tweet.reTweetCount > ANOMALY_RATIO * maxReTweetCount) {
					return true;
				}
				
				return false;
			}
		})
		.subscribe(
			tweet -> System.out.println(tweet.user + " " + tweet.reTweetCount),
			Throwable::printStackTrace);
	}
}