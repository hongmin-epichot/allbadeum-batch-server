#!/usr/bin/bash

nohup java -jar -Duser.timezone="Asia/Seoul" \
  /home/ec2-user/allbadeum-batch-server/build/libs/allbadeum-batch-server-1.0.0.jar \
  --spring.profiles.active=dev > /home/ec2-user/nohup.out 2>&1 &