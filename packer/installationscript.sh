# Copy the systemd service file
sudo cp /tmp/healthzcloud.service /etc/systemd/system/healthzcloud.service

# Install required packages
sudo yum install -y java-17-openjdk

# Add user and group for the application
sudo groupadd csye6225
sudo useradd -r -m -g csye6225 -s /usr/sbin/nologin csye6225

curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh
sudo bash add-google-cloud-ops-agent-repo.sh --also-install

# Copy the Java application and set permissions
sudo cp /tmp/healthcheck-0.0.1-SNAPSHOT.jar /home/csye6225
sudo cp /tmp/opsagentconfig.yaml /etc/google-cloud-ops-agent/config.yaml
sudo chown -R csye6225:csye6225 /home/csye6225

# Reload systemd and enable services
sudo systemctl daemon-reload
sudo systemctl enable healthzcloud
sudo systemctl restart google-cloud-ops-agent"*"
