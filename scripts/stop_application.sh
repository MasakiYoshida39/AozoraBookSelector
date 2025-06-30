#!/bin/bash
# Stop Tomcat gracefully
systemctl stop tomcat

# Wait for shutdown
sleep 10

echo "Application stopped successfully" 