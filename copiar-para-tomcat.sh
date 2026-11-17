#!/bin/bash
# Copia o .war do Incremento 2 para o Tomcat que JÁ ESTÁ LIGADO (hot deploy).
# O Incremento 1 (/habit-tracker) continua no ar; este vira /habit-tracker-v2.
#
# Uso (dentro da pasta do projeto):   ./copiar-para-tomcat.sh
# Se o seu Tomcat estiver em outra pasta:   TOMCAT_HOME=/caminho/do/tomcat ./copiar-para-tomcat.sh

TOMCAT="${TOMCAT_HOME:-$HOME/apache-tomcat-9.0.123}"
WAR="$(cd "$(dirname "$0")" && pwd)/target/habit-tracker-v2.war"

if [ ! -f "$WAR" ]; then
    echo "Não achei o .war. No NetBeans: botão direito no projeto > Clean and Build."
    exit 1
fi
if [ ! -d "$TOMCAT/webapps" ]; then
    echo "Não achei o Tomcat em $TOMCAT (ajuste com TOMCAT_HOME)."
    exit 1
fi

cp "$WAR" "$TOMCAT/webapps/" && echo "Copiado! Em uns 5 segundos abra: http://localhost:8080/habit-tracker-v2/"
