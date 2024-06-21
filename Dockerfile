FROM openjdk:11

# Install Tesseract dependencies
RUN apt-get update && \
    apt-get install -y tesseract-ocr && \
    apt-get clean

# Remove existing tessdata directory and create a new one
RUN rm -rf /usr/share/tesseract-ocr/4.00/tessdata && \
    mkdir -p /usr/share/tesseract-ocr/4.00/tessdata

# Copy tessdata contents to the container
COPY src/main/resources/tessdata /usr/share/tesseract-ocr/4.00/tessdata

# Add the application jar file
ADD target/PDFExtractorService.jar PDFExtractorService.jar

# Set the TESSDATA_PREFIX environment variable
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/4.00/tessdata

# Expose the application port
EXPOSE 8086

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "PDFExtractorService.jar"]

