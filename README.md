### Dev Setup
1. uncomment AppInitializer to load list form db
2. set hibernate.ddl-auto to create
3. mvn spring-boot:run

#### TODO:
- AQUAMNANGA CRAWLER https://aquamanga.com/read/boss-in-school/chapter-18/
- https://www.mangaha.org/manga/spy-x-family/chapter-52

#### How to add new manga resource to crawl
1. update enum MangaSite.java
2. create new sitecrawler instance
3. update SiteCrawlFactory.java  refering to new instance
4. create unit tests
5. Mangasitecrawler test