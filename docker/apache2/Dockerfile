FROM httpd
RUN mkdir -p /usr/local/apache2/ssl
ADD private.crt /usr/local/apache2/ssl
ADD private.key /usr/local/apache2/ssl
ADD httpd.conf /usr/local/apache2/conf
ADD httpd-vhosts.conf /usr/local/apache2/conf/extra
ADD httpd-ssl.conf /usr/local/apache2/conf/extra
WORKDIR /usr/local/apache2
EXPOSE 80 443
