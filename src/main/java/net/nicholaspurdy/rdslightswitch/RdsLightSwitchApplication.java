package net.nicholaspurdy.rdslightswitch;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.StartDbInstanceRequest;
import software.amazon.awssdk.services.rds.model.StartDbInstanceResponse;
import software.amazon.awssdk.services.rds.model.StopDbInstanceRequest;
import software.amazon.awssdk.services.rds.model.StopDbInstanceResponse;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

public class RdsLightSwitchApplication implements RequestHandler<ScheduledEvent, String> {

    public String handleRequest(ScheduledEvent scheduledEvent, Context context) {

        System.out.println("Starting RDSLightSwitch. Time: " + scheduledEvent.getTime());

        return flipSwitch();

    }

    private String flipSwitch() {

        String direction = requireNonNull(System.getenv("FLIP"), "Please set the 'FLIP' environment variable.");

        String instance = requireNonNull(System.getenv("DB_INSTANCE"),
                "Please set the DB_INSTANCE environment variable");

        System.out.println("Flipping " + instance + " " + direction);

        try {
            if (direction.equals("ON")) {
                startInstance(instance);
            }
            else if (direction.equals("OFF")) {
                stopInstance(instance);
            }
            else {
                throw new IllegalStateException("'FLIP' must be set to either 'ON' or 'OFF'.");
            }
        }
        catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            return "RDSLightSwitch completed in error. Time: " + LocalDateTime.now();
        }

        return "RDSLightSwitch completed successfully. Time: " + LocalDateTime.now();

    }

    private void startInstance(String instance) {

        RdsClient client = RdsClient.create();

        StartDbInstanceRequest request = StartDbInstanceRequest.builder()
                .dbInstanceIdentifier(instance)
                .build();

        StartDbInstanceResponse response = client.startDBInstance(request);

        System.out.println("Response received. " + instance + " status: " + response.dbInstance().dbInstanceStatus());

    }

    private void stopInstance(String instance) {

        RdsClient client = RdsClient.create();

        StopDbInstanceRequest request = StopDbInstanceRequest.builder()
                .dbInstanceIdentifier(instance)
                .build();

        StopDbInstanceResponse response = client.stopDBInstance(request);

        System.out.println("Response received. " + instance + " status: " + response.dbInstance().dbInstanceStatus());

    }

}
