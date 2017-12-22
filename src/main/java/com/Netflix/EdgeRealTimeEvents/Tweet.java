package main.java.com.Netflix.EdgeRealTimeEvents;

import java.util.Date;
import org.json.JSONObject;

public class Tweet {
	public String tweet;
	public String user;
	public long reTweetCount;
	public Date createdAt;
	public boolean verified;
	public String lang;

	public Tweet(String tweet) {
		// Assuming the tweet data has the prefix of format "data: ".
		JSONObject jsonObj = new JSONObject(tweet.substring(6));

		// Setting fields from the json object.
		this.tweet = jsonObj.getString("tweet");
		this.user = jsonObj.getString("user");
		this.reTweetCount = jsonObj.getLong("retweet_count");
		this.createdAt = new Date(jsonObj.getLong("created_at"));
		this.verified = jsonObj.getBoolean("verified");
		this.lang = jsonObj.getString("lang");
	}
}
