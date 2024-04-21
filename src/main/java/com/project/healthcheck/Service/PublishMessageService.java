package com.project.healthcheck.Service;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.project.healthcheck.Pojo.User;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

@Service
public class PublishMessageService {
    private static final Logger logger = LogManager.getLogger(PublishMessageService.class);

  public void publishMessage(String projectId, String topicId, User user)
      throws IOException, InterruptedException {
    TopicName topicName = TopicName.of(projectId, topicId);
    Publisher publisher = null;
    System.out.println("publishMessage Service entry");
    logger.debug("publishMessage Service entry");
    System.out.println(user.getUsername());
    
    try {
      // Create a publisher instance with default settings bound to the topic
      
      publisher = Publisher.newBuilder(topicName).build();
      logger.debug(user.toString());
      Gson gson = new Gson();
      String payload = gson.toJson(user);
      ByteString data = ByteString.copyFromUtf8(payload);

      logger.debug(payload);
      logger.debug(data.toString());

      
      PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

        logger.debug(publisher.publish(pubsubMessage).get());
        
    } 
        catch(Exception e){
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            
        }
        finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(2, TimeUnit.MINUTES);
            }
        }
    }
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
