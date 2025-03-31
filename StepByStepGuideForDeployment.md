# Step by Step for AWS EC2 Deployment

- [Step by Step for AWS EC2 Deployment](#step-by-step-for-aws-ec2-deployment)
  - [EC2](#ec2)
    - [Settings](#settings)
    - [Connect to EC2 instance.](#connect-to-ec2-instance)
  - [Aurora \& RDS Database](#aurora--rds-database)
    - [Settings](#settings-1)
    - [Configure the database access for the items](#configure-the-database-access-for-the-items)
    - [Testing from the EC2 Instance Console (optional)](#testing-from-the-ec2-instance-console-optional)

## EC2

### Settings

```
Name          : SofaSoGoodServer1
AMI           : ubuntu
Instance type : t2.micro
Key pair      : vockey
Network       : Allow SSH/ HTTPS/ HTTP traffic
Storage       : 8, gp3
```

### Connect to EC2 instance.

```sh
# configure setup
sudo apt update -y
sudo apt upgrade -y
sudo apt install npm nginx git php php-fpm php-mysql -y

# prepare project
sudo git clone https://github.com/EzraEmma/csd3156-ezra.git
cd csd3156-ezra/my-app

# make the builds for vite and reactjs
sudo npm install
sudo npm audit fix
sudo npm run build

# start, configure nginx
sudo systemctl stop apache2
sudo systemctl disable apache2
sudo systemctl start nginx
sudo systemctl start php8.3-fpm
sudo rm /etc/nginx/sites-enabled/default # remove the default config
sudo nano /etc/nginx/sites-available/vite-app # we write new config
```

copy in
```nginx
server {
    listen 80;
    server_name localhost;

    # directory
    # reactjs project vite build at this root
    root /var/www/vite-app/dist;
    index index.html;

    location / {
        try_files $uri $uri/ =404;
    }

    location /index.html {
        try_files $uri =404;
        add_header Cache-Control "no-cache, no-store, must-revalidate";
    }

    # php API requests
    location /api/ {
        # the root directory of the php project
        root /var/www/php-api;
        index index.php;
        rewrite ^/api/(.*)$ /$1 break;
        include fastcgi_params;
        fastcgi_pass unix:/run/php/php-fpm.sock;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
    }

    # sofasogood/*.php
    location ~ \.php$ {
        # the root directory for the php project again
        root /var/www/php-api;
        include fastcgi_params;
        fastcgi_pass unix:/run/php/php-fpm.sock;
        fastcgi_index index.php;
        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
    }

     # Enable Gzip compression
    gzip on;
    gzip_types text/css application/javascript image/svg+xml;
}
```
CTRL+X, Y, ENTER

> **Troubleshooting PHP not starting 1: can't find your php**
> ```sh
> sudo systemctl list-units --type=service | grep php
> ```
> Then use that version indicated.

> **Troubleshooting PHP not starting 2: because Apache is Running**
>
> Detection:
> ```sh
> sudo lsof -i :80
> ```
> ```sh
> sudo systemctl stop apache2
> sudo systemctl disable apache2
> sudo systemctl restart nginx
> sudo systemctl restart php8.3-fpm
> ```
> Then use that version indicated.

```sh
# enable the site
sudo ln -s /etc/nginx/sites-available/vite-app /etc/nginx/sites-enabled/
sudo nginx -t # verifies nginx
sudo systemctl restart nginx
sudo systemctl restart php8.3-fpm

# deploy build files for vite reactjs
sudo mkdir -p /var/www/vite-app/dist
sudo cp -r dist/* /var/www/vite-app/dist

# deploy the php as it is
sudo mkdir -p /var/www/php-api
sudo cp -r ../DataBaseStuff/* /var/www/php-api

# set correct permissions
sudo chown -R www-data:www-data /var/www/vite-app
sudo chmod -R 755 /var/www/vite-app
sudo chown -R www-data:www-data /var/www/php-api
sudo chmod -R 755 /var/www/php-api
```

~.

Other useful commands

====== if necessary for update/push
```sh
git fetch
git pull
```

====== stopping/restarting nginx
```sh
sudo systemctl stop nginx
sudo systemctl restart nginx
```

====== getting errors from nginx
```sh
sudo tail -f /var/log/nginx/error.log
```

## Aurora & RDS Database

### Settings

```
Create                 : standard create
Engine                 : MySQL
Templates              : Free tier
DB Instance Identifier : sofasogoodDatabase
Username               : [DBUsername]
Master Password        : [DBPassword]
Instance config        : db.t3.micro
Connectivity           : connect to SofaSoGoodServer1
VPC Security Firewall  : new -> SofaSoGoodDatabaseSecurityGroup
Inital DB Name         : sofasogoodDB
```

Remember the following data:

```
Connectivity & security -> endpoint [DBEndpoint]
Connectivity & security -> port [DBPort]
```

### Configure the database access for the items

====== edit `dbinfo.inc`
```sh
sudo rm ../DataBaseStuff/dbinfo.inc # if sure you want to replace
sudo nano ../DataBaseStuff/dbinfo.inc
```

Replace:
```php
<?php

define('DB_SERVER', 'DBEndpoint');
define('DB_USERNAME', 'DBUsername');
define('DB_PASSWORD', 'DBPassword');
define('DB_DATABASE', 'sofasogoodDB');

?>
```

```sh
# copy the new `dbinfo.inc` over the old one
sudo rm /var/www/php-api/dbinfo.inc
sudo cp ../DataBaseStuff/dbinfo.inc /var/www/php-api/dbinfo.inc

# restart site
sudo systemctl restart php8.3-fpm
sudo systemctl restart nginx
```

### Testing from the EC2 Instance Console (optional)

```sh
sudo wget https://dev.mysql.com/get/mysql-apt-config_0.8.29-1_all.deb
sudo dpkg -i mysql-apt-config_0.8.29-1_all.deb
```

Choose `Ubuntu Jammy`.
Choose `Ok`.

```sh
sudo apt update
sudo apt upgrade
sudo apt install mysql-client -y
sudo mysql -h DBEndpoint -u DBUsername -p
```
```console
mysql>Show databases;
```

This should show the databases.
