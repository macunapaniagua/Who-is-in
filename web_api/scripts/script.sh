#!/usr/bin/env bash

echo "publish site"
sudo cp -rf /home/ubuntu/Who-is-in/web_api/app /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/config /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/bootstrap /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/database /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/public /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/resources /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/tests /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/vendor /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/storage /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/composer.json /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/phpunit.xml /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/server.php /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/.env /var/www/html
sudo cp -rf  /home/ubuntu/Who-is-in/web_api/artisan /var/www/html
sudo chmod -R 777 /var/www/html/
echo "publish updated"
