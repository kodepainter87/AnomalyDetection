# Building the project.

Please run './gradlew build' to install dependencies and build the project.
```
./gradlew build
```

# Running the project.

Please run './gradlew run' to run the project.
```
./gradlew run
```

# Future Work.
How would you scale this if all tweets could not be processed on a single processor, or a single machine?
```
The algorithm is state-less and will adjust to any fluctuations in the data. That being said, In order to scale we can allocate a master which distributes the tweets to worker roles randomly. 
Each worker role will be able to quickly calculate the anomalies.
```

How can your solution handle variations in data volume throughout the day?
```
The algorithm constantly calculates the rolling average recount of tweets which moves slowly, the algorithm also keeps track of maxReTweetCount seen so far. 
With the help of these two indices it will be able to adjust itself for upward variations in data volume.
```

How would you productize this application?
```
This application can be developed as a framework library for other clients to consume it. 
Alternatively, we can also develop a microservice which filters the incoming stream from anomalies and clients can create jobs which subscribe to it. 
```

How would you introduce more sophisticated anomaly / outlier detection?
```
Currently the algorithm only concentrates on upward trends of reTweet counts. With few modifications like
calculating MIN_ANOMALY_OFFSET dynamically the algorithm should be able to handle downward trends as well.
```

How would you test your solution to verify that it is functioning correctly?
```
I would take two numbers Min and Max and generate some random numbers between them, occasionally I would also generate a number greater than lets say avg(Min, Max)*2 and the program should catch these anomalies.
We can also start increasing the Min and Max, accordingly the program will re-adjust itself to find new anomalies.
```