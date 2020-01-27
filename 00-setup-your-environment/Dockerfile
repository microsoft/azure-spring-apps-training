FROM mcr.microsoft.com/java/maven:8-zulu-debian9

RUN apt update -y && apt upgrade -y \
    && apt install apt-utils -y \
    && apt install wget -y \
    && apt install git -y \
    && apt install nano -y \
    && apt install procps -y \
    && apt install jq -y

# Install Azure CLI
RUN apt-get install ca-certificates curl apt-transport-https lsb-release gnupg -y  \
    && curl -sL https://packages.microsoft.com/keys/microsoft.asc | \
        gpg --dearmor | \
        tee /etc/apt/trusted.gpg.d/microsoft.asc.gpg > /dev/null \
    && AZ_REPO=$(lsb_release -cs) \
    && echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ $AZ_REPO main" | \
        tee /etc/apt/sources.list.d/azure-cli.list \
    && apt-get update -y \
    && apt-get install azure-cli -y \
    && apt autoremove -y

# Install extensions

RUN az extension add --name spring-cloud -y
RUN az extension add --name db-up -y 

# Install MySQL CLI
RUN apt-get install mysql-client -y

# Install PostgreSQL CLI
RUN apt-get install postgresql-client -y

RUN mkdir /lab

ENTRYPOINT [ "bash", "-c", "tail -f /dev/null" ]
