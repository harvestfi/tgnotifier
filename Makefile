SHELL:=/bin/bash

MYSQL_VERSION=8
MYSQL_PORT=3306
MYSQL_DATA=/tmp/tgnitifier-db

.PHONY: help start-db clean-db connect-db
all: help
help: Makefile
	@echo
	@echo " Choose a command run in "$(PROJECTNAME)":"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' |  sed -e 's/^/ /'
	@echo

start-db:
	@docker network create tg-mysql || true
	@docker run -d --network tg-mysql --name tg-mysql -v ${MYSQL_DATA}:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=tgnotifier -p ${MYSQL_PORT}:3306 mysql:${MYSQL_VERSION} || true
	@echo "DB started, connect with make connect-db"

stop-db:
	@docker stop tg-mysql || true

clean-db: stop-db
	@docker rm tg-mysql || true
	@docker network rm tg-mysql || true
	@sudo rm -Rf ${MYSQL_DATA} || true
	@echo "DB cleaned"

logs-db: start-db
	@docker logs -f tg-mysql

connect-db:
	@echo "Default password is: password"
	@docker run -it --network tg-mysql --rm mysql:${MYSQL_VERSION} mysql -htg-mysql -uroot -p

run:
	@mvn clean
	@mvn spring-boot:run

build:
	@mvn verify