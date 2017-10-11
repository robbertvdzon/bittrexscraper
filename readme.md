Start mysql lokaal:
docker run --name mysql -p 3306:3306 -v db:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=passwd -d mysql
docker run --name myadmin -d --link mysql:db -p 8080:80 phpmyadmin/phpmyadmin 


// bij 4 records per 10 sec: dan 2 miljoen per week 
// 10.000 records = 1.5MB
// 2 miljoen = 300MB
