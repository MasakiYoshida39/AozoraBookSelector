#!/bin/bash
# Deploy WAR file to Tomcat
cp AozoraBookSelector.war /var/lib/tomcat/webapps/

# Set proper permissions
chown tomcat:tomcat /var/lib/tomcat/webapps/AozoraBookSelector.war

# Wait for deployment
sleep 30

# Configure firewall
firewall-cmd --permanent --add-port=8080/tcp
firewall-cmd --reload 