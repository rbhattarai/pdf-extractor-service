# pdf-extractor-service
Uses Spring Boot Web, PDFBox and Tesseract to extract text, tables and images from PDF file


## Steps

  * Build project:
  
    `mvn clean install`

* In local:  
  
  * Run service :
  
    `mvn spring-boot:run`

* In docker container:
  
  * Build project: will use the PDFExtractorService.jar created above

    `docker build -f Dockerfile -t pdf-extractor-service .`

  * Run service:
    
    `docker run -p 8086:8086 pdf-extractor-service`

* Configure TessData and Set Env Vars (for local environment only):

  cd src/main/resources/tessdata

  Either git pull or download data from: https://github.com/tesseract-ocr/tessdata

  Set env vars as:
  
  TESSDATA_PREFIX=src/main/resources/tessdata

  ENV=local

* Use REST client like Postman and make POST Call:

  * Below request helps to extract text content from PDF file:
    * Method: `POST`
    * URL: `http://localhost:8086/api/v1/pdf/extract-text`
    * Body: Param: `file`, Value: `ERDM_PR_Test.pdf`

  * Response:

        `{
        "textContent": "ERDM \r\n \r\nPerformance Return Domain \r\n \r\nStatistics: \r\nSN Portfolios EffectiveDate StdDev ReturnPercent \r\n1 ABC 2024-04-14 0.5 50.00% \r\n2 DEF 2024-04-14 0.05 95.00% \r\n3 DEF 2024-04-14 0.01 99.00% \r\n4 GHI 2024-04-15 0.04 96.00% \r\n5 JKL 2024-04-16 0.2 80.00% \r\n \r\n",
        "tableContent": null,
        "imageContent": null
        }`
  
* Similarly, there are endpoints for `extract-tables`, `extract-images` and `extract-all`  

  * Below request helps to extract text from tables and charts:
    * Method: `POST`
    * URL: `http://localhost:8086/api/v1/pdf/extract-tables`
    * Body: Param: `file`, Value: `ERDM_PR_Test.pdf`

  * Below request helps to extract text from images:
    * Method: `POST`
    * URL: `http://localhost:8086/api/v1/pdf/extract-images`
    * Body: Param: `file`, Value: `ERDM_PR_Test.pdf`

  * Below request helps to extract text from normal text, tables, charts and images:
    * Method: `POST`
    * URL: `http://localhost:8086/api/v1/pdf/extract-all`
    * Body: Param: `file`, Value: `ERDM_PR_Test.pdf`

  * Below request helps to extract text from specific rectangular area or section:
    * Method: `POST`
    * URL: `http://localhost:8086/api/v1/pdf/extract-section`
    * Body: 
      * Param: `file`, Value: `ERDM_PR_Test.pdf`
      * Param: `x`, Value: `50`
      * Param: `y`, Value: `50`
      * Param: `width`, Value: `200`
      * Param: `height`, Value: `200`
    
