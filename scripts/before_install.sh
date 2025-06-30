#!/bin/bash
# Update system packages
yum update -y

# Install Java 17 and Tomcat
yum install -y java-17-amazon-corretto
yum install -y tomcat

# Create application directory
mkdir -p /var/www/html
chown -R tomcat:tomcat /var/www/html

# Configure Tomcat
systemctl enable tomcat
systemctl start tomcat 