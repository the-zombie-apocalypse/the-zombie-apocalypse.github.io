aws ecr get-login-password --region us-east-1
docker login -u AWS 736762908522.dkr.ecr.eu-central-1.amazonaws.com/zombies
gradlew bootJar
docker build -t zombies .
docker tag zombies:latest 736762908522.dkr.ecr.eu-central-1.amazonaws.com/zombies:latest
docker push 736762908522.dkr.ecr.eu-central-1.amazonaws.com/zombies:latest
