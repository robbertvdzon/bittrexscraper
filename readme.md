Start mysql lokaal:
docker run --name mysql -p 3306:3306 -v db:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=passwd -d mysql
docker run --name myadmin -d --link mysql:db -p 8080:80 phpmyadmin/phpmyadmin 


// bij 15 records per minuut, dan 600.000 per maand 
// 10.000 records = 1.5MB
// 600.000 miljoen = 90MB

http://bittrexscraper.vdzon.com/getmarketsummaries
http://bittrexscraper.vdzon.com/coinrates?marketname=BTC-1ST