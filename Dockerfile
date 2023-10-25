# Build runtime image
FROM openjdk:17-jdk

# Set the working VOLUME inside the container
VOLUME /tmp

# Copy the executable JAR file and any other necessary files
COPY target/knot-collaboration-api.jar knot-collaboration-api.jar

# Set the entry point command when a container is run from this image
ENTRYPOINT ["java","-jar","/knot-collaboration-api.jar"]
