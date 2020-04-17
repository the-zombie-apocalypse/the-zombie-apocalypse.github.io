aws ecr get-login-password --region us-east-1
docker login -u AWS 736762908522.dkr.ecr.eu-central-1.amazonaws.com/zombies-front
npm run build:prod
docker build -t zombies-front .
docker tag zombies-front:latest 736762908522.dkr.ecr.eu-central-1.amazonaws.com/zombies-front:latest
docker push 736762908522.dkr.ecr.eu-central-1.amazonaws.com/zombies-front:latest
