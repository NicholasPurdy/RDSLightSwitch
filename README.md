# RDSLightSwitch
A Java program to flip an RDS instance on and off based off environment variables (for AWS Lambda).

## Directions

1. Compile with ```mvn clean install```
2. Specify ```net.nicholaspurdy.rdslightswitch.RdsLightSwitchApplication``` as your handler.
3. Create an IAM policy with the following:
```
{
    "Effect": "Allow",
    "Action": [
        "rds:DescribeDBInstances",
        "rds:StopDBInstance",
        "rds:StartDBInstance"
    ],
    "Resource": "db-instance-arn-goes-here"
}
```

Add this policy to the role associated with your Lambda function and then you can control which instance you want to switch on or off with the following environment variables:

Variable|Values
--------|--------
FLIP|ON/OFF
DB_INSTANCE|Name of your RDS instance

