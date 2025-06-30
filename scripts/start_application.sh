#!/bin/bash
# Start Tomcat
systemctl start tomcat

# Wait for application to be ready
sleep 10

# Health check
curl -f http://localhost:8080/AozoraBookSelector/ || exit 1

echo "Application started successfully" 